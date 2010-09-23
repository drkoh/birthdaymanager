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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.marcoduff.birthdaymanager.model.BirthdayContact;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Event;

/**
 * Classe che si occupa della lettura dei contatti nel db del terminale.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @version 1.1
 */
public class CursorBirthdayManager implements BirthdayManager {
	private Activity activity;
	
	/**
	 * Costruisce un {@link BirthdayManager} per il recupero dei contatti tramite cursore.
	 * 
	 * @param activity
	 */
	public CursorBirthdayManager(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Restituisce la collezione dei contatti.
	 * 
	 * @return La collezione dei contatti.
	 * @since 1.1
	 */
	@Override
	public Collection<BirthdayContact> getBirthdayContactCollection() {
		Map<Long, BirthdayContact> contactsMap = new HashMap<Long, BirthdayContact>(); 
    	String[] projection = new String[] {
    			ContactsContract.Contacts._ID,
    			ContactsContract.Contacts.DISPLAY_NAME,
    			ContactsContract.Contacts.IN_VISIBLE_GROUP,
    	};
		Cursor cursor = activity.managedQuery(
    			ContactsContract.Contacts.CONTENT_URI,
    			projection,
    			null,
    			null,
    			null);
		activity.startManagingCursor(cursor);
		while(cursor.moveToNext()) {
			long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			boolean isVisible = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP))==1;
			if(isVisible) contactsMap.put(new Long(id), new BirthdayContact(id,displayName));
		}
		activity.stopManagingCursor(cursor);
		updateBirthdayContact(contactsMap);
		return contactsMap.values();
	}
	
	private void updateBirthdayContact(Map<Long, BirthdayContact> contactsMap) {
    	String[] projection = new String[] {
    			RawContacts.CONTACT_ID,
    			Data.DATA1
    	};
    	Cursor cursor = activity.managedQuery(
    			ContactsContract.Data.CONTENT_URI,
    			projection,
    			Data.MIMETYPE +" = '"+Event.CONTENT_ITEM_TYPE+"' AND "+Data.DATA2+" = '"+Event.TYPE_BIRTHDAY+"'",
    			null,
    			null);
    	activity.startManagingCursor(cursor);
    	while(cursor.moveToNext()) {
			long id = cursor.getLong(cursor.getColumnIndex(RawContacts.CONTACT_ID));
			String bornDate = cursor.getString(cursor.getColumnIndex(Data.DATA1));
    		BirthdayContact birthdayContact = contactsMap.get(new Long(id));
    		birthdayContact.setBornDate(bornDate);
    	}
    	activity.stopManagingCursor(cursor);
	}
}
