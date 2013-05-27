package com.jkp.excel.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.jkp.mp3tag.Kirtan;

public class ReadMPTagShould {

	ReadMPTag readMPTag = new ReadMPTag();
	
	@Test
	public void processBookCodeCorrectly() {
		
		List<Kirtan> kirtanList = ReadMPTag.readResults("D:/mp3projecttemp/BookCodeTestingResult.xls");
		BookCodeExtractor.reprocess(kirtanList);
		ReadMPTag.writeResults(kirtanList,"D:/mp3projecttemp/BookCodeTestingTemplate.xls","D:/mp3projecttemp/BookCodeTestingResult1.xls");
	}

}
