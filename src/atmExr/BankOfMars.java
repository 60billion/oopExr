package atmExr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class BankOfMars {
	
	String userName;
	String userId;
	String passwd;
	String phone;
	String address;
	int balance;
	String accountNumber;
	String name;
	int bankNumber;
	
	
	//constructor
	public BankOfMars(String userName, String phone, String address, int bankNumber) {
		this.userName = userName;
		this.phone = phone;
		this.address = address;
		this.bankNumber = bankNumber;
		
		Connection conn;
		PreparedStatement ps, pp, ll;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";

		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
		conn=DriverManager.getConnection(url, username, password);
		String sql = "select bankName from bank where bankNumber=?";
		ps= (PreparedStatement) conn.prepareStatement(sql);
		ps.setString(1, String.valueOf(bankNumber).toString());
		ResultSet rs = ps.executeQuery();
		String name = "";
		while(rs.next()) { 
			name=rs.getString("bankName");
		
		}
		this.name=name;
		
		}catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	//sign up
	public void setMember(String userId, String passwd, int balance) {
		//Add a new member at the MARS's member table. And make a new table for the user's table.
		this.userId = userId;
		this.passwd = passwd;
		Connection conn;
		PreparedStatement ps, ss, pp, ll, ch;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try{
			//connect to database
			conn=DriverManager.getConnection(url, username, password);
			
			//check id
			ArrayList<String> idCheck = new ArrayList<String>();
			String getIds = "select userId from "+this.name;
			ch = (PreparedStatement) conn.prepareStatement(getIds);
			ResultSet ls = ch.executeQuery();
			while(ls.next()) {
				idCheck.add(ls.getString("userId"));
			}
			if (idCheck.contains(this.userId)) {
				System.out.println("invaild id :"+this.userId);
				return;
			}else{System.out.println("can use the id of "+this.userId);}
			
			//set member up
		    String sql = "insert into "+this.name+ "(userName,phone,address,userId,passwd,balance) values(?,?,?,?,?,?)";
		    ps = (PreparedStatement) conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		    ps.setString(1, this.userName.toString());
		    ps.setString(2, this.phone.toString());
		    ps.setString(3, this.address.toString());
		    ps.setString(4, this.userId.toString());
		    ps.setString(5, this.passwd.toString());
		    ps.setInt(6, balance);
		    ps.executeUpdate();
		    ResultSet rs = ps.getGeneratedKeys();
		    int insert = 0;
			if(rs.next()){
				insert=rs.getInt(1);
			}
			String a = String.valueOf(insert);
			
			//make accountNumber
			String sql1 = "UPDATE" +this.name+ "SET accountNumber=? where id=?";
			ss = (PreparedStatement) conn.prepareStatement(sql1);
			String bn = String.valueOf(bankNumber)+'-'+a;
			this.accountNumber=bn;
			ss.setString(1, this.accountNumber.toString());
			ss.setString(2, a);
			ss.executeUpdate();
			
			//make personal table
			String create ="CREATE TABLE"+" `"+this.userId+"` "+ "(" + 
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"  `datetime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," + 
					"  `log` varchar(300) NOT NULL," + 
					"  PRIMARY KEY (id)" + 
					") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			pp = (PreparedStatement) conn.prepareStatement(create);
			pp.executeUpdate();	
			
			//update setMember log
			String log = "insert into "+this.userId+" (log) values(?)";
			ll = (PreparedStatement) conn.prepareStatement(log);
			String balances = String.valueOf(balance);
			String logs = "Set New Member "+ "("+this.userName+"). "+"Account Number is "+bn+". "+"Balance is "+balances;
			ll.setString(1, logs);
			ll.executeUpdate();
			System.out.println("Your account number is " + this.accountNumber + ".");
		    
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public int balance(String accountNumber, String userId) {
		//select from user's table and find the balance.
		//insert into the log to the user's table.
		Connection conn;
		PreparedStatement ps, pp;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try{
			//connect to database
			conn=DriverManager.getConnection(url, username, password);
			String sql = "select balance from "+this.name+" where accountNumber=?";
			ps= (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, accountNumber.toString());
			ResultSet rs = ps.executeQuery();
			int nowBalance = 0;
			while(rs.next()) { 
				nowBalance=rs.getInt("balance");
			}
			
			String logs = "Checked balance: "+String.valueOf(nowBalance);
			String sql1 = "insert into "+userId+" (log) values (?)";
			pp = (PreparedStatement) conn.prepareStatement(sql1);
			pp.setString(1, logs);
			pp.executeUpdate();
			
			return nowBalance;
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
		
	}
	
	public void deposit(String accountNumber, int amount) {
		//add the amount to the accountHolder's balance.
		//insert into the log to the user.
		Connection conn;
		PreparedStatement ps, pp, ll;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";
		int lastBalance;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
		conn=DriverManager.getConnection(url, username, password);
		String sql = "select balance,userId from "+this.name+" where accountNumber=?";
		ps= (PreparedStatement) conn.prepareStatement(sql);
		ps.setString(1, accountNumber.toString());
		ResultSet rs = ps.executeQuery();
		int nowBalance = 0;
		String id = "";
		while(rs.next()) { 
			nowBalance=rs.getInt("balance");
			id = rs.getString("userId");
		}
		lastBalance = nowBalance + amount;
		//System.out.println(lastBalance);
		
		String sql1 = "UPDATE "+this.name+" SET balance=? where accountNumber=?";
		pp = (PreparedStatement) conn.prepareStatement(sql1);
		pp.setInt(1, lastBalance);
		pp.setString(2, accountNumber.toString());
		pp.executeUpdate();
		
		String logs = "deposit : "+ amount+" / balance: "+String.valueOf(lastBalance);
		String sql11 = "insert into "+id+" (log) values (?)";
		ll = (PreparedStatement) conn.prepareStatement(sql11);
		ll.setString(1, logs);
		ll.executeUpdate();
		
		}catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	public void withdraw(String accountNumber, int amount) {
		//check the possibility to get withdraw from user's balance.
		//remove the amount from user's balance.
		//insert into the log to the user.
		Connection conn;
		PreparedStatement ps, pp, ll;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";
		int lastBalance;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
		conn=DriverManager.getConnection(url, username, password);
		String sql = "select balance,userId from "+this.name+" where accountNumber=?";
		ps= (PreparedStatement) conn.prepareStatement(sql);
		ps.setString(1, accountNumber.toString());
		ResultSet rs = ps.executeQuery();
		int nowBalance = 0;
		String id = "";
		while(rs.next()) { 
			nowBalance=rs.getInt("balance");
			id = rs.getString("userId");
		}
		if (nowBalance<amount) {
			System.out.println("No Balance");
			return;
		}
		lastBalance = nowBalance - amount;
		//System.out.println(lastBalance);
		
		String sql1 = "UPDATE "+this.name+" SET balance=? where accountNumber=?";
		pp = (PreparedStatement) conn.prepareStatement(sql1);
		pp.setInt(1, lastBalance);
		pp.setString(2, accountNumber.toString());
		pp.executeUpdate();
		
		String logs = "withdraw : "+ amount+" / balance: "+String.valueOf(lastBalance);
		String sql11 = "insert into "+id+" (log) values (?)";
		ll = (PreparedStatement) conn.prepareStatement(sql11);
		ll.setString(1, logs);
		ll.executeUpdate();
		System.out.println("Ypu got $"+String.valueOf(amount));
		
		}catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
		
	}
	
	public void wire(String accountNumber,int bankNumber, String toAccountNumber, int amount) {
		//balance check.
		//to find toAccountNumber's table.
		//remove the amount from sender's balance.
		//add the amount from recevier's balance.
		//insert into the log to each users.
		
		Connection conn;
		PreparedStatement ps, pp, ll, lp, ls, mm, nn;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";
		int lastBalance;
		int tolastBalance;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
		conn=DriverManager.getConnection(url, username, password);
		String sql = "select balance,userId from "+this.name+" where accountNumber=?";
		ps= (PreparedStatement) conn.prepareStatement(sql);
		ps.setString(1, accountNumber.toString());
		ResultSet rs = ps.executeQuery();
		int nowBalance = 0;
		String id = "";
		while(rs.next()) { 
			nowBalance=rs.getInt("balance");
			id = rs.getString("userId");
		}
		if (nowBalance<amount) {
			System.out.println("No Balance");
			return;
		}
		lastBalance = nowBalance - amount;
		//System.out.println(lastBalance);
		String sql1 = "UPDATE "+this.name+" SET balance=? where accountNumber=?";
		ls = (PreparedStatement) conn.prepareStatement(sql1);
		ls.setInt(1, lastBalance);
		ls.setString(2, accountNumber.toString());
		ls.executeUpdate();
		
		
		
		//to account
		conn=DriverManager.getConnection(url, username, password);
		String sql3 = "select bankName from bank where bankNumber=?";
		ll= (PreparedStatement) conn.prepareStatement(sql3);
		ll.setString(1, String.valueOf(bankNumber).toString());
		ResultSet rs3 = ll.executeQuery();
		String toname = "";
		while(rs3.next()) { 
			toname=rs3.getString("bankName");
		
		}
		
		
		
		String sql4 = "select balance,userId from "+toname+" where accountNumber=?";
		pp= (PreparedStatement) conn.prepareStatement(sql4);
		pp.setString(1, accountNumber.toString());
		ResultSet rs4 = pp.executeQuery();
		int tonowBalance = 0;
		String toid = "";
		while(rs4.next()) { 
			tonowBalance=rs4.getInt("balance");
			toid = rs4.getString("userId");
		}
		
		tolastBalance = tonowBalance + amount;
		//System.out.println(lastBalance);
		String sql5 = "UPDATE "+toname+" SET balance=? where accountNumber=?";
		lp = (PreparedStatement) conn.prepareStatement(sql5);
		lp.setInt(1, tolastBalance);
		lp.setString(2, toAccountNumber.toString());
		lp.executeUpdate();
		
		
		//log
		String logs = "wired : "+ amount+" to "+toAccountNumber+"/ balance: "+String.valueOf(lastBalance);
		String sql11 = "insert into "+id+" (log) values (?)";
		mm = (PreparedStatement) conn.prepareStatement(sql11);
		mm.setString(1, logs);
		mm.executeUpdate();
		System.out.println("You wired $"+String.valueOf(amount));
		
		String logs1 = "get : "+ amount+" from " +accountNumber+ "/ balance: "+String.valueOf(tolastBalance);
		String sql12 = "insert into "+toid+" (log) values (?)";
		nn = (PreparedStatement) conn.prepareStatement(sql12);
		nn.setString(1, logs1);
		nn.executeUpdate();
		System.out.println("You got $"+String.valueOf(amount));
		
		
		}catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	//log
	public ArrayList<String> statement(String accountNumber) {
		//show the log column from accountHoler's table.
		Connection conn;
		PreparedStatement ps, pp, ll;
		String url = "jdbc:mysql://localhost:3306/mars";
		String username = "root";
		String password = "1113";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
		conn=DriverManager.getConnection(url, username, password);
		String sql = "select userId from "+this.name+" where accountNumber=?";
		ps= (PreparedStatement) conn.prepareStatement(sql);
		ps.setString(1, accountNumber.toString());
		ResultSet rs = ps.executeQuery();
		String id = "";
		while(rs.next()) { 
			id = rs.getString("userId");
		}
		
		ArrayList<String> logs = new ArrayList<String>();
		String sql1 = "select * from "+id;
		pp= (PreparedStatement) conn.prepareStatement(sql1);
		//pp.setString(1, id.toString());
		ResultSet rs1 = pp.executeQuery();
		while(rs1.next()) { 
			logs.add(rs1.getString("datetime"));
			//logs.add(" : ");
			logs.add(rs1.getString("log"));
			//logs.add(",");
		}

		return logs;
		
		}catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

}
