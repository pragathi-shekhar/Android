package com.MITAquire.aquire;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class HelpActivity extends Activity {
	TextView _tvhelp;
	Button _btnBack;
	TextView _tvSettings;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.help);
	       // _tvhelp = (TextView) findViewById(R.id.textViewhelp1);
	        _btnBack = (Button) findViewById(R.id.buttonhelpback1);
	     //   _tvSettings = (TextView) findViewById(R.id.textViewsettings1);
	      /*  _tvhelp.setText("AQUIRE Commands"+"\n"+
	        				"Detailed instructions-"+"\n"+                   
	        				"SMS command"+"\n"+ 
	        				"Authenticate"+"\n"+
	        				"AU <password>"+"\n"+
	        				"End authentication -"+"\n"+
	        				"EA <password>"+"\n"+
	        				"Single Contact search -"+"\n"+
	        				"CS start/end/equals <name>"+"\n"+ 
	        				"Multiple contact search -"+"\n"+
	        				"MCS <name>"+"\n"+
	        				"File search by filename -"+"\n"+
	        				"FS <filename>"+"\n"+
	        				"File search by content -"+"\n"+
	        				"FCS <key>"+"\n"+
	        				"Missed call alert -"+"\n"+
	        				"MCA"+"\n"+
	        				"Message routing -"+"\n"+
	        		"MR <num>");*/
	   /*     _tvSettings.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//startActivity(new Intent("android.intent.action.MAIN"));
					finish();
				}
			});*/
	        _btnBack.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	  }
}

