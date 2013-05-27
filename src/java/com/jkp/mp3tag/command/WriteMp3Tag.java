package com.jkp.mp3tag.command;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.jkp.excel.utils.UpdateStatus;
import com.jkp.mp3tag.AudioFileWrapper;
import com.jkp.mp3tag.Mp3Bean;
import com.jkp.mp3tag.STATUS;

public class WriteMp3Tag {

	public final void write(final Mp3Bean bean,final UpdateStatus status) {
		try{
			validateIfMp3File(bean, status);
			
			if(status.getStatus() != STATUS.FAIL){
				final File writeFile = new File(bean.getFullPath());
				validateIfExistAndWritable(bean,status,writeFile);
				
				if(status.getStatus() != STATUS.FAIL){
					final AudioFileWrapper wrapper =new AudioFileWrapper(writeFile);
					validateAudioFile(bean,status,wrapper);
					
					if(status.getStatus() != STATUS.FAIL){
						final AudioFile f = wrapper.getAudioFile();
						writeTag(bean,status,f);
					}
				}
			}
		}catch(Exception ex){
			status.setStatus(STATUS.FAIL);
			status.setMessage("Exception while processing for File"+ bean.getFullPath()+" Error : "+ex.getMessage());
		}
	}

	private final void writeTag(final Mp3Bean bean, final UpdateStatus status, final AudioFile f) {

		Tag tag = f.getTag();
		try{
		    tag.setField(FieldKey.TITLE, bean.getTitle().trim());
		}catch(FieldDataInvalidException fieldDataInvalidEx){
			status.setStatus(STATUS.FAIL);
			status.setMessage("Not Able to set title "+bean.getTitle()+" for File"+ f.getFile().getAbsolutePath()+" Error : "+fieldDataInvalidEx.getLocalizedMessage());
		}

		try{
		    if( bean.getArtist() !=null && !bean.getArtist().trim().isEmpty()) {
		    	tag.setField(FieldKey.ARTIST, bean.getArtist().trim());	
		    }
		}catch(FieldDataInvalidException fieldDataInvalidEx){
			status.setStatus(STATUS.FAIL);
			status.setMessage("Not Able to set artist "+bean.getArtist()+" for File"+ f.getFile().getAbsolutePath()+" Error : "+fieldDataInvalidEx.getLocalizedMessage());
		}

		if(status.getStatus() != STATUS.FAIL){
		    try {
				f.commit();
			} catch (CannotWriteException cannotWriteEx) {
				status.setStatus(STATUS.FAIL);
				status.setMessage("Cannot commit tag changes for File"+ f.getFile().getAbsolutePath()+" Error : "+cannotWriteEx.getLocalizedMessage());
			}
		}
	}

	private final void validateAudioFile(final Mp3Bean bean, final UpdateStatus status,
			final AudioFileWrapper wrapper) {
		File writeFile = wrapper.getWriteFile();
		AudioFile audioFile=null;
		if(status.getStatus() != STATUS.FAIL){
			try{
				audioFile = AudioFileIO.read(writeFile);
				wrapper.setAudioFile(audioFile);
			}catch(InvalidAudioFrameException invalidAudioFrameEx){
				status.setStatus(STATUS.FAIL);
				status.setMessage("InValid Audio Frame for File"+ writeFile.getAbsolutePath()+" Error : "+invalidAudioFrameEx.getLocalizedMessage());
			}catch(ReadOnlyFileException readOnlyFileEx){
				status.setStatus(STATUS.FAIL);
				status.setMessage("Writing not allowed for File"+ writeFile.getAbsolutePath()+" Error : "+readOnlyFileEx.getLocalizedMessage());
			}catch(TagException tagEx){
				status.setStatus(STATUS.FAIL);
				status.setMessage("Tags are nor proper for File"+ writeFile.getAbsolutePath()+" Error : "+tagEx.getLocalizedMessage());
			}catch(IOException ioEx){
				status.setStatus(STATUS.FAIL);
				status.setMessage("Input Output Error for File"+ writeFile.getAbsolutePath()+" Error : "+ioEx.getLocalizedMessage());
			}catch(CannotReadException cannotReadEx){
				status.setStatus(STATUS.FAIL);
				status.setMessage("Cannot Read File"+ writeFile.getAbsolutePath()+" Error : "+cannotReadEx.getLocalizedMessage());
			}
		}
	}

	private final void validateIfExistAndWritable(final Mp3Bean bean, final UpdateStatus status,
			final File writeFile) {
		if (!writeFile.exists()) {
			status.setStatus(STATUS.FAIL);
			status.setMessage("File " + bean.getFullPath() + " does not exist");
		}
		if (status.getStatus() != STATUS.FAIL && !writeFile.canWrite()) {
			status.setStatus(STATUS.FAIL);
			status.setMessage("your computer does not allow writing to the File "
					+ bean.getFullPath());
		}
	}

	private final void validateIfMp3File(final Mp3Bean bean, final UpdateStatus status) {
		if (!bean.getFullPath().endsWith("mp3")) {
			status.setStatus(STATUS.FAIL);
			status.setMessage("Not an MP3 File. Only Mp3 supported");
		}
	}

}
