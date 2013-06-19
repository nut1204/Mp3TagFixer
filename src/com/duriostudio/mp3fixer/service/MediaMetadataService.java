package com.duriostudio.mp3fixer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import android.media.MediaMetadataRetriever;
import com.duriostudio.mp3fixer.filter.IMediaMetadataFilter;
import com.duriostudio.mp3fixer.model.MediaMetadata;
import com.duriostudio.mp3fixer.model.MediaMetadataWithFile;

public class MediaMetadataService {

	public ArrayList<MediaMetadataWithFile> getListMediaMetadata(
			List<File> files, IMediaMetadataFilter mediaFilter) {
		ArrayList<MediaMetadataWithFile> mediaMetadataList = new ArrayList<MediaMetadataWithFile>();
		MediaMetadataWithFile mediaMetadataWithFile;
		for (File file : files) {
			mediaMetadataWithFile = new MediaMetadataWithFile(
					file.getAbsolutePath());
			extractMetadata(mediaMetadataWithFile);
			if (mediaFilter.getFilter(mediaMetadataWithFile)) {
				mediaMetadataList.add(mediaMetadataWithFile);
			}
		}
		return mediaMetadataList;
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

		tag.setArtist(mediaMetadata.getArtist());
		tag.setTitle(mediaMetadata.getTitle());
		tag.setAlbum(mediaMetadata.getAlbum());

		mp3File.setID3Tag(tag);

		// update the actual file to reflect the current state of our object
		mp3File.sync();
	}

	public void extractMetadata(MediaMetadataWithFile mediaMetadata) {
		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		metaRetriever.setDataSource(mediaMetadata.getFile().getAbsolutePath());

		byte[] image;
		String title, album, artist;
		title = metaRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		album = metaRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		artist = metaRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		image = metaRetriever.getEmbeddedPicture();

		mediaMetadata.setTitle(title);
		mediaMetadata.setAlbum(album);
		mediaMetadata.setArtist(artist);
		mediaMetadata.setImage(image);

		// validateMetadata(mediaMetadata);
		metaRetriever.release();
	}
}
