package com.example.drawtests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ScoreDbAdapter 
{
	public static final String KEY_SCORE = "score";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "ScoreDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_NAME = "MyDatabase";
    private static final String DATABASE_TABLE = "scores";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    private static final String DATABASE_CREATE =
    		"CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +          
                    KEY_SCORE + " INTEGER NOT NULL);";
                    

   

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
            Log.d(DATABASE_TABLE, "Creating Database");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS scores");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public ScoreDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the scores database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ScoreDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        Log.d(DATABASE_TABLE, "Open function");
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new score using the score provided. If the score is
     * successfully created return the new rowId for that score, otherwise return
     * a -1 to indicate failure.
     * 
     * @param score value of the score
     * @param player_name of player who scored the score
     * @return rowId or -1 if failed
     */
    public long createScore(Long score) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SCORE, score);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the score with the given rowId
     * 
     * @param rowId id of score to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteScore(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all scores in the database
     * 
     * @return Cursor over all scores
     */
    public Cursor fetchAllScores() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SCORE,
                }, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the score that matches the given rowId
     * 
     * @param rowId id of score to retrieve
     * @return Cursor positioned to matching score, if found
     * @throws SQLException if score could not be found/retrieved
     */
    public Cursor fetchScore(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_SCORE}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the score using the details provided. The score to be updated is
     * specified using the rowId, and it is altered to use the score and player name
     * values passed in
     * 
     * @param rowId id of score to update
     * @param score value to set score to
     * @param player_name value to set player name to
     * @return true if the score was successfully updated, false otherwise
     */
    public boolean updateScore(long rowId, Long score) {
        ContentValues args = new ContentValues();
        args.put(KEY_SCORE, score);
        
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
