/****************************************************
 * Author: Miren Tanna
 * NET ID: mat130830
 * Description: Class to handle user input and define 
 * 				actions for menu items, save files, 
 * 				create directory
 * 
 * Comments: The app will create a folder in the public 
 * file system of Android. To look up your files, go to
 * <File Manager>->All Files OR External File Directory->SavedNotes folder * 
 ****************************************************/

package com.utd.notepadui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utd.notepadui.R.id;
import com.utd.notepadui.R.menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//mat130830: create file directory on android system if it does not exist
		String dirPath = File.separator + "SavedNotes";
		Log.d("File Operations", dirPath);
		File projDir = new File(Environment.getExternalStorageDirectory(),
				dirPath);
		if (!projDir.exists()) {
			Log.d("File Operations", "Directory is not created");
			projDir.mkdirs();
			Log.d("File Operations", "Directory is now created");
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText enteredText = (EditText) findViewById(id.typeNoteHere);
	}

	// Written by mat130830: inflate menu layout
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	//psb130230: define actions when the user presses return button on the device
	//user is asked if it wants to exit from the app
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog.Builder exitAlert = new AlertDialog.Builder(
				MainActivity.this);
		exitAlert.setMessage("Are you sure?");
		exitAlert.setCancelable(true);
		exitAlert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("Menu Option Selected",
								"User will now Exit from the app");
						MainActivity.this.finish();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final EditText enteredText = (EditText) findViewById(id.typeNoteHere);
		//mat130830: creates a new note, if user has entered text in the edit text, 
		//user will be prompted if it wants to save the note. Toast messages will be shown
		//if any error is thrown or if file is successfuly saved
			if (item.getItemId() == id.newnote) {
			if (enteredText.getText().toString().equals(null)
					|| enteredText.getText().toString().trim().length() == 0) {
				// do nothing
			} else {
				new AlertDialog.Builder(MainActivity.this)
						.setMessage("Do you want to save this note?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public String fileInputname;

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										final EditText fName = new EditText(
												MainActivity.this);
										fName.setTextSize(20f);

										new AlertDialog.Builder(
												MainActivity.this)
												.setTitle("Enter File Name")
												.setView(fName)
												.setPositiveButton(
														"Ok",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																fileInputname = fName
																		.getText()
																		.toString()
																		.trim();
																if (fileInputname
																		.toString()
																		.trim()
																		.length() == 0) {
																	Toast badFileLength = Toast
																			.makeText(
																					getApplicationContext(),
																					"File name cannot be blank",
																					Toast.LENGTH_LONG);
																	badFileLength
																			.setGravity(
																					Gravity.CENTER,
																					0,
																					0);
																	badFileLength
																			.show();
																} else {
																	File file = new File(
																			Environment
																					.getExternalStorageDirectory()
																					.toString()
																					+ File.separator
																					+ "SavedNotes",
																			fileInputname
																					.trim()
																					+ ".txt");
																	FileOutputStream fos;
																	try {
																		fos = new FileOutputStream(
																				file);
																		OutputStreamWriter osw = new OutputStreamWriter(
																				fos);
																		osw.write(enteredText
																				.getText()
																				.toString());
																		FileWriter fw = new FileWriter(
																				file);
																		fw.append(enteredText
																				.getText()
																				.toString());
																		fw.close();
																		osw.close();
																		fos.flush();
																		fos.close();

																		Toast successfulSave = Toast
																				.makeText(
																						MainActivity.this,
																						fileInputname
																								.trim()
																								.toString()
																								+ " saved successfully",
																						Toast.LENGTH_SHORT);
																		successfulSave
																				.setGravity(
																						Gravity.CENTER,
																						0,
																						0);
																		successfulSave
																				.show();
																	} catch (FileNotFoundException e) {
																		e.printStackTrace();
																	} catch (IOException e) {
																		e.printStackTrace();
																	}
																	enteredText
																			.setText("");
																}
															}
														})
												.setNegativeButton(
														"Cancel",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																dialog.dismiss();
															}
														}).show();
										Log.d("File saved at", Environment
												.getExternalStorageDirectory()
												.toString()
												+ File.separator
												+ "SavedNotes"
												+ File.separator);
										Log.d("File name saved as",
												fileInputname + ".txt");
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										enteredText.setText("");
									}
								}).show();
			}
		}
		//mat130830: saves new note. throws error toast message if file with same name already exists
		if (item.getItemId() == id.savenote) {
			final EditText fName = new EditText(MainActivity.this);
			fName.setTextSize(20f);
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("Enter File Name")
					.setView(fName)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String fileInputname = fName.getText()
											.toString().trim();
									if (fileInputname.toString().trim()
											.length() == 0) {
										Toast badFileLength = Toast.makeText(
												getApplicationContext(),
												"File name cannot be blank",
												Toast.LENGTH_LONG);
										badFileLength.setGravity(
												Gravity.CENTER, 0, 0);
										badFileLength.show();
									} else {
										File file = new File(
												Environment
														.getExternalStorageDirectory()
														.toString()
														+ File.separator
														+ "SavedNotes",
												fileInputname.trim() + ".txt");
										if (!file.exists()) {
											FileOutputStream fos;
											try {
												fos = new FileOutputStream(file);
												OutputStreamWriter osw = new OutputStreamWriter(
														fos);
												osw.write(enteredText.getText()
														.toString());
												FileWriter fw = new FileWriter(
														file);
												fw.append(enteredText.getText()
														.toString());
												fw.close();
												osw.close();
												fos.flush();
												fos.close();

												Toast successfulSave = Toast
														.makeText(
																MainActivity.this,
																fileInputname
																		.trim()
																		+ " saved successfully",
																Toast.LENGTH_SHORT);
												successfulSave.setGravity(
														Gravity.CENTER, 0, 0);
												successfulSave.show();
											} catch (FileNotFoundException e) {
												e.printStackTrace();
											} catch (IOException e) {
												e.printStackTrace();
											}
										} else {
											Toast unsuccessfulSave = Toast
													.makeText(
															MainActivity.this,
															"File already exists. Use different name",
															Toast.LENGTH_LONG);
											unsuccessfulSave.setGravity(
													Gravity.CENTER, 0, 0);
											unsuccessfulSave.show();
										}
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
		//mat130830: throws a toast error message if this option is selected. 
		//User must have opened a file before it can 'Save As'
		if (item.getItemId() == id.saveas) {
			Toast onPressSaveAs = Toast.makeText(getApplicationContext(),
					"You must open a document first", Toast.LENGTH_SHORT);
			onPressSaveAs.setGravity(Gravity.CENTER, 0, 0);
			onPressSaveAs.show();
		}
		//psb130230: actions to be performed when user presses exit menu item
		if (item.getItemId() == id.exit) {
			AlertDialog.Builder exitAlert = new AlertDialog.Builder(
					MainActivity.this);
			exitAlert.setMessage("Are you sure?");
			exitAlert.setCancelable(true);
			exitAlert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Log.d("Menu Option Selected",
									"User will now Exit from the app");
							MainActivity.this.finish();
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
		//lxn130730: user is taken to Second activity if it wants to view documents
		if (item.getItemId() == id.viewrecent) {
			Log.d("Menu Option Selected", "User will now view recent documents");
			Intent intent = new Intent(this, SecondActivity.class);
			startActivity(intent);
		}
		//psb130230: help dialog box is shown to the user
		if (item.getItemId() == id.help) {
			Log.d("Menu option selected", "User will now view help dialog");
			HelpDialogBoxActivity chd = new HelpDialogBoxActivity(
					MainActivity.this);
			chd.show();
		}
		return super.onOptionsItemSelected(item);
	}
}