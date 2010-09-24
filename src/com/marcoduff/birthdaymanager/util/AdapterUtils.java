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
package com.marcoduff.birthdaymanager.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.provider.ContactsContract;

import com.marcoduff.birthdaymanager.R;
import com.marcoduff.birthdaymanager.model.BirthdayContact;

/**
 * Classe di utility per adattare le strutture dati.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @version 1.1
 */
public class AdapterUtils {
	/**
	 * Indica la chiave della mappa che contiene l'id relativo a {@link ContactsContract.Contacts}.
	 */
	public static final String _ID = "_id";
	/**
	 * Indica la chiave della mappa che contiene il nome del contatto.
	 */
	public static final String DISPLAY_NAME = "display_name";
	/**
	 * Indica la chiave della mappa che contiene la data di nascita in formato del contatto.
	 */
	public static final String BORN_DATE = "born_date";
	/**
	 * Indica la chiave della mappa che contiene il giorno del prossimo compleanno in formato del contatto.
	 */
	public static final String NEXT_BIRTHDAY_STRING = "next_birthday_string";
	/**
	 * Indica la chiave della mappa che contiene la stringa esplicativa della data del prossimo anno del contatto.
	 */
	public static final String NEXT_BIRTHDAY_DESCRIPTION = "next_birthday_description";
	/**
	 * Indica la chiave della mappa che contiene un {@link Long} relativo al numero di giorni mancanti al compleanno.
	 */
	public static final String NEXT_BIRTHDAY_IN_DAYS = "next_birthday_in_days";
	
	/**
	 * Trasforma una collection di {@link BirthdayContact} in una mappa utile alla list adapter di una ListView.
	 * 
	 * @param context Il contesto di riferimento.
	 * @param birtdayCollection La collection di {@link BirthdayContact}.
	 * @return La struttura utile ad una ListView.
	 */
	public static List<Map<String, String>> getListAdapterMap(Context context, Collection<BirthdayContact> birtdayCollection, boolean withBirthday) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format));
		List<Map<String, String>> listAdapterMap = new ArrayList<Map<String,String>>();
		Iterator<BirthdayContact> iterator = birtdayCollection.iterator();
		while(iterator.hasNext()) {
			BirthdayContact birthdayContact = iterator.next();
			
    		Map<String,String> map = new HashMap<String, String>();
    		map.put(_ID, String.valueOf(birthdayContact.getContactId()));
    		map.put(DISPLAY_NAME, birthdayContact.getName());
    		Date bornDate = birthdayContact.getBornDate();
    		if(bornDate!=null) {
    			String bornDateString = dateFormat.format(bornDate);
    			String nextBirthdayString = dateFormat.format(birthdayContact.getNextBirthdayDate());
	    		long nextBirthdayInDays = birthdayContact.getNextBirthdayInDays();
    			String nextBirthdayDescription = createNextBirthdayDescription(context, nextBirthdayInDays, nextBirthdayString);
    			map.put(BORN_DATE, bornDateString);
	    		map.put(NEXT_BIRTHDAY_STRING, nextBirthdayString);
	    		map.put(NEXT_BIRTHDAY_DESCRIPTION, nextBirthdayDescription);
	    		map.put(NEXT_BIRTHDAY_IN_DAYS, String.valueOf(nextBirthdayInDays));
    		}
    		else {
    			map.put(BORN_DATE, null);
	    		map.put(NEXT_BIRTHDAY_STRING, null);
	    		map.put(NEXT_BIRTHDAY_DESCRIPTION, context.getString(R.string.unknownBornDate));
	    		map.put(NEXT_BIRTHDAY_IN_DAYS, "0");
    		}
    		if(withBirthday&&bornDate!=null) listAdapterMap.add(map);
    		else if(!withBirthday&&bornDate==null) listAdapterMap.add(map);
		}
		Collections.sort(listAdapterMap,new BirthdayComparator());
		return listAdapterMap;
	}

	private static String createNextBirthdayDescription(Context context, long nextBirthdayInDays, String nextBirthdayString) {
		if(nextBirthdayInDays==0) return context.getString(R.string.today);
		else if(nextBirthdayInDays==1) return context.getString(R.string.tomorrow);
		else if(nextBirthdayInDays<=7) return String.format(context.getString(R.string.inaweek), nextBirthdayInDays, nextBirthdayString);
		else if(nextBirthdayInDays<=31) return String.format(context.getString(R.string.inamonth), nextBirthdayInDays, nextBirthdayString);
		else return String.format(context.getString(R.string.inayear), nextBirthdayInDays, nextBirthdayString); 
	}
}
