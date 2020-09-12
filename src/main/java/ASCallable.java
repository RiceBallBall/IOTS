import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

class ASCallable extends Message implements Callable<Boolean>, DES {
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
    public BufferedReader tgs_br;
    public BufferedWriter tgs_bw;
    private ASPanel ToTgsPanel;
    public ASCallable(ServerSocket serverSocket,Socket the_socket,BufferedReader bufferedReader, BufferedWriter bufferedWriter, ASPanel tgsP) throws IOException {
        tools = new Tools();
        this.socket = the_socket;
        rsa_n = 151;
        rsa_pk = 359;
        rsa_sk = 667;
        Panel = new ASPanel("AS",rsa_pk,rsa_sk,rsa_n,serverSocket);
        AS_IP = "192168043188";
        TS = tools.getTS();
        LT = "60";
        tgs_n = 1679;
        tgs_pk = 775;
        //   so_tgs=socket_TGS;
        tgs_br = bufferedReader;
        tgs_bw = bufferedWriter;
        ToTgsPanel = tgsP;


    }

    public void setTS(String tsN) {
        TS = tsN;
        return;
    }

    public void setKc(String ID_c, String psw) {
        Kc = DES.toString(DES.encode(psw, ID_c));
    }


    public Boolean call() {//需要完成的任务
        System.out.println("---------");
        setTS(tools.getTS());
        InetAddress addr = socket.getInetAddress();
        Panel.run();//打开窗口
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {//信息输入
            packSend(socket, "[Connected....Waiting for action.....]");
            while (true) {
                //监听TGS
                int inf_tgs;
                String tgs_package = "";
                if ((inf_tgs = tgs_br.read()) != -1) {
                    do {
//                  String recTgs_Package=tgs_br.readLine();//tgs mes
                        tgs_package += (char) inf_tgs;
                    }
                    while ((inf_tgs = tgs_br.read()) != -1);
                    String Basic_tgs_info[] = Divide(tgs_package);
                    String data[] = m16_d(Basic_tgs_info[6], rsa_sk, rsa_n);//IDc,TS4
                    setTS(data[1]);
                    ToTgsPanel.textArea4.setText(tgs_package);
                    mes_display(Basic_tgs_info, data, ToTgsPanel);
                }
                //收到后的设置显示收到的报文
                int inf;
                String rec_package = "";
                if ((inf = bufferedReader.read()) != -1) {
                    rec_package = (char) inf + bufferedReader.readLine();//message
                    System.out.println("Received a messege from " + addr.getHostAddress());
                    Panel.textArea4.setText(rec_package);//显示收到的包
                    ASexcecution(rec_package);
                }
            }
            //Panel.textArea5.setText();显示解码的包

//            String ar[] = m2_d(inf[6], Kc);
//            String example = "";
//            for (int i = 0; i < ar.length; i++) {
//                System.out.println(ar[i]);
//                example += ar[i] + "#";
//            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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

    public void mes_display(String Basic[], String[] mes, ASPanel this_panel) {
        String bas = "";
        bas = bas + "控制字段：" + Basic[0] + "\n";
        bas = bas + "报类型编号：" + Basic[1] + "\n";
        bas = bas + "发送IP：" + Basic[2] + "\n";
        bas = bas + "接收IP：" + Basic[3] + "\n";
        bas = bas + "包长度：" + Basic[4] + "\n";
        bas = bas + "保留字段：" + Basic[5] + "\n";
        bas = bas + "数据内容：" + "\n";
        for (int i = 0; i < mes.length; i++) {
            bas = mes[i] + "\n";
        }
        this_panel.textArea5.setText(bas);
    }

    public void ASexcecution(String rec_package) throws IOException {
        String Basic_info[] = Divide(rec_package);
        String data[];
        switch (Basic_info[1]) {//IPr Basic_info[3]
            case "1": {//Client发起请求
                setTS(tools.getTS());
                data = m1_d(Basic_info[6]);
                ID_c = data[0];
                Panel.textArea2.setText(ID_c);
                ID_tgs = data[1];
                String Kc_tgs = DES.toString(DES.encode(ID_tgs, ID_c));
                mes_display(Basic_info,data,Panel);
                //查询tgs sql

                String tgt = TGT(Kc_tgs, ID_c, Basic_info[3], ID_tgs, TS, LT, tgs_pk, tgs_n);
                String re_to_Client = m2(ID_tgs, TS, LT, Kc_tgs, tgt, ID_c, AS_IP, Basic_info[3]);//反馈Client
                packSend(socket, re_to_Client);//返回给Client
                this.ToTgsPanel.textArea3.setText(re_to_Client);


                String re_to_tgs = m15(ID_c, Kc, TS, tgs_pk, tgs_n, AS_IP, TGS_IP);
                tgs_bw.write(re_to_tgs);//同步注册信息->tgs
                tgs_bw.flush();
                this.Panel.textArea3.setText(re_to_tgs);

                break;
            }
            case "7": {//Client的注册请求
                data = m7_d(Basic_info[6], rsa_sk, rsa_n);//IDc,IDtgs,TS1
                ID_c = data[0];
                Panel.textArea2.setText(ID_c);
                setKc(ID_c, data[1]);//写入数据库，返回成功报文8
                Boolean result = true;//结果
                String mes_8 = m8(result, rsa_sk, rsa_n, AS_IP, Basic_info[3]);
                packSend(socket, mes_8);//结果
                this.Panel.textArea3.setText(mes_8);
                mes_display(Basic_info,data,Panel);

                break;

            }
//            case "16": {//TGS的时间戳同步
//
//            }
            case "0": {//Client的离线请求
                data = m23a_d(Basic_info[6], rsa_sk, rsa_n);//IDc
                String mes_24 = m24("11", Kc, AS_IP, Basic_info[3]);
                packSend(socket, mes_24);//离线反馈
                this.Panel.textArea3.setText(mes_24);
                mes_display(Basic_info,data,Panel);
                break;
            }

        }

    }

}
