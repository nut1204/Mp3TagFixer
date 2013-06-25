package com.duriostudio.mp3tagfixer.listViewAdapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.duriostudio.mp3tagfixer.R;
import com.duriostudio.mp3tagfixer.model.MediaMetadata;

public class MediaMetadataArrayAdapter extends ArrayAdapter<MediaMetadata> {

	private final List<MediaMetadata> list;
	private final Activity context;

	public MediaMetadataArrayAdapter(Activity context, List<MediaMetadata> list) {
		super(context, R.layout.list_media_convert, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView tbTitle;
		protected TextView tvArtist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.list_media_convert, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.tbTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvArtist = (TextView) view.findViewById(R.id.artist);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.tbTitle.setText(list.get(position).getTitle());
		holder.tvArtist.setText(list.get(position).getArtist());
		return view;
	}
}