package com.duriostudio.mp3tagfixer;

import java.util.ArrayList;
import com.duriostudio.mp3tagfixer.R;
import com.duriostudio.mp3tagfixer.listViewAdapter.MediaMetadataArrayAdapter;
import com.duriostudio.mp3tagfixer.model.MediaMetadata;
import com.duriostudio.mp3tagfixer.model.MediaMetadataParcelable;
import com.duriostudio.mp3tagfixer.model.MediaMetadataWithFile;
import com.duriostudio.mp3tagfixer.service.EncodingService;
import com.duriostudio.mp3tagfixer.service.MediaMetadataService;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ConvertMediaActivity extends Activity implements
		OnItemSelectedListener {

	private boolean isConvert;
	private MediaMetadataArrayAdapter mediaAdapter;
	private ArrayList<MediaMetadataWithFile> mediaMetadataWithFileList;
	private String encoding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_convert_media);

		Intent itn = getIntent();
		ArrayList<Parcelable> mediaParcelableList = (ArrayList<Parcelable>) itn
				.getParcelableArrayListExtra("medialist");

		mediaMetadataWithFileList = new ArrayList<MediaMetadataWithFile>();
		for (Parcelable mediaParcelable : mediaParcelableList) {
			MediaMetadataParcelable mediaMetadataParcelable = (MediaMetadataParcelable) mediaParcelable;
			mediaMetadataWithFileList.add(mediaMetadataParcelable
					.getMediaMetadata());
		}

		addItemsOnListView();
		addItemsOnSpinner();
	}

	private void addItemsOnListView() {
		final ListView lvMedia = (ListView) findViewById(R.id.lvMedia);
		ArrayList<MediaMetadata> mediaMetadataList = getDataOnListView(EncodingService.TisEncoding);
		setMediaAdapter(mediaMetadataList);
		lvMedia.setAdapter(mediaAdapter);
	}

	private void addItemsOnSpinner() {
		Spinner spnEncodingFrom = (Spinner) findViewById(R.id.spinnerEncoding);

		ArrayList<String> lstEncoding = EncodingService.getAvalibleCharsets();

		ArrayAdapter<String> encodingAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lstEncoding);
		encodingAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		int spinnerPosition = encodingAdapter
				.getPosition(EncodingService.TisEncoding);
		spnEncodingFrom.setAdapter(encodingAdapter);
		spnEncodingFrom.setSelection(spinnerPosition);
		spnEncodingFrom.setOnItemSelectedListener(this);
	}

	private ArrayList<MediaMetadata> getDataOnListView(String encoding) {
		EncodingService encodingService = new EncodingService(encoding);
		ArrayList<MediaMetadata> mediaMetadataList = new ArrayList<MediaMetadata>();

		if (mediaMetadataWithFileList != null) {
			for (MediaMetadataWithFile mmdWithFile : mediaMetadataWithFileList) {
				if (EncodingService.defaultEncoding == encoding) {
					mediaMetadataList.add(mmdWithFile);
				} else {
					MediaMetadata mediaMetadata = encodingService
							.getMediaMetadataEncoding(mmdWithFile);
					mediaMetadataList.add(mediaMetadata);
				}
			}
		}
		return mediaMetadataList;
	}

	private void setMediaAdapter(ArrayList<MediaMetadata> mediaMetadataList) {
		if (mediaAdapter == null) {
			mediaAdapter = new MediaMetadataArrayAdapter(this,
					mediaMetadataList);
		} else {
			mediaAdapter.clear();
			mediaAdapter.addAll(mediaMetadataList);
			mediaAdapter.notifyDataSetChanged();
		}
	}

	public void convert(View view) {
		boolean canConvert = mediaMetadataWithFileList != null
				&& !mediaMetadataWithFileList.isEmpty();
		if (canConvert) {
			MediaMetadataService mediaMetadataService = new MediaMetadataService();
			for (MediaMetadataWithFile mediaMetadata : mediaMetadataWithFileList) {
				try {
					mediaMetadataService.saveMediaMetadata(mediaMetadata,
							encoding);
				} catch (Exception e) {
					Toast.makeText(
							getApplicationContext(),
							"Can't convert file : " + mediaMetadata.getPath()
									+ " because " + e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
			isConvert = true;
			Toast.makeText(
					getApplicationContext(),
					"Convert mp3 finish please clear data in google play music.",
					Toast.LENGTH_LONG).show();

			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));

			finish();

		} else {
			Toast.makeText(getApplicationContext(), "Please select music.",
					Toast.LENGTH_LONG).show();
		}

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		encoding = parent.getItemAtPosition(pos).toString();
		ArrayList<MediaMetadata> mediaMetadataList = getDataOnListView(encoding);
		setMediaAdapter(mediaMetadataList);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		if (isConvert) {
			data.putExtra("returnKeyConvert", "Swinging on a star. ");
		}
		setResult(RESULT_OK, data);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selected_media, menu);
		return true;
	}
}