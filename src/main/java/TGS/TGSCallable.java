package TGS;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

class TGSCallable extends Message implements Callable<Boolean>, DES {
    private int rsa_pk;
    private int rsa_sk;
    private int rsa_n;
    private int ser_pk;
    private int ser_n;
    private String Kc = "ID000002";
    private String ID_c;
    private Socket socket = null;
    private String TGS_IP;
    private String TS;
    private String ID_tgs;
    private String LT;
    private SerPanel Panel;
    private Tools tools;


    public TGSCallable(Socket Socket) throws IOException {
        tools = new Tools();
        this.socket = Socket;
        rsa_n = 1147;
        rsa_pk = 643;
        rsa_sk = 907;
        ser_n=4459;
        ser_pk=2501;
        Panel = new SerPanel("TGS",rsa_pk,rsa_sk,rsa_n,socket);
        TGS_IP = "192168043188";
        TS = tools.getTS();
        LT = "60";
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

                //收到后的
                int inf;
                String rec_package = "";
                if ((inf = bufferedReader.read()) != -1) {
                    rec_package = (char) inf + bufferedReader.readLine();//message
                    System.out.println("Received a messege from " + addr.getHostAddress());
                    Panel.textArea4.setText(rec_package);//显示收到的包
                    TGSexcecution(rec_package);
                }
            }
            //Panel.textArea5.setText();显示解码的包
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
        this_panel.textArea5.setText(bas);
    }

    public void TGSexcecution(String rec_package) throws IOException {
        String Basic_info[] = Divide(rec_package);
        String data[];
        switch (Basic_info[1]) {//IPr Basic_info[3]
            case "3": {//C->TGS请求，需要回复4号报文
                data=m3_d(Basic_info[6],rsa_sk,rsa_n);
                String reply;
                if (data==null){
                    reply=m18("00",rsa_sk,rsa_n,TGS_IP,Basic_info[3]);
                }else {
                    String Kc_v="12345678";
                    String st=ST(Kc_v,data[2],Basic_info[3],data[0],ser_pk,ser_n,TS,LT);
                    reply=m4(Kc_v,data[0],TS,st,data[5],TGS_IP,Basic_info[3]);
                }
                mes_display(Basic_info,data,Panel);
                packSend(socket,reply);
                break;
            }
            case "15": {//AS->TGS
                data=m15_d(Basic_info[6],rsa_sk,rsa_n);

                break;
            }
        }

    }

}
