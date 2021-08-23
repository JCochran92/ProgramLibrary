package com.jack.POJO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.jack.DAO.*;

public class Activity implements ActivityDAO{

	int activityID;
	String content;
	String question;
	List<String> answers;
///////////// Constructors //////////////////////////////////////////
	public Activity (int activityID, String content, String question, List<String> answers) {
		
		this.activityID = activityID;
		this.content = content;
		this.question = question;
		this.answers = answers;
		
	}
	
	public Activity(int activityID, String content, String question) {
		this.activityID = activityID;
		this.content = content;
		this.question = question;
	}
	
public Activity (int activityID) {
		
			this.activityID = activityID;
		
			
			Activity tempActivity = readRow(activityID);
			
			this.content = tempActivity.content;
			this.question = tempActivity.question;
			this.answers = tempActivity.answers;
		}
	
	
//////////////////// Methods ///////////////////////////////////////////	
	
/////////////////// Get Activity info /////////////////////////////////////
	@Override
	public Activity readRow(int A_ID) {
		
	String tempContent = "";
	String tempQuestion = "";
	List<String> tempAnswers = null;
	Activity tempActivity = null;
		
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			
			
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Activity\" WHERE \"Activity_ID\" = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, activityID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				tempContent = rs.getString(3);
				tempQuestion = rs.getString(4);
				
			}
			if (question !=  "") {
				tempAnswers = pullAllActivityAnswers(activityID);
				
				}
			tempActivity = new Activity(A_ID, tempContent, tempQuestion,tempAnswers);	
				
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		
		return tempActivity;
	
		
	}

////////////////// Pull All Activities /////////////////////////////////////	
	public static List<Activity> pullAll() {
		//Pulls all activities with questions
		List<Activity> tempActivityList = new ArrayList();
		String tempContent = "";
		String tempQuestion = "";
		Integer tempAnswerID = 0;
		String tempAnswerText = "";
		
		Integer tempActID = 0;
		Integer tempSecID = 0;
		// Query of Activity and Answers
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Activity\" LEFT JOIN \"ProgramLibrary\".\"Answer\" ON \"ProgramLibrary\".\"Activity\".\"Activity_ID\"= \"ProgramLibrary\".\"Answer\".\"Activity_ID\"";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				
				tempActID = rs.getInt(1);
				tempSecID = rs.getInt(2);
				tempContent = rs.getString(3);
				tempQuestion = rs.getString(4);
				tempAnswerID = rs.getInt(5);
				tempAnswerText= rs.getString(6);
				
				
				//If the Activity does not already exist then add to array list.
				
				if (!containsActivityID(tempActivityList, tempActID) ) {
					List<String> tempAnswers = new ArrayList();
					tempAnswers.add(tempAnswerText);
					System.out.println(tempAnswers);
					tempActivityList.add(new Activity(tempActID,tempContent,tempQuestion,tempAnswers));
					
					
				}
				// If Activity already exists then add to activity.
				
				else {
					
					for (int i =0; i<tempActivityList.size(); i++) {
						
						if (tempActID.equals(tempActivityList.get(i).getActivityID())) {
							
							
							tempActivityList.get(i).addAnswerText(tempAnswerText);
							
						}
						
					}
				
					}
				
			}
			
			}catch (SQLException e) {
				e.printStackTrace();
			}	
		return tempActivityList;
		
	}

///////////////////// Pull All Answers for an Activity /////////////////////////////	
	@Override
	public List<String> pullAllActivityAnswers(int A_ID) {
		
		List<String> tempAnswers = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(url,username,password);) {
		String sql = "SELECT \"Answer_Text\" FROM \"ProgramLibrary\".\"Answer\" WHERE \"Activity_ID\" = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, activityID);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			tempAnswers.add(rs.getString(1));	
		}
		}catch (SQLException e) {
			e.printStackTrace();
		}	
		return tempAnswers;
		// TODO Auto-generated method stub
		
	}
	
//////////////////////// Checks Activity for the Activity ID value ////////////////////////////////
	public static boolean containsActivityID(final List<Activity> list, final Integer A_ID){
	    return list.stream().map(Activity::getActivityID).filter(A_ID::equals).findFirst().isPresent();
	}
	
	public void addAnswerText(String answerText) {
		this.answers.add(answerText);
	}
	
//////////// Setters and Getters //////////////////////////
		public int getActivityID() {
			return activityID;
		}

		public void setActivityID(int activityID) {
			this.activityID = activityID;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public List<String> getAnswers() {
			return answers;
		}

		public void setAnswers(List<String> answers) {
			this.answers = answers;
		}
///////////// To String /////////////////////////////////////
	@Override
	public String toString() {
		return "Activity [activityID=" + activityID + ", content=" + content + ", question=" + question + ", answers="
				+ answers + "]";
	}

	
	
}
