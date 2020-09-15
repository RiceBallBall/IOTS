package Client;

import AS.Tools;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FileUP {
    FileUP(Socket socket, BufferedReader bufferedReader, PrintWriter writer,ServerClient client){
        fin="";
        this.socket = socket;
        this.bufferedReader = bufferedReader;
        this.writer = writer;
        myClient=client;
    }
    String fin;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter writer;
    ServerClient myClient;
    public void run(){
        final JFrame jf = new JFrame("文件传输系统-文件上传");
        jf.setSize(400, 250);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        // 创建文本区域, 用于显示相关信息
        final JTextArea msgTextArea = new JTextArea(10, 30);
        msgTextArea.setLineWrap(true);
        panel.add(msgTextArea);

        JButton openBtn = new JButton("打开");
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fin=showFileOpenDialog(jf, msgTextArea);
                    String[] s=msgTextArea.getText().split("/");
                    myClient.name=s[s.length-1];
                    myClient.fileData=fin;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(openBtn);

        JButton upBtn = new JButton("上传");
        upBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                myClient.ClientAction("11", socket, bufferedReader, writer);
                //C->S
                /*for(int i=0;i<filedata.length;i++){
                    System.out.println(filedata[i]);
                }*/
            }
        });
        panel.add(upBtn);

        jf.setContentPane(panel);
        jf.setVisible(true);
    }

    //打开文件
    private static String showFileOpenDialog(Component parent, JTextArea msgTextArea) throws IOException {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));
        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(true);
        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        // 设置默认使用的文件过滤器
        fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            //从本地读取文件
            msgTextArea.append("打开文件: " + file.getAbsolutePath() + "\n\n");
            String Files = Tools.FileIn(file.getAbsolutePath());
            System.out.println(Files);
            return Files;
        }
        return null;
    }

    //选择文件上传路径
    private static void showFileSaveDialog(Component parent, JTextArea msgTextArea) {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();
        // 设置打开文件选择框后默认输入的文件名
        fileChooser.setSelectedFile(new File("测试文件.zip"));
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"上传", 则获取选择的保存路径
            File file = fileChooser.getSelectedFile();
            msgTextArea.append("上传文件: " + file.getAbsolutePath() + "\n\n");
        }
    }
}
