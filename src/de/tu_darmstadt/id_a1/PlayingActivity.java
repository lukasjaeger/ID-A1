package de.tu_darmstadt.id_a1;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PlayingActivity extends Activity {
	Intent callingIntent;
	int periode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playing);
		this.callingIntent = getIntent();
		this.periode = this.callingIntent.getIntExtra("Periode", 0);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(Integer.toString(periode));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playing, menu);
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
	
	@Override
	protected void onResume(){
		super.onResume();
		MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.beep);
		mp.start();
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  goBack();
		  }
		}, 5000);
	}
	
	private void goBack(){
		Intent nextIntent;
		//If periode is 0, the app goes back to welcome screen
		//Otherwise PeriodicalReplayActivity is called
		if (periode == 0) nextIntent = new Intent(PlayingActivity.this, AudioProbMainActivity.class);
		else{
			nextIntent = new Intent(PlayingActivity.this, PeriodicalReplayActivity.class);
			nextIntent.putExtra("Periode", this.periode);
		}
		startActivity(nextIntent);
	}
}
