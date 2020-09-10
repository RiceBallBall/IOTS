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
    ASPanel Panel;
    Tools tools;

    public ASCallable(Socket socket, ASPanel asPanel) {
        tools = new Tools();
        this.socket = socket;
        rsa_n = 151;
        rsa_pk = 359;
        rsa_sk = 667;
        Panel = asPanel;
        AS_IP = "192168043188";
        TS = tools.getTS();

    }

    public void setTS(String tsN) {
        TS = tsN;
        return;
    }
    public void setKc(String ID_c,String psw){

    }


    public String call() {//需要完成的任务
        System.out.println("---------");
        setTS(tools.getTS());
        InetAddress addr = socket.getInetAddress();
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {//这里

            String rec_package = bufferedReader.readLine();//message
            System.out.println(addr.getHostName() + "'s message: " + rec_package + " from" + addr.getHostAddress());
            Panel.textArea4.setText(rec_package);
            String inf[] = Divide(rec_package);
            String ar[] = m2_d(inf[6], Kc);
            String example = "";
            for (int i = 0; i < ar.length; i++) {
                System.out.println(ar[i]);
                example += ar[i] + "#";
            }
            Panel.textArea5.setText(example);
            packSend(socket, "[Connected....Waiting for action.....]");
            /*if(bufferedReader.readLine().equals("send")){

            }
            else{

            }*/
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

    public ArrayList<String> packReceive(Socket socket, ArrayList<String> arrayList) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while (line != "exit") {
                Thread.sleep(100);
                line = bufferedReader.readLine();
                arrayList.add(line);
            }
            return arrayList;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    public void ASexcecution(String Package) {
        String Basic_info[] = Divide(Package);
        String data[];
        switch (Basic_info[1]) {
            case "1": {//Client发起请求
                data = m1_d(Basic_info[6]);
                ID_c=data[0];

                break;
            }
            case "7": {//Client的注册请求
                data = m7_d(Basic_info[6], rsa_sk, rsa_n);//IDc,IDtgs,TS1
                String ID = data[0];
                String Kc = data[1];//写入数据库，返回成功报文8
                Boolean result = true;//结果
                ID_c=data[0];
                packSend(socket, m8(result, rsa_sk, rsa_n, AS_IP, Basic_info[3]));
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
