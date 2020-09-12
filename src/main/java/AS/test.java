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
    //    String TGT=t.TGT(k,ID1,AD,ID2,TS,LT,rec[3],rec[2]);
        String mes = t.m7(ID1, k, rsa[3] ,rsa[2], AD, AD);
        System.out.println(mes);
        System.out.println("------------------");
               System.out.println(t.verify_m(mes));
        String inf[] = t.Divide(mes);
        String data[] = t.m7_d(inf[6], rsa[4],rsa[2]);
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
