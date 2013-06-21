package com.duriostudio.mp3tagfixer.filter;

import com.duriostudio.mp3tagfixer.model.MediaMetadataWithFile;

public interface IMediaMetadataFilter {
	boolean getFilter(MediaMetadataWithFile mediaMetadataWithFile);
}
