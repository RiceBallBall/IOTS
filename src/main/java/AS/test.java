package AS;

public class test extends Message {
    public static void main(String[] args) {
        String s = "abcdefghijk";
        String AD = "192168007007";
        String ID1 = "ID000001";
        String ID2 = "ID000002";
        String k = "12345678";
        String TS = "20071212060606";
        String LT = "10";
        String order="or";
        String warning="wa";
        String num="20";
        String seq="66";
        int pk =1129,n=1517,sk=389;
        test t = new test();
        int [] rsa=RSA.rsa();
String ST=t.ST("12345678","ID000001","192169001001","ser000001",2501,4550,"20000101101010","60");
    //    String TGT=t.TGT(k,ID1,AD,ID2,TS,LT,rec[3],rec[2]);
        String mes = t.m5(ST,"IDC00001","123456789012",Tools.getTS(),"12345678","192123001001","123145167167");
        System.out.println(mes);
        String inf[] = t.Divide(mes);
        System.out.println("ST:"+ST.length());
       // String f[]=t.ST_d(ST,1997,4559);
        System.out.println(t.verify_m(mes));
        String data[] = t.m5_d(inf[6],"12345678");
//        for (int i = 0; i < f.length; i++) {
//            System.out.println(f[i]);
//        }
        for (int i = 0; i < inf.length; i++) {
            System.out.println(inf[i]);
        }
        System.out.println("------------------");
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
        System.out.println("------------------");
    }
}
