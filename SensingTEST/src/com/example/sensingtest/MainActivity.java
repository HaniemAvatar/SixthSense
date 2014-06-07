package com.example.sensingtest;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	SensorManager sm;
	Sensor light_sensor;
	TextView tex;
	EditText Setlight;
	CheckBox autocheck;
	int Ref=100, LightBulb;
	int CheckReader;
	ImageView vlight ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        light_sensor= sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        tex = (TextView)findViewById(R.id.llight);
        autocheck = (CheckBox)findViewById(R.id.AutoSense);
        vlight = (ImageView)findViewById(R.id.vlight);
        
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    

}
