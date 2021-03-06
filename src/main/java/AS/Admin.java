package AS;

import java.sql.*;

public class Admin {
    private String id;//编号
    private String password;//密码
    void setID(String id) {
        this.id=id;
    }
    void setPassword(String password) {
        this.password=password;
    }

    String getID() {
        return this.id;
    }
    String getPassword() {
        return this.password;
    }

    public static class Login {
        Admin admin;

        void setAdmin(Admin admin) {
            this.admin=admin;
            //System.out.println(this.admin.getPassword()+"   " + this.admin.getID());
        }
        /*
         * JudgeAdmin()方法
         * 判断Admin的ID和密码是否正确，如果正确，显示登录成功
         * 如果错误，弹出一个窗口，显示账号或密码错误
         */
        private String driver = "com.mysql.cj.jdbc.Driver";
        private String url = "jdbc:mysql://172.20.10.2:3306/test?characterEncoding=UTF-8";
        private String user = "zzr";
        private String password = "zzr123";



        public boolean login(Admin admin) throws SQLException, ClassNotFoundException {
            String sql="select * from admin where id=? and password=?";

            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, admin.getID());
            ps.setString(2, admin.getPassword());
            ResultSet rs = ps.executeQuery();
            int ans = 0;
            if(rs.next()) {
                ans = 1;
            }
            rs.close();
            ps.close();
            conn.close();
            if(ans == 1) {
                return true;
            }
            else return false;
        }
        int JudgeAdmin() {

            try {
                if(login(this.admin)) {
                    System.out.println("登录成功");
                    return 1;
                }else {
                    return 0;
                }
            }catch(Exception e) {
                //e.printStackTrace();
                //System.out.println("!!!!!!!!!");
            }
            return 0;

        }
    }
}
