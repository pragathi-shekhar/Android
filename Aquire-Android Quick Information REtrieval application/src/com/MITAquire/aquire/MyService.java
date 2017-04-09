package com.MITAquire.aquire;


import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service{
	private SMSReceiver mSMSreceiver;
    private IntentFilter mIntentFilter;      
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	 public void onCreate(){
	        super.onCreate();
	        //SMS event receiver
	       mSMSreceiver = new SMSReceiver(); // object to call broadcast reciever 
	       mIntentFilter = new IntentFilter(); 
	       mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED"); // event on sms recieved
	       registerReceiver(mSMSreceiver, mIntentFilter);
	  }
	 
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}
	
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mSMSreceiver);
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

}