package AS;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AS extends Message implements RSA,DES{
    private static int peopleNumber=4;
    private static int port=8888;
    private static ExecutorService serviceThread = Executors.newFixedThreadPool(peopleNumber);
    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private SerPanel ToTgsPanel;
//    private static int port_TGS=4444;
//    ServerSocket server_TGS;
//    String divided[] = {filed,type,IPs,IPr,len,retain,data};

    public AS() throws IOException {
        rsa_n =3071;
        rsa_pk=2317;
        rsa_sk=781;
 //       this.server_TGS= new ServerSocket(port_TGS);
//        ToTgsPanel= new SerPanel("AS-TGS",rsa_pk,rsa_sk,rsa_n,server_TGS,true);
    }

    public void AS_start(){
            try {
//                System.out.println("Waiting for TGS...");
//                InputStreamReader tgs_StreamReader = new InputStreamReader(server_TGS.accept().getInputStream());
//                BufferedReader tgs_bufferedReader = new BufferedReader(tgs_StreamReader);
//                OutputStreamWriter tgs_StreamWriter = new OutputStreamWriter(server_TGS.accept().getOutputStream());
//                BufferedWriter tgs_bufferedWriter = new BufferedWriter(tgs_StreamWriter);
                ServerSocket server= new ServerSocket(port);

                while (true) {
                    Socket socket=server.accept();
                    ASCallable task = new ASCallable(server,socket,ToTgsPanel);
                    try{
                        serviceThread.submit(task);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
      //  asPanel.run(rsa_pk,rsa_sk,rsa_n);
    }

    public static void main(String[] args) throws IOException {
        AS as=new AS();
//        ASPanel asPanel=new ASPanel("AS");
    //    System.out.println("===========");
        as.AS_start();
    }
}
