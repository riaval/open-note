package com.opennote.model.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.opennote.R;

public class ListNoteAddapter extends BaseAdapter {

	private List<InList> mObjects;
	private LayoutInflater mIInflater;
	private Context mContext;
	
	public ListNoteAddapter(Context context) {
		mContext = context;
		mObjects = new ArrayList<InList>();;
		mIInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = mIInflater.inflate(R.layout.list_note_checkbox, parent, false);
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.listNoteCheck);
		EditText editText = (EditText) view.findViewById(R.id.listNoteEdit);
		ImageButton dismissButton = (ImageButton) view.findViewById(R.id.list_dismiss_img);
		
		boolean state = mObjects.get(position).state;
		String value = mObjects.get(position).value;
		checkBox.setChecked( state );
		editText.setText( value );
		
		if(position+1 == getCount()){
			checkBox.setEnabled(false);
		}
		if(state){
			editText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		if( position+1 != getCount() ){
			editText.setHint(null);
		}
		
		checkBox.setOnClickListener(new onCheckListener(view, position));
		editText.addTextChangedListener(new OnEditListener(editText, checkBox, position));
		editText.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				ImageButton dismissBt = (ImageButton) ((View) v.getParent()).findViewById(R.id.list_dismiss_img);
				if (hasFocus && (position+1 != getCount())){
					dismissBt.setVisibility(View.VISIBLE);
				} else {
					dismissBt.setVisibility(View.INVISIBLE);
				}
			}
		});
		final EditText editForDismiss = editText;
		dismissButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editForDismiss.clearFocus();
				mObjects.remove(position);
				ListNoteAddapter.this.notifyDataSetChanged();
			}
		});

		return view;
	}
	
	public void add() {
		mObjects.add(new InList());
	}
	
	public void add(boolean state, String value) {
		mObjects.add(new InList(state, value));
	}

	private class InList{
		
		InList(){
		}
		
		InList(boolean state, String value){
			this.state = state;
			this.value = value;
		}
		
		boolean state;
		String value;
	}
	
	class onCheckListener implements OnClickListener {
		private View mParent;
		private int mPosition;
		
		onCheckListener(View parent, int position){
			mParent = parent;
			mPosition = position;
		}
		
		@Override
		public void onClick(View v) {
			EditText edit = (EditText) mParent.findViewById(R.id.listNoteEdit);
			InList item = ((InList) getItem(mPosition));
			if ( ((CheckBox) v).isChecked() ){
				edit.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				item.state = true;
			} else {
				edit.setPaintFlags(Paint.DEV_KERN_TEXT_FLAG);
				item.state = false;
			}
		}
		
	}
	
	class OnEditListener implements TextWatcher {
		private CheckBox mCheckBox;
		private EditText mEditText;
		private int mPosition;
		
		OnEditListener(EditText editText, CheckBox checkBox, int position) {
			mCheckBox = checkBox;
			mEditText = editText;
			mPosition = position;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (mEditText.getText().toString().equals("") && mPosition + 1 != getCount()) {
				mEditText.setHint(null);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			InList item = ((InList) getItem(mPosition));
			item.value = s.toString();
			if( mPosition+1 == getCount() ){
				add();
				mCheckBox.setEnabled(true);
//				mEditText.setHint(null);
				ImageButton dismissBt = (ImageButton) ((View) mEditText.getParent()).findViewById(R.id.list_dismiss_img);
				dismissBt.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	public boolean getState(int position) {
		return mObjects.get(position).state;
	}
	
	public String getValue(int position) {
		return mObjects.get(position).value;
	}
	
}
