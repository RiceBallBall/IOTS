package AS;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
    public static String getTS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }

    public static String[] splite_name(String names) {
        String name[] = names.split("#");
        return name;
    }

    public static String[] dataSplite(String longdata, int len) {//数据，单个数据长度
        int n = longdata.length() / len;
        if (longdata.length() % len != 0) {
            n += 1;
        }
        String data[] = new String[n];
        for (int i = 0; i < n; i++) {
            if (i != n - 1) {
                data[i] = longdata.substring(i * len, (i + 1) * len);
            } else {
                data[i] = longdata.substring(i * len);
            }
        }
        return data;//返回数组

    }

    public static  String FileIn(String f) throws IOException {//本地文件读到字符串
//        f = "C:\\Users\\caorui\\Pictures\\ps\\DSC_0649.jpg";
//        save="C:\\Users\\caorui\\Pictures\\ps\\a.txt";
        File file = new File(f);

        InputStream is = new FileInputStream(file);
        try {
            byte[] bb = new byte[256];
            int ch;
            ch = is.read(bb);
            String str = "";
            while (ch != -1) {
                str += conver2HexStr(bb);
                ch = is.read(bb);
            }
            String s = "";
            for (int j = 0; j < str.length() / 8; j++) {
                String t1 = str.substring(j * 8, j * 8 + 8);
                int temp = Integer.valueOf(t1, 2);
                s = s + (char) temp;

            }
            return s;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void FileOut(String filePath, String fileStr) {//从字符串存到本地
        String fileBin = "";
        char[] strChar = fileStr.toCharArray();
        String temp;
        for (int i = 0; i < strChar.length; i++) {
            temp = String.format("%08d", Integer.parseInt(Integer.toBinaryString(strChar[i])));
            fileBin += temp;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.isFile() && file.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//                    System.out.println("==="+text.length());
                byte b[] = conver2HexToByte(fileBin);
                fileOutputStream.write(b);
                fileOutputStream.flush();

                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public String conver2HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(String.format("%08d", Integer.parseInt(Long.toString(b[i] & 0xff, 2))));
//            result.append(Long.toString(b[i] & 0xff, 2)+",");

        }
        return result.toString().substring(0, result.length());
    }

    static public byte[] conver2HexToByte(String hex2Str) {
        int max = hex2Str.length() / 8;
        String[] temp = new String[max];
        for (int i = 0; i < max; i++) {
            temp[i] = hex2Str.substring(i * 8, i * 8 + 8);
        }
        byte[] b = new byte[temp.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }

    public static void main(String[] args) throws IOException {
        String s = Tools.FileIn("/Users/katherine/Desktop/Study/IOTS/iots.cr/src/main/java/testdata.txt");
        System.out.println("[test:" + s + "]");
        s=DES.encode("12345678",s);
        System.out.println("[Encoded:" + s + "]");
        String dataSplite[]=dataSplite(s,4);
        String integrated="";

        for (int i=0;i<dataSplite.length;i++){
            System.out.println(dataSplite[i]);
            integrated+=dataSplite[i];
        }

        s=DES.decode("12345678",integrated);
        System.out.println("[Decoded:"+s+"]");
        Tools.FileOut("/Users/katherine/Desktop/Study/IOTS/iots.cr/src/main/java/test.txt", s);
//
//        System.out.println(DES.decode("12345678",s).length());
    }
}
