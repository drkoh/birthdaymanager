/*
 *  Birthday Manager
 *  Copyright (C) 2010 MarcoDuff.com
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.marcoduff.birthdaymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.provider.ContactsContract;

import com.marcoduff.birthdaymanager.util.BirthdayComparator;

/**
 * Classe Manager per il recupero della lista dei compleanni.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 */
public abstract class BirthdayManager {
	/**
	 * Indica la chiave della mappa che contiene l'id relativo a {@link ContactsContract.RawContacts}.
	 * 
	 * See {@link #getContactList()}
	 */
	public static final String RAW_ID = "raw_id";
	/**
	 * Indica la chiave della mappa che contiene il nome del contatto.
	 * 
	 * See {@link #getContactList()}
	 */
	public static final String DISPLAY_NAME = "display_name";
	/**
	 * Indica la chiave della mappa che contiene la data di nascita in formato yyyy-MM-dd del contatto.
	 * 
	 * See {@link #getContactList()}
	 */
	public static final String BORN_DATE = "born_date";
	/**
	 * Indica la chiave della mappa che contiene il giorno del prossimo compleanno in formato {@link Calendar} del contatto.
	 * 
	 * See {@link #getContactList()}
	 */
	public static final String NEXT_BIRTHDAY_CALENDAR = "next_birthday_calendar";
	/**
	 * Indica la chiave della mappa che contiene la stringa esplicativa della data del prossimo anno del contatto.
	 * 
	 * See {@link #getContactList()}
	 */
	public static final String NEXT_BIRTHDAY_STRING = "next_birthday_string";
	/**
	 * Indica la chiave della mappa che contiene un {@link Long} relativo al numero di giorni mancanti al compleanno.
	 * 
	 * See {@link #getContactList()}
	 */
	public static final String NEXT_BIRTHDAY_IN_DAYS = "next_birthday_in_days";
	
	private Context context;
	
	/**
	 * Costruisce un nuovo gestore di compleanni.
	 * 
	 * @param context Contesto dell'applicazione.
	 */
	public BirthdayManager(Context context) {
		this.context = context;
	}
	
	/**
	 * Restituisce la lista dei contatti contenente la mappa con le relative informaizoni.
	 * 
	 * @return La lista dei contatti contenente la mappa con le relative informaizoni.
	 */
    public List<Map<String,Object>> getContactList() {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.date_format));
    	
    	List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
    	
    	Calendar today = Calendar.getInstance();
    	resetHoursMinutesSeconds(today);
    	
    	startIteration();
    	while(moveToNext()) {
    		String rawId = getRawId();
	   		String name = getName();
	  		String dateString = getBornDate();

	  		Calendar nextBirthdayCalendar = getNextBirthdayDate(today, dateString);
	  		String nextBirthdayFormat = simpleDateFormat.format(nextBirthdayCalendar.getTime());
    		long diff;
    		String nextBirthdayString;
    		diff = (nextBirthdayCalendar.getTimeInMillis()-today.getTimeInMillis())/86400000;
    		if(diff==0) nextBirthdayString = context.getString(R.string.today);
    		else if(diff==1) nextBirthdayString = context.getString(R.string.tomorrow);
    		else if(diff<=7) nextBirthdayString = String.format(context.getString(R.string.inaweek), diff, nextBirthdayFormat);
    		else if(diff<=31) nextBirthdayString = String.format(context.getString(R.string.inamonth), diff, nextBirthdayFormat);
    		else nextBirthdayString = String.format(context.getString(R.string.inayear), diff, nextBirthdayFormat);
    		
    		Map<String,Object> map = new HashMap<String, Object>();
    		map.put(RAW_ID, rawId);
    		map.put(DISPLAY_NAME, name);
    		map.put(BORN_DATE, dateString);
    		map.put(NEXT_BIRTHDAY_CALENDAR, nextBirthdayCalendar);
    		map.put(NEXT_BIRTHDAY_STRING, nextBirthdayString);
    		map.put(NEXT_BIRTHDAY_IN_DAYS, diff);
    		data.add(map);
    	}
    	stopIteration();
    	Collections.sort(data, new BirthdayComparator());

    	return data;
    }
    
	/**
	 * Inizia l'iterazione.
	 */
    protected abstract void startIteration();
	/**
	 * Muove l'iteratore al prossimo elemento.
	 */
    protected abstract boolean moveToNext();
	/**
	 * Restituisce l'id relativo a {@link ContactsContract.RawContacts}.
	 */
    protected abstract String getRawId();
	/**
	 * Restituisce il nome del contatto.
	 */
    protected abstract String getName();
	/**
	 * Restituisce la data di nascita.
	 */
    protected abstract String getBornDate();
	/**
	 * Finisce l'iterazione.
	 */
    protected abstract void stopIteration();
    
    private Calendar getNextBirthdayDate(Calendar calendarFrom, String dateString) {
		try {
	    	int year = calendarFrom.get(Calendar.YEAR);
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = simpleDateFormat.parse(dateString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.YEAR, year);
			resetHoursMinutesSeconds(calendar);
			if(calendar.before(calendarFrom)) calendar.add(Calendar.YEAR, 1);
			return calendar;
		}
		catch (ParseException e) {
			return null;
		}
    }
    
    private void resetHoursMinutesSeconds(Calendar calendar) {
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    }
}
