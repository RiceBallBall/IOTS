package AS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class ASTread extends Message implements Callable {

    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private String Kc = "ID000002";
    private String ID_c;
    private Socket socket;
    private String AS_IP;
    private String TS;
    private String ID_tgs;
    private String LT;
    private int tgs_pk;
    private int tgs_n;
    // private SerPanel Panel;
    private Tools tools;
    private String TGS_IP;
    private sqlOperation sql;


    private final int privateKey = 1997;//server私钥
    public final int publicKey = 2501;//server公钥
    public final int rsaN = 4559; // N
    private String[] messageT;
    private String[] messageA;

    public ASTread(Socket socket,sqlOperation operation){
        this.socket = socket;
        tools = new Tools();
        this.socket = socket;
        rsa_n = 3071;
        rsa_pk = 2317;
        rsa_sk = 781;
        // Panel = new SerPanel("AS",rsa_pk,rsa_sk,rsa_n,serverSocket);
        AS_IP = "192168043188";
        TS = tools.getTS();
        LT = "60";
        tgs_n = 1679;
        tgs_pk = 775;
        sql = operation;
    }

    public Boolean call(){
        InetAddress adder = socket.getInetAddress();
        try(InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());)
        {
            String cipher = bufferedReader.readLine();
            System.out.println(adder);
            System.out.println(cipher);

                writer.println("============");
                writer.flush();
                writer.close();

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean clientVerify(){//发送给client的密文
        InetAddress adder = socket.getInetAddress();
        try(PrintWriter writer = new PrintWriter(socket.getOutputStream());)
        {
            String messageClient = m6(getTS(), messageT[0], socket.getInetAddress().toString(), adder.getHostAddress());
            writer.println(messageClient);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getTS(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }
}
