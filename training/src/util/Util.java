package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Util {

	public static Connection getConnection() {
		try {
			
			String url = "jdbc:mysql://localhost:3306/ct";
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			Connection connection = DriverManager.getConnection (url, "root", "pass");
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public static String sendPostRequest(String url,String content){
		String result=null;
		try {
		
			URL object=new URL(url);

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("POST");
			
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(content.toString());
			wr.flush();
			
			StringBuilder sb = new StringBuilder();  
			int HttpResult = con.getResponseCode(); 
			if (HttpResult == HttpURLConnection.HTTP_OK) {
			    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			    String line = null;  
			    while ((line = br.readLine()) != null) {  
			        sb.append(line + "\n");  
			    }
			    br.close();
			    System.out.println("" + sb.toString()); 
			    result=sb.toString();
			} else {
			    System.out.println(con.getResponseMessage());  
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String sendPutRequest(String url,String content){
		String result=null;
		try {
		
			URL object=new URL(url);

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("PUT");
			
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(content.toString());
			wr.flush();
			
			StringBuilder sb = new StringBuilder();  
			int HttpResult = con.getResponseCode(); 
			if (HttpResult == HttpURLConnection.HTTP_OK) {
			    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			    String line = null;  
			    while ((line = br.readLine()) != null) {  
			        sb.append(line + "\n");  
			    }
			    br.close();
			    System.out.println("" + sb.toString()); 
			    result=sb.toString();
			} else {
			    System.out.println(con.getResponseMessage());  
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static String buildScreenType(String screenTypeReturn,String screenType){
		
		if(screenTypeReturn==null){
			screenTypeReturn=screenType;
		}else {
			if(!screenTypeReturn.contains(screenType)){
				screenTypeReturn+="_"+screenType;
			}
		}
		
		return screenTypeReturn;
	}
	
	public static Connection getConnectionMSSQL() {
		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://hhytt4uwze.database.windows.net;user=TestLessonBasket@hhytt4uwze;password=@ccount4dev;database=LessonBasketCoreDev");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	

	public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(Connection connection, Statement statement) {

		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void closeConnection(Connection connection, PreparedStatement statement) {

		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection(Connection connection, PreparedStatement statement,ResultSet resultSet) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(Connection connection) {

		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
