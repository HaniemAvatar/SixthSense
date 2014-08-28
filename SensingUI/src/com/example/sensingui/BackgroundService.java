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

	String url = "http://14.63.214.50:2670/list";

	Context mContext;
	
	

	public BackgroundService() {
		super("BackgroundService");
		new InitialLoadData().execute(); //서버로의 데이터 값을 읽어서 itemdata[][]에 저장.			
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				new LoadData().execute();
				
			}
		}

		, 0, 3000);

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
				for (int i = 0; i < 5; i++) {
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
        	SensingConditionView.initialprogress[0]=(int)(Math.log10(Double.parseDouble(HomeActivity.itemdata[0][1])+1)*20);
        	SensingConditionView.initialprogress[1]=(int)(Math.log10(Double.parseDouble(HomeActivity.itemdata[1][1])+1)*20);
        	SensingConditionView.initialprogress[2]=(int)(Math.log10(Double.parseDouble(HomeActivity.itemdata[2][1])+1)*20);
        	SensingConditionView.initialprogress[3]=(int)(Math.log10(Double.parseDouble(HomeActivity.itemdata[3][1])+1)*20);
        	SensingConditionView.initialprogress[4]=(int)(Math.log10(Double.parseDouble(HomeActivity.itemdata[4][1])+1)*20);
        	SensingConditionView.Seek[0].setProgress(SensingConditionView.initialprogress[0]);
        	SensingConditionView.Seek[1].setProgress(SensingConditionView.initialprogress[1]);
        	SensingConditionView.Seek[2].setProgress(SensingConditionView.initialprogress[2]);
        	SensingConditionView.Seek[3].setProgress(SensingConditionView.initialprogress[3]);
        	SensingConditionView.Seek[4].setProgress(SensingConditionView.initialprogress[4]);
        	SensingConditionView.check[0].setChecked(Boolean.parseBoolean(HomeActivity.itemdata[0][7]));
        	SensingConditionView.check[1].setChecked(Boolean.parseBoolean(HomeActivity.itemdata[1][7]));
        	SensingConditionView.check[2].setChecked(Boolean.parseBoolean(HomeActivity.itemdata[2][7]));
        	SensingConditionView.check[3].setChecked(Boolean.parseBoolean(HomeActivity.itemdata[3][7]));
        	SensingConditionView.check[4].setChecked(Boolean.parseBoolean(HomeActivity.itemdata[4][7]));
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
				for (int i = 0; i < 5; i++) {
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
	
}