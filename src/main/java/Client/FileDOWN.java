package Client;

import TGS.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.awt.event.ActionListener;
import java.net.Socket;

public class FileDOWN {

    final JFrame jf = new JFrame("文件传输系统-文件下载");
    public JPanel panel = new JPanel();
    private JLabel label1 = new JLabel("文件目录");
    private JLabel label2 = new JLabel("输入完整文件名：");
    //下载文件列表
    public JTextArea textArea1 = new JTextArea();
    JScrollPane scrollPane_1 = new JScrollPane();
    ServerSocket the_socket;
    public Boolean tgs;
    // 创建文本框，指定可见列数为50
    final JTextField textField = new JTextField(50);
    JButton btn01 = new JButton("下载");
    JButton btn02 = new JButton("删除");
    public ServerClient myClient;
    public Socket socket;
    BufferedReader bufferedReader;
    PrintWriter writer;// client.ClientAction("7",socket);


    FileDOWN(String Name, ServerClient serverClient, Socket ser_socket, BufferedReader bufferedReader, PrintWriter writer, String dir) {
        this.myClient = serverClient;
        JFrame frame = new JFrame(Name);
        // Setting the width and height of frame
        frame.setSize(820, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        placeComponents1();
        frame.setVisible(true);
        textArea1.setText(dir);
        textField.setFont(new Font(null, Font.PLAIN, 20));
        panel.add(textField);
        socket=ser_socket;
        this.bufferedReader = bufferedReader;
        this.writer = writer;

        // 设置下载按钮监听，点击后获取文本框中的文本
        btn01.setFont(new Font(null, Font.PLAIN, 20));
        btn01.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String data[];
                String Basic[];
                myClient.name=textField.getText();
                myClient.ClientAction("13", socket, bufferedReader, writer);
                String pack="";
                boolean j = false;
                //等待回复
                while (!j) {
                    j = myClient.verify_m(pack);
                    if (!j) {
                        myClient.ClientAction("13", socket, bufferedReader, writer);
                    }
                }
                //监听
                String context = "";
                String rec_File = "";
                if (myClient.Clientexcecution(pack)) {
                    try{
                        int turn = 0, sumDatagram = 10;
                        while(turn < sumDatagram) {
                            String line = bufferedReader.readLine();
                            if (line != null) {//成功收到报文
                                String situation = "11";
                                String[] fields_11 = myClient.m14_d(line.substring(100, line.length() - 32), myClient.Kc_v);
                                sumDatagram = Integer.parseInt(fields_11[1]);
                                rec_File = fields_11[0];
                                if (turn == sumDatagram - 1) situation = "00";
                                context +=fields_11[3];
                                String ack = myClient.m12(String.valueOf(turn), situation, myClient.Kc_v, myClient.V_IP, myClient.C_IP);
                                writer.println(ack);
                                writer.flush();
                                turn++;
                            } else {
                                String ack = myClient.m12(String.valueOf(turn), "01", myClient.Kc_v, myClient.V_IP, myClient.C_IP);
                                writer.println(ack);
                                writer.flush();
                            }
                        }
                        Tools.FileOut(System.getProperty("user.dir") + "/" + rec_File, context);
                    }catch (IOException eq){
                        eq.printStackTrace();
                    }
                }
                System.out.println("下载文件: " + textField.getText());
            }
        });
        panel.add(btn01);


        // 设置删除按钮监听，点击后获取文本框中的文本
        btn02.setFont(new Font(null, Font.PLAIN, 20));
        btn02.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("删除文件: " + textField.getText());
                String data[];
                String Basic[];
                myClient.ClientAction("21", socket, bufferedReader, writer);
                String pack="";
                boolean j = false;
                //等待回复
                while (!j) {
                    j = myClient.verify_m(pack);
                    if (!j) {
                        myClient.ClientAction("21", socket, bufferedReader, writer);
                    }
                }
                myClient.Clientexcecution(pack);

            }
        });
        panel.add(btn02);
    }

    private void placeComponents1() {
        panel.setLayout(null);
        label1.setBounds(360, 20, 80, 15);//文件目录
        panel.add(label1);
        label2.setBounds(40, 360, 160, 30);//输入提示
        panel.add(label2);
        textArea1.setBounds(40, 40, 740, 300);//目录框
        textArea1.setLineWrap(true);
        scrollPane_1.setBounds(40, 40, 740, 300);//滚动条
        panel.add(scrollPane_1);
        scrollPane_1.setViewportView(textArea1);
        textField.setBounds(170, 360, 580, 30);//文本框
        panel.add(textField);
        btn01.setBounds(180, 430, 80, 20);//下载按钮
        panel.add(btn01);
        btn02.setBounds(480, 430, 80, 20);//删除按钮
        panel.add(btn02);
    }

    /*public void sendPack(String recPack, Socket socket) {

    }*/
}
