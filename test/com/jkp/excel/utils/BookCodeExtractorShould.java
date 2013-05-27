package com.jkp.excel.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class BookCodeExtractorShould {

	@Test
	public void test() {
		assertEquals("_q&a",BookCodeExtractor.regExtractBookCode("kauna hari hamaro tumharo nata_q&a"));
		
	}

}
