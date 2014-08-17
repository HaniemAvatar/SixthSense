package com.example.sensingui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class SensingGraphView extends Fragment {

	private Context mContext;
	private final Handler mHandler = new Handler();
	private Runnable mTimer;
	private GraphView graphView;
	private TextView[] led = new TextView[5];
	private TextView[] name = new TextView[5];
	private GraphViewSeries[] sensorSeries = new GraphViewSeries[5];
	private double[][] graphdata = {
			{ 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d },
			{ 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d },
			{ 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d },
			{ 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d },
			{ 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d } };
	private int[] colorlistLine = { R.color.holo_red, R.color.holo_yellow,
			R.color.holo_green, R.color.holo_blue, R.color.holo_violet,
			R.color.holo_pink };
	private int[] colorlistText = { R.color.holo_red_t, R.color.holo_yellow_t,
			R.color.holo_green_t, R.color.holo_blue_t, R.color.holo_violet_t,
			R.color.holo_pink_t };
	public static final int ledNum=5;
	SensorManager sm;
	Sensor light_sensor;
	int lightBulb;

	public SensingGraphView() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.graph_main, container, false);
		final Resources resources = getResources();
		mContext = rootView.getContext();

		sm = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		light_sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		name[0]=(TextView)rootView.findViewById(R.id.name01);
		name[1]=(TextView)rootView.findViewById(R.id.name02);
		name[2]=(TextView)rootView.findViewById(R.id.name03);
		name[3]=(TextView)rootView.findViewById(R.id.name04);
		name[4]=(TextView)rootView.findViewById(R.id.name05);
		led[0]=(TextView)rootView.findViewById(R.id.per01);
		led[1]=(TextView)rootView.findViewById(R.id.per02);
		led[2]=(TextView)rootView.findViewById(R.id.per03);
		led[3]=(TextView)rootView.findViewById(R.id.per04);
		led[4]=(TextView)rootView.findViewById(R.id.per05);

		for (int i = 0; i < ledNum; i++) {
			sensorSeries[i] = new GraphViewSeries(new GraphViewData[] {});
			sensorSeries[i].getStyle().color = resources.getColor(colorlistLine[i%5]);
			sensorSeries[i].getStyle().thickness = 5;
			name[i].setTextColor(resources.getColor(colorlistText[i%5]));
			led[i].setTextColor(resources.getColor(colorlistText[i%5]));
		}
		graphView = new LineGraphView(container.getContext(), "");
		((LineGraphView) graphView).setDrawBackground(true);
		graphView.setBackgroundColor(0X17729FCF);

		for (int i = 0; i < ledNum; i++) {
			graphView.addSeries(sensorSeries[i]);
		}

		graphView.setManualYAxisBounds(5, 0);
		graphView.getGraphViewStyle().setNumVerticalLabels(6);
		graphView.getGraphViewStyle().setNumHorizontalLabels(1);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(0);
		graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
		graphView.setViewPort(1, 8);
		graphView.setScalable(true);
		graphView.getGraphViewStyle().setGridColor(Color.BLACK);
		graphView.setScrollable(true);

		LinearLayout layout = (LinearLayout) rootView
				.findViewById(R.id.opgraph);
		layout.addView(graphView);

		return rootView;
	}

	@Override
	public void onPause() {
		mHandler.removeCallbacks(mTimer);
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mTimer = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < ledNum; i++) {
					sensorSeries[i].resetData(new GraphViewData[] {
							new GraphViewData(1, graphdata[i][0]),
							new GraphViewData(2, graphdata[i][1]),
							new GraphViewData(3, graphdata[i][2]),
							new GraphViewData(4, graphdata[i][3]),
							new GraphViewData(5, graphdata[i][4]),
							new GraphViewData(6, graphdata[i][5]),
							new GraphViewData(7, graphdata[i][6]),
							new GraphViewData(8, graphdata[i][7]),
							new GraphViewData(9, graphdata[i][8]),
							new GraphViewData(10, graphdata[i][9]) });					
				}
				mHandler.postDelayed(this, 600);
				for (int i = 0; i < ledNum; i++) {
					for (int j = 0; j < 9; j++) {
						graphdata[i][j] = graphdata[i][j + 1];
					}
					sm.registerListener(listener, light_sensor, SensorManager.SENSOR_DELAY_NORMAL);
					double data = Math.log10(lightBulb + 1)+Math.random();
					graphdata[i][9] = data;
					led[i].setText((int)(data*20)+"%");
				}
				
			}
			
		};
		mHandler.postDelayed(mTimer, 600);

	}

	SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			lightBulb = (int) event.values[0];
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
}
