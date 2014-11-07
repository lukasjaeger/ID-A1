package de.tu_darmstadt.id_a1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class AudioProbMainActivity extends Activity {
	
	private Switch wifiSwitchButton;
	
	private Button startButton;
	
	private WifiManager wifiManager;
	
	private BroadcastReceiver wifiStateChangeListener = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {

			updateWifiSettings();
		}
	};
	
	private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			
			/**
			 * If the WiFi is enabled then next activity will start
			 */
			if (wifiSwitchButton.isChecked()) {
				Intent nextIntent = new Intent(AudioProbMainActivity.this, AppModesActivity.class);
				startActivity(nextIntent);
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_prob_main);
		
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiSwitchButton = (Switch) findViewById(R.id.wifiswitch);
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (wifiSwitchButton.isChecked()) //Wifi switch button is checked, start listening on wifi
				{
					Intent nextIntent = new Intent(AudioProbMainActivity.this, AppModesActivity.class);
					startActivity(nextIntent);
				}
				else //Nothing chosen. Print a message that tells the user to decide
				{
					Toast.makeText(AudioProbMainActivity.this, "Please activate Wifi!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		updateWifiSettings();
	}

	protected void onResume() {

		super.onResume();

		registerReceiver(wifiStateChangeListener, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
	}


	protected void onPause() {

		super.onPause();

		unregisterReceiver(wifiStateChangeListener);
	}
	
	private void updateWifiSettings() {

		wifiSwitchButton.setChecked(wifiManager.isWifiEnabled());

		String wifiIsOnText = getString(R.string.switchWiFiOn_text);
		String wifiIsOffText = getString(R.string.switchWiFiOff_text);

		if (wifiSwitchButton.isChecked())
			Toast.makeText(AudioProbMainActivity.this, wifiIsOnText, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(AudioProbMainActivity.this, wifiIsOffText, Toast.LENGTH_SHORT).show();
	}
	
	public void switchWiFi(View view) {

		wifiManager.setWifiEnabled(wifiSwitchButton.isChecked());

	}
}
