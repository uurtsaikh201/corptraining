package auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import auth.AuthHelper;
import settings.Settings;
import to.GeneralResult;
import to.LoginResult;
import to.LoginTO;
import to.UserRegister;
import util.Util;

public class AuthenticationManager {

	public static GeneralResult register(UserRegister userRegister) {
		GeneralResult generalResult = new GeneralResult();
		generalResult.setResult(Settings.UN_SUCCESSFUL_RESULT);
		try {

			boolean duplicateStatus = false;
			duplicateStatus = checkDuplicate(userRegister.getEmail());
			if (duplicateStatus == false) {
				Connection connection = Util.getConnection();
				String registerSQL = "insert into system_users(firstname,lastname,email,phone,password) values(?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(registerSQL);
				preparedStatement.setString(1, userRegister.getFirstName());
				preparedStatement.setString(2, userRegister.getLastName());
				preparedStatement.setString(3, userRegister.getEmail());
				preparedStatement.setLong(4, userRegister.getPhone());
				preparedStatement.setString(5, userRegister.getPassword());
				preparedStatement.executeUpdate();
				generalResult.setResult(Settings.SUCCESSFUL_RESULT);
				Util.closeConnection(connection, preparedStatement);
			}else {
				generalResult.setResult(Settings.DUPLICATE_RESULT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generalResult;
	}
	public static LoginResult Login(LoginTO loginTO) {
		LoginResult loginResult=new LoginResult();
		loginResult.setResult(Settings.UN_SUCCESSFUL_RESULT);
		try {
			Connection connection = Util.getConnection();
			String selectSQL = "SELECT * FROM system_users WHERE email=? and password=?";
			PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, loginTO.getEmail());
			preparedStatement.setString(2, loginTO.getPassword());
			preparedStatement.executeQuery();
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				loginResult.setResult(Settings.SUCCESSFUL_RESULT);
			}
			if(loginResult.getResult().equals(Settings.SUCCESSFUL_RESULT)){
				loginResult.setToken(AuthHelper.createJsonWebToken(loginTO.getEmail(), 90L));
			}
			Util.closeConnection(connection, preparedStatement, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginResult;
	}
	
	
	public static boolean checkDuplicate(String email) {
		boolean returnValue = false;
		try {
			Connection connection = Util.getConnection();
			String selectSQL = "SELECT * FROM system_users WHERE email=?";
			PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				returnValue = true;
			}
			Util.closeConnection(connection, preparedStatement, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}
