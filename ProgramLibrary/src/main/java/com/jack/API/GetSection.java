package com.jack.API;

import com.jack.POJO.*;

public class GetSection {

	private final Section sectionResponse;
	
	GetSection(int SectionID){
		this.sectionResponse = new Section(SectionID);
	}

	public Section getSectionResponse() {
		return sectionResponse;
	}
	
	
	
}
