package com.example.sensingui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	Button Resister,Login;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login_main);
	    Resister=(Button)findViewById(R.id.resbt);
	    Resister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	    Login=(Button)findViewById(R.id.loginbt);
	    Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	    // TODO Auto-generated method stub
	}

}
