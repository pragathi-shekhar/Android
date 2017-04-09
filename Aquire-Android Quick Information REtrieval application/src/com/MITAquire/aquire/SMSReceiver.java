package com.MITAquire.aquire;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.annotation.SuppressLint;


@SuppressLint("ParserError")
public class SMSReceiver extends BroadcastReceiver {
	DBAdapter _dba;
	private static final int READ_BLOCK_SIZE = 160;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub			
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null){
		//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i < msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				str += "" + msgs[i].getOriginatingAddress();
				str += ":";
				str += msgs[i].getMessageBody().toString();				
			}
			String[] getnumber = str.split(":");
			String[] getCode = extractMessage(getnumber[1]);
			

			_dba = new DBAdapter(context);
			boolean flag =false;
			if(getCode[0].toLowerCase().trim().equals("au") || getCode[0].toLowerCase().trim().equals("ea") || getCode[0].trim().toLowerCase().equals("cs")){
				flag = getdata( getCode[0].toLowerCase().trim(),  getnumber[0], getCode[1].toLowerCase().trim());
			}else if(getCode[0].toLowerCase().trim().equals("mr") || getCode[0].toLowerCase().trim().equals("fcs") || getCode[0].toLowerCase().trim().equals("fs") || getCode[0].toLowerCase().trim().equals("mcs")){
				flag = getdata( getCode[0].toLowerCase().trim(),  getnumber[0], getCode[1].toLowerCase().trim());
			}else if(getCode[0].toLowerCase().trim().equals("mca") || getCode[0].toLowerCase().trim().equals("sms")){
				flag = getDataFlag(getnumber[0]);
			}
			//routing 
			/*	String command = "au password"+"\n";
								command += "ea password"+"\n";
								command += "cs start keyword"+"\n";
								command += "cs end keyword"+"\n";
								command += "cs equals keyword"+"\n";
								command += "mcs keyword"+"\n";
								command += "fs filename"+"\n";
								command += "fcs keyword"+"\n";					
								command += "mca"+"\n";
								command += "mr routenumber"+"\n";	*/
			else{
				_dba.open();	
				Cursor _getResult  = _dba.getRouteNumbers();
				if(_getResult.moveToFirst()){
					   do {					     
						   Uri personUri = Uri.withAppendedPath( ContactsContract.PhoneLookup.CONTENT_FILTER_URI, getnumber[0]);  
						   String contactnumber ="";
						   Cursor phoneCursorr = context.getContentResolver().query(personUri, new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null );
						   if( phoneCursorr.moveToFirst() ) {  
				                int nameIndex = phoneCursorr.getColumnIndex(PhoneLookup.DISPLAY_NAME);
				                contactnumber = phoneCursorr.getString(nameIndex); 
						   }
						   phoneCursorr.close();						   
						   sendSms(_getResult.getString(0), "Routed.."+"\n"+ getnumber[1] +" from "+contactnumber+" "+ getnumber[0]);
			        } while(_getResult.moveToNext());
				}
				_getResult.close();
				_dba.close();
			}
	
			if(flag){
				if(getCode[0].trim().toLowerCase().equals("cs")){
					if(getCode[1].toLowerCase().equals("equals")){
							Uri allContacts = android.provider.ContactsContract.Contacts.CONTENT_URI;
							String[] projection = new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER};
							Cursor _csr = context.getContentResolver().query(allContacts, projection, null, null,null); 	
							String number = getEquals(_csr, getCode[2]);
							if(!number.equals("num") ){
								Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+number, null, null);
								String endValue = "";
		    					while (phoneCursor.moveToNext()) {
		    						Log.v("Content Providers", phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		    						String numbers = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		    						endValue +=  numbers;
		    					}    					
		    					phoneCursor.close();
		    					if(!endValue.equals("")){
		    						sendSms(getnumber[0], getCode[2]+ " : "+ endValue);
		    					}
							}else{
	    						sendSms(getnumber[0], getCode[2]+ ":Contact Not Found");
	    					}
							_csr.close();
						}
						// if the condition is START
						if(getCode[1].toLowerCase().equals("start")){
							Uri allContacts = android.provider.ContactsContract.Contacts.CONTENT_URI;
							String[] projection = new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER};
							Cursor _csr = context.getContentResolver().query(allContacts, projection, null, null,null); 	
							String number = getStart(_csr, getCode[2]);
							String[] names =number.split(":"); 
							if(!number.equals("num") ){
								Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+names[0], null, null);
								String endValue = "";
		    					while (phoneCursor.moveToNext()) {
		    						Log.v("Content Providers", phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		    						String numbers = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
		    						endValue +=  numbers;
		    					}    					
		    					phoneCursor.close();
		    					if(!endValue.equals("")){
		    						sendSms(getnumber[0], getCode[2]+ " : "+names[1] +" :"+ endValue);
		    					}
							}else{
	    						sendSms(getnumber[0], getCode[2]+ ":Contact Not Found");
	    					}
							_csr.close();
						}
						// if the condition is END
						if(getCode[1].toLowerCase().equals("end")){
							Uri allContacts = android.provider.ContactsContract.Contacts.CONTENT_URI;
							String[] projection = new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER};
							Cursor _csr = context.getContentResolver().query(allContacts, projection, null, null,null); 	
							String number = getEnd(_csr, getCode[2]);
							String[] names =number.split(":");
							if(!number.equals("num") ){
								Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+names[0], null, null);
								String endValue = "";
		    					while (phoneCursor.moveToNext()) {
		    						Log.v("Content Providers", phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		    						String numbers = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
		    						endValue +=  numbers;
		    					}    					
		    					phoneCursor.close();
		    					if(!endValue.equals("")){
		    						sendSms(getnumber[0], getCode[2]+ " : "+names[1] +" :"+ endValue);
		    					}
							}else{
	    						sendSms(getnumber[0], getCode[2]+ ":Contact Not Found");
	    					}
							_csr.close();
						}
						}//add route number
						else if(getCode[0].toLowerCase().trim().equals("mr")){
							saveRouteNumber(getCode[1]);
							sendSms(getnumber[0], "Route Created");
						} // file search
						else if(getCode[0].toLowerCase().trim().equals("fs")){
							File files = new File(Environment.getExternalStorageDirectory() + "/AQUIREStore/");
							File[] list = files.listFiles();
							String folder = "AQUIREStore";
							boolean creator = false;
							for(int index = 0; index < list.length; index++){
								if(list[index].toString().toLowerCase().startsWith(Environment.getExternalStorageDirectory() + "/aquirestore/"+ getCode[1].toLowerCase())){
									File getFile = new File(list[index].toString());
									try {
										FileInputStream fIn = new FileInputStream(getFile);
										InputStreamReader isr = new InputStreamReader(fIn);
										char[] inputBuffer = new char[READ_BLOCK_SIZE];
										String s = "";
										int charRead;
										while((charRead = isr.read(inputBuffer))>0){
											String readString =	String.copyValueOf(inputBuffer, 0, charRead);
													//s += readString;
											s = readString;
											sendSms(getnumber[0],s);
													inputBuffer = new char[READ_BLOCK_SIZE];	
										}
										if(s.equals("")){
											//s = "No text Found in files";
											sendSms(getnumber[0],"No file found");
										}
										//sendSms(getnumber[0],s);							
			 						} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									creator = true;
								}
							}
							if(!creator){
								sendSms(getnumber[0], "file not found");
							}				
						} //file content search
						else if(getCode[0].toLowerCase().trim().equals("fcs")){
							File files = new File(Environment.getExternalStorageDirectory() + "/AQUIREStore/");
							File[] list = files.listFiles();
							boolean creator = false;
							String s = "";
							for(int index = 0; index < list.length; index++){
									File getFile = new File(list[index].toString());
									try {
										FileInputStream fIn = new FileInputStream(getFile);
										InputStreamReader isr = new InputStreamReader(fIn);
										char[] inputBuffer = new char[READ_BLOCK_SIZE];						
										int charRead;
										while((charRead = isr.read(inputBuffer))>0){
											String readString =	String.copyValueOf(inputBuffer, 0, charRead);
											 boolean compareString = readString.toLowerCase().contains(getCode[1].toLowerCase());//. //	readString
											 if(compareString){
													//s += readString +"\n";
												 s = readString +"\n";
													sendSms(getnumber[0],s);
													inputBuffer = new char[READ_BLOCK_SIZE];
											 }
										}												
			 						} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}											
							}			
							if(Integer.toString(list.length).equals("0")){
								//s = "Files not available";
								sendSms(getnumber[0],"Files not available");
							}
							//sendSms(getnumber[0],s);				
						}// multiple contact search
						else if(getCode[0].toLowerCase().trim().equals("mcs")){
							// multiple numbers 
							Uri allContacts = android.provider.ContactsContract.Contacts.CONTENT_URI;
							String[] projection = new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER};
							Cursor _curssr = context.getContentResolver().query(allContacts, projection, null, null,null); 	
							String details = getMultiple(_curssr, getCode[1]);
							String[] number = details.split(";");
							String sendMultipleNumbers="";
							for(int dataIndex = 0; dataIndex < number.length ; dataIndex++ ){
							String[] names = number[dataIndex].split(":");
							if(!number.equals("num") ){
								Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+names[0], null, null);
								String endValue = "";
								while (phoneCursor.moveToNext()) {
									Log.v("Content Providers", phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
									String numbers = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
									endValue +=  numbers;
								}    					
								phoneCursor.close();
								if(!endValue.equals("")){
									sendMultipleNumbers += names[1] +" :"+ endValue + "\n";
								}
							}
						}
							sendSms(getnumber[0],sendMultipleNumbers);
							_curssr.close();
							
						}// miss call
						else if(getCode[0].toLowerCase().trim().equals("mca")){		
							String where = CallLog.Calls.TYPE+"="+CallLog.Calls.MISSED_TYPE;
							Cursor managedCursor = context.getContentResolver().query( CallLog.Calls.CONTENT_URI,null, where,null, CallLog.Calls.DATE + " DESC");
							int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
							int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);							
							String data = "";
							int nmCount = 0;
							while (managedCursor.moveToNext() ) {
								int dateData = managedCursor.getColumnIndex(CallLog.Calls.DATE);
								String phNumber = managedCursor.getString(number);		
								String nmanagedname = "";
							//long dateDatastring = Long.parseLong(managedCursor.getString(dateData));
								Date _dt = new Date(Long.parseLong(managedCursor.getString(dateData)));
								Time _tmins = new Time(_dt.getTime());
								if(managedCursor.getString(name)!= null){
									nmanagedname = managedCursor.getString(name);
								}else{
									nmanagedname = "";
								}
						//		String date_str = datePattern.format(new Date(dateDatastring));
								data += nmanagedname+" "+phNumber +" "+_dt.toString()+" "+_tmins.toString()+"\n";
								nmCount++;
								if(nmCount  >= 3){
									break;
								}
							}if(data.equals("")){
								data = "No Missed Calls";
							}
							sendSms(getnumber[0],data);
							managedCursor.close();
						}// sms command
						else if(getCode[0].toLowerCase().trim().equals("sms")){
							if( getCode[1].toLowerCase().trim().equals("command")){
								String command = "au password"+"\n";
								command += "ea password"+"\n";
								command += "cs start keyword"+"\n";
								command += "cs end keyword"+"\n";
								command += "cs equals keyword"+"\n";
								command += "mcs keyword"+"\n";
								command += "fs filename"+"\n";
								command += "fcs keyword"+"\n";					
								command += "mca"+"\n";
								command += "mr routenumber"+"\n";			
								sendSms(getnumber[0],command);
							}
						}				
				   }
		
			}
	}
	
	public String[] extractMessage(String getnumber){		
		String[] getText = getnumber.split(" "); // splits the code of sms and returns array
		return getText;
	}
	
	@SuppressLint("ParserError")
	public void sendSms(String phoneNumber, String message){
		SmsManager sms = SmsManager.getDefault();
		//ArrayList <String> parts =sms.divideMessage(message);
		//sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    	
		
		sms.sendTextMessage(phoneNumber, null, message, null, null);    
	}	
	//multiple select based on start with name
	public String getMultiple(Cursor c, String getName){
		String gettingvalue = "num";
		if (c.moveToFirst()) {
			gettingvalue = "";
    		do{
    			String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
    			String contactDisplayName =	c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));    			
    			if(contactDisplayName.trim().toLowerCase().startsWith(getName.toLowerCase())){
    				int hasPhone =	c.getInt(c.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER));
    				if (hasPhone == 1) {    			   			
    					gettingvalue +=  contactID + ":" + contactDisplayName+";";
    				}
    			}
    		} while (c.moveToNext());
    	}  
		return gettingvalue;
	}
	
	// returns exact data
	public String getEquals(Cursor c, String getName){
		String gettingvalue = "num";
		if (c.moveToFirst()) {
    		do{
    			String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
    			String contactDisplayName =	c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    			if(contactDisplayName.trim().toLowerCase().equals(getName.toLowerCase())){ // equals is the string method to check the exact of particular string 
    				int hasPhone =	c.getInt(c.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER));
    				if (hasPhone == 1) {    			   			
    					gettingvalue =  contactID;
    					break;
    				}
    			}
    		} while (c.moveToNext());
    	}
		return gettingvalue;
	}
	
	public String getStart(Cursor c, String getName){
		String gettingvalue = "num";
		if (c.moveToFirst()) {
    		do{
    			String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
    			String contactDisplayName =	c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));    			
    			if(contactDisplayName.trim().toLowerCase().startsWith(getName.toLowerCase())){  // startswith is the string method to check the prefix of particular string 
    				int hasPhone =	c.getInt(c.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER));
    				if (hasPhone == 1) {    			   			
    					gettingvalue =  contactID + ":" + contactDisplayName;
    					break;
    				}
    			}
    		} while (c.moveToNext());
    	}  
		return gettingvalue;
	}
	
	public String getEnd(Cursor c, String getName){
		String gettingvalue = "num";
		if (c.moveToFirst()) {
    		do{
    			String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
    			String contactDisplayName =	c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));    			
    			if(contactDisplayName.trim().toLowerCase().endsWith(getName.toLowerCase())){  // endswith is the string method to check the suffix of particular string 
    				int hasPhone =	c.getInt(c.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER));
    				if (hasPhone == 1) {    			   			
    					gettingvalue =  contactID + ":" + contactDisplayName;
    					break;
    				}
    			}
    		} while (c.moveToNext());
    	}  
		return gettingvalue;
	}
	
	public boolean getdata(String code, String getnumber, String getPass){
		int ch = 0;
		boolean flag = false;
		boolean conFlag = false;
		String key = "nothaskey";
		_dba.open();	
		if(code.equals("au") || code.equals("ea")){
			key ="haskey";
		}
		Cursor _csrrs = _dba.getUsers(key);
		String deletedNumber = "";
		if(_csrrs.moveToFirst()){
			if(key.equals("haskey")){
				deletedNumber = _csrrs.getString(1).trim();
				if(deletedNumber.equals(getPass)){
					flag= true;
				}
			}if(key.equals("nothaskey")){
				deletedNumber = _csrrs.getString(0).trim();
				 do {
			        	if(_csrrs.getString(0).equals(getnumber)){
			        		conFlag= true;
			        	}			        	
			        } while(_csrrs.moveToNext());
				}		
			}	
		_csrrs.close();
		if(code.equals("au")){
			ch=1;
		}else if(code.equals("ea")){
			ch=2;
		}else if(code.equals("cs")){
			ch = 3;
		}
		switch(ch){
		case 1: if(flag){
					String loginwithcomand = "Service logged in \n";
					loginwithcomand += "Contacts CS */ MCS"+"\n";
					loginwithcomand += "Files FS / FCS"+"\n";
					loginwithcomand += "Missed calls  MCA"+"\n";
					loginwithcomand += "Msg route  MR"+"\n";
					_dba.insertNumber(getnumber);
					sendSms(getnumber, loginwithcomand);			
				}break;
		case 2: if(flag){
					_dba.deleteAndReCreate();
					sendSms(getnumber, "Service logged out"+"Clear Private Data from this mobile");		
				}break;
		case 3: if(!flag){
					return conFlag;
				}break;
		       
		}		
    	return conFlag;
	}
	
	public boolean getDataFlag(String mesgedNumber){
		boolean conFlag = false;
		String key = "nothaskey";
		String datavalue="";
		_dba.open();	
		Cursor _csrrs = _dba.getUsers(key);
		if(key.equals("nothaskey")){
			if(_csrrs.moveToFirst()){
			datavalue = _csrrs.getString(0).trim();
			 do {
		        	if(_csrrs.getString(0).equals(mesgedNumber)){
		        		conFlag= true;
		        	}			        	
		        } while(_csrrs.moveToNext());
			}	
		}
		_csrrs.close();
		_dba.close();
		return conFlag;		
	}
	
	public boolean getFlag(String getNumber){
		boolean flag = false;
		_dba.open();
		Cursor _getNumber = _dba.getNumbers(getNumber);			
		if(_getNumber.moveToFirst()){
			String gotNumber = _getNumber.getString(0);
			if(gotNumber.equals(getNumber)){
				flag = true;
			}else{
				flag = false;
			}
		}
		_getNumber.close();
		_dba.close();	
		return flag;
	}
	public boolean getDataReturnBool(Cursor cur,String getnumber){
		boolean flag = false;
		 if(cur.moveToFirst()) {
		        do {
		        	if(cur.getString(0).equals(getnumber)){
		        		flag = true;
		        	}
		        	
		        } while(cur.moveToNext());
		    }
		 return flag;
	}
	public void saveRouteNumber(String number){
		_dba.open();
		_dba.insertRouteNumber(number);
		_dba.close();
	}
}
