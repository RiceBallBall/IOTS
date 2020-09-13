package AS;


import TGS.Message;
import TGS.SerPanel;
import TGS.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends Message {
    private String ID_tgs;
    private int tgs_pk;
    private int tgs_sk;
    private int tgs_n;
   // private SerPanel ToAS;
    public Socket socket;
    public String AS_IP;
    int as_port;
    public String TS;

    Client() throws IOException {
        ID_tgs = "TGS00001";
        tgs_pk = 121;
        tgs_sk = 1453;
        tgs_n = 1679;
        as_port = 8888;
        AS_IP = "127.0.0.1";
        socket = new Socket(AS_IP, 8888);
        TS = Tools.getTS();
       // ToAS = new SerPanel("TGS", tgs_pk, tgs_sk, tgs_n, socket, true);
    }

    public static void main(String[] args) throws IOException {
        Client client_ = new Client();
        InputStreamReader isr;
        BufferedReader br;
        OutputStreamWriter osw;
        BufferedWriter bw;
        String ID_c = "ID000001";
        String K_c = "12345678";
        String IPs = "192168001001";
        String IPr = "127010001001";
//        Scanner in = new Scanner(System.in);

        try {

            //  Socket socket = new Socket("localhost", 8888);
//     System.out.println(socket.getInetAddress());// 输出连接者的IP。
            System.out.println("成功连接服务器");
            // while (true) {
            osw = new OutputStreamWriter(client_.socket.getOutputStream());
            bw = new BufferedWriter(osw);
            //测试内容
            String mes_7 = client_.m7(ID_c, K_c, 2317 ,3071, IPs, IPr);
            String mes_1= client_.m1(ID_c, client_.ID_tgs, client_.TS, IPs,IPr);
            String mes_23a= client_.m23a("00",ID_c,2317,3071,IPs,IPr);
//            str = in.nextLine();
            bw.write(mes_23a);
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
            while (true){}
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
}