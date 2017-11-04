package com.evervecorp.mydailybeat.object;

import android.icu.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Virinchi on 4/17/2017.
 */

public class User {
    protected int id;
    protected String firstName, lastName, emailAddress, mobilePh, zipCode, screenName, password;
    protected Date dob;

    public User() {

    }

    public User(int id, String firstName, String lastName, String emailAddress, String mobilePh, String zipCode, String screenName, String password, Date dob) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.mobilePh = mobilePh;
        this.zipCode = zipCode;
        this.screenName = screenName;
        this.password = password;
        this.dob = dob;
    }

    public User(int id, String firstName, String lastName, String emailAddress, String mobilePh, String zipCode, String screenName, String password, int birth_month, int birth_day, int birth_year) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.mobilePh = mobilePh;
        this.zipCode = zipCode;
        this.screenName = screenName;
        this.password = password;
        Calendar selected = Calendar.getInstance();
        selected.set(Calendar.YEAR, birth_year);
        selected.set(Calendar.MONTH, birth_month);
        selected.set(Calendar.DAY_OF_MONTH, birth_day);
        this.dob = selected.getTime();
    }

    public User(JSONObject json) throws Exception {
        this.id = json.getInt("id");
        this.firstName =  json.getString("fname");
        this.lastName =  json.getString("lname");
        this.emailAddress = json.getString("email");
        this.mobilePh = json.getString("mobile");
        this.screenName = json.getString("screenName");
        this.password = json.getString("password");
        int month = json.getInt("birth_month");
        int day = json.getInt("birth_day");
        int year = json.getInt("birth_year");
        Calendar selected = Calendar.getInstance();
        selected.set(Calendar.YEAR, year);
        selected.set(Calendar.MONTH, month);
        selected.set(Calendar.DAY_OF_MONTH, day);
        this.dob = selected.getTime();
        this.zipCode = json.getString("zipcode");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        StringTokenizer tokenizer = new StringTokenizer(name, " ");
        this.firstName = tokenizer.nextToken();
        this.lastName = tokenizer.nextToken();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobilePh() {
        return mobilePh;
    }

    public void setMobilePh(String mobilePh) {
        this.mobilePh = mobilePh;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setDob(int birth_month, int birth_day, int birth_year) {
        Calendar selected = Calendar.getInstance();
        selected.set(Calendar.YEAR, birth_year);
        selected.set(Calendar.MONTH, birth_month);
        selected.set(Calendar.DAY_OF_MONTH, birth_day);
        this.dob = selected.getTime();
    }
}
