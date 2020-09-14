package AS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class sqlOperation {

    private String dbDriver;
    private String url;
    private String username;
    private String password;
    private ResultSet mResultSet;//用于反馈从数据库中获取的结果
    private Statement mStatement;//用于向数据库发送sql语句
    private Connection mConnect;//用于数据库的连接

    // 构造函数
    public sqlOperation() {
        this.dbDriver = "com.mysql.jdbc.Driver";
        this.url = "jdbc:mysql://172.20.10.11:3306/test?useUnicode=true&characterEncoding=UTF-8";
        this.username = "zzr";
        this.password = "zzr123";
        this.mStatement = null;
        this.mResultSet = null;
        this.mConnect = null;
    }

    // 连接数据库
    public Connection connect() {
        try {
            Class.forName(this.dbDriver).newInstance();
            mConnect = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("连接数据库错误: \n" + url + "\n" + e.getMessage());
        }
        return mConnect;
    }

    // 关闭数据库
    public boolean disConnect() {
        try {
            this.mConnect.close();
            this.mStatement.close();
        } catch (SQLException e) {
            System.out.println("关闭数据库错误：\n" + e.getMessage());
            return false;
        }

        return true;
    }

    // 获得 statement
    public Statement getStatement() {
        try {
            this.mStatement = this.connect().createStatement();
        } catch (SQLException e) {
            System.out.println("获取statement失败：\n" + e.getMessage());
            return null;
        }
        return mStatement;
    }

    // 执行查询语句
    public ResultSet searchSQL(String TableName, String ID) {
        String judge = TableName;
        try {
            if(TableName == "TGS") {
                String sqlsearch = "select * from TGS where ID = ? ";
                PreparedStatement search = (PreparedStatement) connect().prepareStatement(sqlsearch);
                search.setString(1, ID);
                this.mResultSet = search.executeQuery();
                System.out.println("查询TGS成功！");
            }
            if(TableName == "server") {
                String sqlsearch = "select * from server where ID = ? ";
                PreparedStatement search = (PreparedStatement) connect().prepareStatement(sqlsearch);
                search.setString(1, ID);
                search.executeQuery();
                this.mResultSet = search.executeQuery();
                System.out.println("查询server成功！");
            }
            if(TableName == "client") {
                String sqlsearch = "select * from client where ID = ? ";
                PreparedStatement search = (PreparedStatement) connect().prepareStatement(sqlsearch);
                search.setString(1, ID);
                search.executeQuery();
                this.mResultSet = search.executeQuery();
                System.out.println("查询client成功！");
            }
        } catch (SQLException e) {
            System.out.println("查询失败：\n" +  e.getMessage());
            return null;
        }
        return mResultSet;
    }

    //执行插入语句/
    public ResultSet insertSQL(String TableName,String ID,String Kc,String n) {
        String judge = TableName;
        try{
            if(judge == "TGS"){
                String sqlinsert="insert into TGS(ID,Pk,n)values(?,?,?)";
                PreparedStatement insert= connect().prepareStatement(sqlinsert);
                insert.setString(1,ID);
                insert.setString(2,Kc);
                insert.setString(3,n);
                insert.executeUpdate();
                System.out.println("插入TGS成功！");
            }
            if(judge == "server"){
                String sqlinsert="insert into server(ID,Pk,n)values(?,?,?)";
                PreparedStatement insert= connect().prepareStatement(sqlinsert);
                insert.setString(1,ID);
                insert.setString(2,Kc);
                insert.setString(3,n);
                insert.executeUpdate();
                System.out.println("插入server成功！");
            }
            if(judge == "client"){
                String sqlinsert="insert into client(ID,Kc,n)values(?,?,?)";
                PreparedStatement insert= connect().prepareStatement(sqlinsert);
                insert.setString(1,ID);
                insert.setString(2,Kc);
                insert.setString(3,n);
                insert.executeUpdate();
                System.out.println("插入client成功！");
            }
        }catch(SQLException e){
            System.out.println("插入失败：\n" + e.getMessage());
        }
        return mResultSet;
    }

    public ResultSet alertSQl(String tablename,String ID,String n) {
        String judge = tablename;
        try{
            if(tablename == "TGS") {
                String sqlalertTGS = "UPDATE TGS set n = ? WHERE ID = ?";
                PreparedStatement alert = connect().prepareStatement(sqlalertTGS);
                alert.setString(1, n);
                alert.setString(2, ID);
                alert.executeUpdate();
                System.out.println("修改TGS成功！");
            }
            if(tablename == "server") {
                String sqlalertServer = "UPDATE server set n = ? WHERE ID = ?";
                PreparedStatement alert = connect().prepareStatement(sqlalertServer);
                alert.setString(1, n);
                alert.setString(2, ID);
                alert.executeUpdate();
                System.out.println("修改server成功！");
            }
            if(tablename == "client") {
                String sqlalertClient = "UPDATE client set n = ? WHERE ID = ?";
                PreparedStatement alert = connect().prepareStatement(sqlalertClient);
                alert.setString(1, n);
                alert.setString(2, ID);
                alert.executeUpdate();
                System.out.println("修改client成功！");
            }
        }catch(SQLException e){
            System.out.println(("修改失败：" + e.getMessage()));
        }
        return mResultSet;
    }

    public static void main(String[] args) {

        //String sql="select * from TGS";
        //String sql1="select * from server";
        sqlOperation sqlConn = new sqlOperation();
        sqlConn.connect();

       //插入语句测

        sqlOperation insert =new sqlOperation();
        //insert.insertSQL("client","zzr2","123a","sdd");

        //查询语句测试
        ResultSet TGS;
        TGS = sqlConn.searchSQL("client","zzr2");
        try {
        while (TGS.next()) {
                System.out.print(TGS.getString(1) + " \t");
                System.out.print(TGS.getString(2) + " \t");
            }
        } catch (Exception e) {
            System.out.println("数据库操作错误！ \n" + e.getMessage());
        }

insert.alertSQl("client","zzr2","zzr");

    }
}









