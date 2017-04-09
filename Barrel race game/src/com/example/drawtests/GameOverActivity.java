package com.example.drawtests;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class GameOverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_over);
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		Intent x= new Intent(getApplicationContext(), MainActivity.class);
		x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		x.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		x.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		x.putExtra("GameOver", true);
		startActivity(x);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}

}
