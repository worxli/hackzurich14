package com.example.android_client;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardListAdapter extends BaseAdapter {
	
	private ArrayList<LCard> mCards;
    private LayoutInflater mInflator;
    
    public CardListAdapter(Context context) {
        super();
        mCards = new ArrayList<LCard>();
        mInflator = (LayoutInflater)context.getSystemService
        	      (Context.LAYOUT_INFLATER_SERVICE);
    }

	@Override
	public int getCount() {
		return mCards.size();
	}
	
	public void addLCard(LCard lcard) {
		boolean present = false;
		for (LCard lCard : mCards) {
			if(lCard.getUuid().equals(lcard.getUuid())){
				present = true;
			}
		}
		if(!present) {
            mCards.add(lcard);
        }
	}

	@Override
	public Object getItem(int i) {
		return mCards.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_card, null);
            viewHolder = new ViewHolder();
            viewHolder.cardName = (TextView) view.findViewById(R.id.name);
            viewHolder.cardUUID = (TextView) view.findViewById(R.id.uuid);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        LCard card = mCards.get(i);
        viewHolder.cardName.setText(card.getNameString());
        viewHolder.cardUUID.setText(card.getUuid());

        return view;
    }

	public void empty() {
		mCards = new ArrayList<LCard>();
	}

}
