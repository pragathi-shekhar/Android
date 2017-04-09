package com.MITAquire.aquire;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;

@SuppressLint("ParserError")
public class ForgotPassword extends Activity {
	Button _getpassbutton;
	TextView _tvquestion;
	TextView _tvAnswer; 
	EditText _getAnswer;
	DBAdapter _dba;
	public static String passwordIs="";
	public static String answerIs="";
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.forgotpassword);
	        _getpassbutton = (Button) findViewById(R.id.button1submitanswer);
	        _tvquestion = (TextView) findViewById(R.id.textView2question);
	        _tvAnswer = (TextView) findViewById(R.id.textView3displayanswer);
	        _getAnswer = (EditText) findViewById(R.id.editText1getanswer);
	        _dba = new DBAdapter(this);
	        _dba.open();
			//_tvquestion.setText("");
	        Cursor c = _dba.getQuestion();
	        getString(c);
			_dba.close();
	        _getpassbutton.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(_getAnswer.getText().toString().trim().equals(answerIs)){
						_tvAnswer.setText("Password is :"+passwordIs);
					}
				}
			});	        
	  }
	  public void getString(Cursor c){
		  if(c.moveToFirst()){
			  _tvquestion.setText(c.getString(2).toString());
			  passwordIs = c.getString(1).toString();
			  answerIs = c.getString(3).toString();
		  }
	  }

}

