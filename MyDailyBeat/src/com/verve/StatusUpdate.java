package com.verve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;

public class StatusUpdate {
	private static int UPDATE_ID_START = 0;

	public int UPDATE_ID = 0;
	public String name;
	public Calendar when;
	public String lastEvent;
	/**
	 * @param name
	 * @param when
	 * @param lastEvent
	 */
	@SuppressLint("SimpleDateFormat")
	public StatusUpdate(String name, String when, String lastEvent) {
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
		
		this.name = name;
		this.when = Calendar.getInstance();
		try {
			this.when.setTime(format.parse(when));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.lastEvent = lastEvent;
		UPDATE_ID = UPDATE_ID_START++;
	}
	
	
	
	

}
