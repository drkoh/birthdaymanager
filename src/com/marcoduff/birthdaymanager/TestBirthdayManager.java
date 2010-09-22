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

import android.app.Activity;
import android.provider.ContactsContract;

/**
 * Classe per effettuare dei test sopratutto dall'emulatore dell'SDK.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 */
public class TestBirthdayManager extends BirthdayManager {
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
	};
	private int currentIndex;
	
	/**
	 * Costruisce un {@link BirthdayManager} di default per effettuare i test.
	 * 
	 * @param activity
	 */
	public TestBirthdayManager(Activity activity) {
		super(activity);
		currentIndex = -1;
	}
	
	/**
	 * Restituisce la data di nascita.
	 */
	@Override
	protected String getBornDate() {
		return NAME_BORNS[currentIndex][1];
	}

	/**
	 * Restituisce l'id relativo a {@link ContactsContract.RawContacts}.
	 */
	@Override
	protected String getRawId() {
		return "1";
	}
	
	/**
	 * Restituisce il nome del contatto.
	 */
	@Override
	protected String getName() {
		return NAME_BORNS[currentIndex][0];
	}

	/**
	 * Muove l'iteratore al prossimo elemento.
	 */
	@Override
	protected boolean moveToNext() {
		currentIndex++;
		return currentIndex<NAME_BORNS.length;
	}

	/**
	 * Inizia l'iterazione.
	 */
	@Override
	protected void startIteration() {
		currentIndex = -1;
	}

	/**
	 * Finisce l'iterazione.
	 */
	@Override
	protected void stopIteration() {
		currentIndex = NAME_BORNS.length;
	}
}
