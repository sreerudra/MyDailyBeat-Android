package edu.bu.met.vbalabhadrapatruni.mypeeps.list;

/**
 * Created by Virinchi on 3/27/2017.
 */

public class SubtitleMenuItem {
    String mTitle;
    String mSubtitle;
    int mIcon;
    boolean hasImage = true;

    public SubtitleMenuItem(String mTitle, String mSubtitle, int mIcon) {
        this.mTitle = mTitle;
        this.mSubtitle = mSubtitle;
        this.mIcon = mIcon;
        if (this.mIcon == -1) {
            hasImage = false;
        }
    }
}
