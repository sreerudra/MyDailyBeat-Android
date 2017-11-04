package com.evervecorp.mydailybeat.list;

import android.content.Context;
import android.graphics.Color;
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
    ArrayList<MenuItem> mNavItems;
    boolean iconIsOnTheLeft = true;
    boolean textIsWhite = true;

    public DrawerListAdapter(Context context, ArrayList<MenuItem> mNavItems, boolean rightSide, boolean textIsWhite) {
        this.context = context;
        this.mNavItems = mNavItems;
        this.iconIsOnTheLeft = !rightSide;
        this.textIsWhite = textIsWhite;
    }

    public DrawerListAdapter(Context context, ArrayList<MenuItem> mNavItems, boolean rightSide) {
        this(context, mNavItems, rightSide, true);
    }

    public DrawerListAdapter(Context context, ArrayList<MenuItem> mNavItems) {
        this(context, mNavItems, false);
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

        MenuItem item = mNavItems.get(position);
        if (item instanceof SubtitleMenuItem) {
            SubtitleMenuItem menuItem = (SubtitleMenuItem) item;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_list_item, null);

            TextView titleView = (TextView) view.findViewById(R.id.titleText);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitleText);
            ImageView iconView = (ImageView) view.findViewById(R.id.iconView);

            titleView.setText(menuItem.mTitle);
            subtitleView.setText(menuItem.mSubtitle);
            if (this.textIsWhite) {
                titleView.setTextColor(Color.WHITE);
                subtitleView.setTextColor(Color.WHITE);
            } else {
                titleView.setTextColor(Color.BLACK);
                subtitleView.setTextColor(Color.BLACK);
            }
            if (menuItem.hasImage) {
                iconView.setImageResource(menuItem.mIcon);
            } else {
                iconView.setImageDrawable(null);
            }

            return view;
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = R.layout.regular_menu_list_item;
            if (!this.iconIsOnTheLeft) {
                layout = R.layout.right_menu_list_item;
            }
            view = inflater.inflate(layout, null);

            TextView titleView = (TextView) view.findViewById(R.id.titleText);
            ImageView iconView = (ImageView) view.findViewById(R.id.iconView);
            if (this.textIsWhite) {
                titleView.setTextColor(Color.WHITE);
            } else {
                titleView.setTextColor(Color.BLACK);
            }
            titleView.setText(item.mTitle);
            if (item.hasImage) {
                iconView.setImageResource(item.mIcon);
            } else {
                iconView.setImageDrawable(null);
            }

            return view;
        }
    }
}
