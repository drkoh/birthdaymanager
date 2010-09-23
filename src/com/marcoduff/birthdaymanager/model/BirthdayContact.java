package com.marcoduff.birthdaymanager.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BirthdayContact {
	private long id;
	private String name;
	private Date bornDate;
	private Date nextBirthdayDate;
	private long nextBirthdayInDays;
	
	public BirthdayContact(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getContactId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getBornDate() {
		return bornDate;
	}
	
	public void setBornDate(String bornDateString) {
		Pattern p = Pattern.compile("^([0-9]{4})-([0-9]{2})-([0-9]{2})$");
		Matcher m = p.matcher(bornDateString);
		if(m.matches()) {
			int year = Integer.valueOf(m.group(1));
			int month = Integer.valueOf(m.group(2))-1;
			int day = Integer.valueOf(m.group(3));
			bornDate = new GregorianCalendar(year, month, day).getTime();
			
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
			nowCalendar.set(Calendar.MINUTE, 0);
			nowCalendar.set(Calendar.SECOND, 0);
			nowCalendar.set(Calendar.MILLISECOND, 0);
			int yearNow = nowCalendar.get(Calendar.YEAR);
			Calendar nextBirthdayCalendar = new GregorianCalendar(yearNow, month, day);
			if(nextBirthdayCalendar.before(nowCalendar)) nextBirthdayCalendar.add(Calendar.YEAR, 1);
			nextBirthdayDate = nextBirthdayCalendar.getTime();
			
			nextBirthdayInDays = (nextBirthdayCalendar.getTimeInMillis()-nowCalendar.getTimeInMillis())/86400000;
		}
		else {
			bornDate = null;
			nextBirthdayDate = null;
			nextBirthdayInDays = 0;
		}
	}

	public Date getNextBirthdayDate() {
		return nextBirthdayDate;
	}

	public long getNextBirthdayInDays() {
		return nextBirthdayInDays;
	}
}
