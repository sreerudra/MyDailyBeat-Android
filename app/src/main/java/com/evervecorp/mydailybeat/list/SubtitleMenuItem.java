package com.evervecorp.mydailybeat.list;

/**
 * Created by Virinchi on 3/27/2017.
 */

public class SubtitleMenuItem extends MenuItem {

    String mSubtitle;

    public SubtitleMenuItem(String mTitle, String mSubtitle, int mIcon) {
        super(mTitle, mIcon);
        this.mSubtitle = mSubtitle;
    }
}
