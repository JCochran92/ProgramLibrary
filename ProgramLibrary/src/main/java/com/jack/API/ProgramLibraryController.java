package com.jack.API;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgramLibraryController {


	@GetMapping("/getprograms")
	public GetPrograms GetPrograms() {
		return new GetPrograms();
	}
	
	@GetMapping("/getsection")
	public GetSection getSection(@RequestParam(value = "SectionID", defaultValue = "1")int SectionID){
		return new GetSection(SectionID);
	}
	
}
