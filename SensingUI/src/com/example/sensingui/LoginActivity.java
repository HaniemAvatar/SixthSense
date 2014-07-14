package com.example.sensingui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
				AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this);
				alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				alert.setMessage("E-mail과 Password를 등록하려면 Sixthsense팀으로 가입신청을 하시기 바랍니다.");
				alert.show();
			}
		});
	    
	    Login=(Button)findViewById(R.id.loginbt);
	    Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText uemail = (EditText)findViewById(R.id.useremail);
				EditText upassword = (EditText)findViewById(R.id.userpassword);
				String semail=uemail.getText().toString();
				String spassword=upassword.getText().toString();
				if(semail.equals("sixthsense")&&spassword.equals("12345")){
				Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				}
				else{
					AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this);
					alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					alert.setMessage("E-mail과 Password를 확인하세요.");
					alert.show();
				}
			}
		});

	    // TODO Auto-generated method stub
	}

}
