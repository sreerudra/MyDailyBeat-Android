package com.verve;

import java.util.Calendar;

import org.json.JSONArray;

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

}
