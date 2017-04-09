package com.example.drawtests;



import java.io.File;

import org.apache.http.client.CircularRedirectException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.View.OnClickListener;


public class CustomizeActivity extends Activity
{

	private SeekBar seekBarAcc;
	private TextView textViewAcc;
	private SeekBar seekBarRadius;
	private TextView textViewRadius;
	Button custButton;
	float radius, accelerometer_value;
	float circleRadius;
	float accelerometer_reading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customize);
		seekBarAcc = (SeekBar) findViewById(R.id.seekBarAcc);
		textViewAcc = (TextView) findViewById(R.id.textView1);
		
		SharedPreferences sharedPref = getSharedPreferences("key", 0);;
		if(sharedPref.contains("Radius") && sharedPref.contains("Accelerometer_Reading")){
			circleRadius = sharedPref.getFloat("Radius",10f);
			accelerometer_reading = sharedPref.getFloat("Accelerometer_Reading", 10f);
		}
		else
		{
			circleRadius = 5f;
			accelerometer_reading=5f;
			
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putFloat("Radius", circleRadius);
			editor.putFloat("Accelerometer_Reading", accelerometer_reading);
			editor.commit();
		}
//		final float circleRadius = sharedPref.getFloat("Radius",10f);
//		final float accelerometer_reading = sharedPref.getFloat("Accelerometer_Reading", 10f);
		
		// Initialize the textViewAcc with '0'
		Log.d("check here",  accelerometer_reading + ", " + circleRadius);
		seekBarAcc.setProgress((int)accelerometer_reading);	//<---
		textViewAcc.setText(seekBarAcc.getProgress() + "/" + seekBarAcc.getMax());
		
		seekBarAcc.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = (int) accelerometer_reading;	//<---

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				//progress = progresValue;
				progress = progresValue;	//<---
				accelerometer_value = (float)progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do something here, if you want to do anything at the start of
				// touching the seekbar
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Display the value in textview
				textViewAcc.setText(progress + "/" + seekBar.getMax());
			}
		});
		
		//Set the values for radius 
		seekBarRadius = (SeekBar) findViewById(R.id.seekBarRad);
		textViewRadius = (TextView) findViewById(R.id.textView2);
		
		// Initialize the textViewRadius with '0'
		seekBarRadius.setProgress((int)circleRadius);
		textViewRadius.setText(seekBarRadius.getProgress() + "/" + seekBarRadius.getMax());
		
		seekBarRadius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = (int) circleRadius;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				progress = progresValue;
				radius = (float)progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do something here, if you want to do anything at the start of
				// touching the seekbar
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Display the value in textview
				textViewRadius.setText(progress + "/" + seekBar.getMax());
			}
		});
		
		addListenerOnButton();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//			
			
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		 
		 startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
		super.onBackPressed();
	}
	 
	public void addListenerOnButton() {
		 
		custButton = (Button) findViewById(R.id.custBtn);
 
		custButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				AlertDialog.Builder exitAlert = new AlertDialog.Builder(CustomizeActivity.this);
				exitAlert.setMessage("Are you sure?");
				exitAlert.setCancelable(true);
				exitAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Log.d("Menu Option Selected", "You have modified the settings");
						SharedPreferences sharedPref = getSharedPreferences("key", 0);
						SharedPreferences.Editor editor = sharedPref.edit();
						float number = Float.valueOf(radius);
						//editor.putFloat("Accelerometer", R.id.seekBarAcc);
						Log.d("number, accel", Float.toString(number) + ", " + accelerometer_value);
						editor.putFloat("Radius", seekBarRadius.getProgress());
						editor.putFloat("Accelerometer_Reading", seekBarAcc.getProgress());
						editor.commit();
						
						CustomizeActivity.this.finish();
						Intent customizeIntent = 
	                            new Intent(CustomizeActivity.this, MainScreenActivity.class);
						startActivity(customizeIntent);
					}
				});
				
				
				
				exitAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.cancel();
					}
				});
				AlertDialog exitAlertDialog= exitAlert.create();
				exitAlertDialog.show();
 
			}
 
		});
 
	}
}
