package com.evervecorp.mydailybeat.list;

/**
 * Created by Virinchi on 8/16/2017.
 */

public class MenuItem {
    String mTitle;
    int mIcon;
    boolean hasImage = true;

    public MenuItem(String mTitle, int mIcon) {
        this.mTitle = mTitle;
        this.mIcon = mIcon;
        if (this.mIcon == -1) {
            hasImage = false;
        }
    }
}
