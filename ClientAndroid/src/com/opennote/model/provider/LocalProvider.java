package com.opennote.model.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.opennote.model.provider.LocalContact.LocalNotes;

public class LocalProvider extends ContentProvider {
	
	final String TAG = getClass().getSimpleName();
	
	private static final String TABLE_LOCAL_NOTES = "local_notes";
	
	private static final String DB_NAME = TABLE_LOCAL_NOTES + ".db";
	private static final int DB_VERSION = 1;
	
	private static final UriMatcher sUriMatcher;
	
	private static final int PATH_ROOT = 0;
	private static final int PATH_TWEETS = 1;
	
	static {
		sUriMatcher = new UriMatcher(PATH_ROOT);
		sUriMatcher.addURI(LocalContact.AUTHORITY, LocalContact.LocalNotes.CONTENT_PATH, PATH_TWEETS); 
	}
	
	private DatabaseHeloper mDatabaseHelper;
	
	class DatabaseHeloper extends SQLiteOpenHelper {

		public DatabaseHeloper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = 
				"create table " + TABLE_LOCAL_NOTES + " (" + 
					LocalNotes._ID + " integer primary key autoincrement, " +
					LocalNotes.TITLE + " text, " +
					LocalNotes.BODY + " text, " +
					LocalNotes.DATE + " text, " +
					LocalNotes.COLOR + " text " +
				")";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHeloper(getContext(), DB_NAME, null, DB_VERSION);
		return true;
	}
	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS: {
			Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_LOCAL_NOTES, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), LocalContact.LocalNotes.CONTENT_URI);
			return cursor;
		}
		default:
			return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS:
			return LocalContact.LocalNotes.CONTENT_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS: {
			mDatabaseHelper.getWritableDatabase().insert(TABLE_LOCAL_NOTES, null, values);
			getContext().getContentResolver().notifyChange(LocalContact.LocalNotes.CONTENT_URI, null);
		}
		default:
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS:
			return mDatabaseHelper.getWritableDatabase().delete(TABLE_LOCAL_NOTES, selection, selectionArgs);
		default:
			return 0;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PATH_TWEETS:
			return mDatabaseHelper.getWritableDatabase().update(TABLE_LOCAL_NOTES, values, selection, selectionArgs);
		default:
			return 0;
		}
	}

}
