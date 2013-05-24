package com.cman.mediatagconvertor;

import java.io.File;
import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import com.cman.mediatagconvertor.model.ID3Tag;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ListView listview = (ListView) findViewById(R.id.listView1);
		final ArrayList<String> list = new ArrayList<String>();
		List<ID3Tag> id3TagList = new ArrayList<ID3Tag>();
		try {
			File curentDir = new File(getCurrentDir());
			List<File> files = getListFiles(curentDir);
			id3TagList = getListID3Tag(files);
			for (int i = 0; i < id3TagList.size(); ++i) {
				list.add(id3TagList.get(i).getTitle());
			}
			list.add(getCurrentDir());
			list.add("size : " + id3TagList.size());

		} catch (Exception e) {
			list.add("list error: " + e.getMessage());
		}

		ArrayAdapter<ID3Tag> adapter = new ID3TagArrayAdapter(this, id3TagList);
		
		listview.setAdapter(adapter);
	}

	private List<ID3Tag> getListID3Tag(List<File> files) {
		ArrayList<ID3Tag> id3TagList = new ArrayList<ID3Tag>();
		ID3Tag id3Tag;
		for (File file : files) {
			MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
			metaRetriever.setDataSource(file.getAbsolutePath());

			id3Tag = new ID3Tag();

			String title, encodedTitle;
			String album, encodedAlbum;
			String artist, encodeArtist; 
			title = metaRetriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			album = metaRetriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
			artist = metaRetriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			
			encodedTitle = getEncoding("title", title);
			encodedAlbum = getEncoding("album", album);
			encodeArtist = getEncoding("artist", artist);
			
			id3Tag.setTitle(encodedTitle);
			id3Tag.setAlbum(encodedAlbum);
			id3Tag.setArtist(encodeArtist);
			
			id3TagList.add(id3Tag);
			metaRetriever.release();
		}
		return id3TagList;
	}

	public String getEncoding(String metadata, String value) {
		if (value != null && !value.isEmpty()) {
			try {
				return new String(value.getBytes("ISO-8859-1"), "tis-620");
			} catch (UnsupportedEncodingException e) {
				return metadata + " can't encoded";
			}
		}
		return metadata + " is null";
	}

	private String getCurrentDir() {
		String root = Environment.getExternalStorageDirectory().toString();
		return root + "/" + Environment.DIRECTORY_MUSIC;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
