

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
    private ASPanel ToTgsPanel;
    private static int port_TGS=4444;
    ServerSocket server_TGS;
//    String divided[] = {filed,type,IPs,IPr,len,retain,data};

    public AS() throws IOException {
        rsa_n =151;
        rsa_pk=359;
        rsa_sk=667;
        this.server_TGS= new ServerSocket(port_TGS);
        ToTgsPanel= new ASPanel("AS-TGS",rsa_pk,rsa_sk,rsa_n,server_TGS);
    }

    public void AS_start(){
            try {
                ServerSocket server= new ServerSocket(port);
                InputStreamReader tgs_StreamReader = new InputStreamReader(server_TGS.accept().getInputStream());
                BufferedReader tgs_bufferedReader = new BufferedReader(tgs_StreamReader);
                OutputStreamWriter tgs_StreamWriter = new OutputStreamWriter(server_TGS.accept().getOutputStream());
                BufferedWriter tgs_bufferedWriter = new BufferedWriter(tgs_StreamWriter);
                ToTgsPanel.run();
                while (true) {
                    Socket socket=server.accept();
                    ASCallable task = new ASCallable(server,socket,tgs_bufferedReader,tgs_bufferedWriter,ToTgsPanel);
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
        as.AS_start();
    }
}
