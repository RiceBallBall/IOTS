import javax.swing.*;


public class ASPanel extends Thread {
    public JPanel panel = new JPanel();
    JLabel label = new JLabel("公钥:");
    JLabel label8 = new JLabel("N:");
    JLabel label1 = new JLabel("私钥:");
    JLabel label2 = new JLabel("客户ID:");
    JLabel label3 = new JLabel("发送包内容:");
    JLabel label4 = new JLabel("收到包内容:");
    JLabel label5 = new JLabel("收到包明文:");


    JTextField textField = new JTextField();
    JPasswordField textField1 = new JPasswordField(20);
    JTextArea textArea3 = new  JTextArea();
    JTextArea textArea4 = new  JTextArea();
    JTextArea textArea5 = new  JTextArea();
    JTextArea textArea2 = new  JTextArea();
    JTextField textField8 = new JTextField();

    JScrollPane scrollPane_1 = new JScrollPane();
    JScrollPane scrollPane_2 = new JScrollPane();
    JScrollPane scrollPane_3 = new JScrollPane();



    public ASPanel(String Name) {
        JFrame frame = new JFrame(Name);
        // Setting the width and height of frame
        frame.setSize(820, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        placeComponents1();
        frame.setVisible(true);
    }


    private void placeComponents1() {
        int para[] = RSA.rsa();
        panel.setLayout(null);

        label.setBounds(40, 30, 30, 25);//pk
        panel.add(label);
        textField.setBounds(75, 30, 50, 25);
        textField.setText(Integer.toString(para[3]));
        textField.setEditable(false);
        panel.add(textField);

        label8.setBounds(130, 30, 15, 25);//n
        panel.add(label8);
        textField8.setBounds(145, 30, 50, 25);
        textField8.setText(Integer.toString(para[2]));
        textField8.setEditable(false);
        panel.add(textField8);

        label1.setBounds(200, 30, 30, 25);//sk
        panel.add(label1);
        textField1.setBounds(235, 30, 50, 25);
        textField1.setText(Integer.toString(para[4]));
        textField1.setEditable(false);
        panel.add(textField1);

        label2.setBounds(500, 30, 80, 25);//rpk
        panel.add(label2);
        textArea2.setBounds(560, 30, 220, 25);
        textArea2.setLineWrap(true);
        panel.add(textArea2);

        label3.setBounds(40, 70, 80, 20);//m
        panel.add(label3);
        textArea3.setBounds(40, 100, 220, 350);
        textArea3.setLineWrap(true);
        scrollPane_1.setBounds(40, 100, 220, 350);
        panel.add(scrollPane_1);
        scrollPane_1.setViewportView(textArea3);

        label4.setBounds(300, 70, 80, 20);
        panel.add(label4);
        textArea4.setBounds(300, 100, 220, 350);
        textArea4.setLineWrap(true);
        scrollPane_2.setBounds(300, 100, 220, 350);
        panel.add(scrollPane_2);
        scrollPane_2.setViewportView(textArea4);

        label5.setBounds(560, 70, 80, 20);
        panel.add(label5);
        textArea5.setBounds(560, 100, 220, 350);
        textArea5.setLineWrap(true);
        scrollPane_3.setBounds(560, 100, 220, 350);
        panel.add(scrollPane_3);
        scrollPane_3.setViewportView(textArea5);

    }

    public void run(int rsa_pk,int rsa_sk,int rsa_n) {
        byte[] b = new byte[1024];
        textField.setText(String.valueOf(rsa_pk));
        textField1.setText(String.valueOf(rsa_sk));
        textField8.setText(String.valueOf(rsa_n));
        while (true) {
        }

    }

    public static void main(String[] args) {
        ASPanel asPanel=new ASPanel("AS端");
        asPanel.run();
    }

}

