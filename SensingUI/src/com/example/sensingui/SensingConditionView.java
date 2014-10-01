package com.example.sensingui;

import java.util.Timer;
import java.util.TimerTask;

import com.example.sensingui.background.BackgroundService;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
	public static CheckBox check[] = new CheckBox[5];
	
	public static int SeekValue[] = { 0, 0, 0, 0, 0 };
	public static SeekBar Seek[] = new SeekBar[5];
	int CheckReader[] = new int[5];
	TextView per[] = new TextView[5];
	public static int initialprogress[] = new int[5];
    ProgressDialog mProgressDialog;
    
    public String itemdata[][] =new String[5][7];
    String columndata[] = {"room","lux","on_off","live_power","total_power","user_state","id"};

	ImageView led[] = new ImageView[5];
	Context mContext;
	Intent intent;
	
	Bundle bundle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.condition_main, container,
				false);
		mContext = rootView.getContext();
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
								BackgroundService.LightValue[i] = HomeActivity.SensorData;
								
							} else {
								BackgroundService.LightValue[i] = SeekValue[i]*10;

							}
							
							if (BackgroundService.LightValue[i] > 800) {
								led[i].setImageResource(R.drawable.lightbulb_4);
							} else if (BackgroundService.LightValue[i] > 600) {
								led[i].setImageResource(R.drawable.lightbulb_3);
							} else if (BackgroundService.LightValue[i] > 400) {
								led[i].setImageResource(R.drawable.lightbulb_2);
							} else if (BackgroundService.LightValue[i] > 200) {
								led[i].setImageResource(R.drawable.lightbulb_1);
							} else {
								led[i].setImageResource(R.drawable.lightbulb_0);
							}
							per[i].setText(BackgroundService.LightValue[i]/10 + "%");
						}
						
					}
				});
			} 

		}, 0, 500);
		return rootView;
	}

}