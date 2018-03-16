package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBase {
	// JDBC 驱动名及数据库 URL
    final String driver = "com.mysql.jdbc.Driver";  
    final String url = "jdbc:mysql://localhost:3306/lease";
 
    // 数据库的用户名与密码
    final String user = "root";
    final String password = "123456";
    Connection conn=null;
    Statement sta=null;
    ResultSet rs=null;
    public void connect()
    {  	
    	//加载驱动
    	try {
			Class.forName(driver);			
			conn=DriverManager.getConnection(url,user,password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			sta=conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void sqlQuery(String sql) throws SQLException
    {
    	rs=sta.executeQuery(sql);
    }
    public int sqlUpdate(String sql) throws SQLException
    {
    	return sta.executeUpdate(sql);
    }
	public Statement getSta() {
		return sta;
	}
	public void setSta(Statement sta) {
		this.sta = sta;
	}
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
}
