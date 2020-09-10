import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class MyCallable implements Callable {
    private Socket socket = null;
    // private String way = null;

    public MyCallable(Socket socket){
        this.socket = socket;
    }

    public String call() {//需要完成的任务
        InetAddress addr = socket.getInetAddress();
        try(InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);)
        {//这里
            String line = bufferedReader.readLine();

            System.out.println(addr.getHostName() + "'s message: "+ line + " from" + addr.getHostAddress());
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("server");
            packSend(socket, arrayList);
            /*if(bufferedReader.readLine().equals("send")){

            }
            else{

            }*/
        }catch (IOException e){
            e.printStackTrace();
            return "false";
        }
        return "true";
    }

    public boolean packSend(Socket socket, ArrayList<String> arrayList){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            for(int i = 0; i < arrayList.size(); ++i){
                bufferedWriter.write(arrayList.get(i));
            }
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<String> packReceive(Socket socket, ArrayList<String> arrayList){
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while (line != "exit"){
                Thread.sleep(100);
                line = bufferedReader.readLine();
                arrayList.add(line);
            }
            return arrayList;
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
            return arrayList;
        }
    }

}
