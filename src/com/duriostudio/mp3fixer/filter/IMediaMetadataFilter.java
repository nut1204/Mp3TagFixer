package com.duriostudio.mp3fixer.filter;

import com.duriostudio.mp3fixer.model.MediaMetadataWithFile;

public interface IMediaMetadataFilter {
	boolean getFilter(MediaMetadataWithFile mediaMetadataWithFile);
}
