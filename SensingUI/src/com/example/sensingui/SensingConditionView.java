package com.example.sensingui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SensingConditionView extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";
	SensorManager sm;
	Sensor light_sensor;
	CheckBox check[] = new CheckBox[5];
	int SensorData;
	int LightValue[] = new int[5];
	int SeekValue[] = { 0, 0, 0, 0, 0 };
	SeekBar Seek[] = new SeekBar[5];
	int CheckReader[] = new int[5];
	TextView per[] = new TextView[5];
	int initialprogress[] = new int[5];

    String url = "http://14.63.214.50:2670/list";
    ProgressDialog mProgressDialog;
    
    String itemdata[][] =new String[5][7];
    String columndata[] = {"room","lux","on_off","live_power","total_power","user_state","id"};

	ImageView led[] = new ImageView[5];
	Context mContext;
	
	HttpClient httpClient = new DefaultHttpClient();
	HttpPost httpPost = new HttpPost("");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.condition_main, container,
				false);
		mContext = rootView.getContext();
		sm = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		light_sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		check[0] = (CheckBox) rootView.findViewById(R.id.check01);
		check[1] = (CheckBox) rootView.findViewById(R.id.check02);
		check[2] = (CheckBox) rootView.findViewById(R.id.check03);
		check[3] = (CheckBox) rootView.findViewById(R.id.check04);
		check[4] = (CheckBox) rootView.findViewById(R.id.check05);

		Seek[0] = (SeekBar) rootView.findViewById(R.id.seek01);
		Seek[1] = (SeekBar) rootView.findViewById(R.id.seek02);
		Seek[2] = (SeekBar) rootView.findViewById(R.id.seek03);
		Seek[3] = (SeekBar) rootView.findViewById(R.id.seek04);
		Seek[4] = (SeekBar) rootView.findViewById(R.id.seek05);

		led[0] = (ImageView) rootView.findViewById(R.id.light01);
		led[1] = (ImageView) rootView.findViewById(R.id.light02);
		led[2] = (ImageView) rootView.findViewById(R.id.light03);
		led[3] = (ImageView) rootView.findViewById(R.id.light04);
		led[4] = (ImageView) rootView.findViewById(R.id.light05);

		per[0] = (TextView) rootView.findViewById(R.id.per01);
		per[1] = (TextView) rootView.findViewById(R.id.per02);
		per[2] = (TextView) rootView.findViewById(R.id.per03);
		per[3] = (TextView) rootView.findViewById(R.id.per04);
		per[4] = (TextView) rootView.findViewById(R.id.per05);
		
		new LoadData().execute();
		
		
		Seek[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SeekValue[0] = progress;
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});
		Seek[1].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SeekValue[1] = progress;
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});
		Seek[2].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SeekValue[2] = progress;
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});
		Seek[3].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SeekValue[3] = progress;
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});
		Seek[4].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SeekValue[4] = progress;
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});
		
		

		
		sm.registerListener(listener, light_sensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (int i = 0; i < 5; i++) {
							if (check[i].isChecked()) {
								LightValue[i] = SensorData;
							} else {
								LightValue[i] = SeekValue[i];
							}

							if (LightValue[i] > 80) {
								led[i].setImageResource(R.drawable.lightbulb_4);
							} else if (LightValue[i] > 60) {
								led[i].setImageResource(R.drawable.lightbulb_3);
							} else if (LightValue[i] > 40) {
								led[i].setImageResource(R.drawable.lightbulb_2);
							} else if (LightValue[i] > 20) {
								led[i].setImageResource(R.drawable.lightbulb_1);
							} else {
								led[i].setImageResource(R.drawable.lightbulb_0);
							}
							per[i].setText(LightValue[i] + "%");
						}
						check[0].setText(itemdata[1][1]);
					}
				});
			}

		}, 0, 1000);
		return rootView;
	}


	SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			SensorData = (int) (Math.log10((double) event.values[0] + 1) * 20);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
	private class LoadData extends AsyncTask<Void, Void, Void> { 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setTitle("Load Data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                for(int i=0;i<5;i++){
                	for(int j=0;j<7;j++){
                Elements reader=document.select("meta[name="+columndata[j]+(i+1)+"]");
                // Locate the content attribute
                itemdata[i][j] = reader.attr("content");
                	}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
        	initialprogress[0]=(int)(Math.log10(Double.parseDouble(itemdata[0][1])+1)*20);
    		initialprogress[1]=(int)(Math.log10(Double.parseDouble(itemdata[1][1])+1)*20);
    		initialprogress[2]=(int)(Math.log10(Double.parseDouble(itemdata[2][1])+1)*20);
    		initialprogress[3]=(int)(Math.log10(Double.parseDouble(itemdata[3][1])+1)*20);
    		initialprogress[4]=(int)(Math.log10(Double.parseDouble(itemdata[4][1])+1)*20);
    		Seek[0].setProgress(initialprogress[0]);
    		Seek[1].setProgress(initialprogress[1]);
    		Seek[2].setProgress(initialprogress[2]);
    		Seek[3].setProgress(initialprogress[3]);
    		Seek[4].setProgress(initialprogress[4]);
            mProgressDialog.dismiss();
        }
    }
}