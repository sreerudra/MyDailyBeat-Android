package com.verve;

import java.util.Calendar;

public class VolunteeringOpportunity {
	
	public String title, details, where;
	public Calendar when;
	
	public VolunteeringOpportunity(String string, String string2,
			String string3, Calendar instance) {
		title = string;
		details = string2;
		where = string3;
		when = instance;
	}

	public String getTitle() {
		return title;
	}
	
	public String getDetails() {
		return "Where: " + where + "\nWhen: " + Statics.getDateString(when) + " " + Statics.getTimeString(when) + "\nDetails: " + details;
	}

}
