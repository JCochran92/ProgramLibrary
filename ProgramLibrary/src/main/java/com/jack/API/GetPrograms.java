package com.jack.API;

import com.jack.POJO.*;
import java.util.List;
import java.util.ArrayList;


public class GetPrograms {

	private final List <Program> listOfPrograms;

	public GetPrograms() {
		
		listOfPrograms = Program.pullAllPrograms();
		
}

	public List<Program> getListOfPrograms() {
		return listOfPrograms;
	}
	
	
	}
