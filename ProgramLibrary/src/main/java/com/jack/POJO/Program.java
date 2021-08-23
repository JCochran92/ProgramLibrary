package com.jack.POJO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jack.DAO.*;
//import com.revature.POJO.Reimbursement;

public class Program implements ProgramDAO{
	
	int programID;
	String name;
	String Description;
	List <Section> Sections;

	
////////////// Constructors //////////////////////////////////////
	public Program (int P_ID) {
		programID = P_ID;
		Program tempProgram = pullProgramInfo(P_ID);
		this.Description = tempProgram.getDescription();
		this.name = tempProgram.getName();
		this.Sections = tempProgram.getSections();
	
		tempProgram = null;
	};
	
	public Program() {
		
	};

///////////// Methods ///////////////////////////////////////////////
	
////////////////////////////// Pulls Program Info for given Program ID ///////////////////////
	@Override
	public Program pullProgramInfo(int P_ID) {
		// TODO Auto-generated method stub
		
		Program tempProgram = new Program();
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Program\" WHERE \"Program_ID\" = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, P_ID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				tempProgram.setName(rs.getString(1));
				tempProgram.setDescription(rs.getString(2));
				
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}	
		
			tempProgram.setSections(pullAllProgramSections(P_ID));
		
			return tempProgram;
	}
	
/////////////////////// Pulls all Program Sections ////////////////////////////////////////////

	@Override
	public List<Section> pullAllProgramSections(int P_ID) {
		// Return list of section using a section method.
		return Section.pullSectionsByProgram(P_ID);
	}


///////////////////// Pulls all Programs ///////////////////////////////////////////////	
	public static List<Program> pullAllPrograms() {
		// Grabs all of the current programs
		
		List<Program> tempListPrograms = new ArrayList();
		try (Connection con = DriverManager.getConnection(url,username,password);) {
			String sql = "SELECT * FROM \"ProgramLibrary\".\"Program\"";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Program tempProgram = new Program();
				tempProgram.setName(rs.getString(1));
				tempProgram.setDescription(rs.getString(2));
				tempProgram.setProgramID(rs.getInt(3));
				tempProgram.setSections(tempProgram.pullAllProgramSections(rs.getInt(3)));
				
				
				tempListPrograms.add(tempProgram);
				
				
			}
			}catch (SQLException e) {
				e.printStackTrace();
			}	
		
			
		
		return tempListPrograms;
		
		
	}
/////////////////// Setters and Getters ///////////////////////	
	public int getProgramID() {
		return programID;
	}

	public void setProgramID(int programID) {
		this.programID = programID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public List<Section> getSections() {
		return Sections;
	}

	public void setSections(List<Section> sections) {
		Sections = sections;
	}

	
//////////////// To String //////////////////////////////////	
	@Override
	public String toString() {
		return "Program [programID=" + programID + ", name=" + name + ", Description=" + Description + ", Sections="
				+ Sections + "]";
	}

	
	
	
	
	
}
