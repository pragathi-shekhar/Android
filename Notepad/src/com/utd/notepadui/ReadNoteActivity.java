/**************************************************************************/
//                      Author: 
//                      Net ID: lxn130730
//                      Date Started:3/14/2014
//                      Purpose :An android notepad app Assignment for the course CS 6301:User Interface Design  
//                               and development of mobile applications 
//                      Description:Class to read file contents when selected it on list view.
//
/**************************************************************************/

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
import java.util.StringTokenizer;

import com.utd.notepadui.R.id;
import com.utd.notepadui.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReadNoteActivity extends Activity {

	String filePath = null;
	String actualFileName = null;

	//lxn130730: check if activity has received any intents, 
	//if yes, display the file contents from the file name it has received
	//psb130230: obtain file name from extra intents received. set layout to activity_main
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			filePath = extras.getString("filePath");
			actualFileName = extras.getString("ActualName");
		}
		Log.d("ReadNoteActivity", "start of oncreate");
		Log.d("file path", filePath);
		Log.d("Content View", "Read Note");

		setContentView(layout.activity_main);
		displayFileContent();
		Log.d("file path", filePath);
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	//lxn130730: retrieve contents of the file to be displayed. assign the edit text
	//with the contents of the file for user to edit.
	public void displayFileContent() {

		Log.d("displayFileContent", "start of displayFileContent");
		Log.d("File Path", filePath);
		Log.d(filePath, "Open the file");

		EditText tv = (EditText) findViewById(id.typeNoteHere);
		File file = new File(filePath);

		if (!file.exists()) {
			tv.append("This file does not exist.");
		} else {
			FileInputStream fInputStream = null;
			try {
				Log.d(filePath, "This is the file path");
				fInputStream = new FileInputStream(filePath);
				InputStreamReader inputReader = new InputStreamReader(
						fInputStream);
				BufferedReader bReader = new BufferedReader(inputReader, 8192);

				String test;
				while (true) {
					test = bReader.readLine();
					Log.d(test, "This is the file content");
					// readLine() returns null if no more lines in the file
					if (test == null)
						break;
					tv.append(test);
				}
				bReader.close();
				inputReader.close();
				fInputStream.close();
			} catch (IOException e) {
				String errorMessage = (e.getMessage() == null) ? "Message is empty"
						: e.getMessage();
				Log.e("Message:", errorMessage);
			}
		}
	}

	// Written by mat130830: inflate menu layout
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//Written by psb130230
	//Method which defines actions when a user presses 'Return' button from the device
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		return;
	}

	// Written by mat130830
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
				new AlertDialog.Builder(ReadNoteActivity.this)
						.setMessage("Do you want to save this note?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public String fileInputname;

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										final EditText fName = new EditText(
												ReadNoteActivity.this);

										new AlertDialog.Builder(
												ReadNoteActivity.this)
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
																		.trim()
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
																						ReadNoteActivity.this,
																						fileInputname
																								.trim()
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
																	startActivity(new Intent(
																			getApplicationContext(),
																			MainActivity.class));
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
										startActivity(new Intent(
												getApplicationContext(),
												MainActivity.class));
									}
								}).show();
			}
		}
		
		//mat130830: saves new note. throws error toast message if file with same name already exists
		if (item.getItemId() == id.savenote) {
			final EditText fName = new EditText(ReadNoteActivity.this);

			new AlertDialog.Builder(ReadNoteActivity.this)
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
												fileInputname + ".txt");
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
																ReadNoteActivity.this,
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
															ReadNoteActivity.this,
															"File already exists. Use different name",
															Toast.LENGTH_LONG);
											unsuccessfulSave.setGravity(
													Gravity.CENTER, 0, 0);
											unsuccessfulSave.show();
										}
									}
								}
							}).show();
		}
		//mat130830: Allows user to rename a file. 
		//Dialog box with existing file name is shown to the user when this option is pressed
		if (item.getItemId() == id.saveas) {
			StringTokenizer fileNameString = new StringTokenizer(
					actualFileName, ".");
			String unformattedFileName = fileNameString.nextToken().trim();

			final EditText fName = new EditText(ReadNoteActivity.this);
			fName.setText(unformattedFileName);
			new AlertDialog.Builder(ReadNoteActivity.this)
					.setTitle("Save File As")
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
												fileInputname + ".txt");
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
																ReadNoteActivity.this,
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
															ReadNoteActivity.this,
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
		//psb130230: actions to be performed when user presses exit menu item
		if (item.getItemId() == id.exit) {
			AlertDialog.Builder exitAlert = new AlertDialog.Builder(
					ReadNoteActivity.this);
			exitAlert.setMessage("Are you sure?");
			exitAlert.setCancelable(true);
			exitAlert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d("Menu Option Selected",
									"User will now Exit from the app");
							ReadNoteActivity.this.finish();
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
		//lxn130730: user is taken back to Second activity if it wants to view documents
		if (item.getItemId() == id.viewrecent) {
			Log.d("Menu Option Selected", "User will now view recent documents");
			Intent intent = new Intent(this, SecondActivity.class);
			startActivity(intent);
		}
		//psb130230: help dialog box is shown to the user
		if (item.getItemId() == id.help) {
			Log.d("Menu option selected", "User will now view help dialog");
			HelpDialogBoxActivity chd = new HelpDialogBoxActivity(
					ReadNoteActivity.this);
			chd.show();
		}
		return super.onOptionsItemSelected(item);
	}
}