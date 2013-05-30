package com.cman.mediatagconvertor.ListViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.cman.mediatagconvertor.R;
import com.cman.mediatagconvertor.model.MediaMetadataWithFile;

public class MediaMetadataWithFileArrayAdapter extends
		ArrayAdapter<MediaMetadataWithFile> {

	private final Activity context;
	private final List<MediaMetadataWithFile> list;

	public MediaMetadataWithFileArrayAdapter(Activity context,
			List<MediaMetadataWithFile> list) {
		super(context, R.layout.list_media_select, list);
		this.list = list;
		this.context = context;
	}

	static class ViewHolder {
		protected String path;
		protected TextView tbTitle;
		protected TextView tvArtist;
		protected CheckBox checkbox;
		protected ImageView imageView;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.list_media_select, null);

			holder = new ViewHolder();
			holder.tbTitle = (TextView) convertView.findViewById(R.id.title);
			holder.tvArtist = (TextView) convertView.findViewById(R.id.artist);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.check);
			holder.checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int getPosition = (Integer) buttonView.getTag();
							list.get(getPosition).setSelected(
									buttonView.isChecked());
						}
					});
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.albumView);
			convertView.setTag(holder);
			convertView.setOnLongClickListener(myLong);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MediaMetadataWithFile element = (MediaMetadataWithFile) list
				.get(position);
		holder.tbTitle.setText(element.getTitle());
		holder.tvArtist.setText(element.getArtist());
		holder.path = element.getPath();

		byte[] image = element.getImage();
		if (image != null) {
			holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
					image, 0, image.length));
		} else {
			holder.imageView.setImageResource(android.R.color.transparent);
		}

		holder.checkbox.setTag(position);
		holder.checkbox.setChecked(element.isSelected());

		return convertView;
	}

	public OnLongClickListener myLong = new OnLongClickListener() {
		public boolean onLongClick(View view) {
			// do something

			ViewHolder holder = (ViewHolder) view.getTag();
			File file = new File(holder.path);
			Uri uri = Uri.fromFile(file);

			// Toast.makeText(view.getContext(), "long click " + holder.path,
			// Toast.LENGTH_LONG)
			// .show();

			Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(uri, "audio/mp3");
			view.getContext().startActivity(intent);
			return true;
		}
	};

}
