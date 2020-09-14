package Client;

import TGS.TGS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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


    Login_Register() {
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
        AS_IP = "192.168.43.188";
        TGS_IP="192.168.43.112";
        SER_IP="192.168.43.3";
        func();
    }


    //登录界面初始化
    public void func() {
        //为登录按钮添加监听器
        buttonlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ID = userID.getText();
                String passwd = new String(password.getPassword());
                String data[];
                //socket连接as
                Socket socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress(AS_IP, 8888), 10000);
                    myClient = new ServerClient(socket);
                    myClient.ClientAction("1", socket);
                    InputStreamReader inputStreamReader = null;
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String pack = bufferedReader.readLine();
                    boolean j = false;
                    //等待回复
                    while (!j) {//包的验证
                        //等待回复 readline阻塞
                        j = myClient.verify_m(pack);
                        if (!j) {//验证失败则重新请求
                            myClient.ClientAction("1", socket);
                            j = myClient.verify_m(pack);

                        }
                    }
                    myClient.Clientexcecution(pack);
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                // socket.setSoTimeout(10000);//设置超时时间
                try {
                    socket.connect(new InetSocketAddress(TGS_IP, 8888), 10000);
                    myClient.ClientAction("3", socket);
                    InputStreamReader inputStreamReader = null;
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String pack = bufferedReader.readLine();
                    //等待回复
                    boolean j = false;
                    j = myClient.verify_m(pack);
                    while (!j) {//包的验证
                        //等待回复 readline阻塞
                        j = myClient.verify_m(pack);
                        if (!j) {//验证失败则重新请求
                            myClient.ClientAction("3", socket);
                            j = myClient.verify_m(pack);

                        }
                        boolean tgt_v = myClient.Clientexcecution(pack);
                        if (tgt_v) {
                            //断开TGT连接V
                            try {
                                socket.close();
                                socket.connect(new InetSocketAddress(SER_IP, 8888), 10000);
                                myClient.ClientAction("5", socket);
                                InputStreamReader inputStreamReader1 = null;
                                inputStreamReader = new InputStreamReader(socket.getInputStream());
                                BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader);
                                String pack1 = bufferedReader.readLine();
                                j = false;
                                while (!j) {//包的验证
                                    //等待回复 readline阻塞
                                    j = myClient.verify_m(pack1);
                                    if (!j) {//验证失败则重新请求
                                        myClient.ClientAction("5", socket);
                                    }
                                }
                                boolean ser_v = myClient.Clientexcecution(pack1);
                                if (ser_v) {
                                    JOptionPane.showMessageDialog(null, "登陆成功", "登陆成功", JOptionPane.NO_OPTION);
                                    //点击确定后会跳转到主窗口
                                    Function function = new Function(myClient, socket);
                                    function.run();
                                    frame.setVisible(false);
                                    //                            frame.dispose();
                                } else {
                                    //弹窗ser验证失败
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

                //为注册按钮添加监听器
                buttonregister.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //注册页面
                        frame.setVisible(false);
                        AdminRegister ar = new AdminRegister(myClient, socket);
                    }
                });
            }
        });

    }

    public static void main(String[] args) {

    }
}
