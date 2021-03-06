package TGS;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TGS extends Message implements RSA, DES {
    private static int peopleNumber=4;
    private static int port=8888;
    private static ExecutorService serviceThread = Executors.newFixedThreadPool(peopleNumber);
    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private static int port_TGS=4444;
    ServerSocket server_TGS;
//    String divided[] = {filed,type,IPs,IPr,len,retain,data};

    public TGS() throws IOException {
        rsa_n =1147;
        rsa_pk=643;
        rsa_sk=907;
        this.server_TGS= new ServerSocket(port_TGS);
      //  ToTgsPanel= new SerPanel("AS-TGS",rsa_pk,rsa_sk,rsa_n,server_TGS,true);
    }

    public void TGS_start(){
            try {
                ServerSocket server= new ServerSocket(port);
                while (true) {
                    Socket socket=server.accept();
                    TGSCallable task = new TGSCallable(socket);
                    try{
                        serviceThread.submit(task);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) throws IOException {
        TGS tgs=new TGS();
//        Panel asPanel=new ASPanel("AS");
        tgs.TGS_start();
    }
}
