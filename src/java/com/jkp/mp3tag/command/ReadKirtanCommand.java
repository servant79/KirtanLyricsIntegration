package com.jkp.mp3tag.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.jkp.excel.utils.BookCodeExtractor;
import com.jkp.mp3tag.BookCode;
import com.jkp.mp3tag.Constants;
import com.jkp.mp3tag.Kirtan;


public class ReadKirtanCommand extends AbstractCommand {

	static Logger log = Logger.getLogger(ReadKirtanCommand.class);
	Logger logmaster = Logger.getLogger("Master");
	Map<String,List<Kirtan>> titleKirtanMap;
	List<Kirtan> kirtanList;
	int count;
	String title=null;
	String artist = null;
	int jkpId = -1;
	BookCode bookCode=null;
	String absPath = null;
	
	public ReadKirtanCommand(Map<String,List<Kirtan>> titleKirtanMap) {
		super();
		this.titleKirtanMap = titleKirtanMap;
	}

	@Override
	public void execute(File readFile) {

		if(!readFile.getAbsolutePath().endsWith("mp3")){
			return;
		}
	    try {
	    	count++;
	    	AudioFile f = AudioFileIO.read(readFile);
	    	Tag tag = f.getTag();
	    	title = tag.getFirst(FieldKey.TITLE);
	    	artist = tag.getFirst(FieldKey.ARTIST);

/*	    	try{
	    	jkpId = Integer.parseInt(tag.getFirst(FieldKey.CUSTOM1));
	    	}catch(NumberFormatException fex){
	    		logmaster.info("NK|"+tag.getFirst(FieldKey.CUSTOM1)+"|"+title+"|"+artist+"|"+readFile.getAbsolutePath());
	    		log.error("Not able to parse ["+tag.getFirst(FieldKey.CUSTOM1)+"] into integer");
	    		return;
	    	}*/
	    	bookCode = BookCodeExtractor.regExtractBookCode(title);
	    	
	    	absPath = readFile.getAbsolutePath();

	    	kirtanList = titleKirtanMap.get(absPath);
	    	if(kirtanList ==null){
	    		kirtanList = new ArrayList<Kirtan>();
	    		titleKirtanMap.put(absPath,kirtanList);
	    	}
	    	Kirtan kirtan = new Kirtan();
	    	kirtan.setAbsFileName(readFile.getAbsolutePath());
	    	kirtan.setTitle(title);
	    	kirtan.setArtist(artist);
	    	kirtan.setJkpId(jkpId);
	    	kirtan.setBookCode(bookCode);
	    	kirtanList.add(kirtan);
		} catch (Exception e) {
			log.error("Error found for file:"+readFile.getAbsolutePath(),e);
		}
	}

	@Override
	public void finalExecute() {
		log.info("Size of titleKirtanMap = "+ titleKirtanMap.size());
		int totalMapContent=0;
		for(Map.Entry<String,List<Kirtan>> entry : titleKirtanMap.entrySet()){
//			log.info("key = "+ entry.getKey()+" value size = "+ entry.getValue().size());
			totalMapContent = totalMapContent + entry.getValue().size();
		}
		log.info("Total Map Content = "+totalMapContent);
	}

}
