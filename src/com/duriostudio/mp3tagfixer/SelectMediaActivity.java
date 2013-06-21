package com.duriostudio.mp3tagfixer;

import java.util.ArrayList;

import com.duriostudio.mp3fixer.R;
import com.duriostudio.mp3tagfixer.filter.DefaultFilter;
import com.duriostudio.mp3tagfixer.listViewAdapter.MediaMetadataWithFileArrayAdapter;
import com.duriostudio.mp3tagfixer.model.MediaMetadataParcelable;
import com.duriostudio.mp3tagfixer.model.MediaMetadataWithFile;
import com.duriostudio.mp3tagfixer.service.FileService;
import com.duriostudio.mp3tagfixer.service.MediaMetadataService;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class SelectMediaActivity extends Activity {

	private static final int REQUEST_CODE = 10;
	private MediaMetadataWithFileArrayAdapter mediaAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_media);

		final ListView listview = (ListView) findViewById(R.id.lvMedia);
		setMediaAdapter(getDataOnListView());
		listview.setAdapter(mediaAdapter);
		listview.setLongClickable(true);
	}

	public ArrayList<MediaMetadataWithFile> getDataOnListViewInitial() {
		Intent itn = getIntent();
		ArrayList<Parcelable> mediaParcelableList = (ArrayList<Parcelable>) itn
				.getParcelableArrayListExtra("medialist");

		ArrayList<MediaMetadataWithFile> mediaMetadata = new ArrayList<MediaMetadataWithFile>();
		for (Parcelable mediaParcelable : mediaParcelableList) {
			MediaMetadataParcelable mediaMetadataParcelable = (MediaMetadataParcelable) mediaParcelable;
			mediaMetadata.add(mediaMetadataParcelable.getMediaMetadata());
		}
		return mediaMetadata;
	}

	public ArrayList<MediaMetadataWithFile> getDataOnListView() {
		FileService fileService = new FileService();
		ArrayList<MediaMetadataWithFile> mediaMetadata = new MediaMetadataService()
				.getListMediaMetadata(fileService.getListFiles(),
						new DefaultFilter());
		return mediaMetadata;
	}

	public void setMediaAdapter(ArrayList<MediaMetadataWithFile> mediaMetadata) {
		if (mediaAdapter == null) {
			mediaAdapter = new MediaMetadataWithFileArrayAdapter(this,
					mediaMetadata);
		} else {
			mediaAdapter.clear();
			mediaAdapter.addAll(mediaMetadata);
			mediaAdapter.notifyDataSetChanged();
		}
	}

	public void confirm(View view) {
		ArrayList<MediaMetadataParcelable> mediaMetadataParcelableList = new ArrayList<MediaMetadataParcelable>();
		ArrayList<MediaMetadataWithFile> mediaMetadata = mediaAdapter
				.getMediadataWithFileList();
		if (mediaMetadata != null) {
			for (MediaMetadataWithFile media : mediaMetadata) {
				if (media.isSelected()) {
					mediaMetadataParcelableList
							.add(new MediaMetadataParcelable(media));
				}
			}
		}
		Intent itn = new Intent(this, ConvertMediaActivity.class);
		itn.putExtra("medialist", mediaMetadataParcelableList);
		startActivityForResult(itn, REQUEST_CODE);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("returnKeyConvert")) {
				setMediaAdapter(getDataOnListView());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
