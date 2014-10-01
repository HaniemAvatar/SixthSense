package com.example.sensingui.preferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.sensingui.Alarm;
import com.example.sensingui.preferences.AlarmPreference.Type;


public class AlarmPreferenceListAdapter extends BaseAdapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6574085153908419476L;
	private Context context;
	private Alarm alarm;
	private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
	private final String[] repeatDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	private final String[] LEDs = {"LED01","LED02","LED03","LED04"};
	private final String[] LEDOnOff = {"Turn ON","Turn OFF"};
	
	public AlarmPreferenceListAdapter(Context context, Alarm alarm) {
		setContext(context);
	    setLEDAlarm(alarm);		
	}

	@Override
	public int getCount() {
		return preferences.size();
	}

	@Override
	public Object getItem(int position) {
		return preferences.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		switch (alarmPreference.getType()) {
		case BOOLEAN:
			if(null == convertView || convertView.getId() != android.R.layout.simple_list_item_checked)
			convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);

			CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
			checkedTextView.setText(alarmPreference.getTitle());
			checkedTextView.setChecked((Boolean) alarmPreference.getValue());
			break;
		case INTEGER:
		case STRING:
		case MULTIPLE_LIST:
		case TIME:
		default:
			if(null == convertView || convertView.getId() != android.R.layout.simple_list_item_2)
			convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
			
			TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
			text1.setTextSize(18);
			text1.setText(alarmPreference.getTitle());
			
			TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
			text2.setText(alarmPreference.getSummary());
			break;
		}

		return convertView;
	}

	public Alarm getLEDAlarm() {		
		for(AlarmPreference preference : preferences){
			switch(preference.getKey()){
				case ALARM_ACTIVE:
					alarm.setAlarmActive((Boolean) preference.getValue());
					break;
				case ALARM_LABEL:
					alarm.setAlarmLabel((String) preference.getValue());
					break;
				case ALARM_TIME:
					alarm.setAlarmTime((String) preference.getValue());
					break;
				case ALARM_LEDBRIGHTNESS:
					alarm.setLEDBrightness((int) preference.getValue());
					break;
				case ALARM_LEDONOFF:
					alarm.setLEDOnOff((Boolean) preference.getValue());
					break;
				case ALARM_REPEAT:
					alarm.setDays((Alarm.Day[]) preference.getValue());
					break;
				case ALARM_LEDPICK:
					alarm.setLEDPicks((Alarm.LEDPick[]) preference.getValue());
					break;
			}
		}
				
		return alarm;
	}

	public void setLEDAlarm(Alarm alarm) {
		this.alarm = alarm;
		preferences.clear();
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_ACTIVE,"Active", null, null, alarm.getAlarmActive(),Type.BOOLEAN));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_LABEL, "Label",alarm.getAlarmLabel(), null, alarm.getAlarmLabel(), Type.STRING));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME, "Set time",alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_LEDONOFF, "ON/OFF", null, null, alarm.getLEDOnOff(), Type.BOOLEAN ));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_LEDBRIGHTNESS, "Brightness Setting", null,null,alarm.getLEDBrightness(),Type.STRING));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_REPEAT, "Repeat",alarm.getRepeatDaysString(), repeatDays, alarm.getDays(),Type.MULTIPLE_LIST));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_LEDPICK, "LED Pick",alarm.getLEDPicksString(), LEDs, alarm.getLEDPicks(),Type.MULTIPLE_LIST));
		
	}

	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String[] getRepeatDays() {
		return repeatDays;
	}
	public String[] getLEDPicks() {
		return LEDs;
	}
	
	public String[] getLEDOnOff() {
		return LEDOnOff;
	}

}
