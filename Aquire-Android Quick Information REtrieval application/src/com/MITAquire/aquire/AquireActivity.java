package com.MITAquire.aquire;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

@SuppressLint({ "ParserError", "ParserError", "ParserError" })
public class AquireActivity extends Activity {
	TextView _tvhelp;
	Button _register;
	Button forgotpass;
	Button changepass;
	Button clearData; 
	TextView displayUser;
	TextView safeSender;
	TextView routeNumber;
DBAdapter _dba;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquire);
        _tvhelp = (TextView) findViewById(R.id.textViewhelp1);
        _register = (Button) findViewById(R.id.RegisterorConfirmPassword);
        forgotpass = (Button) findViewById(R.id.forgotpassword);
        changepass = (Button) findViewById(R.id.changepass);
        displayUser = (TextView) findViewById(R.id.textViewusername1);
        safeSender = (TextView) findViewById(R.id.textViewsafesender3);
        routeNumber = (TextView) findViewById(R.id.textViewroutenumber2);
        clearData = (Button) findViewById(R.id.button1cleardata);
        clearData.setEnabled(false);
        _dba = new DBAdapter(this);
		startService(new Intent(getBaseContext(), MyService.class));
        _dba.open();
        
        Cursor users = _dba.userDetails();
        if(users.moveToFirst()){
        	displayUser.setText("User Name: "+users.getString(0));
        	_register.setEnabled(false);
        }else{
        	displayUser.setText("User Name: NA");
        }
        users.close();
        Cursor safeUser = _dba.safeSenderDetails();
        if(safeUser.moveToFirst()){
        	clearData.setEnabled(true);
        	safeSender.setText("Safe Sender :"+ safeUser.getString(0));
        }else{
        	safeSender.setText("Safe Sender : NA");
        }
        safeUser.close();
        Cursor routeUser = _dba.routeNumberDetails();
        if(routeUser.moveToFirst()){
        	routeNumber.setText("Route Number :" + routeUser.getString(0));
        }else{
        	routeNumber.setText("Route Number : NA");
        }
        routeUser.close();
        _dba.close();
        // opens change password  page
        changepass.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("android.intent.action.CHANGEPASSWORD"));
			}
		});
        // opens forgot password activity page
        
        forgotpass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("android.intent.action.FORGOTPASSWORD"));
			}
		});
        
        // opens registration page
        _register.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("android.intent.action.REGISTER"));
			}
		});
        // clear data
   clearData.setOnClickListener(new View.OnClickListener() {	
	   @Override
	   public void onClick(View v) {
		// TODO Auto-generated method stub
		   /////clearData.setEnabled(false);
		   _dba.open();
		   _dba.deleteAndReCreate();
		   _dba.close();
			Intent refresh = new Intent(getBaseContext(), AquireActivity.class);
			startActivity(refresh);
			finish();
		}
   });     
        
        
        // opens help view
        _tvhelp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent("android.intent.action.HELPACTIVITY"));
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_aquire, menu);
        return true;
    }    
}
