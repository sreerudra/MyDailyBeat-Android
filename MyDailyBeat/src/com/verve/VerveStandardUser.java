package com.verve;

import org.json.JSONException;
import org.json.JSONObject;

public class VerveStandardUser {
	
	public String name, email, password, screenName, mobile, zipcode, birth_month;
	public long birth_year;
	
	public static int PERSON_COUNT = 1;
	public VerveStandardUser(String name, String email, String password,
			String screenName, String mobile, String zipcode,
			String birth_month, long birth_year) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.screenName = screenName;
		this.mobile = mobile;
		this.zipcode = zipcode;
		this.birth_month = birth_month;
		this.birth_year = birth_year;
	}
	public VerveStandardUser() {
		this.name = "Person " + PERSON_COUNT;
		this.email = "test@example.com";
		this.password = "test";
		this.screenName = "test";
		this.mobile = "123-456-7890";
		this.zipcode = "11111";
		this.birth_month = "September";
		this.birth_year = 1987;
		PERSON_COUNT++;
	}
	public static VerveStandardUser userFromJSON(JSONObject result) {
		
		VerveStandardUser user = new VerveStandardUser();
		
		try {
			user = new VerveStandardUser(result.getString("name"), result.getString("email"), result.getString("password"), result.getString("screenName"), result.getString("mobile"), result.getString("zipcode"), result.getString("birth_month"), result.getLong("birth_year"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
		
		
	}

}
