package com.duriostudio.mp3fixer;

import com.duriostudio.mp3fixer.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LogoActivity extends Activity {

	long splashTime = 2000;
	boolean active = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);

		// MediaMetadataTask task = new MediaMetadataTask();
		// task.execute("");

		Thread splashTimer = new Thread() {

			@Override
			public void run() {
				try { // Wait loop
					long waited = 0;
					while (active && waited < splashTime) {
						sleep(100); // Advance the timer only if we're running.
						if (active)
							waited += 100;
					} // Advance to the next screen.

				} catch (Exception e) {
					// Log.e("Splash", e.toString());
				} finally {
					finish();
					Intent itn = new Intent(getApplicationContext(),
							SelectMediaActivity.class);
					startActivity(itn);
				}
			}
		};
		splashTimer.start();
	}

	// private class MediaMetadataTask extends
	// AsyncTask<String, Void, ArrayList<MediaMetadataWithFile>> {
	//
	// protected ArrayList<MediaMetadataWithFile> doInBackground(
	// String... params) {
	// FileService fileService = new FileService();
	// ArrayList<MediaMetadataWithFile> mediaMetadata = new
	// MediaMetadataService()
	// .getListMediaMetadata(fileService.getListFiles(),
	// new DefaultFilter());
	// return mediaMetadata;
	// }
	//
	// protected void onPostExecute(
	// ArrayList<MediaMetadataWithFile> mediaMetadata) {
	//
	// ArrayList<MediaMetadataParcelable> mediaMetadataParcelableList = new
	// ArrayList<MediaMetadataParcelable>();
	// for (MediaMetadataWithFile media : mediaMetadata) {
	// mediaMetadataParcelableList.add(new MediaMetadataParcelable(
	// media));
	// }
	// finish();
	// Intent itn = new Intent(getApplicationContext(),
	// SelectMediaActivity.class);
	// itn.putExtra("medialist", mediaMetadataParcelableList);
	// itn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// startActivity(itn);
	// }
	// }
}
