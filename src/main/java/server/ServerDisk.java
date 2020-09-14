//package server;
//
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class ServerDisk {
//    private static int peopleNumber = 8;
//    private static int port = 9999;
//    private static ExecutorService serviceThread = Executors.newFixedThreadPool(peopleNumber);
//
//    public static void main(String[] args){
//        try {
//            ServerSocket server = new ServerSocket(port);
//            Socket socket = null;
//            while (true) {
//                socket = server.accept();
//                netDisk.Authentication task = new netDisk.Authentication(socket);
//                try{
//                    Future future = serviceThread.submit(task);
//                    String feedback = future.get().toString();
//                    if(!feedback.equals("false")){
//                        Disk disk = new Disk(socket, feedback.split(" ")[0], feedback.split(" ")[1]);
//                        serviceThread.submit(disk);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//            // serviceThread.shutdown();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
