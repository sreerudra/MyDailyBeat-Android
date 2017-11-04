package com.evervecorp.mydailybeat;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;

/**
 * Created by Virinchi on 8/23/2017.
 */

public class Utils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
