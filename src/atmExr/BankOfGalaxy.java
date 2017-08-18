package atmExr;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.sql.Connection;


public class BankOfGalaxy {

	String name;
	String phone;
	String address;
	int bankNumber =1001;
	int balance;
	
	//constructor
	public BankOfGalaxy(String name, String phone, String address, int balance) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.balance = balance;
	}
		
	
	
	public void setMember() {
		Connection conn;
		PreparedStatement ps, ss;
		String url = "jdbc:mysql://localhost:3306/public";
		String username = "root";
		String password = "1113";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try{
			conn=DriverManager.getConnection(url, username, password);
		    String sql = "insert into BOG1 (name,phone,address,bankNumber,balance) values(?,?,?,?,?)";
		    ps = (PreparedStatement) conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		    ps.setString(1, this.name.toString());
		    ps.setString(2, this.phone.toString());
		    ps.setString(3, this.address.toString());
		    ps.setInt(4, bankNumber);
		    ps.setInt(5, this.balance);	
		    ps.executeUpdate();
		    ResultSet rs = ps.getGeneratedKeys();
		    int insert = 0;
			if(rs.next()){
				insert=rs.getInt(1);
			}
			String a = String.valueOf(insert);
			
			String sql1 = "UPDATE BOG1 SET accountNumber=? where id=?";
			ss = (PreparedStatement) conn.prepareStatement(sql1);
			String bn = String.valueOf(bankNumber)+'-'+a;
			ss.setString(1, bn.toString());
			ss.setString(2, a);
			ss.executeUpdate();
			System.out.println("Your account number is" + bn + "."+ " And your balance is $"+ this.balance);
		    
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	

	public void statement() {}
	
	public void balance() {}
	
	public void wire() {}
	
	public void deposit() {}
	
	public void withdraw() {}
	
}
