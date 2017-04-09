/*
 * Author: Lavanya Nadikuda 
 * Net ID: lxn130730
 * Description: Class to view Recent Documents 
 * 				once a user clicks the View Recent 
 * 				icon from Main Activity. 
 * 				Activity_Second XML is assigned 
 * 				to this activity.                      
*/
package com.utd.notepadui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SecondActivity extends ListActivity {

	private TextView tv;
	ArrayList<String> MyFiles = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		displayFilePaths();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	//Written by psb130230
	//Method which defines actions when a user presses 'Return' button from the device
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.viewrecent:	//lxn130730: provides file path to method to obtain file names
			displayFilePaths();
			return true;
		case R.id.newnote:		//psb130230: to create new note when user is on View Recent screen
			startActivity(new Intent(this, MainActivity.class));
			return true;
		case R.id.help: {		//psb130230: to view the help menu
			HelpDialogBoxActivity chd = new HelpDialogBoxActivity(
					SecondActivity.this);
			chd.show();
			return true;
		}
		case R.id.exit: {		//mat130830: to exit out of the app when user is on view recent screen
			AlertDialog.Builder exitAlert = new AlertDialog.Builder(
					SecondActivity.this);
			exitAlert.setMessage("Are you sure?");
			exitAlert.setCancelable(true);
			exitAlert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d("Menu Option Selected",
									"User will now Exit from the app");
							SecondActivity.this.finish();
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
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * lxn130730
	 * Method to read in a text file placed in the res/raw directory of the
	 * application. The method reads in all lines of the file sequentially.
	 */

	private void displayFilePaths() {

		Log.d("read File", "User will now view files from folder in a list ");

		MyFiles = new ArrayList<String>();

		File sdcard = Environment.getExternalStorageDirectory();
		Log.d(sdcard.getPath(), "The path of external storage");
		File file = new File(sdcard, "SavedNotes");

		if (!file.exists()) {
			tv.append("There are no files in the directory");

		} else {
			File[] files = file.listFiles();

			//Written by mat130830
			//to compare files based on their last modified date and sort them
			Arrays.sort(files, new Comparator() {
				public int compare(Object o1, Object o2) {

					if (((File) o1).lastModified() > ((File) o2).lastModified()) {
						return -1;
					} else if (((File) o1).lastModified() < ((File) o2)
							.lastModified()) {
						return +1;
					} else {
						return 0;
					}
				}
			});

			if (files.length == 0)
				tv.append("There are no files in the directory");
			else {
				for (int i = 0; i < files.length; i++)
					MyFiles.add(files[i].getName());
			}
		}
		ListView lv = (ListView) findViewById(android.R.id.list);

		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, MyFiles));
	}

	//lxn130730: method to send intents to Read Note activity with information
	//about the file it needs to display to the user.
	//mat130830: extra intent data sent to read note activity to obtain
	//file name
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String filePath = null;
		String folderPath = null;
		folderPath = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "SavedNotes";
		File file = new File(MyFiles.get(position));
		Log.d(MyFiles.get(position).toString(),
				"This is the file name from MyFiles list ");
		filePath = folderPath + File.separator + file.getName();
		Log.d("file Name", filePath);
		Log.d("actual name", file.getName());
		Intent intent = new Intent(this, ReadNoteActivity.class);
		intent.putExtra("filePath", filePath);
		intent.putExtra("ActualName", file.getName());
		startActivity(intent);
	}
}