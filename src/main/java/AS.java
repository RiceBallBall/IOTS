
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AS extends Message implements RSA,DES{
    private static int peopleNumber=4;
    private static int port=4444;
    private static int port_TGS=8888;
    private static ExecutorService serviceThread = Executors.newFixedThreadPool(peopleNumber);
    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
//    String divided[] = {filed,type,IPs,IPr,len,retain,data};

    public AS(){
        rsa_n =151;
        rsa_pk=359;
        rsa_sk=667;


    }

    public void AS_start(){
            try {
                ServerSocket server = new ServerSocket(port);
                ServerSocket server_TGS = new ServerSocket(port_TGS);
                while (true) {
                    Socket socket = server.accept();
                    Socket socket_TGS=server_TGS.accept();
                    ASCallable task = new ASCallable(socket,socket_TGS);
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

    public static void main(String[] args) {
        AS as=new AS();
        ASPanel asPanel=new ASPanel("AS");
        as.AS_start();
    }
}
