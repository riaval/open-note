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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = mIInflater.inflate(R.layout.list_note_checkbox, parent, false);
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.listNoteCheck);
			EditText editText = (EditText) view.findViewById(R.id.listNoteEdit);
			checkBox.setOnClickListener(new onCheckListener(view, position));
			editText.addTextChangedListener(new onEditListener(checkBox, position));
		}

		InList p = mObjects.get(position);
		return view;
	}
	
	public void add() {
		mObjects.add(new InList());
	}

	private class InList{
		boolean checked;
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
				item.checked = true;
			} else {
				edit.setPaintFlags(Paint.DEV_KERN_TEXT_FLAG);
				item.checked = false;
			}
			
		}
		
	}
	
	class onEditListener implements TextWatcher {
		private boolean mIsNextCreated;
		private CheckBox mCheckBox;
		private int mPosition;
		
		onEditListener(CheckBox checkBox, int position) {
			mCheckBox = checkBox;
			mPosition = position;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			InList item = ((InList) getItem(mPosition));
			item.value = s.toString();
			if(!mIsNextCreated){
				add();
				mIsNextCreated = true;
				mCheckBox.setEnabled(true);
			}
		}
		
	}
	
	public boolean getState(int position) {
		return mObjects.get(position).checked;
	}
	
	public String getValue(int position) {
		return mObjects.get(position).value;
	}
	
}
