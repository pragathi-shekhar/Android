package com.MITAquire.aquire;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.Activity;

public class ChangePassword extends Activity {	
	Button _update;
	EditText _setusername;
	EditText previouspass;
	EditText currentpass;
	Spinner question;
	EditText answer;
	DBAdapter _dba;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.changepassword);
	        _update = (Button) findViewById(R.id.updateuserdetails);
	        answer = (EditText) findViewById(R.id.updatesequrityanswer);
	        _setusername = (EditText) findViewById(R.id.editText1updateuser);
	        previouspass = (EditText) findViewById(R.id.editText2updatecheckprevpass);
	        currentpass = (EditText) findViewById(R.id.editText3setcurrentpass);
	        question = (Spinner) findViewById(R.id.spinner1selectsequrity);
	        _dba = new DBAdapter(this);
	        _update.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					_dba.open();
					_dba.getUserUpdate(_setusername.getText().toString(), previouspass.getText().toString(), currentpass.getText().toString(), question.getSelectedItem().toString(), answer.getText().toString());
					_dba.close();
				}
			});
	        
	  }
}
