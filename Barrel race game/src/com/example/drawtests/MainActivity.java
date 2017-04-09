package com.example.drawtests;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.HttpVersion;

import com.example.drawtests.R.color;
import com.example.drawtests.R.dimen;
import com.example.drawtests.R.drawable;
import com.example.drawtests.R.id;
import com.example.drawtests.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.Camera.Size;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements SensorEventListener {
	static Sensor accelerometerSensor;
	static SensorManager sensorManager;
	public static Vibrator touchedCircle;
	public static float xStart= 640;
	public static float yStart= 560;
	public static float circleRadius;
	public static float accelerometer_reading;
	
	
	 
	static float height;
	static float width;
	static float circleACenterX, circleACenterY, circleBCenterX, circleBCenterY, circleCCenterX, circleCCenterY;
	static boolean AnotInCircle=false, BnotInCircle= false, CnotInCircle=false, outsideStadium = false;
	static boolean AcenterCircled = false, BcenterCircled=false, CcenterCircled=false;
	static Vector<Long> circleACoordinates=new Vector<Long>();
	static Vector<Long> circleBCoordinates= new Vector<Long>();
	static Vector<Long> circleCCoordinates= new Vector<Long>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
		touchedCircle = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		SharedPreferences sharedPref = getSharedPreferences("key", 0);
		circleRadius = sharedPref.getFloat("Radius", 0f);
		accelerometer_reading = sharedPref.getFloat("Accelerometer_Reading", 0f);
		Log.d("in main->acc, rad", accelerometer_reading + ", " + circleRadius);
		//sharedPref.getFloat("Radius",circleRadius);
		Log.d("Circle Radius Value", Float.toString(circleRadius));
		if(circleRadius > 0)
		{
			circleRadius = circleRadius * 15;
		}
		else
		{
			circleRadius = 10;
		}
		if(accelerometer_reading>0)
			accelerometer_reading=Math.round(accelerometer_reading*2.5);
		else
			accelerometer_reading=2;
		Log.d("in main2->acc, rad", accelerometer_reading + ", " + circleRadius);
		
		Log.d("Menu Option Selected", Float.toString(circleRadius));
		Bundle extras = getIntent().getExtras();
		if(extras!=null)
			{
			sensorManager.unregisterListener(this);
			xStart=600;
			yStart=560;
			AcenterCircled=false;
			BcenterCircled=false;
			CcenterCircled=false;
			circleACoordinates.clear();
			circleBCoordinates.clear();
			circleCCoordinates.clear();
			}
        setContentView(new SampleView(this));
		
	}

private static class SampleView extends View{

	Display display;
	Canvas canvas = new Canvas();
	boolean checkCoordinateVector=false;
	boolean touched=false;
	String timer;
	long count = 0L;
	long highScore = 0L;
	SharedPreferences settings;
	private ScoreDbAdapter highScoreAccessor; 
	private Long mRowId;
	
	public SampleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		setFocusable(true);
		
		highScoreAccessor = new ScoreDbAdapter(context);
		highScoreAccessor.open();
		
		Log.d("SampleView", "Something wrong here");

		
		settings = context.getSharedPreferences("key", 0);

		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("Score", count);		
		editor.putLong("HighScore", highScore);
		editor.commit();
	}
	
	
	

	@SuppressLint("DrawAllocation")
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(final Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		height = display.getHeight();
		width = display.getWidth();
		circleACenterX= width/2;
		circleACenterY = height/3;
		circleBCenterX=width/4;
		circleBCenterY=height*0.66f;
		circleCCenterX=width*0.75f;
		circleCCenterY=height*0.66f;
		canvas.drawColor(Color.WHITE);
		Paint paintA = new Paint();
		Paint paintB = new Paint();
		Paint paintC = new Paint();
		Paint textPaint = new Paint();
		Paint textScorePaint = new Paint();
		Paint fillColor = new Paint();
		Paint stadiumPaint = new Paint();
		Paint startEndPaint = new Paint();
		
	paintA.setAntiAlias(true);
	paintA.setColor(Color.rgb(230, 138, 0));
	paintA.setStyle(Style.FILL_AND_STROKE);
	paintA.setStrokeWidth(5.0f);
	Bitmap image = BitmapFactory.decodeResource(getResources(), drawable.grass_background);
//	canvas.drawBitmap(image, 0, 0, null);
	Bitmap grass_tufts= BitmapFactory.decodeResource(getResources(), drawable.grass_tufts);
	Bitmap stone_brown= BitmapFactory.decodeResource(getResources(), drawable.stone_cartoon);
	Bitmap stone_black= BitmapFactory.decodeResource(getResources(), drawable.stone_cartoon_2);
	canvas.drawBitmap(grass_tufts, 100, 100, null);
	canvas.drawBitmap(grass_tufts, 300, 300, null);
	canvas.drawBitmap(grass_tufts, 500, 300, null);
	canvas.drawBitmap(grass_tufts, 500, 600, null);
	canvas.drawBitmap(grass_tufts, 1000, 630, null);
	canvas.drawBitmap(grass_tufts, 750, 270, null);
	canvas.drawBitmap(grass_tufts, 1100, 120, null);
	canvas.drawBitmap(grass_tufts, 410, 60, null);
	
	canvas.drawBitmap(stone_brown, 200, 200, null);
	canvas.drawBitmap(stone_brown, 960, 200, null);
	canvas.drawBitmap(stone_black, 640, 400, null);
	canvas.drawBitmap(stone_black, 900, 300, null);
	canvas.drawBitmap(stone_black, 150, 640, null);
	canvas.drawBitmap(stone_black, 120, 430, null);
	canvas.drawBitmap(stone_black, 560, 120, null);
	canvas.drawBitmap(stone_brown, 700, 160, null);
	canvas.drawBitmap(stone_brown, 740, 640, null);
	canvas.drawBitmap(stone_brown, 1100, 500, null);
	canvas.drawBitmap(stone_black, 700, 50, null);
	
	paintB.setAntiAlias(true);
	paintB.setColor(Color.rgb(204, 153, 51));
	paintB.setStyle(Style.FILL_AND_STROKE);
	paintB.setStrokeWidth(5.0f);

	paintC.setAntiAlias(true);
	paintC.setColor(Color.rgb(204, 153, 51));
	paintC.setStyle(Style.FILL_AND_STROKE);
	paintC.setStrokeWidth(5.0f);

	textPaint.setAntiAlias(true);
	textPaint.setColor(Color.RED);
	textPaint.setStyle(Style.STROKE);
	textPaint.setTextSize(15f);
	
	textScorePaint.setAntiAlias(true);
	textScorePaint.setColor(Color.RED);
	textScorePaint.setTextSize(45.0f);
	textScorePaint.setStrokeWidth(2.0f);
	textScorePaint.setStyle(Paint.Style.FILL_AND_STROKE);
	textScorePaint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
    
	
	stadiumPaint.setAntiAlias(true);
	stadiumPaint.setColor(Color.rgb(102, 51, 0));
	stadiumPaint.setStyle(Style.STROKE);
	stadiumPaint.setTextSize(20f);
	stadiumPaint.setStrokeWidth(20f);
	
	startEndPaint.setAntiAlias(true);
	startEndPaint.setColor(Color.RED);
	startEndPaint.setStyle(Style.STROKE);
	startEndPaint.setStrokeWidth(3F);
	
	fillColor.setAntiAlias(true);
	fillColor.setColor(Color.BLACK);
	fillColor.setStyle(Style.STROKE);
	fillColor.setStrokeWidth(25.0f);
	
	canvas.drawRect(0+5, 0+5, width-5, height-5, stadiumPaint);
	canvas.drawRect(circleACenterX-50, circleBCenterY+20, circleACenterX+50, circleBCenterY+120, startEndPaint);
	canvas.drawCircle(circleACenterX, circleACenterY, circleRadius, paintA);
	canvas.drawCircle(circleBCenterX, circleBCenterY, circleRadius, paintB);
	canvas.drawCircle(circleCCenterX, circleCCenterY, circleRadius, paintC);
	
	if(touched)	//game begins from this method, user needs to touch the screen to let the point move
	{				
		textPaint.setStyle(Style.FILL_AND_STROKE);
	//	textPaint.setColor(Color.argb(0, 255, 255, 255));
	//	canvas.drawText(Math.round(Math.toDegrees(Math.atan2((circleACenterY-yStart), (circleACenterX-xStart)))) + "", 200, 200, textPaint);
		canvas.drawText(accelerometer_reading + ", " + circleRadius/15, 200, 200, textPaint);
		canvas.drawText(Math.round(Math.toDegrees(Math.atan2((circleBCenterY-yStart), (circleBCenterX-xStart)))) + "", 200, 250, textPaint);
		canvas.drawText(Math.round(Math.toDegrees(Math.atan2((circleCCenterY-yStart), (circleCCenterX-xStart)))) + "", 200, 300, textPaint);
//		canvas.drawBitmap(horse_image, xStart-25, yStart-25, null);
		canvas.drawPoint(xStart, yStart, fillColor);

		//lxn130730
		//Timer to help in counting.
		new CountDownTimer(60000, 1000) {
		     public void onTick(long millisUntilFinished) {
		         timer = String.valueOf(millisUntilFinished / 1000);
		         count ++;
		         invalidate(); // Force the View to redraw
		     }

		     public void onFinish() {}
		 }.start();
		//Draw the count text
		canvas.drawText(Long.toString(count), 30, 55, textScorePaint);        		       	
		
	}
	
	if(touched && AnotInCircle && !AcenterCircled)
	{
	//	canvas.drawPoint(xStart, yStart, fillColor);
		textPaint.setColor(Color.BLACK);
		touchedCircle.cancel();
		long x=Math.round(Math.toDegrees(Math.atan2((circleACenterY-yStart), (circleACenterX-xStart))));
		circleACoordinates.addElement(x);
		if((circlesBarrel(circleACoordinates) ) && !AcenterCircled)
			{
			paintA.setColor(Color.GREEN);
			canvas.drawCircle(circleACenterX, circleACenterY, circleRadius, paintA);
			AcenterCircled=true;
			}
		this.invalidate();
		}
	/****************************/
	if(touched && BnotInCircle && !BcenterCircled)
	{
	//	canvas.drawPoint(xStart, yStart, fillColor);
		textPaint.setColor(Color.BLACK);
		touchedCircle.cancel();
		long x=Math.round(Math.toDegrees(Math.atan2((circleBCenterY-yStart), (circleBCenterX-xStart))));
		circleBCoordinates.addElement(x);
		if((circlesBarrel(circleBCoordinates) ) && !BcenterCircled)
			{
			paintB.setColor(Color.GREEN);
			canvas.drawCircle(circleBCenterX, circleBCenterY, circleRadius, paintB);
			BcenterCircled=true;
			}
		this.invalidate();
	}
	if(touched && CnotInCircle && !CcenterCircled)
	{
	//	canvas.drawPoint(xStart, yStart, fillColor);
		textPaint.setColor(Color.BLACK);
		touchedCircle.cancel();
		long x=Math.round(Math.toDegrees(Math.atan2((circleCCenterY-yStart), (circleCCenterX-xStart))));
		circleCCoordinates.addElement(x);
		if((circlesBarrel(circleCCoordinates) ) && !CcenterCircled)
			{
			paintB.setColor(Color.GREEN);
			canvas.drawCircle(circleCCenterX, circleCCenterY, circleRadius, paintC);
			CcenterCircled=true;
			}
		this.invalidate();
	}
	if(outsideStadium)
	{
		textPaint.setColor(Color.BLACK);
		touchedCircle.vibrate(100);
		this.invalidate();
	}
	if(AcenterCircled)
	{
		paintA.setColor(Color.GREEN);
		canvas.drawCircle(circleACenterX, circleACenterY, circleRadius, paintA);
	}
	if(BcenterCircled)
	{
		paintB.setColor(Color.GREEN);
		canvas.drawCircle(circleBCenterX, circleBCenterY, circleRadius, paintB);
	}
	if(CcenterCircled)
	{
		paintC.setColor(Color.GREEN);
		canvas.drawCircle(circleCCenterX, circleCCenterY, circleRadius, paintC);
	}
	if((float) Math.sqrt(Math.pow(xStart-circleACenterX, 2) + Math.pow(yStart-circleACenterY, 2))<=circleRadius+1 || (float) Math.sqrt(Math.pow(xStart-circleBCenterX, 2) + Math.pow(yStart-circleBCenterY, 2))<=circleRadius+1 || (float) Math.sqrt(Math.pow(xStart-circleCCenterX, 2) + Math.pow(yStart-circleCCenterY, 2))<=circleRadius+1)	//point is on or inside the circle here
	{
		textPaint.setColor(Color.BLACK);
		touchedCircle.vibrate(100);
		Context cx= getContext();
		Intent n=new Intent(cx, MainScreenActivity.class);
		n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		n.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.d("Timer value on end", Long.toString(count));
		n.putExtra("count", count);
		
		if(highScore<count)
		{
			highScore = count;	
			long id =  highScoreAccessor.createScore(Long.valueOf(highScore));
	 		if(id > 0){
	    		mRowId = id;
	    	}else{
	    		Log.d("submitscore","failed to create note");
	    	}
		}
		
		n.putExtra("highscore", highScore);
		n.putExtra("mRowId", mRowId);
		cx.startActivity(n);
		
		//	this.invalidate();
	}	
	//perform checking of start and end points here
	if(AcenterCircled && BcenterCircled && CcenterCircled && xStart>(circleACenterX-20) && xStart<(circleACenterX+20) && yStart>(circleBCenterY+20) && yStart<(circleBCenterY+60))
		{
			canvas.drawText("game over", 100, 100, textPaint);
			Context cx = getContext();
			Intent gameOverIntent = new Intent(cx, MainScreenActivity.class);
			gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			cx.startActivity(gameOverIntent);
		}
	this.invalidate();
	}
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN)// && event.getRawX()>=100 && event.getRawX() <=200 && event.getRawY()>=100 && event.getRawY()<=200)
			{
				touched = true;
			}
		return super.onTouchEvent(event);
	}
	public boolean circlesBarrel(Vector<Long> coordinates){
		if((Long)coordinates.firstElement() == (Long)coordinates.lastElement() || ((Long)coordinates.firstElement() >= ((Long)coordinates.lastElement()-30) && (Long)coordinates.firstElement() <= ((Long)coordinates.lastElement()+30)))
			checkCoordinateVector=true;
		else 
			checkCoordinateVector = false;
		
		boolean check0= false, check90 = false, check180= false, checkN90= false;
		
		for(int i=-20; i<=20; i++){
			if(coordinates.contains(0L + i))
				{
				check0 = true;
				break;
				}
			else
				check0=false;
		}
		for(int i=-20; i<=20; i++){
			if(coordinates.contains(90L + i))
			{
			check90 = true;
			break;
			}
		else
			check90=false;
		}
		for(int i=-20; i<=20; i++){
			if(coordinates.contains(180L + i))
			{
			check180 = true;
			break;
			}
		else
			check180=false;
		}
		for(int i=-20; i<=20; i++){
			if(coordinates.contains(-90L + i))
			{
			checkN90 = true;
			break;
			}
		else
			checkN90=false;
		}
		
		
	//	if((coordinates.contains(0L) || coordinates.contains(0L+1) || coordinates.contains(0L-1) || coordinates.contains(0L+2) || coordinates.contains(0L-2)) && (coordinates.contains(90L) || coordinates.contains(90L+1) || coordinates.contains(90L-1) || coordinates.contains(90L+2) || coordinates.contains(90L-2)) && (coordinates.contains(180L) || coordinates.contains(180L+1) || coordinates.contains(180L-1) || coordinates.contains(180L+2) || coordinates.contains(180L-2)) && (coordinates.contains(-90L) || coordinates.contains(-90L+1) || coordinates.contains(-90L-1) || coordinates.contains(-90L+2) || coordinates.contains(-90L-2)) && checkCoordinateVector)
		if(check0 && check90 && check180 && checkN90 && checkCoordinateVector)
				return true;
		else
			return false;
	}	
}
@Override
public void onAccuracyChanged(Sensor arg0, int arg1) {
	// TODO Auto-generated method stub
	
}
@Override
public void onSensorChanged(SensorEvent arg0) {
	// TODO Auto-generated method stub
	
/*1*/if(arg0.values[0]<0 && arg0.values[1] >=1 && xStart<width && yStart>0 && yStart<height)
	{
		xStart+=accelerometer_reading;
		yStart-=accelerometer_reading;
	}
/*2*/if(arg0.values[0]>=1 && arg0.values[1]>=1 && xStart>0 && yStart>0 && yStart<height)
	{
		xStart+=accelerometer_reading;
		yStart+=accelerometer_reading;
	}
/*3*/if(arg0.values[0]>=1 && arg0.values[1]<0 && yStart<height && xStart>0 && yStart>0)
	{
		xStart-=accelerometer_reading;
		yStart+=accelerometer_reading;
	}
/*4*/if(arg0.values[0]<0 && arg0.values[1]<0 && yStart>0 && xStart>0 && xStart<width)
	{
		xStart-=accelerometer_reading;
		yStart-=accelerometer_reading;
	}
/*4,1*/if(arg0.values[0]<0 && arg0.values[1]<1 && arg0.values[1]>=0 && xStart>0 && xStart<width && yStart>0)
	{
		yStart-=accelerometer_reading;
		xStart=xStart;
	}
/*1,2*/if(arg0.values[0]<1 && arg0.values[0]>=0 && arg0.values[1]>0 && xStart>0 && xStart<width && yStart>0)
	{
		xStart+=accelerometer_reading;
		yStart=yStart;
	}
/*2,3*/if(arg0.values[0]>0 && arg0.values[1]<1 && arg0.values[1]>=0 && xStart<width && yStart>0 && yStart<height)
	{
		yStart+=accelerometer_reading;
		xStart=xStart;
	}
/*3,4*/if(arg0.values[0]<1 && arg0.values[0] >=0 && arg0.values[1]<0 && xStart>0 && yStart>0 && yStart<height)
	{
		xStart-=accelerometer_reading;
		yStart=yStart;
	}
if(yStart<=5)
	yStart+=accelerometer_reading;
if(yStart>=height-5)
	yStart-=accelerometer_reading;
if(xyDistance(xStart, yStart, circleACenterX, circleACenterY)>circleRadius+1)// && xyDistance(xStart, yStart, circleACenterX, circleACenterY)<circleRadius+100)
		{
		AnotInCircle=true;
	}
	else
		AnotInCircle=false;
	if(xyDistance(xStart, yStart, circleBCenterX, circleBCenterY)>circleRadius+1 )//&& xyDistance(xStart, yStart, circleBCenterX, circleBCenterY)<circleRadius+100)
	{
		BnotInCircle=true;
	}
	else
		BnotInCircle=false;
	if(xyDistance(xStart, yStart, circleCCenterX, circleCCenterY)>circleRadius+1)// && xyDistance(xStart, yStart, circleCCenterX, circleCCenterY)<circleRadius+100)
	{
		CnotInCircle=true;
	}
	else
		CnotInCircle=false;

	
	if(xStart<=10 || xStart >= width-10 || yStart <=10 || yStart >=height-10)
		outsideStadium = true;
	else
		outsideStadium= false;
	
}

public float xyDistance(float xStart, float yStart, float circleCenterX, float circleCenterY){
	return (float) Math.sqrt(Math.pow(xStart-circleCenterX, 2) + Math.pow(yStart-circleCenterY, 2));
}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	sensorManager.unregisterListener(this);
}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
	
}
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	sensorManager.unregisterListener(this);
	Intent x= new Intent(getApplicationContext(), MainScreenActivity.class);
	x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	x.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	x.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	x.putExtra("GameOver", true);
	startActivity(x);
	this.finish();
}
}