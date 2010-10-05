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
import java.util.List;
import java.util.Map;

import com.marcoduff.birthdaymanager.R;
import com.marcoduff.birthdaymanager.model.BirthdayContact;
import com.marcoduff.birthdaymanager.receiver.BirthdayCheckReceiver;
import com.marcoduff.birthdaymanager.util.AdapterUtils;
import com.marcoduff.birthdaymanager.util.AlarmManagerUtils;
import com.marcoduff.birthdaymanager.util.Eula;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

/**
 * Activity che si occupa della visualizzazione della lista dei compleanni.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @version 1.1
 */
public class BirthdayManagerActivity extends TabActivity implements Eula.OnEulaAgreedTo {
	private static final int DIALOG_ABOUT = 1;
	
	public static final boolean IS_TEST = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	boolean acceptEula = Eula.show(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost tabHost = this.getTabHost();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getString(R.string.birtdayList),getResources().getDrawable(R.drawable.ic_tab_birthday)).setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getString(R.string.noBirtdayList),getResources().getDrawable(R.drawable.ic_tab_birthday)).setContent(R.id.tab2));
        
        
        BirthdayManager birthdayManager;
        if(!IS_TEST) {
        	birthdayManager = new CursorBirthdayManager(this);
        }
        else {
        	birthdayManager = new TestBirthdayManager();
        }
        
        Collection<BirthdayContact> birtdayCollection = birthdayManager.getBirthdayContactCollection();
        initListView(R.id.birthdayList, birtdayCollection, true);
        initListView(R.id.noBirthdayList, birtdayCollection, false);
        
        if(acceptEula) AlarmManagerUtils.initAlarmManager(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater menuInflater = this.getMenuInflater();
    	menuInflater.inflate(R.menu.menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menuPreferences: {
    		Intent intent = new Intent(this, BirthdayPreference.class);
    		startActivity(intent);
    		return true;
    	}
		case R.id.menuDonate: {
			Intent intentDonate = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=9B8HAGSQWTUVY"));
			this.startActivity(intentDonate);
			return true;
		}
		case R.id.menuAbout: {
			this.showDialog(DIALOG_ABOUT);
			return true;
		}
		case R.id.menuShowNotification: {
			this.sendBroadcast(new Intent(BirthdayCheckReceiver.ACTION_CHECK_BIRTHDAYS));
			return true;
		}
    	}
    	return super.onOptionsItemSelected(item);
    }
    
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ABOUT: {
			String message = String.format(this.getResources().getString(R.string.aboutMsg), this.getResources().getString(R.string.version));
			return new AlertDialog.Builder(this)
				.setTitle(R.string.menuAbout)
				.setMessage(message)
				.setPositiveButton(R.string.showSite, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://android.marcoduff.com/"));
						BirthdayManagerActivity.this.startActivity(intent);
						dialog.dismiss();
					}
				})					
				.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();
		}
		default:
			return super.onCreateDialog(id);
		}
	}
    
    private void initListView(int listViewId, Collection<BirthdayContact> birtdayCollection, boolean withBirthday) {
        int resource = (withBirthday?android.R.layout.simple_list_item_2:android.R.layout.simple_list_item_1);
    	String[] from = (withBirthday?new String[] {AdapterUtils.DISPLAY_NAME, AdapterUtils.NEXT_BIRTHDAY_DESCRIPTION}:new String[] {AdapterUtils.DISPLAY_NAME});
    	int[] to = (withBirthday?new int[] {android.R.id.text1, android.R.id.text2}:new int[] {android.R.id.text1});
        
    	List<Map<String,String>> mListAdapterMap = AdapterUtils.getListAdapterMap(this, birtdayCollection, withBirthday);
        ListAdapter listAdapter = new SimpleAdapter(this,mListAdapterMap,resource,from,to);
        ListView listView = (ListView)findViewById(listViewId);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new MyItemClickListener(mListAdapterMap));
    }

    /**
     * Richiamata non appena viene accettata l'eula.
     */
	@Override
	public void onEulaAgreedTo() {
		AlarmManagerUtils.initAlarmManager(this);
	}
	
    private class MyItemClickListener implements AdapterView.OnItemClickListener {
    	List<Map<String, String>> listAdapterMap;
    	
		public MyItemClickListener(List<Map<String, String>> listAdapterMap) {
			this.listAdapterMap = listAdapterMap;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        if(!IS_TEST) {
				Map<String,String> map = listAdapterMap.get(position);
				String contactId = (String)map.get(AdapterUtils._ID);
				Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);
				Intent intent = new Intent(Intent.ACTION_VIEW,contactUri);
				BirthdayManagerActivity.this.startActivity(intent);
	        }
		}   	
    }
}