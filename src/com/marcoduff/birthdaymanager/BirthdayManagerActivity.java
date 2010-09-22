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

import java.util.List;
import java.util.Map;

import com.marcoduff.birthdaymanager.R;
import com.marcoduff.birthdaymanager.util.Eula;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Activity che si occupa della visualizzazione della lista dei compleanni.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 */
public class BirthdayManagerActivity extends Activity {
	private static final boolean IS_EMULATOR = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Eula.show(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListView mBirthdayList = (ListView)findViewById(R.id.birthdayList);
        
        BirthdayManager birthdayManager;
        if(!IS_EMULATOR) {
        	birthdayManager = new CursorBirthdayManager(this);
        }
        else {
        	birthdayManager = new TestBirthdayManager(this);
        }
        
        final List<Map<String,Object>> data = birthdayManager.getContactList();
        
        ListAdapter listAdapter = new SimpleAdapter(
        		this,
        		data,
        		android.R.layout.simple_list_item_2,
        		new String[] {BirthdayManager.DISPLAY_NAME, BirthdayManager.NEXT_BIRTHDAY_STRING},
        		new int[] {android.R.id.text1, android.R.id.text2});
        mBirthdayList.setAdapter(listAdapter);
        mBirthdayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String,Object> map = data.get(position);
				String rawId = (String)map.get(BirthdayManager.RAW_ID);
				
				Cursor c = BirthdayManagerActivity.this.managedQuery(
						RawContacts.CONTENT_URI,
						new String[]{RawContacts.CONTACT_ID},
						RawContacts._ID+" = ?",
						new String[]{rawId},
						null);
				
				BirthdayManagerActivity.this.startManagingCursor(c);
				if(c.moveToNext()) {
					Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, c.getString(c.getColumnIndex(RawContacts.CONTACT_ID)));
					Intent intent = new Intent(Intent.ACTION_VIEW,contactUri);
					BirthdayManagerActivity.this.startActivity(intent);
				}
				BirthdayManagerActivity.this.stopManagingCursor(c);
			}
		});
    }
}