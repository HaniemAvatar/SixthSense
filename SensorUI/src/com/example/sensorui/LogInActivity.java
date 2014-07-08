package com.example.sensorui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android

public class LogInActivity extends Activity {
	
	Button LogInBT;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.login_layout);
	    LogInBT = (Button)findViewById(R.id.loginbut);
	    LogInBT.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LogInActivity.this, HomeActivity.class);
			}
		});
	
	    }

}
