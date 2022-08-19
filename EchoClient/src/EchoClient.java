import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        String localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
        System.out.printf("Напиши 'bye' чтобы выйти%n%n%n");

        try (Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in, "UTF-8");
            InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");

            try (PrintWriter writer = new PrintWriter(socket.getOutputStream()); Scanner sc = new Scanner(isr)) {
                while (true) {
                    String message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    if ("bye".equalsIgnoreCase(message)) {
                        return;
                    }
                    String msg = sc.nextLine().strip();
                    System.out.printf("Got from server: %s%n", msg);
                }
            }
        } catch (NoSuchElementException ex) {
            System.out.println("Connection dropped!");
        } catch (IOException e) {
            System.out.printf("Can`t connect to %s:%s !%n", host, port);
            e.printStackTrace();
        }


    }
}
