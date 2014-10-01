package com.example.sensingui.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sensingui.Alarm;

/* 
 * usage:  
 * DatabaseSetup.init(egActivityOrContext); 
 * DatabaseSetup.createEntry() or DatabaseSetup.getContactNames() or DatabaseSetup.getDb() 
 * DatabaseSetup.deactivate() then job done 
 */

public class Database extends SQLiteOpenHelper {
	static Database instance = null;
	static SQLiteDatabase database = null;
	
	static final String DATABASE_NAME = "DB";
	static final int DATABASE_VERSION = 1;
	
	public static final String ALARM_TABLE = "LEDAlarm";
	public static final String COLUMN_ALARM_ID = "_id";
	public static final String COLUMN_ALARM_ACTIVE = "alarm_active";	
	public static final String COLUMN_ALARM_TIME = "alarm_time";
	public static final String COLUMN_ALARM_DAYS = "alarm_days";
	public static final String COLUMN_ALARM_LEDPICKS = "alarm_ledpicks";
	public static final String COLUMN_ALARM_LEDBRIGHTNESS = "alarm_ledbrightness";
	public static final String COLUMN_ALARM_LEDONOFF = "alarm_ledonoff";
	public static final String COLUMN_ALARM_LABEL = "alarm_label";	
	
	public static void init(Context context) {
		if (null == instance) {
			instance = new Database(context);
		}
		Log.d("Database Read", "init");
	}

	public static SQLiteDatabase getDatabase() {
		if (null == database) {
			database = instance.getWritableDatabase();
		}
		Log.d("Database Read", "GetDB");
		return database;
	}

	public static void deactivate() {
		if (null != database && database.isOpen()) {
			database.close();
		}
		database = null;
		instance = null;
	}

	public static long create(Alarm alarm) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
		cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = null;
		    oos = new ObjectOutputStream(bos);
		    oos.writeObject(alarm.getDays());
		    byte[] buff = bos.toByteArray();
		    
		    cv.put(COLUMN_ALARM_DAYS, buff);
		} catch (Exception e){
		}	
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = null;
		    oos = new ObjectOutputStream(bos);
		    oos.writeObject(alarm.getLEDPicks());
		    byte[] buff = bos.toByteArray();
		    
		    cv.put(COLUMN_ALARM_LEDPICKS, buff);
		} catch (Exception e){
		}	
		cv.put(COLUMN_ALARM_LEDBRIGHTNESS, alarm.getLEDBrightness());
		cv.put(COLUMN_ALARM_LEDONOFF, alarm.getLEDOnOff());
		cv.put(COLUMN_ALARM_LABEL, alarm.getAlarmLabel());
		
		return getDatabase().insert(ALARM_TABLE, null, cv);
	}
	public static int update(Alarm alarm) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
		cv.put(COLUMN_ALARM_TIME, alarm.getAlarmTimeString());
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = null;
		    oos = new ObjectOutputStream(bos);
		    oos.writeObject(alarm.getDays());
		    byte[] buff = bos.toByteArray();
		    Log.d("testing", alarm.getDays().toString());
		    cv.put(COLUMN_ALARM_DAYS, buff);
		    
		} catch (Exception e){
		}
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = null;
		    oos = new ObjectOutputStream(bos);
		    oos.writeObject(alarm.getLEDPicks());
		    byte[] buff = bos.toByteArray();
		    Log.d("testing", alarm.getLEDPicks().toString());
		    cv.put(COLUMN_ALARM_LEDPICKS, buff);
		    
		} catch (Exception e){
		}			
		cv.put(COLUMN_ALARM_LEDBRIGHTNESS, alarm.getLEDBrightness());
		cv.put(COLUMN_ALARM_LEDONOFF, alarm.getLEDOnOff());
		cv.put(COLUMN_ALARM_LABEL, alarm.getAlarmLabel());
					
		return getDatabase().update(ALARM_TABLE, cv, "_id=" + alarm.getId(), null);
	}
	public static int deleteEntry(Alarm alarm){
		return deleteEntry(alarm.getId());
	}
	
	public static int deleteEntry(int id){
		return getDatabase().delete(ALARM_TABLE, COLUMN_ALARM_ID + "=" + id, null);
	}
	
	public static int deleteAll(){
		return getDatabase().delete(ALARM_TABLE, "1", null);
	}
	
	public static Alarm getAlarm(int id) {
		// TODO Auto-generated method stub
		Log.d("Database Read", "GetAlarm");
		String[] columns = new String[] { 
				COLUMN_ALARM_ID, 
				COLUMN_ALARM_ACTIVE,
				COLUMN_ALARM_TIME,
				COLUMN_ALARM_DAYS,
				COLUMN_ALARM_LEDPICKS,
				COLUMN_ALARM_LEDBRIGHTNESS,
				COLUMN_ALARM_LEDONOFF,
				COLUMN_ALARM_LABEL
				};
		Cursor c = getDatabase().query(ALARM_TABLE, columns, COLUMN_ALARM_ID+"="+id, null, null, null,
				null);
		Alarm alarm = null;
		
		if(c.moveToFirst()){
			
			alarm =  new Alarm();
			alarm.setId(c.getInt(1));
			alarm.setAlarmActive(c.getInt(2)==1);
			alarm.setAlarmTime(c.getString(3));
			byte[] repeatDaysBytes = c.getBlob(4);
			
			ByteArrayInputStream byteArrayInputStreamDay = new ByteArrayInputStream(repeatDaysBytes);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStreamDay);
				Alarm.Day[] repeatDays;
				Object object = objectInputStream.readObject();
				if(object instanceof Alarm.Day[]){
					repeatDays = (Alarm.Day[]) object;
					alarm.setDays(repeatDays);
				}								
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			byte[] LEDsBytes = c.getBlob(5);
			
			ByteArrayInputStream byteArrayInputStreamLED = new ByteArrayInputStream(LEDsBytes);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStreamLED);
				Alarm.LEDPick[] LEDs;
				Object object = objectInputStream.readObject();
				if(object instanceof Alarm.LEDPick[]){
					LEDs = (Alarm.LEDPick[]) object;
					alarm.setLEDPicks(LEDs);
				}								
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}		
			alarm.setLEDBrightness(c.getInt(6));
			alarm.setLEDOnOff(c.getInt(7)==1);
			alarm.setAlarmLabel(c.getString(8));
		}
		c.close();
		return alarm;
	}
	
	public static Cursor getCursor() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { 
				COLUMN_ALARM_ID, 
				COLUMN_ALARM_ACTIVE,
				COLUMN_ALARM_TIME,
				COLUMN_ALARM_DAYS,
				COLUMN_ALARM_LEDPICKS,
				COLUMN_ALARM_LEDBRIGHTNESS,
				COLUMN_ALARM_LEDONOFF,
				COLUMN_ALARM_LABEL
				};
		return getDatabase().query(ALARM_TABLE, columns, null, null, null, null,
				null);
	}

	Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + ALARM_TABLE + " ( " 
				+ COLUMN_ALARM_ID + " INTEGER primary key autoincrement, " 
				+ COLUMN_ALARM_ACTIVE + " INTEGER NOT NULL, " 
				+ COLUMN_ALARM_TIME + " TEXT NOT NULL, " 
				+ COLUMN_ALARM_DAYS + " BLOB NOT NULL, " 
				+ COLUMN_ALARM_LEDPICKS + " BLOB NOT NULL, " 
				+ COLUMN_ALARM_LEDBRIGHTNESS + " TEXT NOT NULL, " 
				+ COLUMN_ALARM_LEDONOFF + " INTEGER NOT NULL, " 
				+ COLUMN_ALARM_LABEL + " TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
		onCreate(db);
	}

	public static List<Alarm> getAll() {
		List<Alarm> alarms = new ArrayList<Alarm>();
		Cursor cursor = Database.getCursor();
		if (cursor.moveToFirst()) {

			do {
				// COLUMN_ALARM_ID,
				// COLUMN_ALARM_ACTIVE,
				// COLUMN_ALARM_TIME,
				// COLUMN_ALARM_DAYS,
				// COLUMN_ALARM_DIFFICULTY,
				// COLUMN_ALARM_TONE,
				// COLUMN_ALARM_VIBRATE,
				// COLUMN_ALARM_NAME

				Alarm alarm = new Alarm();
				alarm.setId(cursor.getInt(0));
				alarm.setAlarmActive(cursor.getInt(1) == 1);
				alarm.setAlarmTime(cursor.getString(2));
				byte[] repeatDaysBytes = cursor.getBlob(3);

				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						repeatDaysBytes);
				try {
					ObjectInputStream objectInputStream = new ObjectInputStream(
							byteArrayInputStream);
					Alarm.Day[] repeatDays;
					Object object = objectInputStream.readObject();
					if (object instanceof Alarm.Day[]) {
						repeatDays = (Alarm.Day[]) object;
						alarm.setDays(repeatDays);
					}
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				byte[] LEDsBytes = cursor.getBlob(4);

				ByteArrayInputStream byteArrayInputStreamLED = new ByteArrayInputStream(
						LEDsBytes);
				try {
					ObjectInputStream objectInputStream = new ObjectInputStream(
							byteArrayInputStreamLED);
					Alarm.LEDPick[] LEDs;
					Object object = objectInputStream.readObject();
					if (object instanceof Alarm.LEDPick[]) {
						LEDs = (Alarm.LEDPick[]) object;
						alarm.setLEDPicks(LEDs);
					}
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				alarm.setLEDBrightness(cursor.getInt(5));
				alarm.setLEDOnOff(cursor.getInt(6) == 1);
				alarm.setAlarmLabel(cursor.getString(7));
				
				alarms.add(alarm);

			} while (cursor.moveToNext());			
		} 
		cursor.close();
		return alarms;
	}
}