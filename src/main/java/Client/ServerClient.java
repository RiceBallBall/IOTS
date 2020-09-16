package Client;


import TGS.SerPanel;
import TGS.Tools;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerClient extends Message {
    public Socket socket;
    int as_port;
    int tgs_port;
    int ser_port;
    public String C_IP;
    public String TGS_IP;//TGS端IP
    public String AS_IP;//AS端IP
    public String V_IP;//Server端IP
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
    private int v_pk;
    private int v_n;
    public String K_c;
    public String Kc_v;
    public String Kc_tgs;
    public String TS;
    public String LT1;
    public String TGT;
    public String ST;
    public String name;
    public String fileData;


    ServerClient() throws IOException {
        ID_tgs = "TGS00001";
        ID_as = "as000001";
        ID_v = "ser00001";
        ID_c = "IDc00001";
        tgs_pk = 121;
        tgs_n = 1679;
        as_pk = 2317;
        as_n = 3071;
        v_pk = 2501;
        v_n = 4559;
        as_port = 8888;
        tgs_port = 4444;
        ser_port = 8888;
        // socket = new Socket();

        C_IP = "192168002001";
        AS_IP = "127001001001";
        TGS_IP = "127001001001";
        V_IP = "192168000000";
        AD_as = "";
        AD_ser = "";
        AD_tgs = "";
        //socket = new Socket("172.20.10.3", 9999);
        TS = Tools.getTS();
        Kc_tgs = "";
        LT1 = "60";

        // ToAS = new SerPanel("TGS", tgs_pk, tgs_sk, tgs_n, socket, true);
    }

    public void setTS(String tsN) {
        TS = tsN;
        return;
    }

    public void setK_c(String kc) {
        K_c = kc;
    //bufferedReader.close();
        }

    public static void main(String[] args) throws IOException, InterruptedException {
        String ID_c = "ID000001";
        String K_c = "12345678";
        String IPs = "192168001001";
        String IPr = "127010001001";

        Socket socket = new Socket();
        ServerClient client = new ServerClient();
        socket.connect(new InetSocketAddress("192.168.43.3", 9999), 10000);
        // socket.setSoTimeout(10000);//设置超时时间
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

////        client.ST=client.ST("12345678",ID_c, client.C_IP, client.ID_v, client.v_pk, client.v_n, client.TS, client.LT1);
////        String mes_5=client.m5(client.ST, ID_c, client.C_IP, client.TS, "12345678", client.C_IP, client.V_IP);
//        String clientMessage = "";
//     //   client.ST=client.ST("12345678",ID_c, client.C_IP, client.ID_v, client.v_pk, client.v_n, client.TS, client.LT1);
//   //     String mes_5=client.m5(client.ST, ID_c, client.C_IP, client.TS, "12345678", client.C_IP, client.V_IP);
//        String mes_9=client.m9("12345678",client.C_IP,client.V_IP);
//        String mes_11=client.m11("1.txt","1","0","000000001111","12345678",client.C_IP,client.V_IP);
//        String mes_13=client.m13("1.txt","12345678", client.C_IP, client.V_IP);
//        String mes_20=client.m20("1.txt","12345678",client.C_IP,client.V_IP);
//        String mes_23=client.m23s("11",ID_c,"12345678",client.C_IP,client.V_IP);


//        writer.println(mes_23);
        writer.flush();
//        //writer.close();
//        clientMessage = bufferedReader.readLine();
//        System.out.println(clientMessage);

        while (true) {

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


    public boolean Clientexcecution(String Package) {
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
                    return true;
                } else if (feedback.equals("00")) {//反馈失败,拒绝注册
                    //UI弹窗
                    System.out.println("申请失败,无法进行注册");
                    return false;
                }
            }
            case "2": {//登陆验证成功
                //C->AS  发送1号请求AS登陆验证
                setTS(Tools.getTS());//获取时间戳
                // packSend(socket, m1(ID_c, ID_tgs, TS, C_IP, AS_IP));//发送报文
                //AS->C  验证成功反馈2号
                data = m2_d(Basic_info[6], K_c);
                Kc_tgs = data[3];
                TGT = data[4];
                return true;
            }
            case "4": {//TGS->C身份认证成功
                data = m4_d(Basic_info[6], Kc_tgs);
                ST = data[3];
                TS = Tools.getTS();
                Kc_v=data[0];
                return true;
            }
            case "6": {//TGS->C身份认证成功
                data = m6_d(Basic_info[6], Kc_v);
                //ui跳转功能界面（上传下载删除推出）
                return true;
            }
            case "12": {//ack
                data = m12_d(Basic_info[6],Kc_v);
                //UI操作插入
                System.out.println("suc");
                return true;
            }
            case "18": {//TGS->C身分验证失败
                data = m18_d(Basic_info[6], tgs_pk, tgs_n);
                //UI操作插入
                System.out.println("TGS身份验证失败,无法登录");
                return false;
            }
            case "17": {//登陆验证失败
                data = m17_d(Basic_info[6], as_pk, as_n);
                //UI操作插入
                System.out.println("AS验证失败,无法登陆");
                return false;
            }

            case "19": {//S->C 认证反馈失败
                data = m19_d(Basic_info[6], Kc_v);
                //UI操作插入
                System.out.println("Server验证失败,无法登陆");
                return false;
            }
            case "22": {//S->C 删除结果
                data = m22_d(Basic_info[6], Kc_tgs);
                String feedback = data[1];
                if (feedback.equals("11")) {
                    //UI弹窗
                    System.out.println("删除成功");
                    return true;
                } else if (feedback.equals("00")) {
                    //UI弹窗
                    System.out.println("删除失败");
                    return false;
                }
                //UI操作插入

            }
            case "24": {//S->C 离线反馈
                //TGS->C身份认证成功
                data = m24_d(Basic_info[6], Kc_tgs);
                String feedback = data[1];
                if (feedback.equals("11")) {
                    //UI弹窗
                    System.out.println("离线失败");
                    return true;
                } else if (feedback.equals("00")) {
                    //UI弹窗
                    System.out.println("离线失败");
                    return false;
                }
                //UI操作插入
            }
        }
        return false;
    }

    public boolean packSend(Socket socket, String sen_package, PrintWriter writer) {
        writer.println(sen_package);
        System.out.println("sem_page:"+sen_package);
        writer.flush();
        return true;
    }

    public void ClientAction(String type, Socket socket, BufferedReader bufferedReader, PrintWriter writer) {
        String mes_sen;
        switch (type) {
            case "1": {
                TS = Tools.getTS();
                mes_sen = m1(ID_c, ID_tgs, TS, C_IP, AS_IP);
                packSend(socket, mes_sen, writer);
                System.out.println("m1:" + mes_sen);
//                try {
//                    String string = bufferedReader.readLine();
//                    System.out.println("收到的报文: "+ string);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
                break;
            }
            case "3": {
                TS = Tools.getTS();
                mes_sen = m3(ID_v, TGT, ID_c, C_IP, TS, Kc_tgs, C_IP, TGS_IP);
                packSend(socket, mes_sen, writer);
                break;
            }
            case "5": {
                TS = Tools.getTS();
                System.out.println("ST"+ST);
                System.out.println("IDc"+ID_c);
                System.out.println("Kcv"+Kc_v);

                mes_sen = m5(ST, ID_c, C_IP, TS, Kc_v, C_IP, V_IP);
                System.out.println(mes_sen);
                packSend(socket, mes_sen, writer);
                break;
            }
            case "7": {
                //K_c从UI获取
                mes_sen = m7(ID_c, K_c, as_pk, as_n, C_IP, AS_IP);
                packSend(socket, mes_sen, writer);
                System.out.println("7:" + mes_sen);
                break;
            }
            case "9": {
                mes_sen = m9(Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen, writer);
                break;
            }
            case "11": {
                //UI操作，获取文件名
                //文件分块

                name = name.substring(0, 8);
                String file[] = Tools.dataSplite(fileData, 1024);

                System.out.println("fileData:  " + fileData);
                System.out.println("client file name: " + name);

                String sum = String.valueOf(file.length);
                for (int i = 0; i < file.length; i++) {
                    mes_sen = m11(name.substring(0, 8), sum, String.valueOf(i), file[i], Kc_v, C_IP, V_IP);
                    System.out.println("mes"+mes_sen);
                    packSend(socket, mes_sen, writer);

                    try {
                        String string = bufferedReader.readLine();
                        System.out.println("收到的报文: " + string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
            case "13": {
                //UI操作，获取文件名

                mes_sen = m13(name, Kc_v, C_IP, V_IP);
                System.out.println(mes_sen);

                packSend(socket, mes_sen, writer);

                break;
            }
            case "20": {
                //UI操作，获取文件名
                // String name = "";
                mes_sen = m20(name, Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen, writer);

                break;
            }
            case "21": {
                //UI操作，获取文件名
                // String name = "";
                mes_sen = m21(name, Kc_v, C_IP, V_IP);
                packSend(socket, mes_sen, writer);

                break;
            }
            case "23a": {
                TS = Tools.getTS();
                mes_sen = m23a("11", ID_c, as_pk, as_n, C_IP, AS_IP);
                packSend(socket, mes_sen, writer);
                break;
            }
            case "23s": {
                TS = Tools.getTS();
                mes_sen = m23s("11", ID_c, K_c, C_IP, AS_IP);
                packSend(socket, mes_sen, writer);
                break;
            }
        }
    }
}
