package de.tu_darmstadt.id_a1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PeriodicalReplayActivity extends Activity {
	int periode;
	Button startButton;
	WaitingTask wt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periodical_replay);
		Intent i = getIntent();
		this.periode = i.getIntExtra("Periode", 0);
		this.startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent backIntent = new Intent (PeriodicalReplayActivity.this, AudioProbMainActivity.class);
				wt.cancel(true);
				startActivity(backIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.periodical_replay, menu);
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
		this.waitForThePeriod();
	}
	
	private void waitForThePeriod(){
		//Thread to stop the time
		/*
		WaitThread t = new WaitThread(periode);
		t.start();
		try {
			t.join();
			this.triggerReplay();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		this.wt = new WaitingTask(this.periode);
		wt.execute(this.periode);
		
	}
	
	private void triggerReplay(){
		Intent replayIntent = new Intent(PeriodicalReplayActivity.this,PlayingActivity.class);
		replayIntent.putExtra("Periode", this.periode);
		startActivity(replayIntent);
	}
	
	protected class WaitingTask extends AsyncTask{
		int periode = 0;
		
		public WaitingTask(int period){
			this.periode = period;
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(this.periode);
				Intent replayIntent = new Intent(PeriodicalReplayActivity.this,PlayingActivity.class);
				replayIntent.putExtra("Periode", this.periode);
				startActivity(replayIntent);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
