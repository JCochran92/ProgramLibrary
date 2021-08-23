package com.jack.DAO;

import java.util.List;

import com.jack.POJO.Activity;
import com.jack.POJO.Section;

public interface SectionDAO extends DAO{

	public Section pullSection(int S_ID);
	
	public List<Activity>  pullAllSectionActivities(int S_ID); 
	
}
