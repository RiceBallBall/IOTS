public class test01 extends Message{
    public void test(String mes) {
        test01 t=new test01();
        String ID2="ID0002";
        System.out.println(t.verify_m(mes));
        String inf[] = t.Divide(mes);
        String data[] = t.m2_d(inf[6],ID2);
        for (int i = 0; i < inf.length; i++) {
            System.out.println(inf[i]);
        }
        System.out.println("------------------");
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
    }

}
