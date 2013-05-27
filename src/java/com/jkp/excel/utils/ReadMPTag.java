package com.jkp.excel.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.jkp.mp3tag.BookCode;
import com.jkp.mp3tag.Constants;
import com.jkp.mp3tag.Kirtan;
import com.jkp.mp3tag.MasterLibrary;
import com.jkp.mp3tag.Mp3Bean;
import com.jkp.mp3tag.command.WriteMp3Tag;

public class ReadMPTag {
	
	public static Logger log = Logger.getLogger(ReadMPTag.class);
	
	public ReadMPTag() {}
	
	public static int KIRTANS = 8000;
	
	public static void main(String args[])  throws Exception {
		String args1[] = new String[]{"F:/master/audio"};
		 Formatter formatter = new Formatter();
		File masterLibraryFolder = new File(args1[0]);
		MasterLibrary masterLibrary = new MasterLibrary(masterLibraryFolder);
		masterLibrary.load();
		List<Kirtan> allKirtans = masterLibrary.getAllKirtans();
		int k = 0;
		//log.info(formatter.format("%5s %50s %15s %50s", "Id*", "Title*", "BookCode*", "Path*"));
		log.info(formatter.format("%s#%s#%s#%s", "Id", "Title", "BookCode", "Path"));
		formatter.flush();
		writeResults(allKirtans,"D:/mp3projecttemp/BookCodeTestingTemplate.xls","D:/mp3projecttemp/BookCodeTestingResult.xls");
		for(Kirtan kirtan: allKirtans){
			k++;
			formatter = new Formatter();
			formatter.format("%s#%s#%s#%s", k, kirtan.getTitle(),kirtan.getBookCode(),kirtan.getAbsFileName());
			log.info(formatter.toString());
			formatter.flush();
		}
	}
	
	
	public static void writeResults(List<Kirtan> allKirtans, String templateFile, String outputFile)  {
		WritableWorkbook writeWorkBook=null;
		Workbook readWorkBook = null;
		File inputWorkbook = new File(templateFile);
		try {
			readWorkBook = Workbook.getWorkbook(inputWorkbook);
			writeWorkBook = Workbook.createWorkbook(new File(outputFile),readWorkBook);
			WritableSheet writeSheet =	writeWorkBook.getSheet(0);
			int r = 1;
			for (Kirtan kirtan: allKirtans) {
				ExcelUtil.updateCell(writeSheet, r, 0,""+r);
				ExcelUtil.updateCell(writeSheet, r, 1,kirtan.getTitle());
				ExcelUtil.updateCell(writeSheet, r, 2,kirtan.getBookCode().getCode());
				ExcelUtil.updateCell(writeSheet, r, 3,""+kirtan.getBookCode().isKirtan());
				ExcelUtil.updateCell(writeSheet, r, 4,kirtan.getAbsFileName());
				
				r++;
			}
			writeWorkBook.write();
			writeWorkBook.close();
		} catch(Exception ex){
			log.error(ex,ex);
		}		
	}
	
	public static List<Kirtan> readResults(String inFile)  {
		
		List<Kirtan> kirtanList = new ArrayList<Kirtan>();
		Workbook readWorkBook = null;
		File inputWorkbook = new File(inFile);
		try {
			readWorkBook = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet =	readWorkBook.getSheet(0);
			String id,title,path,bookCodeStr = null;
			int rowCount = sheet.getRows();
			Kirtan kirtan = null;
			BookCode bookCode = null;
			for (int r = 1; r < rowCount; r++) {
				title = sheet.getCell(1, r).getContents();
				bookCodeStr = sheet.getCell(2, r).getContents();
				path = sheet.getCell(3, r).getContents();
				
				kirtan = new Kirtan();
				kirtan.setTitle(title);
				bookCode = BookCodeExtractor.regExtractBookCode(title);
				if(bookCode !=null && !bookCode.getCode().equals(bookCodeStr)) {
					kirtan.setStatus(Constants.ERROR);
					bookCode.setCode(bookCodeStr);
				}
				kirtan.setBookCode(bookCode);
				kirtan.setAbsFileName(path);
				kirtanList.add(kirtan);
				
			}
			readWorkBook.close();
		} catch(Exception ex){
			log.error(ex,ex);
		}		
		return kirtanList;
	}
	
	
}