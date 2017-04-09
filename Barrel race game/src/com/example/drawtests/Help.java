package com.example.drawtests;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends Activity {
	
	private Button helpButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}

	public void addListenerOnButton() {
		 
		helpButton = (Button) findViewById(R.id.bhelp);
 
		helpButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
						Intent helpIntent = 
	                            new Intent(Help.this, MainScreenActivity.class);
						startActivity(helpIntent);
					}
				
				
				
		
 
	

});
	}
}
