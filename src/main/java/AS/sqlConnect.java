package AS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class sqlConnect {

    private String dbDriver;
    private String url;
    private String username;
    private String password;
    private ResultSet mResultSet;//用于反馈从数据库中获取的结果
    private Statement mStatement;//用于向数据库发送sql语句
    private Connection mConnect;//用于数据库的连接
    private static String sql="select ID, Pk, n from TGS where id='TGS00001' ";
    private static String sql1="select ID, Pk, n from server where id='ser00001' ";

    // 构造函数
    public sqlConnect() {
        this.dbDriver = "com.mysql.jdbc.Driver";
        this.url = "jdbc:mysql://172.20.10.2:3306/test?useUnicode=true&characterEncoding=UTF-8";
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
    public ResultSet searchSQL(String sql) {
        try {
            this.mResultSet = this.getStatement().executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("执行语句错误：\n" + sql + "\n" + e.getMessage());
            return null;
        }
        return mResultSet;
    }

    //执行插入语句
    /*
    public ResultSet insertSQL(String ID,String key,String n) {
        try{
            this.mResultSet = this.getStatement().executeUpdate()
        }catch(SQLException e){
            System.out.println("执行语句错误：\n" + sql + "\n" + e.getMessage());
        }

    }*/


    public static void main(String[] args) {
        ResultSet TGS;
        ResultSet server;
        sqlConnect sqlConn = new sqlConnect();

        sqlConn.connect();
        TGS = sqlConn.searchSQL(sql);
        try {
        while (TGS.next()) {
                System.out.print(TGS.getString(1) + " \t");
                System.out.print(TGS.getString(2) + " \t");
                System.out.println("查询成功");
            }
        } catch (Exception e) {
            System.out.println("数据库操作错误！ \n" + e.getMessage());
        }
        server = sqlConn.searchSQL(sql1);
        try {
            while (server.next()) {
                System.out.print(server.getString(1) + " \t");
                System.out.print(server.getString(2) + " \t");
                System.out.println("查询成功");
            }
        } catch (Exception e) {
            System.out.println("数据库操作错误！ \n" + e.getMessage());
        }
    }
}









