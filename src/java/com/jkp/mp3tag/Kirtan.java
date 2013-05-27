package com.jkp.mp3tag;

public class Kirtan {

	String absFileName;
	String title;
	String artist;
	String modTitle;
	String modArtist;
	String satsangi;
	int jkpId;
	BookCode bookCode = new BookCode();
	String status = Constants.SUCCESS;
	BookCode prevBookCode;
	
	public String getAbsFileName() {
		return absFileName;
	}
	public void setAbsFileName(String absFileName) {
		this.absFileName = absFileName;
	}
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getSatsangi() {
		return satsangi;
	}
	public void setSatsangi(String satsangi) {
		this.satsangi = satsangi;
	}
	public int getJkpId() {
		return jkpId;
	}
	public void setJkpId(int jkpId) {
		this.jkpId = jkpId;
	}
	
	public String toString(){
		return this.jkpId+";"+absFileName+";"+modTitle+";"+modArtist+";"+bookCode+";"+satsangi;
	}

	public String getModTitle() {
		return modTitle;
	}
	public void setModTitle(String modTitle) {
		this.modTitle = modTitle;
	}
	public String getModArtist() {
		return modArtist;
	}
	public void setModArtist(String modArtist) {
		this.modArtist = modArtist;
	}
	public BookCode getPrevBookCode() {
		return prevBookCode;
	}
	public void setPrevBookCode(BookCode prevBookCode) {
		this.prevBookCode = prevBookCode;
	}
	public BookCode getBookCode() {
		return bookCode;
	}
	public void setBookCode(BookCode bookCode) {
		this.bookCode = bookCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
