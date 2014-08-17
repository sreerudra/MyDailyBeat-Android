package com.verve;

import java.io.InputStream;
import java.util.Calendar;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Statics {

	public static String getDateString(Calendar when) {
		String month = "/";
		if (when.get(Calendar.MONTH) < 10) {
			month = "0" + (when.get(Calendar.MONTH) + 1) + month;
		} else {
			month = when.get(Calendar.MONTH) + month;
		}

		String day = "/";
		if (when.get(Calendar.DAY_OF_MONTH) < 10) {
			day = "0" + when.get(Calendar.DAY_OF_MONTH) + day;
		} else {
			day = when.get(Calendar.DAY_OF_MONTH) + day;
		}

		String year = "";
		if (when.get(Calendar.YEAR) < 1000) {
			year = "0" + when.get(Calendar.YEAR);
		} else {
			year = when.get(Calendar.YEAR) + "";
		}

		return month + day + year;
	}

	public static String getTimeString(Calendar when) {
		String hour = ":";
		if (when.get(Calendar.HOUR) < 10) {
			hour = "0" + when.get(Calendar.HOUR) + hour;
		} else {
			hour = when.get(Calendar.HOUR) + hour;
		}
		String min = "";
		if (when.get(Calendar.MINUTE) < 10) {
			min = "0" + when.get(Calendar.MINUTE);
		} else {
			min = when.get(Calendar.MINUTE) + "";
		}
		String suffix = " ";
		if (when.get(Calendar.AM_PM) == Calendar.AM) {
			suffix += "AM";
		} else {
			suffix += "PM";
		}

		return hour + min + suffix;
	}

	public static JSONArray createInterestsJSON() {
		JSONArray arr = new JSONArray();

		arr.put("Arts/Culture");
		arr.put("Books");
		arr.put("Car Enthusiast");
		arr.put("Card Games");
		arr.put("Dancing");
		arr.put("Dining Out");
		arr.put("Fitness/Wellbeing");
		arr.put("Golf");
		arr.put("Ladies' Night Out");
		arr.put("Men's Night Out");
		arr.put("Movies");
		arr.put("Outdoor Activities");
		arr.put("Spiritual");
		arr.put("Baseball");
		arr.put("Football");
		arr.put("Hockey");
		arr.put("Car Racing");
		arr.put("Woodworking");

		return arr;
	}

	public static JSONArray createVolunteeringJSON() {
		JSONArray arr = new JSONArray();

		arr.put("Spiritual");
		arr.put("Nonprofit");
		arr.put("Community");

		return arr;
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
	public static Bitmap decodeSampledBitmapFromInputStream(WindowManager mngr, InputStream stream,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(stream, null, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    Bitmap bitmapOrg =  BitmapFactory.decodeStream(stream, null, options);
	    
	    DisplayMetrics metrics = new DisplayMetrics();
	    mngr.getDefaultDisplay().getMetrics(metrics);

	    int width = bitmapOrg.getWidth();
	    int height = bitmapOrg.getHeight();

	    float scaleWidth = metrics.scaledDensity;
	    float scaleHeight = metrics.scaledDensity;

	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();
	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);

	    // recreate the new Bitmap
	    return Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
	}

}
