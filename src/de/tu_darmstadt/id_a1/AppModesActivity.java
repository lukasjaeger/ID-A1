package de.tu_darmstadt.id_a1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
		startBroadcast.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent nextIntent = new Intent(AppModesActivity.this,
						PlayingActivity.class);
				startActivity(nextIntent);
			}

		});
		recordDummyButton = (Button) findViewById(R.id.recordDummyButton);
		recordDummyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(AppModesActivity.this, "Recording started",
						Toast.LENGTH_SHORT).show();
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

	private void recordFile() {
		// Preliminary settings ordered for easy changeability
		final int timeInSeconds = 5;							//Duration of recording
		final int audioSource = MediaRecorder.AudioSource.MIC;	//Audio source: Microphone
		final int sampleRate = 44100;							//Default samplerate that is supported on all Android devices
		final int channelConfig = AudioFormat.CHANNEL_IN_MONO;	//Mono...let's not make things too complicated
		final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;	//16 bit PCM-encoding
		final int byteRate = sampleRate * 2;					//One sample has 16 bits
		final int bufferSize = byteRate * timeInSeconds;		
		String absolutePath = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MUSIC).getAbsolutePath()
				+ "/";
		String fileName = "testfile.wav";
		AudioRecord ar = new AudioRecord(audioSource, sampleRate,
				channelConfig, audioFormat, bufferSize);
		byte[] buffer = new byte[bufferSize];

		// Starting time is taken to measure the record duration;
		long startingTime = System.currentTimeMillis();

		// Samples are recorded
		ar.startRecording();
		while (System.currentTimeMillis() - startingTime < timeInSeconds * 1000) {
			ar.read(buffer, 0, bufferSize);
		}
		ar.stop();
		Toast.makeText(AppModesActivity.this, "Recording stopped",
				Toast.LENGTH_SHORT).show();
		
		//Calculating some values for the wave-file
		int fileSize = bufferSize;
		int totalSize = fileSize + 44;

		// Delete the old recording and creating a new file
		File f = new File(absolutePath + fileName);
		if (f.exists())
			f.delete();
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(AppModesActivity.this, "Could not create file",
					Toast.LENGTH_SHORT).show();
		}
		try {
			DataOutputStream outStream = new DataOutputStream(
					new FileOutputStream(absolutePath + fileName));
			// Creating the RIFF-header
			byte[] header = new byte[44];

			header[0] = 'R'; // RIFF/WAVE header
			header[1] = 'I';
			header[2] = 'F';
			header[3] = 'F';
			header[4] = (byte) (totalSize - 8 & 0xff);
			header[5] = (byte) ((totalSize - 8 >> 8) & 0xff);
			header[6] = (byte) ((totalSize - 8 >> 16) & 0xff);
			header[7] = (byte) ((totalSize - 8 >> 24) & 0xff);
			header[8] = 'W';
			header[9] = 'A';
			header[10] = 'V';
			header[11] = 'E';
			header[12] = 'f'; // 'fmt ' chunk
			header[13] = 'm';
			header[14] = 't';
			header[15] = ' ';
			header[16] = 16; // 4 bytes: size of 'fmt ' chunk
			header[17] = 0;
			header[18] = 0;
			header[19] = 0;
			header[20] = 1; // format = 1
			header[21] = 0;
			header[22] = (byte) 1;
			header[23] = 0;
			header[24] = (byte) (sampleRate & 0xff);
			header[25] = (byte) ((sampleRate >> 8) & 0xff);
			header[26] = (byte) ((sampleRate >> 16) & 0xff);
			header[27] = (byte) ((sampleRate >> 24) & 0xff);
			header[28] = (byte) (byteRate & 0xff);
			header[29] = (byte) ((byteRate >> 8) & 0xff);
			header[30] = (byte) ((byteRate >> 16) & 0xff);
			header[31] = (byte) ((byteRate >> 24) & 0xff);
			header[32] = (byte) (2 * 16 / 8); // block align
			header[33] = 0;
			header[34] = 16; // bits per sample
			header[35] = 0;
			header[36] = 'd';
			header[37] = 'a';
			header[38] = 't';
			header[39] = 'a';
			header[40] = (byte) (fileSize & 0xff);
			header[41] = (byte) ((fileSize >> 8) & 0xff);
			header[42] = (byte) ((fileSize >> 16) & 0xff);
			header[43] = (byte) ((fileSize >> 24) & 0xff);

			outStream.write(header, 0, 44);

			// Buffer written to output file
			outStream.write(buffer);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(AppModesActivity.this, "FileNotFound",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(AppModesActivity.this, "IOException",
					Toast.LENGTH_SHORT).show();
		}
	}
}