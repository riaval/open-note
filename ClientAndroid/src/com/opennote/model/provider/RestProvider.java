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

import com.opennote.model.provider.RestContact.Group;
import com.opennote.model.provider.RestContact.Note;

public class RestProvider extends ContentProvider {

	final String TAG = getClass().getSimpleName();
	
	private static final String TABLE_GROUPS = "groups";
	private static final String TABLE_NOTES = "notes";
	
	private static final String DB_NAME = TABLE_GROUPS + ".db";
	private static final int DB_VERSION = 1;
	
	private static final UriMatcher sUriMatcher;
	
	private static final int PATH_ROOT = 0;
	private static final int PATH_GROUPS = 1;
	private static final int PATH_NOTES = 2;
	
	static {
		sUriMatcher = new UriMatcher(PATH_ROOT);
		sUriMatcher.addURI(RestContact.AUTHORITY, RestContact.Group.CONTENT_PATH, PATH_GROUPS);
		sUriMatcher.addURI(RestContact.AUTHORITY, RestContact.Note.CONTENT_PATH, PATH_NOTES);
	}
	
	private DatabaseHeloper mDatabaseHelper;
	
	class DatabaseHeloper extends SQLiteOpenHelper {

		public DatabaseHeloper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = 
				"create table " + TABLE_GROUPS + " (" + 
						Group._ID + " integer primary key autoincrement, " +
						Group.SLUG + " text, " +
						Group.NAME + " text " +
				")";
			db.execSQL(sql);
			sql = 
				"create table " + TABLE_NOTES + " (" + 
						Note._ID + " integer primary key autoincrement, " +
						Note.TITLE + " text, " +
						Note.BODY + " text, " +
						Note.DATE + " text, " +
						Note.USER + " text, " +
						Note.GROUP + " text " +
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
		case PATH_GROUPS: {
			Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_GROUPS, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), RestContact.Group.CONTENT_URI);
			return cursor;
		}
		case PATH_NOTES: {
			Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_NOTES, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), RestContact.Note.CONTENT_URI);
			return cursor;
		}
		default:
			return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case PATH_GROUPS:
			return RestContact.Group.CONTENT_TYPE;
		case PATH_NOTES: 
			return RestContact.Note.CONTENT_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (sUriMatcher.match(uri)) {
		case PATH_GROUPS: {
			mDatabaseHelper.getWritableDatabase().insert(TABLE_GROUPS, null, values);
			getContext().getContentResolver().notifyChange(RestContact.Group.CONTENT_URI, null);
			return null;
		}
		case PATH_NOTES: {
			System.out.println(PATH_NOTES);
			System.out.println(sUriMatcher.match(uri));
			mDatabaseHelper.getWritableDatabase().insert(TABLE_NOTES, null, values);
			getContext().getContentResolver().notifyChange(RestContact.Note.CONTENT_URI, null);
			return null;
		}
		default:
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PATH_GROUPS:
			return mDatabaseHelper.getWritableDatabase().delete(TABLE_GROUPS, selection, selectionArgs);
		case PATH_NOTES: 
			return mDatabaseHelper.getWritableDatabase().delete(TABLE_NOTES, selection, selectionArgs);
		default:
			return 0;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (sUriMatcher.match(uri)) {
		case PATH_GROUPS:
			return mDatabaseHelper.getWritableDatabase().update(TABLE_GROUPS, values, selection, selectionArgs);
		case PATH_NOTES:
			return mDatabaseHelper.getWritableDatabase().update(TABLE_NOTES, values, selection, selectionArgs);
		default:
			return 0;
		}
	}
	
}