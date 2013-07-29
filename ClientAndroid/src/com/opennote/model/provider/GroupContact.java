package com.opennote.model.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GroupContact {

	public static final String AUTHORITY = "com.opennote.group";
	
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	
	public interface GroupCoulmns {
		public static final String SLUG = "slug";
		public static final String NAME = "name";
	}
	
	public static final class Group implements BaseColumns, GroupCoulmns {
		public static final String CONTENT_PATH = "group";
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
	}
	
}
