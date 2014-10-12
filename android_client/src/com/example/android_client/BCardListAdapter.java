package com.example.android_client;

import java.util.ArrayList;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BCardListAdapter extends BaseAdapter {
	
	private ArrayList<Pair<String,String>> bcard;
	private LayoutInflater mInflator;

	public BCardListAdapter(Context applicationContext) {
		bcard = new ArrayList<Pair<String,String>>();
		mInflator = (LayoutInflater)applicationContext.getSystemService
      	      (Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return bcard.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void empty() {
		bcard = null;
		bcard = new ArrayList<Pair<String,String>>();
	}
	
	public void addString(String arg1, String arg2){
		bcard.add(new Pair<String,String>(arg1,arg2));
	}
	
	@Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_entry, null);
            viewHolder = new ViewHolder();
            viewHolder.cardName = (TextView) view.findViewById(R.id.name);
            viewHolder.cardUUID = (TextView) view.findViewById(R.id.uuid);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Pair<String,String> pair = bcard.get(i);
        viewHolder.cardName.setText(pair.first);
        viewHolder.cardUUID.setText(pair.second);

        return view;
    }

}
