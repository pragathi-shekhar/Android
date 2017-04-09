package com.example.drawtests;

import com.example.drawtests.MainActivity;
import com.example.drawtests.R.layout;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;




public class MainScreenActivity extends Activity
{
	//declaration for button
		//Button customizeButton;
		ImageButton customizeButton;
		private ScoreDbAdapter highScoreAccessor;
		private TextView score;
		private TextView highScore;
		private TextView yourScore;
		private Long mRowId;
		private Button startButton;
		private Button helpButton;
		private Button exitButton;
		
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Bundle extras = getIntent().getExtras(); 
	        Long countvalue = 0L;

	        if (extras != null) {
	        	countvalue = extras.getLong("count", 0);
	            // and get whatever type user account id is
	        }
	        if (mRowId == null) {
	            mRowId = extras != null ? extras.getLong(highScoreAccessor.KEY_ROWID)
	                                    : null;
	        }
	        
	        setContentView(layout.activity_mainscreen);
	        
	        //Call the button event.
	        addListenerOnCustButton();
	        addListenerOnStartButton();
	        addListenerOnHelpButton();
	        addListenerOnExitButton();
	        Log.d("oncreate","Will open database");
	        
	        highScoreAccessor = new ScoreDbAdapter(this);
			highScoreAccessor.open();
			fillData();
	        SharedPreferences sharedPref = getSharedPreferences("key", 0);;
			
			long score1= sharedPref.getLong("Score",0);
			long highscore = sharedPref.getLong("highscore", 0);
			
			yourScore = (TextView) findViewById(R.id.textViewYourScore);
			
			yourScore.setText(Double.toString(countvalue));
			
			
			
	     // we'll use this to manipulate the list of high scores
	     	highScoreAccessor = new ScoreDbAdapter(this);
	     	highScoreAccessor.open();
	     	
//	     	//mRowId = Long.valueOf(ScoreDbAdapter.KEY_ROWID);
    		
	    }

	    private void fillData() 
	    {
	        // Get all of the notes from the database and create the item list
	    	 if (mRowId != null) {
		            Cursor score = highScoreAccessor.fetchScore(mRowId);
		            startManagingCursor(score);
		            highScore = (TextView) findViewById(R.id.textViewYourHighScore);
		        	Log.d("Database", Double.toString(score.getDouble(0)));
		        	highScore.setText(Double.toString(score.getDouble(0)));
	    	 }
	        
//	        if (scoreCursor.getColumnCount()>0)
//	        {
//	        	highScore = (TextView) findViewById(R.id.textViewYourHighScore);
//	        	Log.d("Database", Double.toString(scoreCursor.getDouble(-1)));
//	        	highScore.setText(Double.toString(scoreCursor.getDouble(-1)));
//	        }
	        
	    }
	    
	 	
	 	
	   
	    //Button Listener for the customize view.
	    public void addListenerOnCustButton() {
	    	 
	    	customizeButton = (ImageButton) findViewById(R.id.imageButton1);

	    	customizeButton.setOnClickListener(new View.OnClickListener() {

	    		@Override
	    		public void onClick(View arg0) {

	    			/******************************************************
	    			 * Starting the customize activity
	    			 ******************************************************/
	    			Log.d("Main Screen Activity", "Starting Main Activity");
	    			Intent customizeIntent = 
	    					new Intent(MainScreenActivity.this, CustomizeActivity.class);
	    			startActivity(customizeIntent);
	 
				}
	 
			});
	 
		}
	    
	    public void addListenerOnExitButton() {
	    	 
	    	exitButton = (Button) findViewById(R.id.bexit);

	    	exitButton.setOnClickListener(new View.OnClickListener() {

	    		@Override
	    		public void onClick(View arg0) {
	    			
	    			
	    			AlertDialog.Builder exitAlert = new AlertDialog.Builder(
	    					MainScreenActivity.this);
	    			exitAlert.setMessage("Are you sure to Exit?");
	    			exitAlert.setCancelable(true);
	    			exitAlert.setPositiveButton("Yes",
	    					new DialogInterface.OnClickListener() {

	    						@Override
	    						public void onClick(DialogInterface dialog, int which) {
	    							Log.d("Menu Option Selected",
	    									"User will now Exit from the app");
	    							MainScreenActivity.this.finish();
	    						}
	    					});
	    			exitAlert.setNegativeButton("No",
	    					new DialogInterface.OnClickListener() {

	    						@Override
	    						public void onClick(DialogInterface dialog, int which) {
	    							dialog.cancel();
	    						}
	    					});
	    			AlertDialog exitAlertDialog = exitAlert.create();
	    			exitAlertDialog.show();
	    		}


	    			/******************************************************
	    			 * Starting the customize activity
	    			 ******************************************************/
	    			
	 
				});
	 
			}
	 
		
	    
	    public void addListenerOnHelpButton() {
	    	 
	    	helpButton = (Button) findViewById(R.id.bhelp);

	    	helpButton.setOnClickListener(new View.OnClickListener() {

	    		@Override
	    		public void onClick(View arg0) {

	    			/******************************************************
	    			 * Starting the customize activity
	    			 ******************************************************/
	    			Log.d("Main Screen Activity", "Starting Main Activity");
	    			Intent helpIntent = 
	    					new Intent(MainScreenActivity.this, Help.class);
	    			startActivity(helpIntent);
	 
				
	 
	    		}});
	 
		}
	    
	    
	    
	  //Button Listener for the customize view.
	    public void addListenerOnStartButton() {
	    	 
	    	startButton = (Button) findViewById(R.id.bstart);
	 
	    	startButton.setOnClickListener(new View.OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				/******************************************************
				 * Starting the Game
				 ******************************************************/
				Log.d("Main Screen Activity", "Starting Game");
				  Intent gameIntent = 
	                            new Intent(MainScreenActivity.this, MainActivity.class);
				  gameIntent.putExtra("GameOver", true);
				    startActivity(gameIntent);
	 
				}
	 
			});
	 
		}
	    @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	   // 	super.onBackPressed();
	    
	    	AlertDialog.Builder exitAlert = new AlertDialog.Builder(
					MainScreenActivity.this);
			exitAlert.setMessage("Are you sure to Exit?");
			exitAlert.setCancelable(true);
			exitAlert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d("Menu Option Selected",
									"User will now Exit from the app");
							MainScreenActivity.this.finish();
						}
					});
			exitAlert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			AlertDialog exitAlertDialog = exitAlert.create();
			exitAlert.show();
			
	    }
}
