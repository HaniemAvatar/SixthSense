package com.example.sensingui.preferences;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sensingui.Alarm;
import com.example.sensingui.BaseActivity;
import com.example.sensingui.R;
import com.example.sensingui.database.Database;

public class AlarmPreferencesActivity extends BaseActivity {

	ImageButton deleteButton;
	TextView okButton;
	TextView cancelButton;
	private Alarm alarm;

	private ListAdapter listAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.alarm_preferences);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("alarm")) {
			setLEDAlarm((Alarm) bundle.getSerializable("alarm"));
		} else {
			setLEDAlarm(new Alarm());
		}
		if (bundle != null && bundle.containsKey("adapter")) {
			setListAdapter((AlarmPreferenceListAdapter) bundle
					.getSerializable("adapter"));
		} else {
			setListAdapter(new AlarmPreferenceListAdapter(this, getLEDAlarm()));
		}

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
				final AlarmPreference alarmPreference = (AlarmPreference) alarmPreferenceListAdapter
						.getItem(position);

				AlertDialog.Builder alert;
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				switch (alarmPreference.getType()) {
				case BOOLEAN:
					CheckedTextView checkedTextView = (CheckedTextView) v;
					boolean checked = !checkedTextView.isChecked();
					((CheckedTextView) v).setChecked(checked);
					switch (alarmPreference.getKey()) {
					case ALARM_ACTIVE:
						alarm.setAlarmActive(checked);
						break;
					case ALARM_LEDONOFF:
						alarm.setLEDOnOff(checked);
						break;
					default:
						break;
					}
					alarmPreference.setValue(checked);
					break;
				case STRING:
					alert = new AlertDialog.Builder(
							AlarmPreferencesActivity.this);

					alert.setTitle(alarmPreference.getTitle());
					final EditText input = new EditText(
							AlarmPreferencesActivity.this);

					input.setText(alarmPreference.getValue().toString());
					switch (alarmPreference.getKey()) {
					case ALARM_LABEL:
						input.setHint("알람의 명칭을 적으세요");
						alert.setView(input);
						
						alert.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										alarmPreference.setValue(input
												.getText().toString());
										alarm.setAlarmLabel(alarmPreference
												.getValue().toString());
										alarmPreferenceListAdapter
												.setLEDAlarm(getLEDAlarm());
										alarmPreferenceListAdapter
												.notifyDataSetChanged();
									}
								});
						alert.show();
						break;
					case ALARM_LEDBRIGHTNESS:
						input.setHint("0~100사이의 숫자로 적으세요");
						alert.setView(input);
						
						alert.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										alarmPreference.setValue(input
												.getText().toString());
										alarm.setLEDBrightness(Integer
												.parseInt(alarmPreference
														.getValue().toString()));
										alarmPreferenceListAdapter
												.setLEDAlarm(getLEDAlarm());
										alarmPreferenceListAdapter
												.notifyDataSetChanged();
									}
								});
						alert.show();
						break;
					default:
						break;
					}
					break;
				case MULTIPLE_LIST:
					alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

					alert.setTitle(alarmPreference.getTitle());
					// alert.setMessage(message);

					CharSequence[] multiListItems = new CharSequence[alarmPreference.getOptions().length];
					for (int i = 0; i < multiListItems.length; i++)
						multiListItems[i] = alarmPreference.getOptions()[i];
					switch (alarmPreference.getKey()) {
					case ALARM_REPEAT:
					boolean[] checkedItems = new boolean[multiListItems.length];
					for (Alarm.Day day : getLEDAlarm().getDays()) {
						checkedItems[day.ordinal()] = true;
					}
					alert.setMultiChoiceItems(multiListItems, checkedItems, new OnMultiChoiceClickListener() {

						@Override
						public void onClick(final DialogInterface dialog, int which, boolean isChecked) {

							Alarm.Day thisDay = Alarm.Day.values()[which];

							if (isChecked) {
								alarm.addDay(thisDay);
							} else {
								// Only remove the day if there are more than 1
								// selected
								if (alarm.getDays().length > 1) {
									alarm.removeDay(thisDay);
								} else {
									// If the last day was unchecked, re-check
									// it
									((AlertDialog) dialog).getListView().setItemChecked(which, true);
								}
							}

						}
					});
					break;
					case ALARM_LEDPICK:
						boolean[] checkedLEDs = new boolean[multiListItems.length];
						for (Alarm.LEDPick led : getLEDAlarm().getLEDPicks()) {
							checkedLEDs[led.ordinal()] = true;
						}
						alert.setMultiChoiceItems(multiListItems, checkedLEDs, new OnMultiChoiceClickListener() {

							@Override
							public void onClick(final DialogInterface dialog, int which, boolean isChecked) {

								Alarm.LEDPick thisLED = Alarm.LEDPick.values()[which];

								if (isChecked) {
									alarm.addLEDPick(thisLED);
								} else {
									// Only remove the day if there are more than 1
									// selected
									if (alarm.getLEDPicks().length > 1) {
										alarm.removeLEDPick(thisLED);
									} else {
										// If the last day was unchecked, re-check
										// it
										((AlertDialog) dialog).getListView().setItemChecked(which, true);
									}
								}

							}
						});
						break;
						default:
							break;
					}
					alert.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							alarmPreferenceListAdapter.setLEDAlarm(getLEDAlarm());
							alarmPreferenceListAdapter.notifyDataSetChanged();

						}
					});
					alert.show();
					break;
				case TIME:
					TimePickerDialog timePickerDialog = new TimePickerDialog(
							AlarmPreferencesActivity.this,
							new OnTimeSetListener() {

								@Override
								public void onTimeSet(TimePicker timePicker,
										int hours, int minutes) {
									Calendar newAlarmTime = Calendar
											.getInstance();
									newAlarmTime.set(Calendar.HOUR_OF_DAY,
											hours);
									newAlarmTime.set(Calendar.MINUTE, minutes);
									newAlarmTime.set(Calendar.SECOND, 0);
									alarm.setAlarmTime(newAlarmTime);
									alarmPreferenceListAdapter
											.setLEDAlarm(getLEDAlarm());
									alarmPreferenceListAdapter
											.notifyDataSetChanged();
								}
							}, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY),
							alarm.getAlarmTime().get(Calendar.MINUTE), true);
					timePickerDialog.setTitle(alarmPreference.getTitle());
					timePickerDialog.show();
				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.menu_item_new).setVisible(false);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_save:
			Database.init(getApplicationContext());
			if (getLEDAlarm().getId() < 1) {
				Database.create(getLEDAlarm());
			} else {
				Database.update(getLEDAlarm());
			}
			callLEDAlarmScheduleService();
			Toast.makeText(AlarmPreferencesActivity.this,
					getLEDAlarm().getTimeUntilNextAlarmMessage(),
					Toast.LENGTH_LONG).show();
			finish();
			break;
		case R.id.menu_item_delete:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					AlarmPreferencesActivity.this);
			dialog.setTitle("Delete");
			dialog.setMessage("Delete this alarm?");
			dialog.setPositiveButton("Ok", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Database.init(getApplicationContext());
					if (getLEDAlarm().getId() < 1) {
						// Alarm not saved
					} else {
						Database.deleteEntry(alarm);
						callLEDAlarmScheduleService();
					}
					finish();
				}
			});
			dialog.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.show();

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("adapter",
				(AlarmPreferenceListAdapter) getListAdapter());
	};

	public Alarm getLEDAlarm() {
		return alarm;
	}

	public void setLEDAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public ListAdapter getListAdapter() {
		return listAdapter;
	}

	public void setListAdapter(ListAdapter listAdapter) {
		this.listAdapter = listAdapter;
		getListView().setAdapter(listAdapter);

	}

	public ListView getListView() {
		if (listView == null)
			listView = (ListView) findViewById(android.R.id.list);
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@Override
	public void onClick(View v) {
		// super.onClick(v);

	}
}
