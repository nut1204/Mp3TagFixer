package com.duriostudio.mp3tagfixer.listViewAdapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.duriostudio.mp3tagfixer.R;
import com.duriostudio.mp3tagfixer.model.MediaMetadataWithFile;
import com.duriostudio.mp3tagfixer.service.FileService;

public class MediaMetadataWithFileArrayAdapter extends
		ArrayAdapter<MediaMetadataWithFile> {

	private Bitmap mLoadingBitmap;
	private final Activity context;
	private final ArrayList<MediaMetadataWithFile> list;

	public MediaMetadataWithFileArrayAdapter(Activity context,
			ArrayList<MediaMetadataWithFile> list) {
		super(context, R.layout.list_media_select, list);
		this.list = list;
		this.context = context;
	}

	public ArrayList<MediaMetadataWithFile> getMediadataWithFileList() {
		return list;
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

		String title, artist;
		title = element.getTitle();
		artist = element.getArtist();

		if (title != null && title.trim().length() > 0) {
			holder.tbTitle.setText(title);
		} else {
			holder.tbTitle.setText(FileService.getName(element.getPath()));
		}

		if (artist != null && artist.trim().length() > 0) {
			holder.tvArtist.setText(artist);
		} else {
			holder.tvArtist.setText("unknown artist");
		}

		holder.path = element.getPath();
		loadBitmap(element.getPath(), holder.imageView);
		holder.checkbox.setTag(position);
		holder.checkbox.setChecked(element.isSelected());

		return convertView;
	}

	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public static boolean cancelPotentialWork(Object data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.data;
			if (bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	public void loadBitmap(String image, ImageView imageView) {
		if (cancelPotentialWork(image, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					context.getResources(), mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(image);
		}
	}

	public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private String data = null;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];

			byte[] image;
			MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
			try {
				metaRetriever.setDataSource(data);
				image = metaRetriever.getEmbeddedPicture();
			} catch (Exception e) {
				image = null;
			} finally {
				metaRetriever.release();
			}

			return (image != null) ? BitmapFactory.decodeByteArray(image, 0,
					image.length) : null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				final ImageView imageView = imageViewReference.get();
				if (bitmap != null) {
					final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
					if (this == bitmapWorkerTask && imageView != null) {
						imageView.setImageBitmap(bitmap);
					}
				} else {
					imageView.setImageResource(R.drawable.earcandylogo);
				}
			}
		}
	}

	public OnLongClickListener myLong = new OnLongClickListener() {
		public boolean onLongClick(View view) {

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
