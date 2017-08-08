package com.evervecorp.mydailybeat.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.evervecorp.mydailybeat.R;

/**
 * Created by Virinchi on 3/27/2017.
 */

public class DrawerListAdapter extends BaseAdapter {

    Context context;
    ArrayList<SubtitleMenuItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<SubtitleMenuItem> mNavItems) {
        this.context = context;
        this.mNavItems = mNavItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_list_item, null);
        } else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.titleText);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitleText);
        ImageView iconView = (ImageView) view.findViewById(R.id.iconView);

        titleView.setText(mNavItems.get(position).mTitle);
        subtitleView.setText(mNavItems.get(position).mSubtitle);
        if (mNavItems.get(position).hasImage) {
            iconView.setImageResource(mNavItems.get(position).mIcon);
        } else {
            iconView.setImageDrawable(null);
        }

        return view;
    }
}
