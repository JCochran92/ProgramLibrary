package com.jack.DAO;

import java.util.List;

import com.jack.POJO.Activity;

public interface ActivityDAO extends DAO {

	public Activity readRow(int A_ID);
	
	public List<String> pullAllActivityAnswers(int A_ID); 
	
	
}
