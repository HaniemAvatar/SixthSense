package com.example.sensingui.background;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.sensingui.SensingConditionView;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class BackgroundService extends IntentService {

	ProgressDialog mProgressDialog;

	String columndata[] = { "room", "lux", "on_off", "live_power",
			"total_power", "user_state", "id", "mode" };
	String writingdata[] = { "room", "lux", "onoff", "livepower", "totalpower",
			"userstate", "id", "mode" };
	String url = "http://14.63.214.50:2670/list";
	public static int LightValue[] = new int[5];
	public static String itemdata[][] = new String[5][8];
	Context mContext;

	public BackgroundService() {
		super("BackgroundService");
		new InitialLoadData().execute(); // 서버로의 데이터 값을 읽어서 itemdata[][]에 저장.

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
			//	new LoadData().execute();
				new Dataupload().execute();

			}
		}
 
		, 0, 1000);

	}
	
	public Boolean TextToBoolean(String s) {
		if(s.equals("0"))	return false;
		else return true;
	}
	
	public String BooleanToText(Boolean b) {
		if(b.equals(false))	return "0";
		else return "1";
	}

	// will be called asynchronously by Android
	@Override
	protected void onHandleIntent(Intent intent) {
	}

	private class InitialLoadData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Connect to the web site
				Document document = Jsoup.connect(url).get();
				// Using Elements to get the Meta data
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 8; j++) {
						Elements reader = document.select("meta[name="
								+ columndata[j] + (i + 1) + "]");
						// Locate the content attribute
						itemdata[i][j] = reader.attr("content");
					}
				}
				Log.d("Background Running", "Post Running");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			SensingConditionView.initialprogress[0] = Integer.parseInt(itemdata[0][1]);
			SensingConditionView.initialprogress[1] = Integer.parseInt(itemdata[1][1]);
			SensingConditionView.initialprogress[2] = Integer.parseInt(itemdata[2][1]);
			SensingConditionView.initialprogress[3] = Integer.parseInt(itemdata[3][1]);
			SensingConditionView.initialprogress[4] = Integer.parseInt(itemdata[4][1]);
			SensingConditionView.Seek[0]
					.setProgress(SensingConditionView.initialprogress[0]);
			SensingConditionView.Seek[1]
					.setProgress(SensingConditionView.initialprogress[1]);
			SensingConditionView.Seek[2]
					.setProgress(SensingConditionView.initialprogress[2]);
			SensingConditionView.Seek[3]
					.setProgress(SensingConditionView.initialprogress[3]);
			SensingConditionView.Seek[4]
					.setProgress(SensingConditionView.initialprogress[4]);
			SensingConditionView.check[0].setChecked(TextToBoolean(itemdata[0][7]));
			SensingConditionView.check[1].setChecked(TextToBoolean(itemdata[1][7]));
			SensingConditionView.check[2].setChecked(TextToBoolean(itemdata[2][7]));
			SensingConditionView.check[3].setChecked(TextToBoolean(itemdata[3][7]));
			SensingConditionView.check[4].setChecked(TextToBoolean(itemdata[4][7]));
			LightValue[0]=Integer.parseInt(itemdata[0][1])*10;
			LightValue[1]=Integer.parseInt(itemdata[1][1])*10;
			LightValue[2]=Integer.parseInt(itemdata[2][1])*10;
			LightValue[3]=Integer.parseInt(itemdata[3][1])*10;
			LightValue[4]=Integer.parseInt(itemdata[4][1])*10;
		}
	}

	private class LoadData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Connect to the web site
				Document document = Jsoup.connect(url).get();
				// Using Elements to get the Meta data
				for (int i = 0; i < 4; i++) {
					for (int j = 3; j < 5; j++) {
						Elements reader = document.select("meta[name="
								+ columndata[j] + (i + 1) + "]");
						// Locate the content attribute
						itemdata[i][j] = reader.attr("content");
					}
				}
				Log.d("Background Running", "Load Running");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

	private class Dataupload extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < 4; i++) {
				if (Integer.parseInt(itemdata[i][1]) != (LightValue[i]*10)) {
					try {
						String update = "http://14.63.214.50:2670/list"
								+ "/lux?lux="
								+ LightValue[i]*10 + "&id="
								+ (i + 1);
						Document document = Jsoup.connect(update).get();
						Elements reader = document.select("meta[name=lux" + (i + 1) + "]");
						// Locate the content attribute
						itemdata[i][1] = reader.attr("content");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.d("Upload data", itemdata[i][1]);
				}
				if(TextToBoolean(itemdata[i][7]) != SensingConditionView.check[i].isChecked()){
					try {
						String update = "http://14.63.214.50:2670/list"
								+ "/mode?mode="
								+ BooleanToText(SensingConditionView.check[i].isChecked()) + "&id="
								+ (i + 1);
						Document document = Jsoup.connect(update).get();
						Elements reader = document.select("meta[name=mode" + (i + 1) + "]");
						// Locate the content attribute
						itemdata[i][7] = reader.attr("content");
						Log.d("dataupload", i + "-" + itemdata[i][7]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

}