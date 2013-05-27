package com.jkp.mp3tag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.jkp.excel.utils.BookCodeExtractor;
import com.jkp.mp3tag.FolderTraverser;
import com.jkp.mp3tag.command.Command;
import com.jkp.mp3tag.command.ReadKirtanCommand;


public class MasterLibrary {

	File folder;

	Logger log = Logger.getLogger(MasterLibrary.class);
	Map<String,List<Kirtan>> titleKirtanMap = new HashMap<String,List<Kirtan>>();

	public MasterLibrary(File satMiniFile) {
		folder = satMiniFile;
	}

	public boolean load() {
		FolderTraverser traverser = new FolderTraverser(folder);
		Command command =  new ReadKirtanCommand(titleKirtanMap);
		command.setAction(true);
		traverser.traverse(command);
		command.finalExecute();
		return true;
	}
	
	public boolean directLoad(boolean testMode) {
		String title=null,artist = null, bookCode=null;
		List<Kirtan> kirtanList;
		for(int i=1; i < Loc.files.length;i++){
			File readFile = new File(Loc.files[i]);
			readFile(readFile);			
		}
		return true;
	}

	
	public  void readFile(File readFile) {
		String title;
		String artist;
		BookCode bookCode;
		List<Kirtan> kirtanList;
		if(!readFile.getAbsolutePath().endsWith("mp3")){
			return;
		}
		try {
			AudioFile f = AudioFileIO.read(readFile);
			Tag tag = f.getTag();
			title = tag.getFirst(FieldKey.TITLE);
			artist = tag.getFirst(FieldKey.ARTIST);
			bookCode = BookCodeExtractor.regExtractBookCode(title);

			kirtanList = titleKirtanMap.get(bookCode);
			if(kirtanList ==null){
				kirtanList = new ArrayList<Kirtan>();
				titleKirtanMap.put(bookCode.getCode(),kirtanList);
			}
			Kirtan kirtan = new Kirtan();
			//kirtan.setAbsFileName(readFile.getAbsolutePath());
			kirtan.setModTitle(title);
			kirtan.setModArtist(artist);
			//kirtan.setJkpId(i);
			kirtan.setBookCode(bookCode);
			kirtanList.add(kirtan);
		} catch (Exception e) {
			log.error("Error found for file:"+readFile.getAbsolutePath(),e);
		}
	}

	public List<Kirtan> getKirtans(Set<String> satBookCodeSet) {
		List<Kirtan> kirtanList = new ArrayList<Kirtan>();
		List<Kirtan> currentKirtanList=null;
		for(String bookCode: satBookCodeSet) {
			currentKirtanList = titleKirtanMap.get(bookCode);
			if(currentKirtanList !=null ){
				kirtanList.addAll(currentKirtanList);
			}
		}
		return kirtanList;
	}

	public List<Kirtan> getAllKirtans() {
		
		List<Kirtan> kirtanList = new ArrayList<Kirtan>(8000);

		for(Map.Entry<String,List<Kirtan>> entry : titleKirtanMap.entrySet()) {
			kirtanList.addAll(entry.getValue());
		}
		// TODO Auto-generated method stub
		return kirtanList;
	}

}
