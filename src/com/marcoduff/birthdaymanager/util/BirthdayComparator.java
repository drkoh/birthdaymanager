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

import java.util.Comparator;
import java.util.Map;

/**
 * Classe per effettuare il confronto tra due elementi della mappa dei compleanni.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 */
public class BirthdayComparator implements Comparator<Map<String, String>>{
	@Override
	public int compare(Map<String, String> object1, Map<String, String> object2) {
		boolean hasBornDate1 = object1.get(AdapterUtils.BORN_DATE)!=null;
		boolean hasBornDate2 = object2.get(AdapterUtils.BORN_DATE)!=null;
		int diff;
		if(hasBornDate1&&hasBornDate2) {
			Long diff1 = Long.parseLong(object1.get(AdapterUtils.NEXT_BIRTHDAY_IN_DAYS));
			Long diff2 = Long.parseLong(object2.get(AdapterUtils.NEXT_BIRTHDAY_IN_DAYS));
			diff = diff1.compareTo(diff2);
		}
		else if(!hasBornDate1&&!hasBornDate2) diff = 0;
		else if(hasBornDate1) return -1;
		else return 1;
		if(diff==0) {
			String name1 = (String)object1.get(AdapterUtils.DISPLAY_NAME);
			String name2 = (String)object2.get(AdapterUtils.DISPLAY_NAME);
			return name1.compareTo(name2);
		}
		else return diff;
	}
}
