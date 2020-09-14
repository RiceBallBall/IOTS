package Client;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.*;

public class AdminRegister {
    String AS_IP = "";
    AdminRegister(ServerClient myClient, Socket mysocket) {
        socket=mysocket;
        init();
        client = myClient;
    }

    public ServerClient client;
    public  Socket socket;

    void init() {

        JFrame frame = new JFrame("注册账号");
        frame.setLayout(null);

        JLabel nameStr = new JLabel("用户名:");
        nameStr.setBounds(250, 150, 100, 25);
        frame.add(nameStr);

        JLabel IDStr = new JLabel("账号:");
        IDStr.setBounds(250, 200, 100, 25);
        frame.add(IDStr);

        JLabel passwordStr = new JLabel("密码:");
        passwordStr.setBounds(250, 250, 100, 25);
        frame.add(passwordStr);

        JLabel confrimStr = new JLabel("确认密码:");
        confrimStr.setBounds(250, 300, 100, 30);
        frame.add(confrimStr);

        JTextField userName = new JTextField();
        userName.setBounds(320, 150, 150, 25);
        frame.add(userName);

        JTextField userID = new JTextField();
        userID.setBounds(320, 200, 150, 25);
        frame.add(userID);

        JPasswordField password = new JPasswordField();
        password.setBounds(320, 250, 150, 25);
        frame.add(password);

        JPasswordField confrimPassword = new JPasswordField();
        confrimPassword.setBounds(320, 300, 150, 25);
        frame.add(confrimPassword);

        JButton buttonregister = new JButton("注册");
        buttonregister.setBounds(350, 350, 70, 25);
        frame.add(buttonregister);


        frame.setBounds(400, 100, 800, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //为注册按钮增加监听器
        buttonregister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userName.getText();
                client.ID_c = userID.getText();
                client. setK_c(new String(password.getPassword()));
//                String confrimpasswd = new String(confrimPassword.getPassword());
//                socket连接AS
                String pack ="";
                try {
                    socket.connect(new InetSocketAddress(AS_IP, 8888), 10000);
                    client.ClientAction("1", socket);
                    InputStreamReader inputStreamReader = null;
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    pack=bufferedReader.readLine();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                client.ClientAction("7",socket);
//                监听得到AS的反馈
                boolean j= client.verify_m(pack);
                while (!j){
                    client.ClientAction("7",socket);
                    j= client.verify_m(pack);
                     }
                    boolean fb=client.Clientexcecution(pack);
                if(fb){
                    frame.setVisible(false);
//                        frame.dispose();
//                        Login_Register login_register = new Login_Register(client);
                }else{
                JOptionPane.showMessageDialog(null,"注册失败，无法注册","Error", JOptionPane.ERROR_MESSAGE);
                    //失败弹窗
                }
                }


                //创建Register类
//                Reginster register = new Reginster();
//                register.setID();
//                register.setName(name);
//                register.setPassword(passwd);
//                register.setconfirmpasswd(confrimpasswd);

                //如果注册成功，返回登录界面

//                    if (register.JudgeRegister()) {
//
//
//                    }


            });
    }
}
