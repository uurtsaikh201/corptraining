package auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import auth.AuthHelper;
import auth.TokenInfo;
import settings.Settings;
import to.UserInfoTO;
import to.UserTO;
import util.Util;

public class SystemDAO {

	
	public static List<String> getUsersURLList() {

		List<String> userList = new ArrayList<String>();
		try {
			Connection connection = Util.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from system_users order by id");
			while (resultSet.next()) {
				userList.add(Settings.BASE_URL_SYSTEM+""+resultSet.getInt("id")+"/user");
			}
			Util.closeConnection(connection, statement, resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;

	}
	
	public static UserTO getUser(String id) {

		UserTO  userTO= new UserTO();
		try {
			Connection connection = Util.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from system_users where id="+id);
			while (resultSet.next()) {
				userTO.setPassword(resultSet.getString("password"));
				userTO.setUsername(resultSet.getString("username"));
			}
			Util.closeConnection(connection, statement, resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTO;

	}
	
	public static UserInfoTO getUserInfo(String email) {

		UserInfoTO  userInfoTO= new UserInfoTO();
		try {
			
			Connection connection = Util.getConnectionMSSQL();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT UserName,DisplayName,Email,UserID,LinkedUserID,phone FROM BreezeUser WHERE Email = '"+email.trim()+"'");
			while (resultSet.next()) {
				userInfoTO.setId(resultSet.getLong("UserID"));
				String[] names=null;
				try {
					names=resultSet.getString("DisplayName").split(" ");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(names!=null){
					if(names.length==2){
						userInfoTO.setFirstName(names[0]);
						userInfoTO.setLastName(names[1]);
					}else {
						userInfoTO.setFirstName(names[0]);
						userInfoTO.setLastName(names[0]);
					}
				}else {
					userInfoTO.setFirstName(resultSet.getString("DisplayName"));
					userInfoTO.setLastName(resultSet.getString("DisplayName"));	
				}
				
				userInfoTO.setEmail(resultSet.getString("Email"));
				userInfoTO.setPhone(resultSet.getString("phone"));
				//userInfoTO.setPhone(resultSet.getLong("phone"));
				
			}
			Util.closeConnection(connection, statement, resultSet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return userInfoTO;

	}
	
	public static boolean login(UserTO userTO) {

		boolean logged=false;
		try {
			Connection connection = Util.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from system_users where username='"+userTO.getUsername()+"' and password='"+userTO.getPassword()+"'");
			while (resultSet.next()) {
				logged=true;
			}
			Util.closeConnection(connection, statement, resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logged;

	}

	
}
