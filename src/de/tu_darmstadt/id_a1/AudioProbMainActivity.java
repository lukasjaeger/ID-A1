package de.tu_darmstadt.id_a1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AudioProbMainActivity extends Activity {

	private Switch wifiSwitchButton;
	private Switch bluetoothSwitchButton;

	private Button startButton;

	private WifiManager wifiManager;
	private BluetoothAdapter bluetoothAdapter;
	
	private TextView textViewPeriode;
	private EditText editPeriode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_prob_main);

		editPeriode = (EditText) findViewById(R.id.editPeriode);
		textViewPeriode = (TextView) findViewById(R.id.textViewPeriode);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiSwitchButton = (Switch) findViewById(R.id.wifiswitch);
		bluetoothSwitchButton = (Switch) findViewById(R.id.bluetoothswitch);
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (wifiSwitchButton.isChecked()
						^ bluetoothSwitchButton.isChecked()) { // Wifi switch
																// button is
																// checked,
																// start
																// listening on
																// wifi
					Intent nextIntent = new Intent(AudioProbMainActivity.this,
							PlayingActivity.class);
					nextIntent.putExtra("Periode",Integer.parseInt(editPeriode.getEditableText().toString()));
					startActivity(nextIntent);
				} else
					// Nothing chosen. Print a message that tells the user to
					// decide
					Toast.makeText(AudioProbMainActivity.this,
							"Please activate Wifi xor Bluetooth!",
							Toast.LENGTH_SHORT).show();

			}
		});
		bluetoothSwitchButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (bluetoothSwitchButton.isChecked()) {
							// Enable the bluetoothAdapter
							if (!bluetoothAdapter.isEnabled()) {
								Intent enableBtIntent = new Intent(
										BluetoothAdapter.ACTION_REQUEST_ENABLE);
								startActivityForResult(enableBtIntent, 1138);// Rather
																				// random
																				// callback
																				// code

								// Making the device discoverable
								Intent discoverableIntent = new Intent(
										BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
								discoverableIntent
										.putExtra(
												BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
												0);
								startActivity(discoverableIntent);
							}
						}
						setVisibleBroadcastButton();

					}

				});

		wifiSwitchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String wifiIsOnText = getString(R.string.switchWiFiOn_text);
				String wifiIsOffText = getString(R.string.switchWiFiOff_text);
				setVisibleBroadcastButton();
				//if (wifiSwitchButton.isChecked()) {
					// Toast.makeText(getBaseContext(), wifiIsOnText,
					// Toast.LENGTH_SHORT).show();
					//Intent nextIntent = new Intent(getBaseContext(),
					//		AppModesActivity.class);
					//startActivity(nextIntent);
				/*}
				 else
				 */
					Toast.makeText(getBaseContext(), wifiIsOffText,
							Toast.LENGTH_SHORT).show();
				
			}

		});
		if (bluetoothAdapter == null) {
			this.bluetoothSwitchButton.setEnabled(false);
		}
		this.setVisibleBroadcastButton();

	}

	protected void onResume() {

		super.onResume();

		// registerReceiver(wifiStateChangeListener, new
		// IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
	}

	protected void onPause() {

		super.onPause();

		// unregisterReceiver(wifiStateChangeListener);
	}

	private void setVisibleBroadcastButton() {
		if (wifiSwitchButton.isChecked() ^ bluetoothSwitchButton.isChecked()){
			startButton.setVisibility(Button.VISIBLE);
			textViewPeriode.setVisibility(TextView.VISIBLE);
			editPeriode.setVisibility(EditText.VISIBLE);
		}
		else{
			startButton.setVisibility(Button.INVISIBLE);
			textViewPeriode.setVisibility(TextView.INVISIBLE);
			editPeriode.setVisibility(EditText.INVISIBLE);
		}
	}
}