package com.duriostudio.mp3fixer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class FileService {
	
	private String getCurrentDir() {
		String root = Environment.getExternalStorageDirectory().toString();
		return root + "/" + Environment.DIRECTORY_MUSIC;
	}
	
	public List<File> getListFiles(){
		File parentDir = new File(getCurrentDir());
		return getListFiles(parentDir);
	}

	private List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				inFiles.addAll(getListFiles(file));
			} else {
				if (file.getName().endsWith(".mp3")) {
					inFiles.add(file);
				}
			}
		}
		return inFiles;
	}	
}
