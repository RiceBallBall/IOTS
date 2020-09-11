import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class ASCallable extends Message implements Callable,DES {
    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private String Kc = "ID000002";
    private String ID_c;
    private Socket socket = null;
    private String AS_IP;
    private String TS;
    private String ID_tgs;
    private String LT;
    private int tgs_pk;
    private int tgs_n;
    private ASPanel Panel;
    private Tools tools;
    private String TGS_IP;
    public Socket so_tgs;

    public ASCallable(Socket socket,Socket socket_TGS) {
        tools = new Tools();
        this.socket = socket;
        rsa_n = 151;
        rsa_pk = 359;
        rsa_sk = 667;
        Panel = new ASPanel("AS");
        AS_IP = "192168043188";
        TS = tools.getTS();
        LT="60";
        tgs_n=1679;
        tgs_pk=775;
        so_tgs=socket_TGS;
    }

    public void setTS(String tsN) {
        TS = tsN;
        return;
    }
    public void setKc(String ID_c,String psw){
        Kc=DES.toString(DES.encode(psw,ID_c));
    }


    public String call() {//需要完成的任务
        System.out.println("---------");
        setTS(tools.getTS());
        InetAddress addr = socket.getInetAddress();
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {//这里
            //收到后的设置显示收到的报文
            String rec_package = bufferedReader.readLine();//message
            System.out.println("Received a messege from "+ addr.getHostAddress());
            Panel.textArea4.setText(rec_package);//显示收到的包
            String inf[] = Divide(rec_package);
            String Basic_info[] = Divide(rec_package);
            //Panel.textArea5.setText();显示解码的包

//            String ar[] = m2_d(inf[6], Kc);
//            String example = "";
//            for (int i = 0; i < ar.length; i++) {
//                System.out.println(ar[i]);
//                example += ar[i] + "#";
//            }
            packSend(socket, "[Connected....Waiting for action.....]");

        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
        return "true";
    }

    public boolean packSend(Socket socket, String sen_package) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(sen_package);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void mes_display(String Basic[],String[] mes){
        String bas="";
            bas=bas+"控制字段："+Basic[0]+"\n";
            bas=bas+"报类型编号："+Basic[1]+"\n";
            bas=bas+"发送IP："+Basic[2]+"\n";
            bas=bas+"接收IP："+Basic[3]+"\n";
            bas=bas+"包长度："+Basic[4]+"\n";
            bas=bas+"保留字段："+Basic[5]+"\n";
            bas=bas+"数据内容："+"\n";
            for (int i=0;i<mes.length;i++){
                bas=mes[i]+"\n";
            }
            this.Panel.textArea5.setText(bas);
    }

    public void ASexcecution(String Package,String Basic_info[]) {
        String data[];
        switch (Basic_info[1]) {//IPr Basic_info[3]
            case "1": {//Client发起请求
                setTS(tools.getTS());
                data = m1_d(Basic_info[6]);
                ID_c=data[0];
                ID_tgs=data[1];
                String Kc_tgs=DES.toString(DES.encode(ID_tgs,ID_c));
                //查询tgs
                String tgt=TGT(Kc_tgs,ID_c,Basic_info[3],ID_tgs,TS,LT,tgs_pk,tgs_n);
                String re_to_Client=m2(ID_tgs,TS,LT,Kc_tgs,tgt,ID_c,AS_IP,Basic_info[3]);
                String re_to_tgs=m15(ID_c,Kc,TS,tgs_pk,tgs_n,AS_IP,TGS_IP);//
                packSend(socket,re_to_Client);//返回给Client
                this.Panel.textArea3.setText(re_to_Client);
                packSend(socket,re_to_tgs);//同步注册信息到TGS
                this.Panel.textArea3.setText(re_to_tgs);
                break;
            }
            case "7": {//Client的注册请求
                data = m7_d(Basic_info[6], rsa_sk, rsa_n);//IDc,IDtgs,TS1
                ID_c=data[0];
                setKc(ID_c,data[1]);//写入数据库，返回成功报文8
                Boolean result = true;//结果
                packSend(socket, m8(result, rsa_sk, rsa_n, AS_IP, Basic_info[3]));//结果
                break;
            }
            case "16": {//TGS的时间戳同步
                data = m16_d(Basic_info[6], rsa_pk, rsa_n);//IDc,TS4
                setTS(data[1]);
                break;
            }
            case "0": {//Client的离线请求
                data = m23a_d(Basic_info[6], rsa_sk, rsa_n);//IDc
                packSend(socket,m24("11", Kc,AS_IP,Basic_info[3]));//离线反馈
                break;
            }

        }

    }

}
