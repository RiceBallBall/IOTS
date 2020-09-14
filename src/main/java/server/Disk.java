package netDisk;

import network.safety.server.Message;
import network.safety.server.Tools;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Disk extends Message implements Callable {
    private Socket socket = null;
    private String Kc_v;
    private String userName;
    public String datagramType = "";

    public final int time = 3000;

    ArrayList<String> arrayListFile = new ArrayList<String>(); //文件列表

    public Disk(Socket socket, String Kc_v, String userName) {
        this.socket = socket;
        this.Kc_v = Kc_v;
        this.userName = userName;
        try {
            this.socket.setSoTimeout(time);
            String dir = System.getProperty("user.dir") + "/" + userName;
            File file = new File(dir);
            if(!file.exists())file.mkdir();
        }catch (SocketException e){
            e.printStackTrace();
        }
    }

    public String call(){
        InetAddress addr = socket.getInetAddress();
        try(InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());)
        {
            while(true){
                String datagram = bufferedReader.readLine();
                datagramType = datagram.substring(6, 12); //判断报文类型
                String clientData = datagram.substring(100, datagram.length() - 32); //截取message
                if(datagramType.equals("001001"))refreshCatalog(writer);
                if(datagramType.equals("010100")){//ack
                    String[] telegram = m20_d(clientData, Kc_v);
                    String fileName = telegram[0];
                    String ack = m12("00", "10", Kc_v, socket.getInetAddress().toString(), addr.getHostAddress());
                    writer.println(ack);
                    writer.flush();
                    uploadFile(fileName, writer, bufferedReader);
                }
                if(datagramType.equals("001101")){//ack
                    String[] telegram = m13_d(clientData, Kc_v);
                    String fileName = telegram[0];
                    String ack = m12("00", "10", Kc_v, socket.getInetAddress().toString(), addr.getHostAddress());
                    writer.println(ack);
                    writer.flush();
                    downloadFile(fileName, writer, bufferedReader);
                }
                if(datagramType.equals("010101")){//ack
                    String fileName = "";
                    String[] telegram = m21_d(clientData, Kc_v);
                    String filename = telegram[0];
                    String ack = m12("00", "10", Kc_v, socket.getInetAddress().toString(), addr.getHostAddress());
                    writer.println(ack);
                    writer.flush();
                    deteleFile(fileName, writer);
                }
                if(datagramType.equals("000000")){// 23号报文
                    String[] offlineData = m23s_d(clientData, Kc_v);
                    //1,IDc与status写入log
                    writer.println(m24("00", Kc_v, socket.getInetAddress().toString(), addr.getHostAddress())); //offl_fb
                    writer.flush();
                    //2,离线信息写入log
                    socket.close(); //离线关闭socket
                    return "";
                }
                else {
                    // bufferedWriter.write(m19("01", Kc_v, socket.getInetAddress().toString(), addr.getHostAddress())); //m19的修改
                    // bufferedWriter.flush();
                    continue;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    public String refreshCatalog(PrintWriter writer){
        String dir = System.getProperty("user.dir") + "/" + userName;
        arrayListFile.clear();
        getFile(dir, 0);
        String fileName = "";
        for(int i = 0; i < arrayListFile.size(); ++i){
            if(i != arrayListFile.size() - 1)fileName += arrayListFile.get(i) + "#";
        }
        writer.write(m10(arrayListFile.size() + "", fileName, Kc_v, socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress()));
        writer.flush();
        return "";
    }
    public String uploadFile(String fileName, PrintWriter writer, BufferedReader bufferedReader) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + userName + "/" + fileName);//如果有这覆盖原文件
        if(file.exists()) file.delete();
        try{
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        int sum = 100;
        ArrayList<String> arrayListMessage = new ArrayList<>();
        int turn = 0;
        while(turn < sum) {
            String line = bufferedReader.readLine();// 奇偶校验位 假设ack不会丢失
            if (line != null) {//成功收到报文
                String situation = "11";
                String[] fields_11 = m11_d(line.substring(100, line.length() - 32), Kc_v);
                sum = Integer.parseInt(fields_11[1]);
                if (turn == sum - 1) situation = "00";
                arrayListMessage.set(turn, fields_11[3]);// turn的长度是2位还是随意
                String ack = m12(String.valueOf(turn), situation, Kc_v, socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress());
                writer.println(ack);
                writer.flush();
                turn++;
            } else {
                String ack = m12(String.valueOf(turn), "01", Kc_v, socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress());
                writer.println(ack);
                writer.flush();
            }
        }
        String context = "";
        for(int i = 0; i < arrayListMessage.size(); ++i)context += arrayListMessage.get(i);
        Tools.FileOut(System.getProperty("user.dir") + "/" + userName + "/" + fileName, context);
        return "";
    }
    public String downloadFile(String fileName, PrintWriter writer, BufferedReader bufferedReader) throws IOException {
        String Name = System.getProperty("user.dir") + "/" + userName + "/" + fileName;
        String context = Tools.FileIn(Name);
        String[] datagram = Tools.dataSplite(context, 1024);
        for(int i = 0; i < datagram.length; ++i){
            String message = m14(fileName, String.valueOf(datagram.length), String.valueOf(i), datagram[i], Kc_v,
                    socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress());
            writer.println(message);
            writer.flush();
            String ack = bufferedReader.readLine();
            ack = ack.substring(100, ack.length() - 32);
            String[] feedback = m12_d(ack, Kc_v);
            int turn = 0;
            while (!feedback[1].equals("11") && turn < 10){
                if(feedback[1].equals("01")){
                    writer.println(message);
                    writer.flush();
                }
                if(feedback[1].equals("00"))return "";
                ack = bufferedReader.readLine();
                ack = ack.substring(100, ack.length() - 32);
                feedback = m12_d(ack, Kc_v);
            }
            if(turn == 10){
                // ack = m12(String.valueOf(-1), "01", Kc_v, socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress());
                // bufferedWriter.write(ack);
                // bufferedWriter.flush();
                return "";
            }
        }
        return "";
    }
    public String deteleFile(String fileName,  PrintWriter writer){
        String Name = System.getProperty("user.dir") + "/" + userName + "/" + fileName;
        File file = new File(Name);
        if(file.exists()){
            file.delete();
            String feedback = m22("00", "11", Kc_v, socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress());
            writer.println(feedback);
            writer.flush();
        }
        else {
            String feedback = m22("00", "01", Kc_v, socket.getInetAddress().toString(), socket.getInetAddress().getHostAddress());
            writer.println(feedback);
            writer.flush();
        }
        return "";
    }

    private  void getFile(String path,int deep){
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for(int i=0;i<array.length;i++)
        {
            if(array[i].isFile())//如果是文件
            {
                for (int j = 0; j < deep; j++)//输出前置空格
                    //System.out.print(" ");
                // 只输出文件名字
                    arrayListFile.set(arrayListFile.size(), array[i].getName());
                //System.out.println(array[i].getName());
                // 输出当前文件的完整路径
                // System.out.println("#####" + array[i]);
                // 同样输出当前文件的完整路径   大家可以去掉注释 测试一下
                // System.out.println(array[i].getPath());
            }
            /*else if(array[i].isDirectory())//如果是文件夹
            {
                for (int j = 0; j < deep; j++)//输出前置空格
                    System.out.print(" ");

                System.out.println( array[i].getName());
                //System.out.println(array[i].getPath());
                //文件夹需要调用递归 ，深度+1
                getFile(array[i].getPath(),deep+1);
            }*/
        }
    }
}
