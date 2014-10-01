package com.example.sensingui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sensingui.alert.AlarmAlertBroadcastReciever;

public class Alarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Day{
		SUNDAY,
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY;

		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "Sunday";
				case 1:
					return "Monday";
				case 2:
					return "Tuesday";
				case 3:
					return "Wednesday";
				case 4:
					return "Thursday";
				case 5:
					return "Friay";
				case 6:
					return "Saturday";
			}
			return super.toString();
		}		
	}
	public enum LEDPick{
		LED01,
		LED02,
		LED03,
		LED04;

		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "LED01";
				case 1:
					return "LED02";
				case 2:
					return "LED03";
				case 3:
					return "LED04";
			}
			return super.toString();
		}
		
	}
	private int id;
	private Boolean alarmActive = true;
	private Calendar alarmTime = Calendar.getInstance();
	private Day[] days = {Day.MONDAY,Day.TUESDAY,Day.WEDNESDAY,Day.THURSDAY,Day.FRIDAY,Day.SATURDAY,Day.SUNDAY};
	private LEDPick[] ledpicks = {LEDPick.LED01,LEDPick.LED02,LEDPick.LED03,LEDPick.LED04};
	private Boolean LEDOnOff = true;
	private int LEDBrightness = 50;
	private String alarmLabel = "LED Alarm";
	
	
	public Alarm() {
	}

	/**
	 * @return the alarmActive
	 */
	public Boolean getAlarmActive() {
		return alarmActive;
	}
	public void setAlarmActive(Boolean alarmActive) {
		this.alarmActive = alarmActive;
	}

	/**
	 * @return the alarmTime
	 */
	public Calendar getAlarmTime() {
		if (alarmTime.before(Calendar.getInstance()))
			alarmTime.add(Calendar.DAY_OF_MONTH, 1);
		while(!Arrays.asList(getDays()).contains(Day.values()[alarmTime.get(Calendar.DAY_OF_WEEK)-1])){
			alarmTime.add(Calendar.DAY_OF_MONTH, 1);			
		}
		return alarmTime;
	}

	/**
	 * @return the alarmTime
	 */
	public int getLEDBrightness() {
		return LEDBrightness;
	}
	public void setLEDBrightness(int Brightness) {
		this.LEDBrightness = Brightness;
	}
	
	public String getAlarmTimeString() {

		String time = "";
		if (alarmTime.get(Calendar.HOUR_OF_DAY) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.HOUR_OF_DAY));
		time += ":";

		if (alarmTime.get(Calendar.MINUTE) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.MINUTE));

		return time;
	}

	/**
	 * @param alarmTime
	 *            the alarmTime to set
	 */
	public void setAlarmTime(Calendar alarmTime) {
		this.alarmTime = alarmTime;
	}

	/**
	 * @param alarmTime
	 *            the alarmTime to set
	 */
	public void setAlarmTime(String alarmTime) {

		String[] timePieces = alarmTime.split(":");

		Calendar newAlarmTime = Calendar.getInstance();
		newAlarmTime.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(timePieces[0]));
		newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timePieces[1]));
		newAlarmTime.set(Calendar.SECOND, 0);
		setAlarmTime(newAlarmTime);		
	}

	/**
	 * @return the repeatDays
	 */


	public Day[] getDays() {
		return days;
	}
	public LEDPick[] getLEDPicks() {
		return ledpicks;
	}

	/**
	 * @param set
	 *            the repeatDays to set
	 */
	public void setDays(Day[] days) {
		this.days = days;
	}
	public void setLEDPicks(LEDPick[] ledpicks) {
		this.ledpicks = ledpicks;
	}

	public void addDay(Day day){
		boolean contains = false;
		for(Day d : getDays())
			if(d.equals(day))
				contains = true;
		if(!contains){
			List<Day> result = new LinkedList<Day>();
			for(Day d : getDays())
				result.add(d);
			result.add(day);
			setDays(result.toArray(new Day[result.size()]));
		}
	}
	public void addLEDPick(LEDPick ledpick){
		boolean contains = false;
		for(LEDPick led : getLEDPicks())
			if(led.equals(ledpick))
				contains = true;
		if(!contains){
			List<LEDPick> result = new LinkedList<LEDPick>();
			for(LEDPick led : getLEDPicks())
				result.add(led);
			result.add(ledpick);
			setLEDPicks(result.toArray(new LEDPick[result.size()]));
		}
	}
	
	public void removeDay(Day day) {
	    
		List<Day> result = new LinkedList<Day>();
	    for(Day d : getDays())
	        if(!d.equals(day))
	            result.add(d);
	    setDays(result.toArray(new Day[result.size()]));
	}
	public void removeLEDPick(LEDPick ledpick) {
	    
		List<LEDPick> result = new LinkedList<LEDPick>();
	    for(LEDPick led : getLEDPicks())
	        if(!led.equals(ledpick))
	            result.add(led);
	    setLEDPicks(result.toArray(new LEDPick[result.size()]));
	}


	public Boolean getLEDOnOff() {
		return LEDOnOff;
	}


	public void setLEDOnOff(Boolean OnOff) {
		this.LEDOnOff = OnOff;
	}

	/**
	 * @return the AlarmLabel
	 */
	public String getAlarmLabel() {
		return alarmLabel;
	}

	/**
	 * @param AlarmLabel
	 *            the AlarmLabel to set
	 */
	public void setAlarmLabel(String AlarmLabel) {
		this.alarmLabel = AlarmLabel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRepeatDaysString() {
		StringBuilder daysStringBuilder = new StringBuilder();
		if(getDays().length == Day.values().length){
			daysStringBuilder.append("Every Day");		
		}else{
			Arrays.sort(getDays(), new Comparator<Day>() {
				@Override
				public int compare(Day lhs, Day rhs) {
					
					return lhs.ordinal() - rhs.ordinal();
				}
			});
			for(Day d : getDays()){
				switch(d){
				case TUESDAY:
				case THURSDAY:
//					daysStringBuilder.append(d.toString().substring(0, 4));
//					break;
					default:
						daysStringBuilder.append(d.toString().substring(0, 3));		
						break;
				}				
				daysStringBuilder.append(',');
			}
			daysStringBuilder.setLength(daysStringBuilder.length()-1);
		}
			
		return daysStringBuilder.toString();
	}
	public String getLEDPicksString() {
		StringBuilder ledsStringBuilder = new StringBuilder();
		if(getLEDPicks().length == LEDPick.values().length){
			ledsStringBuilder.append("All LED");		
		}else{
			Arrays.sort(getLEDPicks(), new Comparator<LEDPick>() {
				@Override
				public int compare(LEDPick lhs, LEDPick rhs) {
					
					return lhs.ordinal() - rhs.ordinal();
				}
			});
			for(LEDPick led : getLEDPicks()){
				switch(led){
					default:
						ledsStringBuilder.append(led.toString().substring(0, 5));		
						break;
				}				
				ledsStringBuilder.append(',');
			}
			ledsStringBuilder.setLength(ledsStringBuilder.length()-1);
		}
			
		return ledsStringBuilder.toString();
	}

	public void schedule(Context context) {
		setAlarmActive(true);
		
		Intent myIntent = new Intent(context, AlarmAlertBroadcastReciever.class);
		myIntent.putExtra("alarm", this);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime().getTimeInMillis(), pendingIntent);					
	}
	
	public String getTimeUntilNextAlarmMessage(){//알람 설정 시 앞으로 남은시간 알려줌
		long timeDifference = getAlarmTime().getTimeInMillis() - System.currentTimeMillis();
		long days = timeDifference / (1000 * 60 * 60 * 24);
		long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
		long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
		long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
		String alert = "LED Scheduler will work in ";
		if (days > 0) {
			alert += String.format(
					"%d days, %d hours, %d minutes and %d seconds", days,
					hours, minutes, seconds);
		} else {
			if (hours > 0) {
				alert += String.format("%d hours, %d minutes and %d seconds",
						hours, minutes, seconds);
			} else {
				if (minutes > 0) {
					alert += String.format("%d minutes, %d seconds", minutes,
							seconds);
				} else {
					alert += String.format("%d seconds", seconds);
				}
			}
		}
		return alert;
	}
}
