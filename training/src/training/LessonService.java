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

import settings.Settings;
import to.GeneralResult;
import to.ImageTO;
import to.LessonBr;
import to.ModuleTO;
import to.QuestionTO;
import to.Screen;
import to.ScreenComplete;
import to.TestAnswerRequest;
import to.TestAnswerResultTO;
import to.TestResultTO;
import to.TestResultUserTO;
import util.Util;

@Path("/lessons")
public class LessonService {
	@Context
	UriInfo uriInfo;
	static final Logger LOGGER = Logger.getLogger(LessonService.class);
	
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<LessonBr> getLessonURLList(@Context HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getBriefLessons(null);
	}
		
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public LessonBr getLessonWithId(@Context HttpServletRequest request, @PathParam("id") Long id) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);

		return LessonDAO.getLessonBriefLessonById(id);
	}
	
	@GET
	@Path("modules")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<ModuleTO> getModuleList(@Context HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getModuleList();
	}
	
	
	@GET
	@Path("modules/{moduleId}/lessons")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<LessonBr> getModuleLessonList(@Context HttpServletRequest request,@PathParam("moduleId") Long moduleId) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		if(moduleId!=null&& moduleId.longValue()==1){
			List<LessonBr> list=LessonDAO.getBriefLessons("job");
			LessonBr br=new LessonBr();
			br.setId(10000);
			br.setScreenCount(0);
			br.setTitle("AUDITS");
			br.setDescription("No screens");
			list.add(br);
			
			LessonBr br1=new LessonBr();
			br1.setId(10001);
			br1.setScreenCount(0);
			br1.setTitle("EMERGENCY RESPONSE");
			br1.setDescription("No screens");
			
			list.add(br1);
			
			LessonBr br2=new LessonBr();
			br2.setId(10002);
			br2.setScreenCount(0);
			br2.setTitle("HEALTH AND SAFETY RULES");
			br2.setDescription("No screens");
			
			list.add(br2);
			
			
			LessonBr br3=new LessonBr();
			br3.setId(10003);
			br3.setScreenCount(0);
			br3.setTitle("OCCUPATIONAL HEALTH AND SAFETY");
			br3.setDescription("No screens");
			
			list.add(br3);
			/*
			LessonBr br4=new LessonBr();
			br4.setId(10004);
			br4.setScreenCount(0);
			br4.setTitle("SAFE WORK PRACTICES");
			br4.setDescription("No screens");
			
			list.add(br4);
			*/
			return list;
		}
		if(moduleId!=null&& moduleId.longValue()==2){
			List<LessonBr> list=LessonDAO.getBriefLessons("air");
			LessonBr br=new LessonBr();
			br.setId(10006);
			br.setScreenCount(0);
			br.setTitle("Chemical Spill SJP Emergency");
			br.setDescription("No screens");
			list.add(br);
			
			LessonBr br1=new LessonBr();
			br1.setId(10007);
			br1.setScreenCount(0);
			br1.setTitle("Confined Space SJP");
			br1.setDescription("No screens");
			
			list.add(br1);
			
			LessonBr br2=new LessonBr();
			br2.setId(10008);
			br2.setScreenCount(0);
			br2.setTitle("Fall Protection Plan");
			br2.setDescription("No screens");
			
			list.add(br2);
			
			
			LessonBr br3=new LessonBr();
			br3.setId(10009);
			br3.setScreenCount(0);
			br3.setTitle("H2S Alarm");
			br3.setDescription("No screens");
			
			list.add(br3);
			
			LessonBr br4=new LessonBr();
			br4.setId(10010);
			br4.setScreenCount(0);
			br4.setTitle("Line Opening");
			br4.setDescription("No screens");
			
			list.add(br4);
			return list;
		}
		if(moduleId!=null&& moduleId.longValue()==3){
			List<LessonBr> list=LessonDAO.getBriefLessons(null);
			LessonBr br=new LessonBr();
			br.setId(100015);
			br.setScreenCount(0);
			br.setTitle("Crane Truck Operation");
			br.setDescription("No screens");
			list.add(br);
			
			LessonBr br1=new LessonBr();
			br1.setId(100016);
			br1.setScreenCount(0);
			br1.setTitle("Propane Product DeliveryV3");
			br1.setDescription("No screens");
			
			list.add(br1);
			
			LessonBr br2=new LessonBr();
			br2.setId(100017);
			br2.setScreenCount(0);
			br2.setTitle("Propane Tank Evacuation");
			br2.setDescription("No screens");
			
			list.add(br2);
			
			
			LessonBr br3=new LessonBr();
			br3.setId(10018);
			br3.setScreenCount(0);
			br3.setTitle("Trencher Operation");
			br3.setDescription("No screens");
			
			list.add(br3);
			
			LessonBr br4=new LessonBr();
			br4.setId(10019);
			br4.setScreenCount(0);
			br4.setTitle("Working Near Electrical Systems");
			br4.setDescription("No screens");
			
			list.add(br4);
			return list;
		} 
		return new ArrayList<LessonBr>();
	}
	
	@GET
	@Path("{lessonid}/screens")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<Screen> getListOfScreens(@Context HttpServletRequest request, @PathParam("lessonid") Long lessonid) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getScreensLessonById(lessonid);
	}
	
	
	
	
	@GET
	@Path("{lessonid}/screens/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public ScreenComplete getScreenWithId(@Context HttpServletRequest request, @PathParam("id") Long screenId,
			@PathParam("lessonid") Long lessonId) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getCompleteScreensById(lessonId, screenId,null);
	}
	
	
	@GET
	@Path("{lessonid}/screens/position/{position}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public ScreenComplete getScreenByPosition(@Context HttpServletRequest request, @PathParam("id") String id,
			@PathParam("lessonid") Long lessonId, @PathParam("position") Integer position) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getCompleteScreensByPosition(lessonId, position, false,null);
	}
	@GET
	@Path("{lessonid}/screens/nextscreen/{screenid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public ScreenComplete getScreenByNextPosition(@Context HttpServletRequest request,
			@PathParam("lessonid") Long lessonId, @PathParam("screenid") Long currentScreenId) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getNextCompleteScreensByScreenid(lessonId, currentScreenId);
	}
	
	@GET
	@Path("{lessonid}/screens/{screenid}/images")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<ImageTO> getListOfImagesByScreenId(@Context HttpServletRequest request,
			@PathParam("screenid") Long screenId,@PathParam("lessonid") Long lessonId) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getImageListByScreenId(lessonId, screenId);
	}
	
	@GET
	@Path("{lessonid}/screens/{screenid}/options")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<QuestionTO> getListOfOptionsByScreenId(@Context HttpServletRequest request,
			@PathParam("screenid") Long screenId) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getQuestionListByScreenid(screenId);
	}
	
	
	
	@GET
	@Path("testresults/users")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public List<TestResultUserTO> getTestResultUsers(@Context HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getTestUserList();
		
	}
	
	@GET
	@Path("testresults/users/{userid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON })
	public  List<TestResultTO> getTestResultUsers(@Context HttpServletRequest request,@PathParam("userid") Long userId) {
		String ip = request.getRemoteAddr();
		System.out.println("IP:" + ip);
		LOGGER.info("IP:" + ip);
		return LessonDAO.getTestResultList(userId);
	}
	
	@POST
	@Path("{lessonid}/testanswers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAnswers(List<QuestionAnswer> answers,@PathParam("lessonid") Long lessonId) {
		TestAnswerResultTO result = new TestAnswerResultTO();
		result.setResult(Settings.UN_SUCCESSFUL_RESULT);
		Gson gson = new GsonBuilder().create();
		try {
			String response=null;
			Long userId=0L;
			for(QuestionAnswer answer:answers){
				TestAnswerRequest answerRequest=new TestAnswerRequest();
				answerRequest.setAnswer(answer.getOptionId()+"");
				Long dataId=LessonDAO.getQuestionDataId(answer.getScreenId());
				answerRequest.setDataID(dataId+"");
				answerRequest.setLessonID(lessonId+"");
				answerRequest.setUserID(answer.getUserId().toString());
				userId=answer.getUserId();
				System.out.println("GSON:"+new Gson().toJson(answerRequest));
				response=Util.sendPutRequest(Settings.API_URL+"result/saveresults", new Gson().toJson(answerRequest));
				System.out.println("response:"+response);
			}
			if(response!=null && !response.replace("\"", "").trim().equals("0")){
				result.setResult(Settings.SUCCESSFUL_RESULT);
				TestResultTO testResultTO=LessonDAO.getTestResultList(userId, lessonId);
				result.setResult("Thank you for sending your answers.");
				if(testResultTO!=null){
					result.setTotalScore(testResultTO.getTotalScore());
					result.setUserScore(testResultTO.getUserScore());
				}
				return Response.status(200).entity(gson.toJsonTree(result).toString()).build();
			}
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		result.setResult(Settings.UN_SUCCESSFUL_RESULT);
		return Response.status(400).entity(gson.toJsonTree(result).toString()).build();
		// return HTTP response 200 in case of success
	}
	
	
}
