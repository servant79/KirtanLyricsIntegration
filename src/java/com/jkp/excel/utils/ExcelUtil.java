package com.jkp.excel.utils;

import jxl.CellType;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {

	public static void updateCell(WritableSheet copySheet, int r,int c,
			String value) throws WriteException, RowsExceededException {
			WritableCell copyCell = copySheet.getWritableCell(c, r);
			CellType cellType = copyCell.getType();
			if(CellType.LABEL == cellType) {
			  Label l = (Label) copyCell;
			  l.setString(value);
			}
			if(CellType.EMPTY == cellType ) {
				Label doneLabel = new Label(c,r,value);
				copySheet.addCell(doneLabel);
			}
		}
}
