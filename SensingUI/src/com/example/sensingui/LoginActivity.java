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
	
	Button Resister,Login; // 버튼에 대한 상수 정의

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login_main); // login-main이라는 이름을 가진 xml파일의 Layout을 화면에 출력
	     //ID등록 버튼에 대한 기능 정의 
	    Resister=(Button)findViewById(R.id.resbt); //xml의 버튼에 기능을 넣기 위해 Java의 Resister라는 변수에 xml의 resbt라는 id를 가진 버튼을 연결
	    Resister.setOnClickListener(new View.OnClickListener() { //버튼이 클릭되기를 기다리는 함수
			
			@Override // 재정의
			public void onClick(View v) { //클릭퇴었을 때
				AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this); //alert라는 변수를 AlertDialog.Builder로 정의해 LoginActivity Class상에서 Dialog를 띄우는 형태로 생성
				alert.setPositiveButton("확인", new DialogInterface.OnClickListener() { //긍정버튼란에 '확인'을 생성, 확인을 클릭할 경우를 기다림
					@Override
					public void onClick(DialogInterface dialog, int which) { //확인이 클릭될 경우
						// TODO Auto-generated method stub
						dialog.dismiss(); // Alert를 무시, 아무 역할을 하지 않고 Alert을 닫음
					}
				});
				alert.setMessage("E-mail과 Password를 등록하려면 Sixthsense팀으로 가입신청을 하시기 바랍니다."); //Alert에 들어갈 내용을 작성
				alert.show(); //Alert창을 보여줌
			}
		});
	     //로그인 버튼에 대한 기능 정의
	    Login=(Button)findViewById(R.id.loginbt);
	    Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText uemail = (EditText)findViewById(R.id.useremail); //useremail이라는 id를 가진 xml의 edittext를 uemail이라는 변수에 부여
				EditText upassword = (EditText)findViewById(R.id.userpassword); //upassword에 부여
				
				String semail=uemail.getText().toString(); //uemail을 통해 받아온 text의 형식은 edittext이기 때문에 이를 String으로 변환해야만 함. 변환한 String값을 semail에 저장
				String spassword=upassword.getText().toString(); //마찬가지로 spassword에 저장
				
				if(semail.equals("")&&spassword.equals("")){ //semail의 값이 a와 같고,spassword의 값이 b와 같다면 다음을 수행 
					Intent intent=new Intent(LoginActivity.this, HomeActivity.class); //Login Activity에서 HomeActivityfh 전환하기 위한 Intent생성
					startActivity(intent);//Intent를 수행함으로써 HomeActivity를 실행
					finish(); //현재 LoginActivity를 닫음으로써 HomeActivity에서 뒤로버튼을 눌렀을 때 LoginActivity로 돌아오지 않고 바로 종료되도록 설정 
				}
				else{ //ID나 Password가 주어진값과 다를 경우 Alert를 띄우는 역할을 한다.
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
