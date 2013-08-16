package com.opennote.model.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class LocalContact {
	
	public static final String AUTHORITY = "com.opennote.local";
	
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	
	public interface LocalNotesCoulmns {
		public static final String TITLE = "title";
		public static final String BODY = "body";
		public static final String DATE = "date";
		public static final String COLOR = "color";
	}
	
	public static final class LocalNotes implements BaseColumns, LocalNotesCoulmns {
		public static final String CONTENT_PATH = "local";
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
	}
	
}
