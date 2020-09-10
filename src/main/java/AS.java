import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class AS extends Message implements RSA,DES{

//    String divided[] = {filed,type,IPs,IPr,len,retain,data};
    int []rsa;
    public AS(){
        int rsa[]=RSA.rsa();
    }
    public void ASexcecution(String Package){
        String Basic_info[]=Divide(Package);
        String data[];
        switch (Basic_info[1]){
            case "1":{//Client发起请求
                data=m1_d(Basic_info[6]);
                String Commmand;
                break;
            }
            case"7":{//Client的注册请求
                data=m7_d(Basic_info[6],rsa[4],rsa[2]);//IDc,IDtgs,TS1
                break;
            }
            case"16":{//TGS的时间戳同步
                data=m16_d(Basic_info[6],rsa[3],rsa[2]);//IDc,TS4
                break;
            }
            case "0":{//Client的离线请求
                data=m23a_d(Basic_info[6],rsa[4],rsa[2]);//IDc
                break;
            }

        }

    }
    public void AS_start(){
        InputStreamReader isr;
        BufferedReader br;
        OutputStreamWriter osw;
        BufferedWriter bw;
        String str;
        Scanner in = new Scanner(System.in);
        try {
            ServerSocket server = new ServerSocket(4444);// 在本机的4444端口开放Server
            Socket socket = server.accept();// 只要产生连接，socket便可以代表所连接的那个物体，同时这个server.accept()只有产生了连接才会进行下一步操作。
            System.out.println(socket.getInetAddress());// 输出连接者的IP。
            System.out.println("建立了一个连接！");
            while (true) {
                isr = new InputStreamReader(socket.getInputStream());
                br = new BufferedReader(isr);
                System.out.println(socket.getInetAddress() + ":" + br.readLine());
                osw = new OutputStreamWriter(socket.getOutputStream());
                bw = new BufferedWriter(osw);
                System.out.print("回复:");
                str = in.nextLine();
                bw.write(str + "\n");
                bw.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void send(){

    }

}
