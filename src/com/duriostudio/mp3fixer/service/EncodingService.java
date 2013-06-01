package com.duriostudio.mp3fixer.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedMap;

import com.duriostudio.mp3fixer.model.MediaMetadata;

public class EncodingService {

	public final static String TisEncoding = "TIS-620";
	public final static String IsoEncoding = "ISO-8859-1";
	public final static String defaultEncoding = "Default";

	private String encoding;

	public EncodingService(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding(String metadata, String value) {
		if (value != null && !value.isEmpty()) {
			try {
				return new String(value.getBytes(IsoEncoding), encoding);
			} catch (UnsupportedEncodingException e) {
				return metadata + " can't encoded";
			}
		}
		return "Unknown " + metadata;
	}

	public MediaMetadata getMediaMetadataWithEncoding(
			MediaMetadata mediaMetadata) {

		String title, artist;
		MediaMetadata media = new MediaMetadata();
		title = getEncoding("title", mediaMetadata.getTitle());
		artist = getEncoding("artist", mediaMetadata.getArtist());

		media.setTitle(title);
		media.setArtist(artist);
		return media;
	}

	public static ArrayList<String> getAvalibleCharsets() {
		ArrayList<String> lstEncoding = new ArrayList<String>();
		SortedMap<String, Charset> charSets = Charset.availableCharsets();
		Iterator<String> it = charSets.keySet().iterator();
		lstEncoding.add(EncodingService.defaultEncoding);
		while (it.hasNext()) {
			String csName = (String) it.next();
			lstEncoding.add(csName);
		}
		return lstEncoding;
	}
}
