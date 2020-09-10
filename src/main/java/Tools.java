import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
    public String getTS(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return date;
    }
    public String[] splite_name(String names){
        String name[]=names.split("#");
        return name;
    }
}
