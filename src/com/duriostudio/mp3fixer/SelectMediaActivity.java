package com.duriostudio.mp3fixer;

import java.util.ArrayList;

import com.cman.mediatagconvertor.R;
import com.duriostudio.mp3fixer.listViewAdapter.MediaMetadataWithFileArrayAdapter;
import com.duriostudio.mp3fixer.model.MediaMetadataParcelable;
import com.duriostudio.mp3fixer.model.MediaMetadataWithFile;
import com.duriostudio.mp3fixer.service.FileService;
import com.duriostudio.mp3fixer.service.MediaMetadataService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class SelectMediaActivity extends Activity {

	private static final int REQUEST_CODE = 10;
	private MediaMetadataWithFileArrayAdapter mediaAdapter;
	private ArrayList<MediaMetadataWithFile> mediaMetadata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_media);

		final ListView listview = (ListView) findViewById(R.id.lvMedia);
		getDataOnListView();
		listview.setAdapter(mediaAdapter);
		listview.setLongClickable(true);
	}

	public void getDataOnListView() {
		FileService fileService = new FileService();
		mediaMetadata = new MediaMetadataService()
				.getListMediaMetadata(fileService.getListFiles());

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
				getDataOnListView();
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
