package com.example.sensingui;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sensingui.assets.PieGraph;
import com.example.sensingui.assets.PieGraph.OnSliceClickedListener;
import com.example.sensingui.assets.PieSlice;
import com.example.sensingui.background.BackgroundService;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 * 
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */

public class SensingPiechartView extends Fragment {
	Context mContext;
	private int[] colorlistSlice = { R.color.holo_red, R.color.holo_yellow,
			R.color.holo_green, R.color.holo_blue, R.color.holo_violet,
			R.color.holo_pink };
	private int[] colorlistText = { R.color.holo_red_t, R.color.holo_yellow_t,
			R.color.holo_green_t, R.color.holo_blue_t, R.color.holo_violet_t,
			R.color.holo_pink_t };
	public static final int ledNum = 5;
	private TextView[] tper = new TextView[5];
	private TextView[] name = new TextView[5];
	private TextView[] ttime = new TextView[5];
	PieGraph pg;
	double data[] = new double[5];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.piechart_main,
				container, false);
		mContext = rootView.getContext();
		name[0] = (TextView) rootView.findViewById(R.id.name01);
		name[1] = (TextView) rootView.findViewById(R.id.name02);
		name[2] = (TextView) rootView.findViewById(R.id.name03);
		name[3] = (TextView) rootView.findViewById(R.id.name04);
		name[4] = (TextView) rootView.findViewById(R.id.name05);
		tper[0] = (TextView) rootView.findViewById(R.id.tper01);
		tper[1] = (TextView) rootView.findViewById(R.id.tper02);
		tper[2] = (TextView) rootView.findViewById(R.id.tper03);
		tper[3] = (TextView) rootView.findViewById(R.id.tper04);
		tper[4] = (TextView) rootView.findViewById(R.id.tper05);
		ttime[0] = (TextView) rootView.findViewById(R.id.ttime01);
		ttime[1] = (TextView) rootView.findViewById(R.id.ttime02);
		ttime[2] = (TextView) rootView.findViewById(R.id.ttime03);
		ttime[3] = (TextView) rootView.findViewById(R.id.ttime04);
		ttime[4] = (TextView) rootView.findViewById(R.id.ttime05);
		Resources resources = getResources();
		pg = (PieGraph) rootView.findViewById(R.id.piegraph);
		// Pie Chart에 Slice들을 추가
		for (int i = 0; i < ledNum; i++) {
			PieSlice slice = new PieSlice();
			slice.setColor(resources.getColor(colorlistSlice[i % 5]));
			name[i].setTextColor(resources.getColor(colorlistText[i % 5]));
			tper[i].setTextColor(resources.getColor(colorlistText[i % 5]));
			ttime[i].setTextColor(resources.getColor(colorlistText[i % 5]));
			slice.setValue(0);
			pg.addSlice(slice);
		}

		// Pie Chart 디자인
		pg.setInnerCircleRatio(150);
		pg.setPadding(1);
		pg.setOnSliceClickedListener(new OnSliceClickedListener() {
			@Override
			public void onClick(int index) {
				Toast.makeText(getActivity(),
						"Bright Over " + index + " clicked", Toast.LENGTH_SHORT)
						.show();
			}
		});
		// Pie Chart 업데이트
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						PieSlice s;
						
						float temp=0,total=0;
						for (int i = 0; i < pg.getSlices().size(); i++) {
							temp=Float.parseFloat(BackgroundService.itemdata[i][4]);
							s = pg.getSlice(i);
							s.setGoalValue(temp);
							total+=temp;
						}
						for (int i = 0; i < pg.getSlices().size(); i++) {
							temp=Float.parseFloat(BackgroundService.itemdata[i][4]);
							tper[i].setText((int)(temp/total*100+0.5) + "%");
							ttime[i].setText((int)(temp/120) + "d" +(int)(temp%120)/5 +"h");
						}
						pg.setInterpolator(new AccelerateDecelerateInterpolator());
						pg.setAnimationListener(getAnimationListener());
						pg.animateToGoalValues();

					}
				});

			}
		}, 0, 10000);

		return rootView;
	}

	public Animator.AnimatorListener getAnimationListener() {
		return new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {// you might want
																// to call
																// slice.setvalue(slice.getGoalValue)

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		};
	}


}