import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 7777;

        System.out.print("Enter your name: ");
        BufferedReader nameReader = new BufferedReader(new InputStreamReader(System.in));
        String nickName = nameReader.readLine();

        ServerSocket socket = new ServerSocket(port);
        System.out.println("Waiting for a partner...");
        Socket client = socket.accept();
        System.out.println("Partner has connected.");
        System.out.println();

        try (
                BufferedReader reader = nameReader;
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                DataInputStream din = new DataInputStream(in);
                DataOutputStream dout = new DataOutputStream(out);

        ){
            Thread listener = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println(din.readUTF());
                        } catch (IOException e) {
                            System.out.println("Closing listener");
                            break;
                        }
                    }
                }
            });
            listener.start();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

            while (true) {
                try {
                    String message = reader.readLine();
                    System.out.println("[" + dateFormat.format(new Date()) + "] " + nickName + ": " + message);
                    dout.writeUTF("[" + dateFormat.format(new Date()) + "] " + nickName + ": " + message);
                    dout.flush();
                } catch (IOException e) {
                    System.out.println(e.toString());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
