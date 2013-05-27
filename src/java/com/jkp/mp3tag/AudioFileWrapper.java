package com.jkp.mp3tag;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;

public class AudioFileWrapper {

	private AudioFile audioFile;

	private File writeFile;
	
	public AudioFileWrapper(File writeFile) {
		this.writeFile =writeFile;
	}
	public AudioFile getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(AudioFile audioFile) {
		this.audioFile = audioFile;
	}
	public File getWriteFile() {
		return writeFile;
	}

	
	
}
