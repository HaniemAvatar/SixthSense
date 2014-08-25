package com.example.sensingui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

	AsyncTask<Void, Void, Void> mTask;
	String jsonString;

	String url = "http://14.63.214.50:2670/list";

	ImageView led[] = new ImageView[5];
	Context mContext;
	
	HttpClient httpClient = new DefaultHttpClient();
	HttpPost httpPost = new HttpPost("http://www.example.com");

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

		String html;
        html = DownloadHtml("http://14.63.214.50:2670/list");
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
    	nameValuePair.add(new BasicNameValuePair("username", "test_user"));
    	nameValuePair.add(new BasicNameValuePair("password", "123456789"));
		
    	try {
    	      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
    	 
    	} catch (UnsupportedEncodingException e) 
    	{
    	     e.printStackTrace();
    	}
    	
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

		
		check[0].setText(html);


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
						try {
						    HttpResponse response = httpClient.execute(httpPost);
						    // write response to log
						    Log.d("Http Post Response:", response.toString());
						} catch (ClientProtocolException e) {
						    // Log exception
						    e.printStackTrace();
						} catch (IOException e) {
						    // Log exception
						    e.printStackTrace();
						}
					}
				});
			}

		}, 0, 1000);
		return rootView;
	}

	public static String getJsonFromServer(String url) throws IOException {

		BufferedReader inputStream = null;

		URL jsonUrl = new URL(url);
		URLConnection dc = jsonUrl.openConnection();

		dc.setConnectTimeout(5000);
		dc.setReadTimeout(5000);

		inputStream = new BufferedReader(new InputStreamReader(
				dc.getInputStream()));

		// read the JSON results into a string
		String jsonResult = inputStream.readLine();
		return jsonResult;
	}
	String DownloadHtml(String addr){
	       StringBuilder html = new StringBuilder();
	       try{
	          URL url = new URL(addr);
	          HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	          if(conn != null){
	             conn.setConnectTimeout(10000);
	             conn.setUseCaches(false);
	             if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
	                BufferedReader br = new BufferedReader(
	                      new InputStreamReader(conn.getInputStream()));
	                for(;;){
	                      String line = br.readLine();
	                      if(line == null) break;
	                      html.append(line + '\n');
	                }
	                br.close();
	             }
	             conn.disconnect();
	          }
	       }
	       catch(Exception ex){;}
	       return html.toString();
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
}