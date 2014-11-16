package de.tu_darmstadt.id_a1;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AppModesActivity extends Activity {
	Button startBroadcast;
	Button recordDummyButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_modes);
		startBroadcast = (Button) findViewById(R.id.startBroadcasting);
		startBroadcast.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent nextIntent = new Intent(AppModesActivity.this, PlayingActivity.class);
				startActivity(nextIntent);
			}
			
		});
		recordDummyButton = (Button) findViewById(R.id.recordDummyButton);
		recordDummyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(AppModesActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
				recordFile();				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_modes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void recordFile(){
		String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath()+"/";
		String fileName = "testfile.3gp";
		final MediaRecorder mr = new MediaRecorder();
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);
		mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mr.setOutputFile(absolutePath+fileName);
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mr.prepare();
			mr.start();
			Toast.makeText(AppModesActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
			Thread.sleep(7000);
			mr.stop();
			Toast.makeText(AppModesActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mr.release();
	}
}
