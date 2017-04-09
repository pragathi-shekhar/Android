package com.MITAquire.aquire;

//import com.example.demosms.MyService;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class Register extends Activity {
	Button _save;
	Spinner _spin;
	EditText _etusername;
	EditText password;
	EditText confirmpassword;
	EditText answer;
	TextView getprevLayout;
	Button prevlayoutsubmit;
	DBAdapter _dba;
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.registerpage);
	    //    getprevLayout = (TextView) findViewById(R.id.textViewusername1);
	   //     prevlayoutsubmit = (Button) findViewById(R.id.RegisterorConfirmPassword);
	        
	        _spin = (Spinner) findViewById(R.id.spinner1question);
	        _etusername = (EditText) findViewById(R.id.editText1registeruser);
	        password = (EditText) findViewById(R.id.editText2password);
	        confirmpassword = (EditText) findViewById(R.id.editText3confirmpassword);
	        answer = (EditText) findViewById(R.id.answer);
	        _save = (Button) findViewById(R.id.sendmessage);
	        _dba = new DBAdapter(this);
	        _save.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(!_etusername.getText().toString().equals("") && !password.getText().toString().equals("") && !confirmpassword.getText().toString().equals("") && !answer.getText().toString().equals("")){
						_dba.open();
						if(password.getText().toString().equals(confirmpassword.getText().toString())){
						_dba.insertAuth(_etusername.getText().toString(), password.getText().toString(), _spin.getSelectedItem().toString() , answer.getText().toString());
						// create a directory path and mkdir();
//						prevlayoutsubmit.setEnabled(false);
			//			getprevLayout.setText("User Name :"+_etusername.getText().toString());
						String path = Environment.getExternalStorageDirectory() + "/AQUIREStore/";
						File _fle = new File(path);
						_fle.mkdirs();
						startService(new Intent(getBaseContext(), MyService.class));
						}else{
							
						}
						_dba.close();
					}
					Intent refresh = new Intent(getBaseContext(), AquireActivity.class);
					startActivity(refresh);
					finish();
				}
			});
	  }
}
