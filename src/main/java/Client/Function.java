package Client;

import AS.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Function extends Thread{
    public ServerClient myClient;
    public Socket socket;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    PrintWriter writer;
    JButton btn01 = new JButton("文件上传");
    JButton btn02 = new JButton("文件下载/删除");
    JButton btn03 = new JButton("退出系统");

    Function(ServerClient client, Socket as_socket, BufferedReader bufferedReader, PrintWriter writer) throws IOException {
        //init();
        socket = as_socket;
        this.bufferedReader = bufferedReader;
        this.writer = writer;
        myClient = client;
        JFrame jf = new JFrame();
        //窗口名称
        jf.setTitle("文件传输系统");
        //弹窗大小
        jf.setSize(400, 600);
        //居中显示
        jf.setLocationRelativeTo(null);
        //设置进程退出方法
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //流式布局管理器
        JPanel panel = new JPanel(new FlowLayout());
        //创建标题
        JLabel label = new JLabel("功能选择");
        label.setOpaque(true);

        //设置按钮
        btn01.setBounds(100, 150, 100, 50);
        btn02.setBounds(100, 200, 100, 50);
        btn03.setBounds(150, 250, 100, 50);
        // inputStreamReader = new InputStreamReader(socket.getInputStream());
        // bufferedReader = new BufferedReader(inputStreamReader);
        panel.add(btn01);
        panel.add(btn02);
        panel.add(btn03);

        jf.setContentPane(panel);
        jf.setVisible(true);

    }

    //public Client client;
    public void run(){
        //窗口类
        //设置监听
        btn01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取到的事件源就是按钮本身
                // JButton btn = (JButton) e.getSource();
                FileUP fileup = new FileUP(socket, bufferedReader, writer, myClient);
                fileup.run();
                System.out.println("选择进行文件上传");
            }
        });

        btn02.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取到的事件源就是按钮本身
                JButton btn = (JButton) e.getSource();
                String Basic[];
                String data[];
                //socket连接Server端(刷新目录)
                myClient.ClientAction("9", socket, bufferedReader, writer);
                String pack="";
                try {
                    System.out.println("pack:--");
                    pack = bufferedReader.readLine();
                    System.out.println("pack:"+pack);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                boolean j = false;
                //等待Server回复
//                while (!j) {//10号反馈包的验证
                    //等待回复 readline阻塞
                    j = myClient.verify_m(pack);
//                    if (!j) {//刷新请求失败则重新请求
//                        myClient.ClientAction("9", socket, bufferedReader, writer);
//                    }
//                }

                String Basic_info[] = myClient.Divide(pack);
                data = myClient.m10_d(Basic_info[6], myClient.Kc_v);
                String sp[] = Tools.splite_name(data[1]);
                String display = "";
                for (int i = 0; i < sp.length; i++) {
                    display += sp[i] + "\n";
                }
                FileDOWN filedown = new FileDOWN("文件传输系统--文件下载", myClient, socket, bufferedReader, writer, display);
                System.out.println("选择进行文件下载");
            }
        });

        btn03.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取到的事件源就是按钮本身
                JButton btn = (JButton) e.getSource();
                String Basic[];
                String data[];
                //socket连接AS(离线提醒)
                myClient.ClientAction("23a", socket, bufferedReader, writer);
                String pack="";
                boolean j = false;
                //等待AS端回复
                while (!j) {
                    //等待回复 readline阻塞
                    j = myClient.verify_m(pack);
                    if (!j) {//离线请求失败则重新请求
                        myClient.ClientAction("23a", socket, bufferedReader, writer);
                    }
                }
                myClient.Clientexcecution(pack);
                //断开AS连接Server
                myClient.ClientAction("23s", socket, bufferedReader, writer);
                //等待Server端回复
                while (!j) {
                    //等待回复 readline阻塞
                    j = myClient.verify_m(pack);
                    if (!j) {//离线请求失败则重新请求
                        myClient.ClientAction("23s", socket, bufferedReader, writer);
                    }
                }
                boolean ser_v = myClient.Clientexcecution(pack);
                if (ser_v) {
                    JOptionPane.showMessageDialog(null, "成功退出系统，该账号已离线", "Remind", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "离线申请失败", "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("退出系统");
            }
        });

    }
}
