package com.jkp.excel.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jkp.mp3tag.BookCode;
import com.jkp.mp3tag.Constants;
import com.jkp.mp3tag.Kirtan;

public class BookCodeExtractor {

	static Pattern p = Pattern.compile("(_(br)?(ym)?(yr)?(ys)?(rk)?(or)?(pr)?[123]{1,2}(_[a-z]{3})?_[0-9]{1,3}_[0-9]{1,3}(_lm1_[0-9]{1,3})?)$");
	static Pattern plm = Pattern.compile("(_lm1_[0-9]{1,3})$");
	static Pattern pla = Pattern.compile("(_(lec)?(rgg)?(ssg)?(sgg)?" +
			"(bs1)?(kdd)?(rtd)?(art)?(nam)?(sha)?(bks)?(qa1)?(gen)?(cha)?" +
			"(notessential)?(uk)?(ghan)?(nonjkp)?(del)?(ignore)?(duplicate)?(q\\&a)?(stu)?(int)?)$");

	public static BookCode regExtractBookCode(String title){
		BookCode bookCode = new BookCode();
		String trimmed = title.toLowerCase().trim();
		Matcher m = p.matcher(trimmed);
		if (m.find()) {
		    bookCode.setCode(m.group(1));
		    bookCode.setKirtan(true);
		    return bookCode;
		}
		Matcher mlm = plm.matcher(trimmed);
		if(mlm.find()) {
			bookCode.setCode(mlm.group(1));
			bookCode.setKirtan(true);
			return bookCode;
		}
		Matcher mla = pla.matcher(trimmed);
		if(mla.find()) {
			bookCode.setCode(mla.group(1));
			bookCode.setKirtan(false);
			return bookCode;
		}
		
		return bookCode;
	}
	
	public static void reprocess(List<Kirtan> kirtanList){
		for(Kirtan  kirtan: kirtanList){
			kirtan.setPrevBookCode(kirtan.getBookCode());
			kirtan.setBookCode(regExtractBookCode(kirtan.getTitle()));
		}
		
	}
	
}
