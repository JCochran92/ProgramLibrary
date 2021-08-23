package com.jack.POJO;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.jack.DAO.*;

public class Section implements SectionDAO {

	private String Name;
	private String Description;
	private String OverviewImageBase64;
	private int OrderIndex;
	private int SectionID;
	private int ProgramID;
	private List<Activity> Activities;
	
	
	
	///////////////////////////// Constructors //////////////////////////////////////////////
	public Section(String name, String description, String overviewImageBase64, int sectionID, int programID) {
		
		Name = name;
		Description = description;
		OverviewImageBase64 = overviewImageBase64;
		SectionID = sectionID;
		ProgramID = programID;
		Activities = pullAllSectionActivities(SectionID);
	}
	public Section(int sectionID) {
	    this.SectionID = sectionID;
		Section tempSection = pullSection(sectionID);
		this.Description = tempSection.getDescription();
		this.Name = tempSection.getName();
		this.OrderIndex = tempSection.getOrderIndex();
		this.ProgramID = tempSection.getProgramID();
		this.Activities = tempSection.getActivities();
		this.OverviewImageBase64 = tempSection.getOverviewImageBase64();
		tempSection = null;
	}
	
	public Section() {
	}
	
	///////////////////////////// Methods ///////////////////////////////////////////////////

	
/////////////////////////////// This pulls all available sections ///////////////////////////////
	public static List<Section> pullAllSections() {	
		// Queries all sections
		
		List<Section> listTempSection = new ArrayList();
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Sections\"";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Section tempSection = new Section();
				tempSection.setName(rs.getString(1));
				tempSection.setDescription(rs.getString(2));
				
				tempSection.setOrderIndex(rs.getInt(4));
				tempSection.setSectionID(rs.getInt(5));
				tempSection.setProgramID(rs.getInt(5));
				
				//transform to base64
				if (rs.getBlob(3) != null) {
				byte[] OverviewImageByte = rs.getBlob(3).getBytes(1, (int)rs.getBlob(3).length());
				tempSection.setOverviewImageBase64(Base64.getEncoder().encodeToString(OverviewImageByte));
				}
				
				
				listTempSection.add(tempSection);
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}	
		
			return listTempSection;
		
	}
	
/////////////////////////////// Pulls a section by programmatic ID ///////////////////////////////	
	public static List<Section> pullSectionsByProgram(int P_ID){
		
		List<Section> tempSectionList = new ArrayList();
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Sections\" WHERE \"Program_ID\" = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, P_ID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Section tempSection = new Section();
				tempSection.setName(rs.getString(1));
				tempSection.setDescription(rs.getString(2));
				
				tempSection.setOrderIndex(rs.getInt(4));
				tempSection.setSectionID(rs.getInt(5));
				tempSection.setProgramID(rs.getInt(5));
				
				//transform to base 64
				if (rs.getBlob(3) != null) {
				byte[] OverviewImageByte = rs.getBlob(3).getBytes(1, (int)rs.getBlob(3).length());
				tempSection.setOverviewImageBase64(Base64.getEncoder().encodeToString(OverviewImageByte));
				}
				tempSectionList.add(tempSection);
				
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}	
		
			
			return tempSectionList;
		
	}
	
/////////////////////////////// This pulls a section by section ID ///////////////////////////////	

	@Override
	public Section pullSection(int S_ID) {
		// Pull section from DB via S_ID
		
		Section tempSection = new Section();
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Sections\" WHERE \"Section_ID\" = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, S_ID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				tempSection.setName(rs.getString(1));
				tempSection.setDescription(rs.getString(2));
				
				tempSection.setOrderIndex(rs.getInt(4));
				tempSection.setSectionID(rs.getInt(5));
				tempSection.setProgramID(rs.getInt(5));
				
				// transform blob into base64
				if (rs.getBlob(3) != null) {
				byte[] OverviewImageByte = rs.getBlob(3).getBytes(1, (int)rs.getBlob(3).length());
				tempSection.setOverviewImageBase64(Base64.getEncoder().encodeToString(OverviewImageByte));
				}
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}	
		
			tempSection.setActivities(pullAllSectionActivities(S_ID));
			return tempSection;
		
		
		
		
	}
	
/////////////////////////////// Pulls all the activities  ///////////////////////////////
	@Override
	public List<Activity> pullAllSectionActivities(int S_ID) {

		// TODO Auto-generated method stub
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
					
					String sql = "SELECT * FROM \"ProgramLibrary\".\"Activity\" LEFT JOIN \"ProgramLibrary\".\"Answer\" ON \"ProgramLibrary\".\"Activity\".\"Activity_ID\"= \"ProgramLibrary\".\"Answer\".\"Activity_ID\"  WHERE \"ProgramLibrary\".\"Activity\".\"Section_ID\"= ?";
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setInt(1, S_ID);
					ResultSet rs = ps.executeQuery();
					
					while(rs.next()){
						
						tempActID = rs.getInt(1);
						tempSecID = rs.getInt(2);
						tempContent = rs.getString(3);
						tempQuestion = rs.getString(4);
						tempAnswerID = rs.getInt(5);
						tempAnswerText= rs.getString(6);
						
						
						//If the Activity does not already exist then add to array list.
						
						if (!Activity.containsActivityID(tempActivityList, tempActID) ) {
							List<String> tempAnswers = new ArrayList();
							tempAnswers.add(tempAnswerText);
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
	
//////////////////////// Getters and Setters //////////////////////////////////
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getSectionID() {
		return SectionID;
	}
	public void setSectionID(int sectionID) {
		SectionID = sectionID;
	}
	public int getProgramID() {
		return ProgramID;
	}
	public void setProgramID(int programID) {
		ProgramID = programID;
	}
	public List<Activity> getActivities() {
		return Activities;
	}
	public void setActivities(List<Activity> activities) {
		Activities = activities;
	}
	public int getOrderIndex() {
		return OrderIndex;
	}
	public void setOrderIndex(int orderIndex) {
		OrderIndex = orderIndex;
	}
	
	public String getOverviewImageBase64() {
		return OverviewImageBase64;
	}
	public void setOverviewImageBase64(String overviewImageBase64) {
		OverviewImageBase64 = overviewImageBase64;
	}
	
////////////////////To String /////////////////////////////////////////
	@Override
	public String toString() {
		return "Section [Name=" + Name + ", Description=" + Description + ", OverviewImage=" + OverviewImageBase64
				+ ", OrderIndex=" + OrderIndex + ", SectionID=" + SectionID + ", ProgramID=" + ProgramID
				+ ", Activities=" + Activities + "]";
	}
	
	
	
	
	

}
