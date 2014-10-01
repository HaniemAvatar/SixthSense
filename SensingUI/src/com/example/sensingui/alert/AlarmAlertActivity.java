package com.example.sensingui.alert;


import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.sensingui.Alarm;
import com.example.sensingui.Alarm.LEDPick;
import com.example.sensingui.HomeActivity;
import com.example.sensingui.R;
import com.example.sensingui.SensingConditionView;
import com.example.sensingui.background.BackgroundService;

public class AlarmAlertActivity extends Activity implements OnClickListener {

	private Alarm alarm;
	String columndata[] = { "room", "lux", "on_off", "live_power",
			"total_power", "user_state", "id", "mode" };
	String writingdata[] = { "room", "lux", "onoff", "livepower", "totalpower",
			"userstate", "id", "mode" };
	String url = "http://14.63.214.50:2670/list";
	private Vibrator vibrator;
	private boolean alarmActive;
	final static double exp5=Math.exp(5);
	public Boolean TextToBoolean(String s) {
		if(s.equals("0"))	return false;
		else return true;
	}
	
	public String BooleanToText(Boolean b) {
		if(b.equals(false))	return "0";
		else return "1";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.alarm_alert);
		TextView AlertMSG = (TextView) findViewById(R.id.alertmsg);
		Bundle bundle = this.getIntent().getExtras();
		alarm = (Alarm) bundle.getSerializable("alarm");
		Log.d("Alert TEST", alarm.getLEDPicksString());
		String onoffmsg;
		if(alarm.getLEDOnOff()){
			onoffmsg="Ä×½À´Ï´Ù.";
		}
		else{
			onoffmsg="²°½À´Ï´Ù.";
		}
		AlertMSG.setText("LED Scheduler°¡\n "+alarm.getLEDPicksString()+"¸¦\n"+onoffmsg);

		this.setTitle(alarm.getAlarmLabel());
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		PhoneStateListener phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				super.onCallStateChanged(state, incomingNumber);
			}
		};
		


		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);


	

		// Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();
		new AlarmUpload().execute();
		startAlarm();

	}

	@Override
	protected void onResume() {
		super.onResume();
		alarmActive = true;
	}

	private void startAlarm() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		StaticWakeLock.lockOff(this);
	}

	@Override
	protected void onDestroy() {
		try {
			if (vibrator != null)
				vibrator.cancel();
		} catch (Exception e) {

		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (!alarmActive)
			return;
		v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

	}

	public boolean isAnswerCorrect() {
		boolean correct = true;

		return correct;
	}
	private class AlarmUpload extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Connect to the web site
			Arrays.sort(alarm.getLEDPicks(), new Comparator<LEDPick>() {
				@Override
				public int compare(LEDPick lhs, LEDPick rhs) {
					
					return lhs.ordinal() - rhs.ordinal();
				}
			});
			for(LEDPick led : alarm.getLEDPicks()){
				switch(led){
				
				default:					
					int i=led.ordinal();
					// Connect to the web site
					if(HomeActivity.Background!=null){
				        	SensingConditionView.SeekValue[i]=alarm.getLEDBrightness();
				        	SensingConditionView.check[i].setChecked(false);
				        	Log.d("change data", "lightvalue changed to "+String.valueOf(BackgroundService.LightValue[i]));
					}
						try {
							String update = "http://14.63.214.50:2670/list"
									+ "/lux?lux="
									+alarm.getLEDBrightness()*10 
									+ "&id="
									+ (i + 1);
							Document document = Jsoup.connect(update).get();
							Elements reader = document.select("meta[name=lux" + (i + 1) + "]");
							// Locate the content attribute
							BackgroundService.itemdata[i][1] = reader.attr("content");
						} catch (IOException e) {
							e.printStackTrace();
						}
						Log.d("Upload in Alert", BackgroundService.itemdata[i][1]);
					try {
							String update = "http://14.63.214.50:2670/list"
									+ "/onoff?onoff="
									+ BooleanToText(alarm.getLEDOnOff()) + "&id="
									+ (i + 1);
							Document document = Jsoup.connect(update).get();
							Elements reader = document.select("meta[name=on_off" + (i + 1) + "]");
							// Locate the content attribute
							BackgroundService.itemdata[i][2] = reader.attr("content");
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
				}				
			
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
		}

}
