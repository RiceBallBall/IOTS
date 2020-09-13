package AS;


import TGS.Message;
import TGS.SerPanel;
import TGS.TGS;
import TGS.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends Message {

    // private SerPanel ToAS;
    public Socket socket;
    int as_port;
    int tgs_port;
    int ser_port;
    public String C_IP;
    private String TGS_IP;//TGS端IP
    private String AS_IP;//AS端IP
    private String V_IP;//Server端IP
    private String AD_as;
    private String AD_ser;
    private String AD_tgs;
    public String ID_c;
    private String ID_as;
    private String ID_v;
    private String ID_tgs;
    private int as_pk;
    private int as_n;
    private int tgs_pk;
    private int tgs_n;
    private String K_c;
    private String Kc_v;
    public String Kc_tgs;
    public String TS;
    public String LT1;
    public String TGT;
    public String ST;


    Client() throws IOException {
        ID_tgs = "TGS00001";
        ID_as = "as000001";
        ID_v = "ser00001";
        tgs_pk = 121;
        tgs_n = 1679;
        as_pk = 2317;
        as_n = 3071;
        as_port = 8888;
        tgs_port = 4444;
        ser_port = 8888;
        C_IP = "192168002001";
        AS_IP = "127001001001";
        TGS_IP = "127001001001";
        V_IP = "192168000000";
        AD_as = "192.168.0.0";
        AD_ser = "";
        AD_tgs = "";
        socket = new Socket(AS_IP, 8888);
        TS = Tools.getTS();
        Kc_tgs = "";
        // ToAS = new SerPanel("TGS", tgs_pk, tgs_sk, tgs_n, socket, true);
    }

    public void setTS(String tsN) {
        TS = tsN;
        return;
    }

    public static void main(String[] args) throws IOException {
        Client client_ = new Client();
        InputStreamReader isr;
        BufferedReader br;
        OutputStreamWriter osw;
        BufferedWriter bw;
//        String ID_c = "ID000001";
//        String K_c = "12345678";
//        String IPs = "192168001001";
//        String IPr = "127010001001";
//        Scanner in = new Scanner(System.in);

        try {

            //  Socket socket = new Socket("localhost", 8888);
//     System.out.println(socket.getInetAddress());// 输出连接者的IP。
            System.out.println("成功连接服务器");
            // while (true) {
            osw = new OutputStreamWriter(client_.socket.getOutputStream());
            bw = new BufferedWriter(osw);
            //测试内容
//            String mes_7 = client_.m7(ID_c, K_c, 2317 ,3071, IPs, IPr);
//            String mes_1= client_.m1(ID_c, client_.ID_tgs, client_.TS, IPs,IPr);
//            String mes_23a= client_.m23a("00",ID_c,2317,3071,IPs,IPr);
//            str = in.nextLine();
            bw.write("mes");
            bw.flush();
//            isr = new InputStreamReader(client_tgs.socket.getInputStream());
//            br = new BufferedReader(isr);
//            String rec="";
//            int c;
//                if((rec= br.readLine())!=null) {
//                    client_tgs.ToAS.textArea3.setText(rec);
//                    System.out.print("回复:" + rec);//收到消息
//                    String bas[] = client_tgs.Divide(rec);
//                    String rec_d[] = client_tgs.m8_d(rec, 2371, 3071);
//                    client_tgs.mes_display(bas, rec_d, client_tgs.ToAS);
//                    System.out.println(client_tgs.socket.getInetAddress() + " : " + rec_d);
//                }
            //测试内容结束
            //  }
            while (true) {
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        //  this_panel.textArea5.setText(bas);
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

    public void Clientexcecution(String Package) {
        String data[];
        String Basic_info[] = Divide(Package);
        switch (Basic_info[1]) {
            case "8": {//注册反馈
                //C->AS  发送7号请求注册，从UI界面操作
//                packSend(socket, m7(ID_c, K_c, rsa_pk, rsa_n, C_IP, AS_IP));//发送报文
                //AS->C  接受8号反馈
                data = m8_d(Basic_info[6], as_pk, as_n);
                String feedback = data[0];
                if (feedback.equals("11")) {//反馈正确,进行注册操作
                    //UI弹窗
                    System.out.println("注册成功,进行登陆操作");
                } else if (feedback.equals("00")) {//反馈失败,拒绝注册
                    //UI弹窗
                    System.out.println("申请失败,无法进行注册");
                }
                break;
            }
            case "2": {//登陆验证成功
                //C->AS  发送1号请求AS登陆验证
                setTS(Tools.getTS());//获取时间戳
                packSend(socket, m1(ID_c, ID_tgs, TS, C_IP, AS_IP));//发送报文
                //AS->C  验证成功反馈2号
                data = m2_d(Basic_info[6], ID_c);
//                TS = data[1];
//                LT1 = data[2];
                Kc_tgs = data[3];
                TGT = data[4];
                //C->TGS  发送3号请求TGS登陆验证
                TS = Tools.getTS();
                String c_to_tgs = m3(ID_v, TGT, ID_c, C_IP, TS, Kc_tgs, C_IP, TGS_IP);
                //packSend(socket,c_to_tgs);发给tgs
                break;
            }
            case "4": {//TGS->C身份认证成功
                data = m4_d(Basic_info[6], Kc_tgs);
                ST = data[3];
                TS = Tools.getTS();
                String to_ser = m5(ST, ID_c, C_IP, TS, Kc_v, C_IP, V_IP);
                //packSend(socket,to_ser);//发给ser
                break;
            }
            case "6": {//TGS->C身份认证成功
                data = m6_d(Basic_info[6], Kc_v);
                break;
            }
            case "18": {//TGS->C身分验证失败
                data = m18_d(Basic_info[6], tgs_pk, tgs_n);
                //UI操作插入
                System.out.println("TGS身份验证失败,无法登录");
                break;
            }
            case "17": {//登陆验证失败
                data = m17_d(Basic_info[6], as_pk, as_n);
                //UI操作插入
                System.out.println("AS验证失败,无法登陆");
            }

            case "19": {//S->C 认证反馈失败
                //TGS->C身份认证成功
                data = m19_d(Basic_info[6], Kc_v);
                //UI操作插入
                System.out.println("Server验证失败,无法登陆");
            }
            case "22": {//S->C 删除结果
                //TGS->C身份认证成功
                data = m22_d(Basic_info[6], Kc_tgs);
                //UI操作插入
            }
            case "24": {//S->C 离线反馈
                //TGS->C身份认证成功
                data = m24_d(Basic_info[6], Kc_tgs);
                //UI操作插入
            }
        }
    }

    public void ClientAction(String type, Socket socket) {
        String mes_sen;
        switch (type) {
            case "1": {
                TS = Tools.getTS();
                mes_sen = m1(ID_c, ID_tgs, TS, C_IP, AS_IP);
                packSend(socket, mes_sen);
                break;
            }
            case "3": {
                TS = Tools.getTS();
                mes_sen = m3(ID_v, TGT, ID_c, C_IP, TS, Kc_tgs, C_IP, TGS_IP);
                packSend(socket, mes_sen);
                break;
            }
            case "5": {
                TS = Tools.getTS();
                mes_sen = m5(ST, ID_c, C_IP, TS, Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen);
                break;
            }
            case "8": {
                //K_c从UI获取
                mes_sen = m7(ID_c, K_c, as_pk, as_n, C_IP, AS_IP);
                packSend(socket, mes_sen);
                break;
            }
            case "9": {
                mes_sen = m9(Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen);

                break;
            }
            case "11": {
                //UI操作，获取文件名
                String name = "";
                //文件分块
                String file[] = new String[4];//len change
                String sum = String.valueOf(file.length);
                for (int i = 0; i < file.length; i++) {
                    mes_sen = m11(name, sum, String.valueOf(i), file[i], Kc_v, C_IP, V_IP);
                    packSend(socket, mes_sen);
                }

                break;
            }
            case "13": {
                //UI操作，获取文件名
                String name = "";
                mes_sen = m13(name, Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen);

                break;
            }
            case "20": {
                //UI操作，获取文件名
                String name = "";
                mes_sen = m20(name, Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen);

                break;
            }
            case "21": {
                //UI操作，获取文件名
                String name = "";
                mes_sen = m21(name, Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen);

                break;
            }
            case "23a": {
                TS = Tools.getTS();
                mes_sen = m23a("11", ID_c, as_pk, as_n, C_IP, AS_IP);
                packSend(socket, mes_sen);
                break;
            }
            case "23s": {
                TS = Tools.getTS();
                mes_sen = m23s("11", ID_c, K_c, C_IP, AS_IP);
                packSend(socket, mes_sen);
                break;
            }
        }
    }
}