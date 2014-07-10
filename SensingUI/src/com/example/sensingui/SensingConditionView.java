package com.example.sensingui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class SensingConditionView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    SensorManager sm;
	Sensor light_sensor;
	TextView tex;
	EditText Setlight;
	CheckBox autocheck;
	int Ref=100, LightBulb;
	int CheckReader;
	ImageView vlight ;
	Context mContext;
	
    public SensingConditionView() { 	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext=rootView.getContext();
        sm = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        light_sensor= sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        tex=(TextView)rootView.findViewById(R.id.llight);
        autocheck = (CheckBox)rootView.findViewById(R.id.AutoSense);
        vlight = (ImageView)rootView.findViewById(R.id.vlight);
        autocheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					CheckReader=1;
			        }
			    else{
			        CheckReader=0;
			    }
			} 
		});
        sm.registerListener(listener, light_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return rootView;
    }
    SensorEventListener listener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if(CheckReader==1){
			LightBulb=Ref-(int)event.values[0];
			tex.setText(""+LightBulb);
	        if(LightBulb > 80 ){
	        	vlight.setImageResource(R.drawable.light_4);
	        	
	        }
	        else if(LightBulb > 40 ){
	        	vlight.setImageResource(R.drawable.light_3);
	        }
	        else if(LightBulb > 20 ){
	        	vlight.setImageResource(R.drawable.light_2);
	        }
	        else if(LightBulb > 10){
	        	vlight.setImageResource(R.drawable.light_1);
	        }
	        else if(LightBulb < 0){
	        	vlight.setImageResource(R.drawable.light_0);
	        }
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};
}