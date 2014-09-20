package com.example.sensingui;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
	final static double exp5=Math.exp(5);
	Context mContext;

	public BackgroundService() {
		super("BackgroundService");
		new InitialLoadData().execute(); // 서버로의 데이터 값을 읽어서 itemdata[][]에 저장.

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				new LoadData().execute();
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
						HomeActivity.itemdata[i][j] = reader.attr("content");
					}
				}
				Log.d("Background Running", "Background Running");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			SensingConditionView.initialprogress[0] = Integer.parseInt(HomeActivity.itemdata[0][1]);
			SensingConditionView.initialprogress[1] = Integer.parseInt(HomeActivity.itemdata[1][1]);
			SensingConditionView.initialprogress[2] = Integer.parseInt(HomeActivity.itemdata[2][1]);
			SensingConditionView.initialprogress[3] = Integer.parseInt(HomeActivity.itemdata[3][1]);
			SensingConditionView.initialprogress[4] = Integer.parseInt(HomeActivity.itemdata[4][1]);
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
			SensingConditionView.check[0].setChecked(TextToBoolean(HomeActivity.itemdata[0][7]));
			SensingConditionView.check[1].setChecked(TextToBoolean(HomeActivity.itemdata[1][7]));
			SensingConditionView.check[2].setChecked(TextToBoolean(HomeActivity.itemdata[2][7]));
			SensingConditionView.check[3].setChecked(TextToBoolean(HomeActivity.itemdata[3][7]));
			SensingConditionView.check[4].setChecked(TextToBoolean(HomeActivity.itemdata[4][7]));
			SensingConditionView.LightValue[0]=(int) (200*Math.log1p(Double.parseDouble(HomeActivity.itemdata[0][1])*exp5/1000));
			SensingConditionView.LightValue[1]=(int) (200*Math.log1p(Double.parseDouble(HomeActivity.itemdata[1][1])*exp5/1000));
			SensingConditionView.LightValue[2]=(int) (200*Math.log1p(Double.parseDouble(HomeActivity.itemdata[2][1])*exp5/1000));
			SensingConditionView.LightValue[3]=(int) (200*Math.log1p(Double.parseDouble(HomeActivity.itemdata[3][1])*exp5/1000));
			SensingConditionView.LightValue[4]=(int) (200*Math.log1p(Double.parseDouble(HomeActivity.itemdata[4][1])*exp5/1000));
			Log.d("Check View", HomeActivity.itemdata[2][7]);
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
						HomeActivity.itemdata[i][j] = reader.attr("content");
					}
				}
				Log.d("Background Running", "Background Running");
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
			// Connect to the web site
			for (int i = 0; i < 4; i++) {
				if (Integer.parseInt(HomeActivity.itemdata[i][1]) != (int)(Math.exp((double) (SensingConditionView.LightValue[i])/200)*1000/exp5-1000/exp5)) {
					try {
						String update = "http://14.63.214.50:2670/list"
								+ "/lux?lux="
								+ (int)(Math.exp((double) (SensingConditionView.LightValue[i])/200)*1000/exp5-1000/exp5) + "&id="
								+ (i + 1);
						Document document = Jsoup.connect(update).get();
						Elements reader = document.select("meta[name=lux" + (i + 1) + "]");
						// Locate the content attribute
						HomeActivity.itemdata[i][1] = reader.attr("content");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.d("Upload data", HomeActivity.itemdata[i][1]);
				}
				if(TextToBoolean(HomeActivity.itemdata[i][7]) != SensingConditionView.check[i].isChecked()){
					try {
						String update = "http://14.63.214.50:2670/list"
								+ "/mode?mode="
								+ BooleanToText(SensingConditionView.check[i].isChecked()) + "&id="
								+ (i + 1);
						Document document = Jsoup.connect(update).get();
						Elements reader = document.select("meta[name=mode" + (i + 1) + "]");
						// Locate the content attribute
						HomeActivity.itemdata[i][7] = reader.attr("content");
						Log.d("dataupload", i + "-" + HomeActivity.itemdata[i][7]);
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