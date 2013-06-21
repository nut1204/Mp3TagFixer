package com.duriostudio.mp3tagfixer.model;

import java.io.File;

public class MediaMetadataWithFile extends MediaMetadata {
	private String path;

	private boolean selected;

	public MediaMetadataWithFile(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public File getFile() {
		return new File(path);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}