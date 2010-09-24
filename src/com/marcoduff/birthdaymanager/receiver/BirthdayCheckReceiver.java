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
package com.marcoduff.birthdaymanager.receiver;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.marcoduff.birthdaymanager.BirthdayManager;
import com.marcoduff.birthdaymanager.BirthdayManagerActivity;
import com.marcoduff.birthdaymanager.CursorBirthdayManager;
import com.marcoduff.birthdaymanager.R;
import com.marcoduff.birthdaymanager.TestBirthdayManager;
import com.marcoduff.birthdaymanager.model.BirthdayContact;
import com.marcoduff.birthdaymanager.util.AdapterUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receiver dell'AlarmManager per le notifiche giornaliere dei compleanni.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @since 1.2
 */
public class BirthdayCheckReceiver extends BroadcastReceiver {
	public static final String ACTION_CHECK_BIRTHDAYS = "com.marcoduff.birthdaymanager.ACTION_CHECK_BIRTHDAYS";
	
	@Override
	public void onReceive(Context context, Intent intent) {
        BirthdayManager birthdayManager;
        if(!BirthdayManagerActivity.IS_TEST) {
        	birthdayManager = new CursorBirthdayManager(context);
        }
        else {
        	birthdayManager = new TestBirthdayManager();
        }
        
        Collection<BirthdayContact> birtdayCollection = birthdayManager.getBirthdayContactCollection();
        List<Map<String,String>> listAdapterMap = AdapterUtils.getListAdapterMap(context, birtdayCollection, true);
        Iterator<Map<String,String>> iteratorAdapterMap = listAdapterMap.iterator();
        StringBuffer text = new StringBuffer();
        while(iteratorAdapterMap.hasNext()) {
        	Map<String,String> map = iteratorAdapterMap.next();
        	if(map.get(AdapterUtils.BORN_DATE)!=null&&map.get(AdapterUtils.NEXT_BIRTHDAY_IN_DAYS).equals("0")) {
        		if(text.length()>0) text.append(", ");
        		text.append(map.get(AdapterUtils.DISPLAY_NAME));
        	}
        }
        CharSequence contentText;
        if(text.length()==0) contentText = context.getString(R.string.noBirthdaysToday);
        else contentText = String.format(context.getString(R.string.birthdaysToday), text);
        
        Notification notification = new Notification(R.drawable.icon, contentText, System.currentTimeMillis());
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        CharSequence contentTitle = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, BirthdayManagerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
	}

}
