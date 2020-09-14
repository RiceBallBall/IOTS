package AS;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AS extends Message implements RSA,DES{
    private static int peopleNumber=4;
    private static int port=8888;
    private static ExecutorService serviceThread = Executors.newFixedThreadPool(peopleNumber);
    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private SerPanel ToTgsPanel;
    private sqlOperation sql;
    //    private static int port_TGS=4444;
//    ServerSocket server_TGS;
//    String divided[] = {filed,type,IPs,IPr,len,retain,data};

    public AS(){
        rsa_n =3071;
        rsa_pk=2317;
        rsa_sk=781;
        sql=new sqlOperation();
        sql.connect();
 //       this.server_TGS= new ServerSocket(port_TGS);
//        ToTgsPanel= new SerPanel("AS-TGS",rsa_pk,rsa_sk,rsa_n,server_TGS,true);
    }

    public void AS_start(){
      //  asPanel.run(rsa_pk,rsa_sk,rsa_n);
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                ASTread task = new ASTread(socket,sql);
                try{
                        //Disk disk = new Disk(socket, feedback.split(" ")[0], feedback.split(" ")[1]);
                        serviceThread.submit(task);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            // serviceThread.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        AS as=new AS();
//        ASPanel asPanel=new ASPanel("AS");
    //    System.out.println("===========");
        as.AS_start();
    }
}
