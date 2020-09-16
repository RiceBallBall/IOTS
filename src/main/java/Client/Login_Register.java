package Client;

import TGS.TGS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login_Register extends IOException {
    ServerClient myClient;
    Socket socket;
    JFrame frame = new JFrame("登录");
    JLabel nameStr = new JLabel("账号:");
    JLabel passwordStr = new JLabel("密码:");
    JTextField userID = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton buttonlogin = new JButton("登录");
    JButton buttonregister = new JButton("注册");
    String AS_IP = "";
    String TGS_IP = "";
    String SER_IP = "";



    Login_Register(ServerClient client) throws IOException {
        frame.setLayout(null);
        nameStr.setBounds(250, 200, 100, 25);
        frame.add(nameStr);
        passwordStr.setBounds(250, 250, 100, 25);
        frame.add(passwordStr);
        userID.setBounds(300, 200, 150, 25);
        frame.add(userID);
        password.setBounds(300, 250, 150, 25);
        frame.add(password);
        buttonlogin.setBounds(275, 300, 70, 25);
        frame.add(buttonlogin);
        buttonregister.setBounds(375, 300, 70, 25);
        frame.add(buttonregister);
        frame.setBounds(400, 100, 800, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        AS_IP = "127.0.0.1";
        TGS_IP="192.168.43.18";
        SER_IP="192.168.43.197";
        socket=new Socket();
        myClient=client;
        func();
    }


    //登录界面初始化
    public void func() {
        //为登录按钮添加监听器
        buttonlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ID = userID.getText();
                String passwd = new String(password.getPassword());
                myClient.ID_c=ID;
                myClient.K_c=passwd;
                String data[];
                try{
                    socket= new Socket();
                    socket.connect(new InetSocketAddress(AS_IP, 8888), 10000);
                    try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                         PrintWriter writer = new PrintWriter(socket.getOutputStream()))
                    {
                        myClient.ClientAction("1", socket, bufferedReader, writer);

                        String pack = bufferedReader.readLine();
                        System.out.println("pack_rec"+pack); //null 没有数据

                        myClient.ID_c=ID;
                        myClient.setK_c(passwd);
                        boolean j = false;
                        //等待回复
                        //InputStreamReader inputStreamReader = null;
                        //while (!j) {//包的验证
                            //等待回复 readline阻塞
                            j = myClient.verify_m(pack);
                            //if (!j) {//验Login_Register证失败则重新请求
                              //  myClient.ClientAction("1", socket, bufferedReader, writer);
                                //pack = bufferedReader.readLine();
                                j = myClient.verify_m(pack);
                            //}
                        //}
                        myClient.Clientexcecution(pack);
                            //System.out.println("12232421");
                    }catch (IOException em){
                        em.printStackTrace();
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                //socket连接as
                /*try {
                    socket.connect(new InetSocketAddress("172.20.10.2", 8888), 10000);
                    System.out.println("pack_rec1");
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    System.out.println("pack_rec2");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    myClient.ClientAction("1", socket);
                    String pack = bufferedReader.readLine();

                    myClient.ID_c=ID;
                    myClient.setK_c(passwd);
                    System.out.println("pack_rec0");
                    System.out.println("pack_rec3");
                    boolean j = false;
                    //等待回复
                    //InputStreamReader inputStreamReader = null;
                    System.out.println("pack_rec"+pack);

                    while (!j) {//包的验证
                        //等待回复 readline阻塞
                        j = myClient.verify_m(pack);
                        if (!j) {//验Login_Register证失败则重新请求
                            myClient.ClientAction("1", socket);
                            pack = bufferedReader.readLine();
                            j = myClient.verify_m(pack);
                        }
                    }
                    myClient.Clientexcecution(pack);
                    // socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }*/

                // socket.setSoTimeout(10000);//设置超时时间
                try{
                    System.out.println("准备连接");
                    socket.close();
                    socket = new Socket(); //不同的socket
                    System.out.println("开始连接TGS"); //没有运行
                    socket.connect(new InetSocketAddress(TGS_IP, 7777), 10000);
                    System.out.println("完成连接TGS");
                }catch (IOException ee){
                    ee.printStackTrace();
                }
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    {
                    myClient.ClientAction("3", socket, bufferedReader, writer);
                    String pack = bufferedReader.readLine();
                    System.out.println(pack);
                    //等待回复
                    boolean j = false;
                    j = myClient.verify_m(pack);
//                    while (!j) {//包的验证
                        //等待回复 readline阻塞
//                        if (!j) {//验证失败则重新请求
//                            myClient.ClientAction("3", socket, bufferedReader, writer);
//                            j = myClient.verify_m(pack);
//                       }
                        boolean tgt_v = myClient.Clientexcecution(pack);
                        if (tgt_v) {
                            System.out.println("TGS成功");

                            writer.close();
                            //断开TGT连接V
                            try {
                                bufferedReader.close();
                                writer.close();
                                socket.close();
                                socket = new Socket();
                                socket.connect(new InetSocketAddress(SER_IP, 5678), 10000);
                                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                writer = new PrintWriter(socket.getOutputStream());

                                myClient.ClientAction("5", socket, bufferedReader, writer);
                                /*InputStreamReader inputStreamReader1 = null;
                                inputStreamReader = new InputStreamReader(socket.getInputStream());
                                BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);*/
                                String pack1 = bufferedReader.readLine();
                                j = false;

                                    //等待回复 readline阻塞
                                    j = myClient.verify_m(pack1);
//                                    if (!j) {//验证失败则重新请求
//                                        myClient.ClientAction("5", socket, bufferedReader, writer);
//                                    }

                                boolean ser_v = myClient.Clientexcecution(pack1);
                                System.out.println("server pack: " + pack1);

                                if (ser_v) {
                                    JOptionPane.showMessageDialog(null, "登陆成功", "登陆成功", JOptionPane.NO_OPTION);
                                    //点击确定后会跳转到主窗口
                                    Function function = new Function(myClient, socket, bufferedReader, writer);
                                    function.run();
                                    try{
                                        function.join();
                                    }catch (InterruptedException er){
                                        er.printStackTrace();
                                    }
                                    frame.setVisible(false);
                                    //                            frame.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "登陆失败", "登陆失败", JOptionPane.NO_OPTION);

                                }

                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            //弹窗tgt验证失败
                            JOptionPane.showMessageDialog(null, "账号或密码错误", "账号或密码错误", JOptionPane.WARNING_MESSAGE);
                            //清除密码框中的信息
                            password.setText("");
                            //清除账号框中的信息
                            userID.setText("");
                        }
                    }

//                if(login.JudgeAdmin()==0) {
//                    //弹出账号或密码错误的窗口
//                    JOptionPane.showMessageDialog(null, "账号或密码错误", "账号或密码错误", JOptionPane.WARNING_MESSAGE);
//                    //清除密码框中的信息
//                    password.setText("");
//                    //清除账号框中的信息
//                    userID.setText("");
//
//                    //System.out.println("登陆失败");
//                } else {
//                    //弹出登录成功的窗口
//                    JOptionPane.showMessageDialog(null, "登陆成功", "登陆成功", JOptionPane.NO_OPTION);
//                    //点击确定后会跳转到主窗口
//                    frame.setVisible(false);
//                    Function function=new Function();
//
//                }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        //为注册按钮添加监听器
        buttonregister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //注册页面
                frame.setVisible(false);
                //socket = new Socket();
                try{
                    socket.connect(new InetSocketAddress("192.168.43.18", 8888), 10000); //as IP
                    AdminRegister adminRegister = new AdminRegister(myClient, socket);
                    adminRegister.init();
                    // socket.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
                //socket.connect(new InetSocketAddress("172.20.10.3", 8888), 10000);
                //AdminRegister adminRegister = new AdminRegister(myClient, socket);
                //adminRegister.init();
            }
        });
    }

    public static void main(String[] args) throws IOException {
        System.out.println("欢迎使用文件传输系统");
        ServerClient serverClient=new ServerClient();
        Login_Register login_register=new Login_Register(serverClient);
        login_register.func();
    }
}
