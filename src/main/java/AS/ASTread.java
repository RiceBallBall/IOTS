package AS;



import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class ASTread extends Message implements Callable {

    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private String Kc;
    private String ID_c;
    private Socket socket;
    private String AS_IP;
    private String TS;
    private String ID_tgs;
    private String LT;
    private int tgs_pk;
    private int tgs_n;
    private SerPanel Panel;
    private Tools tools;
    private String TGS_IP;
    private sqlOperation sql;
    public InputStreamReader inputStreamReader;
    public BufferedReader bufferedReader;
    public PrintWriter writer;


    private final int privateKey = 1997;//server私钥
    public final int publicKey = 2501;//server公钥
    public final int rsaN = 4559; // N
    private String[] messageT;
    private String[] messageA;

    public ASTread(Socket socket, sqlOperation operation) throws IOException {
        this.socket = socket;
        sql = operation;
        tools = new Tools();
        this.socket = socket;
        rsa_n = 3071;
        rsa_pk = 2317;
        rsa_sk = 781;
         Panel = new SerPanel("AS",rsa_pk,rsa_sk,rsa_n,socket,sql);
        AS_IP = "172020010002";
        TS = tools.getTS();
        LT = "60";
        tgs_n = 1147;
        tgs_pk = 643;
        inputStreamReader = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
        writer = new PrintWriter(socket.getOutputStream());

    }

    public void setTS(String ts) {
        TS = Tools.getTS();
    }

    public void setKc(String kc) {
        Kc = kc;
    }

    public Boolean call() {
        InetAddress adder = socket.getInetAddress();
        System.out.println("ADD:"+adder);
        try  {
//            while(true){
            String rec = bufferedReader.readLine();
            System.out.println("Rec="+rec);
            ASexcecution(rec);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
       return true;
    }

    public void packSend(Socket socket, String sen_package) {
            writer.println(sen_package);
            writer.flush();

    }

    public void mes_display(String Basic[], String[] mes, SerPanel this_panel) {
        String bas = "";
        bas = bas + "控制字段：" + Basic[0] + "\n";
        bas = bas + "报类型编号：" + Basic[1] + "\n";
        bas = bas + "发送IP：" + Basic[2] + "\n";
        bas = bas + "接收IP：" + Basic[3] + "\n";
        bas = bas + "包长度：" + Basic[4] + "\n";
        bas = bas + "保留字段：" + Basic[5] + "\n";
        bas = bas + "数据内容：" + "\n";
        for (int i = 0; i < mes.length; i++) {
            bas += mes[i] + "\n";
        }
        this_panel.textArea5.append(bas);
//        Panel.textArea5.paintImmediately(Panel.textArea5.getBounds());
    }

    public void ASexcecution(String rec_package) throws IOException, SQLException {
        Panel.textArea4.setText(rec_package);
        String Basic_info[] = Divide(rec_package);
        String data[];
        ResultSet resultSet=null;
        switch (Basic_info[1]) {//IPr Basic_info[3]
            case "1": {//Client发起请求
                setTS(tools.getTS());
                data = m1_d(Basic_info[6]);
                ID_c = data[0];
                Panel.textArea2.setText(ID_c);
                ID_tgs = data[1];
                String Kc_tgs = "12345678";
                mes_display(Basic_info, data, Panel);
                //查询tgs sql
                String tgt = TGT(Kc_tgs, ID_c, Basic_info[3], ID_tgs, TS, LT, tgs_pk, tgs_n);
                System.out.println("Id"+ ID_c);
                resultSet=sql.searchSQL("client",ID_c);
                while(resultSet.next()){
                Kc=resultSet.getString(2);}
                System.out.println("Kc"+ Kc);
                String re_to_Client = m2(ID_tgs, TS, LT, Kc_tgs, tgt,Kc, AS_IP, Basic_info[3]);//反馈Client
                System.out.println(re_to_Client);
                packSend(socket, re_to_Client);
                Panel.textArea3.setText(re_to_Client);
                break;
            }
            case "7": {//Client的注册请求
                data = m7_d(Basic_info[6], rsa_sk, rsa_n);//IDc,IDtgs,TS1
                ID_c = data[0];
                Panel.textArea2.setText(ID_c);
                Kc=data[1];//写入数据库，返回成功报文8
                sql.insertSQL("client", ID_c, Kc, "0", TS);
                Boolean result = true;//结果
                mes_display(Basic_info, data, Panel);
                String mes_8 = m8(result, rsa_sk, rsa_n, AS_IP, Basic_info[3]);
                System.out.println(mes_8);
                Panel.textArea3.setText(mes_8);
                System.out.println("set");
                packSend(socket, mes_8);//结果
                System.out.println("sent");
                break;

            }
            case "16": {//TGS的时间戳同步
                setTS(TS);
                data = m16_d(Basic_info[6], rsa_sk, rsa_n);//IDc,TS4
                sql.alertSQl("client",ID_c,TS);
            }
            case "0": {//Client的离线请求
                data = m23a_d(Basic_info[6], rsa_sk, rsa_n);//IDc
                String mes_24 = m24("11", Kc, AS_IP, Basic_info[3]);
                packSend(socket, mes_24);//离线反馈
                setTS(TGS.Tools.getTS());
                sql.alertSQl("client", ID_c, TS);
                Panel.textArea3.setText(mes_24);
                this.Panel.textArea3.setText(mes_24);
                mes_display(Basic_info, data, Panel);
                break;
            }

        }

    }

}
