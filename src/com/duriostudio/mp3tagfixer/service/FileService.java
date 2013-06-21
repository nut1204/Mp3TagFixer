package com.duriostudio.mp3tagfixer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class FileService {

	private String getCurrentDir() {
		String root = Environment.getExternalStorageDirectory().toString();
		return root;
		//return root + "/" + Environment.DIRECTORY_MUSIC;
	}

	public List<File> getListFiles() {
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

	public static String getName(String path) {
		String name = new File(path).getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
			name = name.substring(0, pos);
		}
		return name;
	}
}
