package com.duriostudio.mp3tagfixer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaMetadataParcelable implements Parcelable {

	private MediaMetadataWithFile mediaMetadata;

	public MediaMetadataParcelable(MediaMetadataWithFile mediaMetadata) {
		this.mediaMetadata = mediaMetadata;
	}

	public MediaMetadataWithFile getMediaMetadata() {
		return mediaMetadata;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(mediaMetadata.getPath());
		arg0.writeString(mediaMetadata.getTitle());
		arg0.writeString(mediaMetadata.getArtist());
		arg0.writeString(mediaMetadata.getAlbum());
	}

	private MediaMetadataParcelable(Parcel in) {
		String path = in.readString();
		String title = in.readString();
		String artist = in.readString();
		String album = in.readString();

		mediaMetadata = new MediaMetadataWithFile(path);
		mediaMetadata.setTitle(title);
		mediaMetadata.setArtist(artist);
		mediaMetadata.setAlbum(album);
	}

	public static final Parcelable.Creator<MediaMetadataParcelable> CREATOR = new Parcelable.Creator<MediaMetadataParcelable>() {
		public MediaMetadataParcelable createFromParcel(Parcel in) {
			return new MediaMetadataParcelable(in);
		}

		public MediaMetadataParcelable[] newArray(int size) {
			return new MediaMetadataParcelable[size];
		}
	};
}
