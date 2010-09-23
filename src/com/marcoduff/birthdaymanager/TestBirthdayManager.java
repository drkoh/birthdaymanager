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

import java.util.ArrayList;
import java.util.Collection;

import com.marcoduff.birthdaymanager.model.BirthdayContact;

/**
 * Classe per effettuare dei test sopratutto dall'emulatore dell'SDK.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @version 1.1
 */
public class TestBirthdayManager implements BirthdayManager {
	private static final String[][] NAME_BORNS = new String[][] {
		new String[] {"MarcoDuff",		"1979-10-03"},
		new String[] {"MyLove",			"1978-10-13"},
		new String[] {"Android",		"2007-11-05"},
		new String[] {"Batman",			"1939-05-27"},
		new String[] {"Homer Simpson",	"1989-12-17"},
		new String[] {"Mickey Mouse",	"1928-11-18"},
		new String[] {"Minnie",			"1928-05-15"},
		new String[] {"Pluto",			"1930-08-18"},
		new String[] {"Wikipedia",		"2001-01-15"},
		new String[] {"Sun",			""},
		new String[] {"Universe",		""},
	};
	
	/**
	 * Costruisce un {@link BirthdayManager} di default per effettuare i test.
	 * 
	 * @since 1.1
	 */
	public TestBirthdayManager() {
	}
	
	/**
	 * Restituisce la collezione dei contatti.
	 * 
	 * @return La collezione dei contatti.
	 * @since 1.1
	 */
	@Override
	public Collection<BirthdayContact> getBirthdayContactCollection() {
		Collection<BirthdayContact> contactsCollection = new ArrayList<BirthdayContact>(); 
		for(int i=0;i<NAME_BORNS.length;i++) {
			String displayName = NAME_BORNS[i][0];
			String bornDate = NAME_BORNS[i][1];
			BirthdayContact birthdayContact = new BirthdayContact(i,displayName);
			if(bornDate.length()>0) birthdayContact.setBornDate(bornDate);
			contactsCollection.add(birthdayContact);			
		}
		return contactsCollection;
	}
}
