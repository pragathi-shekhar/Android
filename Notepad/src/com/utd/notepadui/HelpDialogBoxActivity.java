/****************************************************
 * Author: Miren Tanna
 * NET ID: mat130830
 * Description: Class to create Help Dialog Box
 ****************************************************/

package com.utd.notepadui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

public class HelpDialogBoxActivity extends Dialog {

	public Activity c;

	public HelpDialogBoxActivity(Activity a) {
		super(a);
		this.c = a;
	}
//Written by mat130830
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//assign XML layout to activity
		setContentView(R.layout.activity_help_dialog_box);
	}
}