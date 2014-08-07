package com.example.sensingui;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.sensingui.assets.PieGraph;
import com.example.sensingui.assets.PieGraph.OnSliceClickedListener;
import com.example.sensingui.assets.PieSlice;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 * 
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */

public class SensingPiechartView extends Fragment {
    SensorManager sm;
	Sensor light_sensor;
	Context mContext;
	int lightBulb;
	int[] brightFreq = {0, 0, 0, 0, 0};
	PieGraph pg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.piechartvertical_main, container, false);
        mContext=rootView.getContext();
        sm = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        light_sensor= sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        final Resources resources = getResources();
        pg = (PieGraph) rootView.findViewById(R.id.piegraph);
        //Pie Chart에 Slice들을 추가
        PieSlice slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.red));
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.orange));
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.green_light));
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.blue));
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.purple));
        slice.setValue(0);
        pg.addSlice(slice);
        pg.setInnerCircleRatio(150);
        pg.setPadding(1);
        pg.setOnSliceClickedListener(new OnSliceClickedListener() {
            @Override
            public void onClick(int index) {
                Toast.makeText(getActivity(),
                        "Bright Over " + index + " clicked",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        sm.registerListener(listener, light_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {			
			@Override
			public void run() {
				((Activity) mContext).runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						PieSlice s;
						brightFreq[(int) Math.log10(lightBulb+1)]++;
				        for (int i=0;i<pg.getSlices().size();i++){
				            s = pg.getSlice(i);
				        	s.setGoalValue((float)brightFreq[i]);
				        }
				        pg.setDuration(300);//default if unspecified is 300 ms
				        pg.setInterpolator(new AccelerateDecelerateInterpolator());//default if unspecified is linear; constant speed
				        pg.setAnimationListener(getAnimationListener());
				        pg.animateToGoalValues();//animation will always overwrite. Pass true to call the onAnimationCancel Listener with onAnimationEnd
					}
				});
			}
		}, 0, 100);
        
        return rootView;
    }

    public Animator.AnimatorListener getAnimationListener(){
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            	
            }

            @Override
            public void onAnimationCancel(Animator animation) {//you might want to call slice.setvalue(slice.getGoalValue)
            	
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            	
            }
        };
    }
    
    SensorEventListener listener = new SensorEventListener() {		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			lightBulb=(int)event.values[0];
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
	
}