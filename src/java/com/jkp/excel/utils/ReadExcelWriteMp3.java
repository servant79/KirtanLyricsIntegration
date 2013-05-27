package com.jkp.excel.utils;

import java.io.File;


import javafx.concurrent.Task;

import org.apache.log4j.Logger;

import com.jkp.mp3tag.Mp3Bean;
import com.jkp.mp3tag.STATUS;
import com.jkp.mp3tag.command.WriteMp3Tag;

import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReadExcelWriteMp3 extends Task{

	private String inputFile;
	public Logger log = Logger.getLogger(ReadExcelWriteMp3.class);
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}


	public void process() throws Exception  {
		File inputWorkbook = new File(inputFile);
		Workbook readWorkBook;
		WritableWorkbook writeWorkBook=null;;
		WriteMp3Tag writeTag = new WriteMp3Tag();
		Mp3Bean bean = new Mp3Bean();
		try {
			readWorkBook = Workbook.getWorkbook(inputWorkbook);
			String outfile = inputFile.replace(".xls", "_out.xls");
			writeWorkBook = Workbook.createWorkbook(new File(outfile), readWorkBook);
			Sheet sheet = readWorkBook.getSheet(0);
			WritableSheet writeSheet =	writeWorkBook.getSheet(0);
			int rowCount = sheet.getRows();

			//rowCount = 3;
			UpdateStatus status = new UpdateStatus();
			for (int r = 1; r < rowCount; r++) {
				resetStatus(status);
				updateProgress(r,rowCount-1);
				String path = sheet.getCell(3, r).getContents();
				String bookcode = sheet.getCell(4, r).getContents();
				String title = sheet.getCell(5, r).getContents();
				String artist = sheet.getCell(6, r).getContents();
				String updateFlag = sheet.getCell(7,r).getContents();
				log.info(path+" "+bookcode+" "+title+" " +artist+" "+ updateFlag);
				bean.setFullPath(path);
				bean.setTitle(title);
				bean.setArtist(artist);
				if("yes".equals(updateFlag)){
					writeTag.write(bean,status);
					ExcelUtil.updateCell(writeSheet, r, 8,status.getStatus().getDesc());
					ExcelUtil.updateCell(writeSheet, r, 9,status.getMessage());
				}
			}
		} 
		finally{
			if(writeWorkBook !=null){
				writeWorkBook.write();
				writeWorkBook.close();
			}
		}
		
	}
	
	private final void resetStatus(final UpdateStatus status) {
		status.setStatus(STATUS.SUCCESS);
		status.setMessage(STATUS.SUCCESS.getDesc());
	}


	public void dummyProcess() throws Exception {
		for (int r = 1; r <= 5; r++) {
			updateProgress(r,5);
			if(true)
			throw new Exception("Incorrect worksheet columns");
			Thread.sleep(1*1000);
		}
		
	}



	public static void main(String[] args) throws Exception {
		ReadExcelWriteMp3 test = new ReadExcelWriteMp3();
		test.setInputFile(args[0]);
		test.process();
	}

	@Override
	protected Object call() throws Exception {
		process();
		return null;
	}

}