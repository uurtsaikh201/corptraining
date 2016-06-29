package training;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import auth.AuthHelper;
import auth.AuthenticationManager;
import auth.SystemDAO;
import auth.TokenInfo;
import settings.Settings;
import to.GeneralResult;
import to.LoginResult;
import to.LoginTO;
import to.UserInfoTO;
import to.UserRegister;
import util.Util;

@Path("/auth")
public class Authentication {
	@Context
	UriInfo uriInfo;
	static final Logger LOGGER = Logger.getLogger(Authentication.class);


	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginTO loginTO) {
		LoginResult loginResult = new LoginResult();
		Gson gson = new GsonBuilder().create();
		loginResult.setResult(Settings.UN_SUCCESSFUL_RESULT);
		try {
			System.out.println("login:" + loginTO.getEmail() + " " + loginTO.getPassword());
			if(loginTO.getEmail()!=null && loginTO.getEmail().trim().length()>2){
				
			}
			List<String> requestContent=new ArrayList<String>();
			requestContent.add(loginTO.getEmail());
			requestContent.add(loginTO.getPassword());
			requestContent.add("127.0.0.1");
			System.out.println("requestContent:"+new Gson().toJson(requestContent));
			String response=Util.sendPutRequest(Settings.API_URL+"user/authenticate", new Gson().toJson(requestContent));
			System.out.println("response:"+response);
			
			if(response.trim().length()>4){
				loginResult.setResult(Settings.SUCCESSFUL_RESULT);
				loginResult.setToken(AuthHelper.createJsonWebToken(loginTO.getEmail(), 90L));
				return Response.status(200).entity(gson.toJson(loginResult).toString()).build();
			}
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		loginResult.setResult(Settings.UN_SUCCESSFUL_RESULT);
		loginResult.setToken("");
		return Response.status(400).entity(gson.toJson(loginResult).toString()).build();
		// return HTTP response 200 in case of success
	}

	
	
	@GET
	@Path("/profile/{token}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public UserInfoTO getProfile(@Context HttpServletRequest request, @PathParam("token") String token) {
		UserInfoTO userRegister = new UserInfoTO();
		try {
			System.out.println("token:" + token);
			//userRegister = SystemDAO.getUserInfo(token);
			TokenInfo tokenInfo=AuthHelper.verifyToken(token);
			System.out.println("token:" + token+ " ");
			if(tokenInfo!=null && tokenInfo.getUserId()!=null && tokenInfo.getUserId().length()>1){
				System.out.println("token:" + token+ " "+tokenInfo.getUserId() + " "+tokenInfo.getExpires());
				
				UserInfoTO userInfoTO=SystemDAO.getUserInfo(tokenInfo.getUserId());
				if(userInfoTO!=null && userInfoTO.getEmail()!=null){
					return userInfoTO;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Parsing: - ");
		}
		return userRegister;
	}
	
	@GET
	@Path("/logout/{token}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public GeneralResult logout(@Context HttpServletRequest request, @PathParam("token") String token) {
		GeneralResult result = new GeneralResult();
		
		try {
			System.out.println("token:" + token);
			result.setResult(Settings.SUCCESSFUL_RESULT);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Parsing: - ");
		}
		return result;
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(UserRegister register) {
		Gson gson = new GsonBuilder().create();
		GeneralResult generalResult = new GeneralResult();
		generalResult.setResult(Settings.UN_SUCCESSFUL_RESULT);
		try {
			System.out.println("register:" + register.getEmail() + " " + register.getPassword());
			List<String> list=new ArrayList<String>();
			list.add(register.getFirstName());
			list.add(register.getLastName());
			list.add(register.getEmail());
			list.add("14");
			list.add(register.getPhone()+"");
			System.out.println("requestContent:"+new Gson().toJson(list));
			String response=Util.sendPostRequest(Settings.API_URL+"user/addnewuser", new Gson().toJson(list));
			System.out.println("response:"+response);
			
			try {
				if(!response.replace("\"", "").trim().equals("0")){
					generalResult.setResult(Settings.SUCCESSFUL_RESULT);
					return Response.status(200).entity(gson.toJson(generalResult).toString()).build();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		generalResult.setResult(Settings.UN_SUCCESSFUL_RESULT);
		return Response.status(400).entity(gson.toJson(generalResult).toString()).build();
	}

}
