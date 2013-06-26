package com.duriostudio.mp3tagfixer.service;

import java.io.File;
import java.util.ArrayList;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.duriostudio.mp3tagfixer.model.MediaMetadata;
import com.duriostudio.mp3tagfixer.model.MediaMetadataWithFile;

public class MediaMetadataService {

	public ArrayList<MediaMetadataWithFile> getPlayList(Context context) {

		/*
		 * use content provider to get beginning of database query that queries
		 * for all audio by display name, path and mimtype which i dont use but
		 * got it incase you want to scan for mp3 files only you can compare
		 * with RFC mimetype for mp3's
		 */
		final Cursor mCursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.DATA }, null, null,
				"LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

		String audioPath = "";
		String title = "";
		String artist = "";
		String album = "";

		/*
		 * run through all the columns we got back and save the data we need
		 * into the arraylist for our listview
		 */
		ArrayList<MediaMetadataWithFile> songsList = new ArrayList<MediaMetadataWithFile>();
		if (mCursor.moveToFirst()) {
			do {

				title = mCursor.getString(mCursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				artist = mCursor.getString(mCursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				album = mCursor.getString(mCursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				audioPath = mCursor.getString(mCursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

				MediaMetadataWithFile mediaMetadata = new MediaMetadataWithFile(
						audioPath);
				mediaMetadata.setTitle(title);
				mediaMetadata.setAlbum(album);
				mediaMetadata.setArtist(artist);

				songsList.add(mediaMetadata);

			} while (mCursor.moveToNext());
		}

		mCursor.close(); // cursor has been consumed so close it
		return songsList;
	}

	public void saveMediaMetadata(MediaMetadataWithFile mediaMetadataWithFile,
			String encoding) throws Exception {

		File file = mediaMetadataWithFile.getFile();
		EncodingService encodingService = new EncodingService(encoding);
		MediaMetadata mediaMetadata = encodingService
				.getMediaMetadataEncoding(mediaMetadataWithFile);

		MP3File mp3File = new MP3File(file);
		ID3V2Tag tag = mp3File.getID3V2Tag();
		TextEncoding.setDefaultTextEncoding(TextEncoding.UNICODE);

		String title, artist, album;
		title = mediaMetadata.getTitle();
		artist = mediaMetadata.getArtist();
		album = mediaMetadata.getAlbum();

		if (title != null && !title.isEmpty()) {
			tag.setTitle(title);
		}
		if (artist != null && !artist.isEmpty()) {
			tag.setArtist(artist);
		}
		if (album != null && !album.isEmpty()) {
			tag.setAlbum(album);
		}

		mp3File.setID3Tag(tag);
		// update the actual file to reflect the current state of our object
		mp3File.sync();
	}
}
