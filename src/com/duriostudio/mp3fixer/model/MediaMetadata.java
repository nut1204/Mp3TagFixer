package com.duriostudio.mp3fixer.model;

public class MediaMetadata {
	protected String title;
	protected String artist;
	protected String album;
	protected byte[] image;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbum() {
		return album;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist() {
		return artist;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}	
}