import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws IOException {
        int serverPort = 7777;
        BufferedReader ipReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your partner IP: ");
        String address = ipReader.readLine();

        InetAddress ipAddress = InetAddress.getByName(address);
        Socket socket = new Socket(ipAddress, serverPort);

        try (
                BufferedReader reader = ipReader;
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                DataInputStream din = new DataInputStream(in);
                DataOutputStream dout = new DataOutputStream(out)
        ){
            System.out.println();
            System.out.print("Enter your name: ");
            String nickName = reader.readLine();
            System.out.println();
            System.out.println("Connecting to IP " + address);

            System.out.println("Connection success. Now you can start discussion.");
            System.out.println();

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
                String message = reader.readLine();
                System.out.println("[" + dateFormat.format(new Date()) + "] " + nickName + ": " + message);
                dout.writeUTF("[" + dateFormat.format(new Date()) + "] " + nickName + ": " + message);
                dout.flush();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
