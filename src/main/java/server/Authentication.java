//package netDisk;
//
//import network.safety.server.Message;
//
//import java.io.*;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.Callable;
//
//public class Authentication extends Message implements Callable {
//
//    private final int privateKey = 1997;//server私钥
//    public final int publicKey = 2501;//server公钥
//    public final int rsaN = 4559; // N
//    private Socket socket;
//
//    private String[] messageT;
//    private String[] messageA;
//
//    public Authentication(Socket socket){
//        this.socket = socket;
//    }
//
//    public String call(){
//        //if(!verifyClient() || !clientVerify())return "false";
//        if(!verifyClient())return "false";// 验证成功则到第二步，否则发生１９号报文，结束验证
//        if(!clientVerify())return "false"; // 验证成功，则进入下一步，否则不返回报文，直接结束
//        return messageT[0] + " " + messageT[1];
//    }
//
//    public boolean verifyClient(){//验证从client的密文
//        InetAddress adder = socket.getInetAddress();
//        try(InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            PrintWriter writer = new PrintWriter(socket.getOutputStream());)
//        {
//            String cipher = bufferedReader.readLine();
//            cipher = cipher.substring(100, cipher.length() - 32); // 截断报文，获得message
//            messageT = ST_d(cipher.substring(0, 512), privateKey, rsaN); // Kc_v IDc ADc IDv TS4 LT2 //////////////////////!!!长度不够 // 解密ＳＴ
//            messageA = m5_d(cipher, messageT[0]);
//            if (!messageA[1].equals(messageT[1]) || !messageA[2].equals(messageT[2])) { // 验证ADc IDc
//                String feedback = m19("00", messageT[0], socket.getInetAddress().toString(), adder.getHostAddress());
//                writer.println(feedback);
//                writer.flush();
//                return false;// 验证失败
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean clientVerify(){//发送给client的密文
//        InetAddress adder = socket.getInetAddress();
//        try(PrintWriter writer = new PrintWriter(socket.getOutputStream());)
//        {
//            String messageClient = m6(getTS(), messageT[0], socket.getInetAddress().toString(), adder.getHostAddress());
//            writer.println(messageClient);
//            writer.flush();
//        }catch (IOException e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public String getTS(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
//        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
//        return date;
//    }
//}
