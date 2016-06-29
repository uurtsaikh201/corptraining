package training;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import settings.Settings;
import to.ImageTO;
import to.LessonBr;
import to.ModuleTO;
import to.QuestionTO;
import to.RemoteScreen;
import to.Screen;
import to.ScreenComplete;
import to.TestResultTO;
import to.TestResultUserTO;
import to.TextTO;
import util.Util;

public class LessonDAO {

	public static List<ModuleTO> getModuleList() {

		List<ModuleTO> list = new ArrayList<ModuleTO>();
		try {
			Connection connection = Util.getConnection();
			Statement statement = connection.createStatement();
			// ResultSet resultSet = statement.executeQuery("select
			// id,title,screencount,description from lessons order by id");
			String Sql = "SELECT id,name FROM modules";

			ResultSet resultSet = statement.executeQuery(Sql);
			while (resultSet.next()) {
				ModuleTO moduleTO = new ModuleTO();
				moduleTO.setId(resultSet.getLong("id"));
				moduleTO.setName(resultSet.getString("name"));
				list.add(moduleTO);
			}
			Util.closeConnection(connection, statement,resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<LessonBr> getBriefLessons(String lessonName) {

		List<LessonBr> lessonList = new ArrayList<LessonBr>();
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement statement = connection.createStatement();
			// ResultSet resultSet = statement.executeQuery("select
			// id,title,screencount,description from lessons order by id");
			String Sql = "SELECT BreezePages.TestID        AS id,BreezeTest.TestName       AS title, "
					+ "	COUNT(BreezePages.PageID) AS screenCount, BreezeTest.Grade          AS description "
					+ " FROM (select * from BreezePages where PageDisabled='false') BreezePages INNER JOIN BreezeTest BreezeTest "
					+ " ON BreezePages.TestID = BreezeTest.TestID WHERE (BreezeTest.Status = 'Active') ";
			if(lessonName!=null){
				Sql+="and lower(BreezeTest.TestName) like lower('%"+lessonName.trim()+"%')";
			}else {
				Sql+=" and BreezeTest.TestName<>'Safe Job Procedures' and BreezeTest.TestName<>'Air Brake Testing'";
			}
					Sql+= " GROUP BY BreezePages.TestID, BreezeTest.TestName, 	BreezeTest.Grade";
			System.out.println("SELECT LESSONS:"+Sql);
			ResultSet resultSet = statement.executeQuery(Sql);
			while (resultSet.next()) {
				LessonBr lesson = new LessonBr();
				lesson.setId(resultSet.getInt("id"));
				lesson.setTitle(resultSet.getString("title"));
				lesson.setUrl(Settings.BASE_URL + "" + resultSet.getInt("id"));
				lesson.setScreenCount(resultSet.getInt("screencount"));
				lesson.setDescription(resultSet.getString("description"));
				lessonList.add(lesson);
			}
			Util.closeConnection(connection, statement, resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lessonList;
	}

	public static LessonBr getLessonBriefLessonById(Long id) {
		LessonBr lesson = new LessonBr();
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement screenStatement = connection.createStatement();
			String Sql = "SELECT BreezePages.TestID        AS id,BreezeTest.TestName       AS title, "
					+ "	COUNT(BreezePages.PageID) AS screenCount, BreezeTest.Grade          AS description "
					+ " FROM (select * from BreezePages where PageDisabled='false') BreezePages INNER JOIN BreezeTest BreezeTest "
					+ " ON BreezePages.TestID = BreezeTest.TestID WHERE BreezeTest.TestID =" + id
					+ " and (BreezeTest.Status = 'Active') "
					+ " GROUP BY BreezePages.TestID, BreezeTest.TestName, 	BreezeTest.Grade";

			ResultSet lessonResultset = screenStatement.executeQuery(Sql);

			while (lessonResultset.next()) {
				lesson.setId(lessonResultset.getInt("id"));
				lesson.setTitle(lessonResultset.getString("title"));
				lesson.setUrl(Settings.BASE_URL + "" + lessonResultset.getInt("id"));
				lesson.setScreenCount(lessonResultset.getInt("screencount"));
				lesson.setDescription(lessonResultset.getString("description"));
			}
			Util.closeConnection(connection, screenStatement, lessonResultset);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return lesson;
	}

	public static List<Screen> getScreensLessonById(Long id) {
		List<Screen> list = new ArrayList<Screen>();
		boolean isStartZero = false;
		Connection connection = Util.getConnectionMSSQL();
		List<RemoteScreen> remoteScreenList = getRemoteScreenList(connection, id);
		Statement sta = null;
		ResultSet rs = null;
		try {
			String Sql = "select pageId id,OrderID,TestId from BreezePages where PageDisabled='false' and testid in ( select testid from BreezeTest where Status='Active' ) and TestID="
					+ id + "  order by TestID,OrderID";
			System.out.println("screenSQL:" + Sql);
			sta = connection.createStatement();
			rs = sta.executeQuery(Sql);
			while (rs.next()) {
				Screen screen = new Screen();
				screen.setId(rs.getInt("id"));
				if (list.size() == 0) {
					if (rs.getLong("OrderId") == 0) {
						isStartZero = true;
					}
				}
				if (isStartZero == true) {
					screen.setPosition((int) rs.getLong("OrderId"));
				} else {
					screen.setPosition((int) (rs.getLong("OrderId") - 1));
				}
				screen.setTitle((screen.getPosition() + 1) + "");
				System.out.println("SCREENS :" + screen.getId() + " " + screen.getPosition());
				String screenType = null;
				for (RemoteScreen remoteScreen : remoteScreenList) {
					if (remoteScreen.getScreenId() != null
							&& remoteScreen.getScreenId().longValue() == screen.getId().longValue()) {

						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("audio")) {
							screen.setAudio_url(Settings.LESSONS_URL + id + "/" + screen.getId() + "/"
									+ remoteScreen.getDataText().replace(" ", "%20"));
							screenType = Util.buildScreenType(screenType, "audio");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("text")) {

							if (remoteScreen.getDataText() != null) {
								String[] textArray = remoteScreen.getDataText().split("<br>");
								List<TextTO> textTOs = new ArrayList<TextTO>();

								for (int i = 0; i < textArray.length; i++) {
									if (!Jsoup.parse(textArray[i]).text().trim().equals("")) {
										TextTO textTO = new TextTO();
										textTO.setOrder(i);
										textTO.setText(Jsoup.parse(textArray[i]).text().trim());
										textTOs.add(textTO);
									}
								}
								// textTO.setText(remoteScreen.getDataText());
								screen.setTextList(textTOs);
							}

							screenType = Util.buildScreenType(screenType, "text");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("multiple choice")) {
							screen.setQuestion(remoteScreen.getDataText());
							screen.setOptionsUrl(Settings.BASE_URL + id + "/screens/" + screen.getId() + "/options");
							screenType = Util.buildScreenType(screenType, "question");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("image")) {
							// screen.setImagesUrl(Settings.LESSONS_URL+lessonId+"/"+screen.getId()+"/"+remoteScreen.getDataText());
							screen.setImagesUrl(Settings.BASE_URL + id + "/screens/" + screen.getId() + "/images");
							screenType = Util.buildScreenType(screenType, "image");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("video")) {
							if (remoteScreen.getDataText() != null && remoteScreen.getDataText().length() > 2) {
								screen.setVideo_url(Settings.LESSONS_URL + id + "/" + screen.getId() + "/"
										+ remoteScreen.getDataText().replace(" ", "%20"));
							} else {
								screen.setVideo_url("https://player.vimeo.com/video/" + remoteScreen.getMedia());
							}
							screen.setMedia(remoteScreen.getMedia());
							screenType = Util.buildScreenType(screenType, "video");
						}

						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("speeking")) {
							screenType = Util.buildScreenType(screenType, "speeking");
						}

						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("essay")) {
							screenType = Util.buildScreenType(screenType, "essay");
						}
					}
				}
				screen.setType(screenType);
				list.add(screen);
			}
			Util.closeConnection(connection, sta, rs);
		} catch (Exception ee) {
			ee.printStackTrace();
			return new ArrayList<Screen>();
		}
		return list;
	}

	public static ScreenComplete getCompleteScreensById(Long lessonId, Long screenId, Connection connection) {
		ScreenComplete screen = new ScreenComplete();
		if (connection == null) {
			connection = Util.getConnectionMSSQL();
		}
		List<RemoteScreen> remoteScreenList = getRemoteScreenDataListByScreenId(connection, lessonId, screenId);
		Statement sta = null;
		ResultSet rs = null;
		try {
			String Sql = "select pageId id,OrderID,TestId from BreezePages where PageDisabled='false' and testid in ( select testid from BreezeTest where Status='Active' ) and pageId="
					+ screenId + "  order by TestID,OrderID";
			System.out.println("screenSQL:" + Sql);

			sta = connection.createStatement();
			rs = sta.executeQuery(Sql);
			while (rs.next()) {
				screen.setPosition(rs.getInt("OrderID"));
				screen.setTitle((screen.getPosition() + 1) + "");
				screen.setId(rs.getInt("id"));
				System.out.println("SCREENS :" + screen.getId() + " " + screen.getPosition());
				String screenType = null;
				for (RemoteScreen remoteScreen : remoteScreenList) {
					if (remoteScreen.getScreenId() != null
							&& remoteScreen.getScreenId().longValue() == screen.getId().longValue()) {

						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("audio")) {
							screen.setAudio_url(Settings.LESSONS_URL + lessonId + "/" + screen.getId() + "/"
									+ remoteScreen.getDataText());
							screenType = Util.buildScreenType(screenType, "audio");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("text")) {
							String[] textArray = remoteScreen.getDataText().split("<br>");
							List<TextTO> textTOs = new ArrayList<TextTO>();

							for (int i = 0; i < textArray.length; i++) {
								if (!Jsoup.parse(textArray[i]).text().trim().equals("")) {
									TextTO textTO = new TextTO();
									textTO.setOrder(i);
									textTO.setText(Jsoup.parse(textArray[i]).text().trim());
									textTOs.add(textTO);
								}
							}
							// textTO.setText(remoteScreen.getDataText());
							screen.setTextList(textTOs);

							screenType = Util.buildScreenType(screenType, "text");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("multiple choice")) {

							screen.setQuestion(remoteScreen.getDataText());

							screenType = Util.buildScreenType(screenType, "question");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("image")) {
							// screen.setImagesUrl(Settings.LESSONS_URL+lessonId+"/"+screen.getId()+"/"+remoteScreen.getDataText());
							screenType = Util.buildScreenType(screenType, "image");
						}
						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("video")) {
							if (remoteScreen.getDataText() != null && remoteScreen.getDataText().length() > 2) {
								screen.setVideo_url(Settings.LESSONS_URL + lessonId + "/" + screen.getId() + "/"
										+ remoteScreen.getDataText().replace(" ", "%20"));

							} else {
								screen.setVideo_url("https://player.vimeo.com/video/" + remoteScreen.getMedia());
							}
							screen.setMedia(remoteScreen.getMedia());
							screenType = Util.buildScreenType(screenType, "video");
						}

						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("speeking")) {
							screenType = Util.buildScreenType(screenType, "speeking");
						}

						if (remoteScreen.getDataType() != null
								&& remoteScreen.getDataType().toLowerCase().contains("essay")) {
							screenType = Util.buildScreenType(screenType, "essay");
						}
					}
				}
				screen.setOptions(getQuestionList(screenId, connection));
				screen.setImages(getImageList(connection, lessonId, screenId));
				screen.setType(screenType);
			}
			Util.closeConnection(connection, sta, rs);
		} catch (Exception ee) {
			ee.printStackTrace();
			return new ScreenComplete();
		}
		return screen;
	}

	public static ScreenComplete getCompleteScreensByPosition(Long lessonId, Integer position, boolean isNextScreen,
			Connection connection) {
		ScreenComplete screen = new ScreenComplete();
		try {
			if (connection == null) {
				connection = Util.getConnectionMSSQL();
			}
			Long screenId = getScreenIdByPosition(connection, lessonId, position, isNextScreen);
			if (screenId != null) {
				screen = getCompleteScreensById(lessonId, screenId, connection);
			}
			Util.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screen;
	}

	public static ScreenComplete getNextCompleteScreensByScreenid(Long lessonId, Long screenId) {
		ScreenComplete screen = new ScreenComplete();
		try {
			Connection connection = Util.getConnectionMSSQL();

			Integer position = getNextPositionByScreenId(connection, lessonId, screenId);
			if (position != null) {
				screen = getCompleteScreensByPosition(lessonId, position, false, connection);
			}
			Util.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screen;
	}

	public static Long getScreenIdByPosition(Connection connection, Long lessonId, Integer position,
			boolean isNextScreen) {
		Long id = null;
		try {
			if (isNextScreen == true) {
				position += 1;
			}
			Statement st = connection.createStatement();
			String sql = "select pageid screenid from BreezePages where testid=" + lessonId
					+ " and  PageDisabled='false' and  TestID=" + lessonId + " and OrderID=" + position;
			System.out.println("getScreenIdByPosition:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				id = rs.getLong("screenid");
			}
			Util.closeConnection(null, st, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static Integer getNextPositionByScreenId(Connection connection, Long lessonId, Long screenid) {
		Integer id = null;
		try {

			Statement st = connection.createStatement();
			String sql = "select OrderID sc_position from BreezePages where testid=" + lessonId
					+ " and  PageDisabled='false' and OrderID= (select OrderID+1 from BreezePages where TestID="
					+ lessonId + " and PageID=" + screenid + ")";
			System.out.println("getNextPositionByScreenId:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt("sc_position");
			}
			Util.closeConnection(null, st, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static List<ImageTO> getImageList(Connection connection, Long lessonId, Long screenId) {
		List<ImageTO> imageList = new ArrayList<ImageTO>();
		try {

			Statement imagesStatement = connection.createStatement();
			String imageSQL = "select dataid title,datatext url from BreezeData where  BreezeData.datadisabled=0 and  DataType='Image'  and PageID="
					+ screenId;
			// System.out.println("imageSQL:"+imageSQL);
			ResultSet imagesResultset = imagesStatement.executeQuery(imageSQL);
			while (imagesResultset.next()) {
				ImageTO image = new ImageTO();
				image.setTitle(imagesResultset.getString("title"));
				image.setUrl(Settings.LESSONS_URL + lessonId + "/" + screenId + "/" + imagesResultset.getString("url"));
				imageList.add(image);
			}
			Util.closeConnection(null, imagesStatement, imagesResultset);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageList;
	}

	public static List<ImageTO> getImageListByScreenId(Long lessonId, Long screenId) {
		List<ImageTO> imageList = new ArrayList<ImageTO>();
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement imagesStatement = connection.createStatement();
			String imageSQL = "select dataid title,datatext url from BreezeData where DataType='Image' and datadisabled=0  and PageID="
					+ screenId;
			// System.out.println("imageSQL:"+imageSQL);
			ResultSet imagesResultset = imagesStatement.executeQuery(imageSQL);
			while (imagesResultset.next()) {
				ImageTO image = new ImageTO();
				image.setTitle(imagesResultset.getString("title"));
				image.setUrl(Settings.LESSONS_URL + lessonId + "/" + screenId + "/"
						+ imagesResultset.getString("url").replace(" ", "%20"));
				imageList.add(image);
			}
			Util.closeConnection(connection, imagesStatement, imagesResultset);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageList;
	}

	public static List<QuestionTO> getQuestionListByScreenid(Long screenId) {
		List<QuestionTO> questionList = new ArrayList<QuestionTO>();
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement questionStatement = connection.createStatement();
			String questionSQL = "select BreezeSubData.SubDataID,BreezeSubData.DataID,BreezeSubData.SubData title,"
					+ " BreezeSubData.ExtraInfo detail from Breezedata inner join BreezeSubData on "
					+ " Breezedata.DataID=BreezeSubData.DataID  where Breezedata.DataType='Multiple Choice'"
					+ " and BreezeData.datadisabled=0 and Breezedata.PageID=" + screenId + " order by BreezeSubData.SubDataID ";
			System.out.println("questionSQL:" + questionSQL);
			ResultSet questionResultset = questionStatement.executeQuery(questionSQL);
			int order = 0;
			while (questionResultset.next()) {
				QuestionTO question = new QuestionTO();
				question.setId(questionResultset.getInt("SubDataID"));
				System.out.println("SubDataID: " + questionResultset.getInt("SubDataID"));
				String title = "";
				if (questionResultset.getString("title") != null) {
					title = Jsoup.parse(questionResultset.getString("title")).text().trim().replace("&nbsp;", "")
							.replace("&nbsp", "").replace("&#39;", "");
				}
				question.setTitle(title);

				String detail = "";
				if (questionResultset.getString("detail") != null) {
					detail = Jsoup.parse(questionResultset.getString("detail")).text().trim().replace("&nbsp;", "")
							.replace("&nbsp", "").replace("&#39;", "");
				}
				System.out.println("question:" + question.getTitle() + " " + question.getDetail());
				question.setDetail(detail);
				question.setOrder(order);
				questionList.add(question);
				order++;
			}

			Util.closeConnection(connection, questionStatement, questionResultset);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return questionList;
	}

	public static Long getQuestionDataId(Long screenId) {
		Long dataId = 0L;
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement questionStatement = connection.createStatement();
			String questionSQL = "select BreezeSubData.SubDataID,BreezeSubData.DataID,BreezeSubData.SubData title,"
					+ " BreezeSubData.ExtraInfo detail from Breezedata inner join BreezeSubData on "
					+ " Breezedata.DataID=BreezeSubData.DataID  where Breezedata.DataType='Multiple Choice' and BreezeData.datadisabled=0 "
					+ " and Breezedata.PageID=" + screenId + " order by BreezeSubData.SubDataID ";
			System.out.println("questionSQL:" + questionSQL);
			ResultSet questionResultset = questionStatement.executeQuery(questionSQL);

			while (questionResultset.next()) {
				dataId = questionResultset.getLong("DataID");
			}

			Util.closeConnection(connection, questionStatement, questionResultset);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataId;
	}

	public static List<QuestionTO> getQuestionList(Long screenId, Connection connection) {
		List<QuestionTO> questionList = new ArrayList<QuestionTO>();
		try {

			Statement questionStatement = connection.createStatement();
			String questionSQL = "select BreezeSubData.SubDataID,BreezeSubData.DataID,BreezeSubData.SubData title,"
					+ " BreezeSubData.ExtraInfo detail from Breezedata inner join BreezeSubData on "
					+ " Breezedata.DataID=BreezeSubData.DataID  where Breezedata.DataType='Multiple Choice'"
					+ " and BreezeData.datadisabled=0  and Breezedata.PageID=" + screenId + " order by BreezeSubData.SubDataID ";
			System.out.println("questionSQL:" + questionSQL);
			ResultSet questionResultset = questionStatement.executeQuery(questionSQL);
			int order = 0;
			while (questionResultset.next()) {
				QuestionTO question = new QuestionTO();
				String title = "";
				if (questionResultset.getString("title") != null) {
					title = Jsoup.parse(questionResultset.getString("title")).text().replace("&nbsp;", "")
							.replace("&nbsp", "").replace("&#39;", "");
				}
				question.setTitle(title);

				String detail = "";
				if (questionResultset.getString("detail") != null) {
					detail = Jsoup.parse(questionResultset.getString("detail")).text().replace("&nbsp;", "")
							.replace("&nbsp", "").replace("&#39;", "");
					;
					question.setDetail(detail);
				}
				question.setId(questionResultset.getInt("SubDataID"));
				question.setDataId(questionResultset.getInt("DataID"));
				question.setOrder(order);
				questionList.add(question);
				order++;
			}

			Util.closeConnection(null, questionStatement, questionResultset);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return questionList;
	}

	public static List<TestResultUserTO> getTestUserList() {
		List<TestResultUserTO> userList = new ArrayList<TestResultUserTO>();
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement userStatement = connection.createStatement();
			// String userSQL = "SELECT distinct BreezeUser.UserID
			// ,BreezeUser.DisplayName FROM BreezeResults BreezeResults INNER"
			// + " JOIN BreezeData BreezeData ON BreezeResults.DataID =
			// BreezeData.DataID INNER JOIN "
			// + " BreezeUser BreezeUser ON BreezeResults.UserID =
			// BreezeUser.UserID "
			// + " INNER JOIN BreezePages BreezePages ON BreezePages.PageID =
			// BreezeData.PageID "
			// + " INNER JOIN BreezeTest BreezeTest ON BreezePages.TestID =
			// BreezeTest.TestID";

			String userSQL = "SELECT distinct BreezeUser.UserID ,BreezeUser.DisplayName FROM  BreezeUser";
			// + " BreezeResults BreezeResults INNER"
			// + " JOIN BreezeData BreezeData ON BreezeResults.DataID =
			// BreezeData.DataID INNER JOIN "
			// + " BreezeUser BreezeUser ON BreezeResults.UserID =
			// BreezeUser.UserID "
			// + " INNER JOIN BreezePages BreezePages ON BreezePages.PageID =
			// BreezeData.PageID "
			// + " INNER JOIN BreezeTest BreezeTest ON BreezePages.TestID =
			// BreezeTest.TestID";

			System.out.println("userSQL:" + userSQL);
			ResultSet userResultSet = userStatement.executeQuery(userSQL);

			while (userResultSet.next()) {
				TestResultUserTO resultUserTO = new TestResultUserTO();
				resultUserTO.setDisplayName(userResultSet.getString("DisplayName"));
				resultUserTO.setUserId(userResultSet.getLong("UserID"));

				userList.add(resultUserTO);
			}

			Util.closeConnection(connection, userStatement, userResultSet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}

	public static TestResultTO getTestResultList(Long userId, Long lessonId) {

		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement st = connection.createStatement();
			/*
			 * String userSQL =
			 * "select max(tests.TestName) TestName,max(tests.TestId) testid,max(res_users.displayname)"
			 * +
			 * " displayname,sum(tests.score) total_score,sum(ISNULL(res_users.user_score,0)) "
			 * +
			 * " user_score, max(ISNULL(res_users.userid,0)) userid from (select te.TestName,te.testid,min(dt.value)"
			 * +
			 * " score,dt.dataid from breezedata dt inner join breezepages pg on dt.pageid=pg.pageid inner join"
			 * +
			 * " breezetest te on te.testid=pg.testid left join breezeresults rs on dt.dataid=rs.dataid "
			 * +
			 * " where dt.pageid in (select pageid from breezepages where testid in "
			 * + "(select distinct lessonid from breezeresults where userid="
			 * +userId+" )) and datatype='Multiple Choice' " +
			 * " group by te.TestName,te.testid,dt.dataid) tests left join  " +
			 * " (select dataid,breezeresults.userid,score user_score,breezeuser.displayname from "
			 * +
			 * " breezeresults inner join breezeuser on breezeresults.userid=breezeuser.userid "
			 * + " where breezeresults.userid="+userId+
			 * ") res_users on tests.dataid=res_users.dataid";
			 */
			String userSQL = "	select r1.testname,r1.total_score,r2.* from ( select ts.testname,sc.* from breezetest ts inner join "
					+ " (select sum(dt.value) total_score,pg.testid from breezedata dt inner join breezepages  pg on"
					+ " dt.pageid=pg.pageid  where dt.datatype='Multiple Choice' and dt.datadisabled=0 and pg.testid="+lessonId+" group by pg.testid) "
					+ "	sc  on ts.testid=sc.testid where sc.testid=" + lessonId
					+ " ) r1  inner join ( select breezeresults.lessonid "
					+ " testid,breezeresults.userid,sum(score) user_score,breezeuser.displayname from "
					+ " breezeresults inner join breezeuser on breezeresults.userid=breezeuser.userid  where breezeresults.userid= "
					+ userId + " and lessonid=" + lessonId
					+ " group by breezeresults.userid,breezeresults.lessonid,breezeuser.displayname) r2 on r1.testid=r2.testid";
			System.out.println("user result userSQL:" + userSQL);
			ResultSet rs = st.executeQuery(userSQL);

			while (rs.next()) {
				TestResultTO result = new TestResultTO();
				result.setLessonId(rs.getLong("TestID"));
				if (rs.getLong("TestID") > 0) {
					result.setTotalScore(rs.getLong("total_score"));
					result.setUserId(userId);
					result.setUserScore(rs.getLong("user_score"));
					result.setLessonName(rs.getString("TestName"));
					result.setUserDisplayName(rs.getString("DisplayName"));
					System.out.println("result:"+result.getTotalScore() + " "+result.getUserScore());
					return result;
				}

			}

			Util.closeConnection(connection, st, rs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static List<TestResultTO> getTestResultList(Long userId) {
		List<TestResultTO> testResultList = new ArrayList<TestResultTO>();
		try {
			Connection connection = Util.getConnectionMSSQL();
			Statement st = connection.createStatement();
			/*
			 * String userSQL =
			 * "select max(tests.TestName) TestName,max(tests.TestId) testid,max(res_users.displayname)"
			 * +
			 * " displayname,sum(tests.score) total_score,sum(ISNULL(res_users.user_score,0)) "
			 * +
			 * " user_score, max(ISNULL(res_users.userid,0)) userid from (select te.TestName,te.testid,min(dt.value)"
			 * +
			 * " score,dt.dataid from breezedata dt inner join breezepages pg on dt.pageid=pg.pageid inner join"
			 * +
			 * " breezetest te on te.testid=pg.testid left join breezeresults rs on dt.dataid=rs.dataid "
			 * +
			 * " where dt.pageid in (select pageid from breezepages where testid in "
			 * + "(select distinct lessonid from breezeresults where userid="
			 * +userId+" )) and datatype='Multiple Choice' " +
			 * " group by te.TestName,te.testid,dt.dataid) tests left join  " +
			 * " (select dataid,breezeresults.userid,score user_score,breezeuser.displayname from "
			 * +
			 * " breezeresults inner join breezeuser on breezeresults.userid=breezeuser.userid "
			 * + " where breezeresults.userid="+userId+
			 * ") res_users on tests.dataid=res_users.dataid";
			 */
			String userSQL = "select r1.testname,r1.total_score,r2.* from ( select ts.testname,sc.* from breezetest ts inner join "
					+ " (select sum(dt.value) total_score,pg.testid from breezedata dt inner join breezepages "
					+ " pg on dt.pageid=pg.pageid  where dt.datatype='Multiple Choice' and dt.datadisabled=0 group by pg.testid) sc "
					+ " on ts.testid=sc.testid ) r1  inner join ("
					+ " select breezeresults.lessonid testid,breezeresults.userid,sum(score) user_score,breezeuser.displayname from "
					+ " breezeresults inner join breezeuser on breezeresults.userid=breezeuser.userid  where breezeresults.userid= "
					+ userId
					+ "  group by breezeresults.userid,breezeresults.lessonid,breezeuser.displayname) r2 on r1.testid=r2.testid "
					+ " where r2.testid in ( select testid from breezetest where status='Active' "
					+ " union select originalid from breezetest where status='Active')";
			System.out.println("userSQL:" + userSQL);
			ResultSet rs = st.executeQuery(userSQL);

			while (rs.next()) {
				TestResultTO result = new TestResultTO();
				result.setLessonId(rs.getLong("TestID"));
				if (rs.getLong("TestID") > 0) {
					result.setTotalScore(rs.getLong("total_score"));
					result.setUserId(userId);
					result.setUserScore(rs.getLong("user_score"));
					result.setLessonName(rs.getString("TestName"));
					result.setUserDisplayName(rs.getString("DisplayName"));

					testResultList.add(result);
				}

			}

			Util.closeConnection(connection, st, rs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return testResultList;
	}

	public static List<RemoteScreen> getRemoteScreenList(Connection conn, Long lessonId) {

		List<RemoteScreen> remoteScreenList = new ArrayList<RemoteScreen>();
		try {

			String remoteSQL = "SELECT BreezePages.TestID as lessonId, BreezePages.PageID as screenid, BreezeData.DataID ,"
					+ "  BreezeData.DataType, BreezeData.DataText, BreezeData.Media, BreezeTest.TestName AS title FROM"
					+ "  BreezePages BreezePages INNER JOIN BreezeTest BreezeTest ON BreezePages.TestID = BreezeTest.TestID INNER JOIN BreezeData BreezeData	"
					+ "  ON BreezePages.PageID = BreezeData.PageID  WHERE (BreezeTest.Status = 'Active')  and BreezePages.TestID="
					+ lessonId + "  and BreezeData.DataText<>'Enter text in here...' and BreezeData.dataid<>618 and BreezeData.datadisabled=0"
					+ "  ORDER BY BreezeData.DataID ASC";
			System.out.println("remoteSQL:" + remoteSQL);
			Statement remoteStatement = conn.createStatement();
			ResultSet remoteResultSet = remoteStatement.executeQuery(remoteSQL);
			while (remoteResultSet.next()) {
				RemoteScreen remoteScreen = new RemoteScreen();
				remoteScreen.setDataId(remoteResultSet.getInt("DataID"));
				if (remoteResultSet.getString("DataText") != null
						&& !remoteResultSet.getString("DataText").equals("Enter text in here...")) {
					if (remoteResultSet.getString("DataType") != null
							&& remoteResultSet.getString("DataType").toLowerCase().trim().equals("text")) {
						remoteScreen.setDataText(remoteResultSet.getString("DataText").replace("&nbsp;", "")
								.replace("&nbsp", "").replace("&#39;", "").replace("<br/>", "<br>"));
					} else {
						remoteScreen.setDataText(Jsoup.parse(remoteResultSet.getString("DataText")).text()
								.replace("&nbsp;", "").replace("&nbsp", "").replace("&#39;", ""));
					}
				}
				remoteScreen.setDataType(remoteResultSet.getString("DataType"));
				remoteScreen.setLessonId(remoteResultSet.getInt("lessonId"));
				remoteScreen.setMedia(remoteResultSet.getString("Media"));
				remoteScreen.setScreenId(remoteResultSet.getInt("screenid"));
				remoteScreen.setTitle(remoteResultSet.getString("title"));
				System.out.println("lessonId:"+lessonId + " datatype:"+remoteScreen.getDataType()+ " "+remoteScreen.getTitle());
				remoteScreenList.add(remoteScreen);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return remoteScreenList;
	}

	public static List<RemoteScreen> getRemoteScreenDataListByScreenId(Connection conn, Long lessonId, Long screenId) {

		List<RemoteScreen> remoteScreenList = new ArrayList<RemoteScreen>();
		try {

			String remoteSQL = "SELECT BreezePages.TestID as lessonId, BreezePages.PageID as screenid, BreezeData.DataID ,"
					+ "  BreezeData.DataType, BreezeData.DataText, BreezeData.Media, BreezeTest.TestName AS title FROM"
					+ "  BreezePages BreezePages INNER JOIN BreezeTest BreezeTest ON BreezePages.TestID = BreezeTest.TestID INNER JOIN BreezeData BreezeData	"
					+ "  ON BreezePages.PageID = BreezeData.PageID  WHERE (BreezeTest.Status = 'Active')  and BreezePages.PageID="
					+ screenId + " and BreezePages.TestID=" + lessonId
					+ " and BreezeData.DataText<>'Enter text in here...' and BreezeData.dataid<>618 and BreezeData.datadisabled=0 " + "  ORDER BY BreezeData.DataID ASC";
			System.out.println("remoteSQL:" + remoteSQL);
			Statement remoteStatement = conn.createStatement();
			ResultSet remoteResultSet = remoteStatement.executeQuery(remoteSQL);
			while (remoteResultSet.next()) {
				RemoteScreen remoteScreen = new RemoteScreen();
				remoteScreen.setDataId(remoteResultSet.getInt("DataID"));
				if (remoteResultSet.getString("DataText") != null
						&& !remoteResultSet.getString("DataText").equals("Enter text in here...")) {
					if (remoteResultSet.getString("DataType") != null
							&& remoteResultSet.getString("DataType").toLowerCase().trim().equals("text")) {
						remoteScreen.setDataText(remoteResultSet.getString("DataText").replace("&nbsp;", "")
								.replace("&nbsp", "").replace("<br/>", "<br>"));
					} else {
						remoteScreen.setDataText(Jsoup.parse(remoteResultSet.getString("DataText")).text()
								.replace("&nbsp;", "").replace("&nbsp", ""));
					}
				}
				
				remoteScreen.setDataType(remoteResultSet.getString("DataType"));
				remoteScreen.setLessonId(remoteResultSet.getInt("lessonId"));
				remoteScreen.setMedia(remoteResultSet.getString("Media"));
				remoteScreen.setScreenId(remoteResultSet.getInt("screenid"));
				remoteScreen.setTitle(remoteResultSet.getString("title"));
				System.out.println("lessonId:"+lessonId + " datatype:"+remoteScreen.getDataType()+ " "+remoteScreen.getTitle());
				remoteScreenList.add(remoteScreen);
			}
			Util.closeConnection(null, remoteStatement, remoteResultSet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return remoteScreenList;
	}

}
