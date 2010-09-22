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
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;

/**
 * Classe che si occupa della lettura dei contatti nel db del terminale.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 */
public class CursorBirthdayManager extends BirthdayManager {
	private Activity activity;
	private Cursor cursor;
	
	/**
	 * Costruisce un {@link BirthdayManager} per il recupero dei contatti tramite cursore.
	 * 
	 * @param activity
	 */
	public CursorBirthdayManager(Activity activity) {
		super(activity);
		this.activity = activity;
		this.cursor = null;
	}
	
	/**
	 * Inizia l'iterazione.
	 */
	@Override
	protected void startIteration() {
    	String[] projection = new String[] {
    			Data._ID,
    			Data.RAW_CONTACT_ID,
    			Data.DISPLAY_NAME,
    			Data.DATA1
    	};
    	cursor = activity.managedQuery(
    			ContactsContract.Data.CONTENT_URI,
    			projection,
    			Data.MIMETYPE +" = '"+Event.CONTENT_ITEM_TYPE+"' AND "+Data.DATA2+" = '"+Event.TYPE_BIRTHDAY+"'",
    			null,
    			null);
    	activity.startManagingCursor(cursor);
    }

	/**
	 * Muove l'iteratore al prossimo elemento.
	 */
	@Override
	protected boolean moveToNext() {
		return cursor.moveToNext();
	}

	/**
	 * Finisce l'iterazione.
	 */
	@Override
	protected void stopIteration() {
    	activity.stopManagingCursor(cursor);
	}

	/**
	 * Restituisce la data di nascita.
	 */
	@Override
	protected String getBornDate() {
		return cursor.getString(cursor.getColumnIndex(Data.DATA1));
	}
	
	/**
	 * Restituisce il nome del contatto.
	 */
	@Override
	protected String getName() {
		return cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
	}
	
	/**
	 * Restituisce l'id relativo a {@link ContactsContract.RawContacts}.
	 */
	@Override
	protected String getRawId() {
		return cursor.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
	}
}
