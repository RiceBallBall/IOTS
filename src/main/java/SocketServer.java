import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.*;

public class SocketServer {
    private static int peopleNumber = 4;
    private static int port = 4444;
    private static ExecutorService serviceThread = Executors.newFixedThreadPool(peopleNumber);
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                MyCallable task = new MyCallable(socket);
                try{
                    Future future = serviceThread.submit(task);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
