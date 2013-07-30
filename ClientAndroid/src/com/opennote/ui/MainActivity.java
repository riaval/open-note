package com.opennote.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opennote.R;
import com.opennote.model.provider.RestContact;
import com.opennote.ui.accaunt.InvitesFragment;
import com.opennote.ui.accaunt.SearchFragment;
import com.opennote.ui.accaunt.SignInFragment;
import com.opennote.ui.accaunt.SignUpFragment;
import com.opennote.ui.accaunt.UserFragment;
import com.opennote.ui.additional.FeedbackFragment;
import com.opennote.ui.additional.SettingsFragment;
import com.opennote.ui.local.AllNotesFragment;
import com.opennote.ui.local.CreateGroupFragment;
import com.opennote.ui.local.GroupFragment;
import com.opennote.ui.local.LocalFragment;

public class MainActivity extends Activity {	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	private boolean authorized = false;
	private String mUserLogin;
	private Map<String, String> mGroups = new HashMap<String, String>();
	private String mSessionHash;
	
	// Adapter for ListView Contents
	private SeparatedListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		authorized = isAuthorized();

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// Create the ListView Adapter
		adapter = new SeparatedListAdapter(this);

		configurateAdapter(authorized);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description for accessibility */
			R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem("Local");
		}
		
	}
	
	private boolean isAuthorized(){
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		mSessionHash = sharedPref.getString(getString(R.string.session_hash), null);
		
		if(mSessionHash == null){
			return false;
		}
		return true;
	}

	private void configurateAdapter(boolean authorized){
		// SectionHeaders
		String[] headers = new String[] { "Accaunt", "Groups", "Additional" };
		
		String[] accauntText;
		TypedArray accauntIcon;
		String[] groupText;
		TypedArray groupIcon;
		String[] additionalText;
		TypedArray additionalIcon;
		
		// Get Drawer values
		if (authorized){
			// get user login from SharedPreferences
			SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			mUserLogin = sharedPref.getString(getString(R.string.user_login), null);		
			List<String> accauntList = new ArrayList<String>( Arrays.asList( getResources().getStringArray(R.array.drawer_accaunt_text_in) ) );
			accauntList.add(0, mUserLogin);
			
			// get groups from SQLite
			Cursor cursor = this.getContentResolver().query(
					  RestContact.Group.CONTENT_URI
					, null
					, null
					, null
					, null
				);
			List<String> groupList = new ArrayList<String>( Arrays.asList( getResources().getStringArray(R.array.drawer_group_text_in) ) );
			while(cursor.moveToNext()) {
				mGroups.put(cursor.getString(1), cursor.getString(2));
				groupList.add(cursor.getString(1));
			}
			
			accauntText = accauntList.toArray(new String[accauntList.size()]) ;
			accauntIcon = getResources().obtainTypedArray(R.array.drawer_accaunt_icon_in);
			groupText = groupList.toArray(new String[groupList.size()]);
			groupIcon = getResources().obtainTypedArray(R.array.drawer_group_icon);
		}
		else {
			accauntText = getResources().getStringArray(R.array.drawer_accaunt_text_out);
			accauntIcon = getResources().obtainTypedArray(R.array.drawer_accaunt_icon_out);
			groupText = getResources().getStringArray(R.array.drawer_group_text_out);
			groupIcon = getResources().obtainTypedArray(R.array.drawer_group_icon);	
		}
		additionalText = getResources().getStringArray(R.array.drawer_additional_text);
		additionalIcon = getResources().obtainTypedArray(R.array.drawer_additional_icon);

		// Generate Drawer lists
		List<DrawerListItem> accauntItems = DrawerListItem.generateItems(accauntText, accauntIcon);
		List<DrawerListItem> groupItems = DrawerListItem.generateItems(groupText, groupIcon);
		List<DrawerListItem> additionalItems = DrawerListItem.generateItems(additionalText, additionalIcon);

		// Create Sections adapters
		DrawerArrayAdapter accauntAdapter = new DrawerArrayAdapter(this, R.layout.drawer_list_item, accauntItems);
		DrawerArrayAdapter groupAdapter = new DrawerArrayAdapter(this, R.layout.drawer_list_item, groupItems);
		DrawerArrayAdapter additionalAdapter = new DrawerArrayAdapter(this, R.layout.drawer_list_item, additionalItems);

		// Add Sections
		adapter.addSection(headers[0], accauntAdapter);
		adapter.addSection(headers[1], groupAdapter);
		adapter.addSection(headers[2], additionalAdapter);
		
		// Set the adapter on the ListView holder
		mDrawerList.setAdapter(adapter);

		// Listen for Click events
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.setGroupVisible(0, !drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_new:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ViewGroup v = (ViewGroup) view;
			TextView textItem = (TextView) v.getChildAt(DrawerListItem.TEXT_ITEM);
			selectItem(textItem.getText().toString());
		}
	}

	private void selectItem(String text) {
		Fragment fragment = null;
		boolean groupSelect = false;
		
		if(text.equals(mUserLogin)){
			fragment = new UserFragment();
		} else if(text.equals("Log in")){
			fragment = new SignInFragment();
		} else if (text.equals("Sign up")){
			fragment = new SignUpFragment();
		} else if (text.equals("Invites")){
			fragment = new InvitesFragment();
		} else if (text.equals("Search")){
			fragment = new SearchFragment();
		} else if (text.equals("AllNotes")){
			fragment = new AllNotesFragment();
		} else if (text.equals("Local")){
			fragment = new LocalFragment();
		} else if (text.equals("Create group")){
			fragment = new CreateGroupFragment();
		} else if (text.equals("Feedback")){
			fragment = new FeedbackFragment();
		} else if (text.equals("Settings")){
			fragment = new SettingsFragment();
		} else {
			GroupFragment groupFragment = new GroupFragment();
			groupFragment.setValues(mSessionHash, text);
			fragment = groupFragment;
			groupSelect = true;
		}
		
		try {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(adapter.getPossition(text), true);
		if(!groupSelect){
			setTitle(text);
		} else
			setTitle(mGroups.get(text));

		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
}
