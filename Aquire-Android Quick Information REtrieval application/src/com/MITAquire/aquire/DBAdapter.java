package com.MITAquire.aquire;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


@SuppressLint("ParserError")
public class DBAdapter {
	public static final String KEY_ROWID = "_id";	//  column id for user table
	public static final String KEY_EMAIL = "email";  // column email for user table
	public static final String KEY_PIN_PASS = "pin"; // column pin for user table
	public static final String KEY_QUESTION = "question"; // column pin for user table
	public static final String KEY_ANSWER = "answer"; // column pin for user table
	public static final String ROUTE_NUMBER = "rnumber"; // column rnumber for roting table
	private static final int DATABASE_VERSION = 1;  
	private static final String DATABASE_NAME = "Aquire";  // database name
	private static final String DATABASE_TABLE = "userTable";
	private static final String DATABASE_NUMBER_TABLE = "safeSender";
	private static final String DATABASE_ROUTE_NUMBER_TABLE = "routesafeSender";
	public static final String KEY_PHONE_NUMBER = "numberfield";
	private static final String DATABASE_CREATE_NUMBER_TABLE = "CREATE TABLE IF NOT EXISTS safeSender(_id integer primary key autoincrement, numberfield text not null);";
	private static final String DATABASE_CREATE_USERS = "CREATE TABLE IF NOT EXISTS userTable(_id integer primary key autoincrement, email text not null, pin text not null, question text not null, answer text not null);";
	private static final String DATABASE_CREATE_ROUTE_NUMBER_TABLE = "CREATE TABLE IF NOT EXISTS routesafeSender(_id integer primary key autoincrement, rnumber text not null);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx){
	   this.context = ctx;
	   DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		DatabaseHelper(Context context)
		{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
					db.execSQL(DATABASE_CREATE_USERS);
					db.execSQL(DATABASE_CREATE_NUMBER_TABLE);	
					db.execSQL(DATABASE_CREATE_ROUTE_NUMBER_TABLE);	
					} catch (SQLException e) {
						e.printStackTrace();
				}			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS userTable");
			db.execSQL("DROP TABLE IF EXISTS safeSender");
			db.execSQL("DROP TABLE IF EXISTS routesafeSender");
			onCreate(db);
		}	
	}
	
	public DBAdapter open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		DBHelper.close();
	}
	
	public long insertAuth(String email, String pin, String question,String answer){
	   ContentValues initialValues = new ContentValues();
	   initialValues.put(KEY_EMAIL, email);
	   initialValues.put(KEY_PIN_PASS, pin);
	   initialValues.put(KEY_QUESTION, question);
	   initialValues.put(KEY_ANSWER, answer);
	   return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public long insertNumber(String number){
		   ContentValues initialValues = new ContentValues();
		   initialValues.put(KEY_PHONE_NUMBER, number);		   
		   return db.insert(DATABASE_NUMBER_TABLE, null, initialValues);
	}
	
	public long insertRouteNumber(String number){
		   ContentValues initialValues = new ContentValues();
		   initialValues.put(ROUTE_NUMBER, number);		   
		   return db.insert(DATABASE_ROUTE_NUMBER_TABLE, null, initialValues);
	}
	
	public boolean deleteContact(String numbers){
	   return db.delete(DATABASE_NUMBER_TABLE, KEY_PHONE_NUMBER + "=" + numbers, null) > 0;
	}
	
	public void deleteContacts(){
		try{
		db.execSQL("DROP TABLE IF EXISTS safeSender");
		}catch(Exception ex){
			
		}		
	}
	
	public Cursor getRouteNumbers(){
	     return db.query(DATABASE_ROUTE_NUMBER_TABLE, new String[] { ROUTE_NUMBER}, null, null, null, null, null);
	}
	
	public Cursor getNumbers(String number){
		return db.query(DATABASE_NUMBER_TABLE, new String[] { KEY_PHONE_NUMBER}, KEY_PHONE_NUMBER +"="+ number, null, null, null, null);
	}
	
	public Cursor getAllNumbers(){
		return db.query(DATABASE_NUMBER_TABLE, new String[] { KEY_PHONE_NUMBER}, null, null, null, null, null);
	}
	
	public Cursor userDetails(){
		return db.query(DATABASE_TABLE, new String[] { KEY_EMAIL}, null, null, null, null, null);
	}
	public Cursor routeNumberDetails(){
		return db.query(DATABASE_ROUTE_NUMBER_TABLE, new String[] { ROUTE_NUMBER}, null, null, null, null, null);
	}
	public Cursor safeSenderDetails(){
		return db.query(DATABASE_NUMBER_TABLE, new String[] { KEY_PHONE_NUMBER}, null, null, null, null, null);
	}
	
	public Cursor getUsers(String num){
		Cursor _csr;
		if(num.equals("haskey")){
		_csr = db.query(DATABASE_TABLE, new String[] { KEY_EMAIL, KEY_PIN_PASS}, null, null, null, null, null);
		}else{
		_csr = db.query(DATABASE_NUMBER_TABLE, new String[] { KEY_PHONE_NUMBER}, null, null, null, null, null);
		}
		return _csr;
	}
	public Cursor getQuestion(){
		Cursor _cursor = db.query(DATABASE_TABLE, new String[] { KEY_EMAIL, KEY_PIN_PASS, KEY_QUESTION, KEY_ANSWER}, null, null, null, null, null);;
		return _cursor;
	}
	// get users to update;
	public void getUserUpdate(String email, String pin,String latestPin, String question,String answer){
		Cursor _csr;
		_csr = db.query(DATABASE_TABLE, new String[] { KEY_EMAIL, KEY_PIN_PASS, KEY_QUESTION, KEY_ANSWER}, null, null, null, null, null);
		if(_csr.moveToFirst()){
			String mailId =_csr.getString(0);
			String passwords = _csr.getString(1);
			String questions = _csr.getString(2);
			String answers = _csr.getString(3);
			//String answers =_csr.getString(4);
			if(_csr.getString(1).equals(pin)){
				updateUserPassword(_csr.getString(0), latestPin, questions, answers);
			}
		}
	}
	public void updateUserPassword(String rowId,String password,String question, String answer){
		try{
			db.execSQL("UPDATE userTable SET email='"+rowId+"', pin='"+password+"',question='"+question+"',answer='" +answer+ "' WHERE email='"+rowId+"'");
			}catch(Exception ex){				
		}		
	}
	
	public void deleteAndReCreate(){
		try{
			db.execSQL("DROP TABLE IF EXISTS safeSender");
			db.execSQL("DROP TABLE IF EXISTS routesafeSender");
			db.execSQL(DATABASE_CREATE_NUMBER_TABLE);	
			db.execSQL(DATABASE_CREATE_ROUTE_NUMBER_TABLE);	
			}catch(Exception ex){				
		}		
	}
	
	
/*	public boolean updateUsers(long rowId, String name, String email){
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_EMAIL, email);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}*/	
}
