import java.net.Socket;
import java.util.concurrent.Callable;

public class Authentication implements Callable {

    private String ciphertext = "";//密文
    private int secretKey = 1351;//server私钥
    public int publicKey = 775;//server公钥
    public int rsaN = 1679; // N
    private Socket socket = null;

    public Authentication(String ciphertext, Socket socket){
        this.socket = socket;
        this.ciphertext = ciphertext;
    }

    public String call(){
        if(!verifyClient())return "false";
        if(clientVerify().equals("false"))return "false";
        return "true";
    }

    public boolean verifyClient(){//验证从client的密文
        //if()return false; 当验证client票据失败时
        return true;
    }

    public String clientVerify(){//发送给client的密文
        String message = "";//message是client返回的验证成功或者失败的信息
        return message;
    }
}
