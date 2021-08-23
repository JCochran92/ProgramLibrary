package com.jack.DAO;

import java.util.List;

import com.jack.POJO.Program;
import com.jack.POJO.Section;

public interface ProgramDAO extends DAO{
	
	public Program pullProgramInfo(int P_ID);
	
	public List<Section> pullAllProgramSections(int P_ID);
	
}
