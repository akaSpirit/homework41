import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {

    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (var server = new ServerSocket(port)) {
            // обработка подключения
            try (var clientSocket = server.accept()) {
                handle(clientSocket);
            }
        } catch (IOException e) {
            System.out.printf("Вероятнее всего порт %s занят.%n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        // логика обработки

        InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
        String message = "";
        try (Scanner sc = new Scanner(isr)) {
            while (true) {
                message = sc.nextLine().strip();
                System.out.printf("Got from client: %s%n", message);
                if ("bye".equalsIgnoreCase(message)) {
                    System.out.printf("Bye bye!%n");
                    return;
                }

                try (PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
//                    while (true) {
                    writer.write(reverseString(message));
                    writer.write(System.lineSeparator());
                    writer.flush();
//                        return;
//                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Client dropped the connection!");
        }


    }

    public static String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }
}
