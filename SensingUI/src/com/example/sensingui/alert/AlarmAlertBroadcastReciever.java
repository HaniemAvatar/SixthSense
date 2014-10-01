package com.example.sensingui.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.sensingui.Alarm;
import com.example.sensingui.service.AlarmServiceBroadcastReciever;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent LEDAlarmServiceIntent = new Intent(
				context,
				AlarmServiceBroadcastReciever.class);
		context.sendBroadcast(LEDAlarmServiceIntent, null);
		
		StaticWakeLock.lockOn(context);
		Bundle bundle = intent.getExtras();
		final Alarm alarm = (Alarm) bundle.getSerializable("alarm");

		Intent LEDAlarmAlertActivityIntent;

		LEDAlarmAlertActivityIntent = new Intent(context, AlarmAlertActivity.class);

		LEDAlarmAlertActivityIntent.putExtra("alarm", alarm);

		LEDAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(LEDAlarmAlertActivityIntent);
	}

}
