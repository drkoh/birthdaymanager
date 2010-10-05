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

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.marcoduff.birthdaymanager.receiver.BirthdayCheckReceiver;

/**
 * Classe di utility per la gestione degli AlarmManager.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @version 1.3
 */
public class AlarmManagerUtils {
	
	/**
	 * Inizializza l'alarm manager.
	 * 
	 * @param context Contesto dell'applicazione.
	 */
    public static void initAlarmManager(Context context) {
    	Calendar calendarTomorrow = Calendar.getInstance();
    	calendarTomorrow.set(Calendar.HOUR_OF_DAY,0);
    	calendarTomorrow.set(Calendar.MINUTE,0);
    	calendarTomorrow.set(Calendar.SECOND,0);
    	calendarTomorrow.set(Calendar.MILLISECOND,0);
    	calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1);
    	long triggerAtTime = calendarTomorrow.getTimeInMillis();
    	
    	Intent intent = new Intent(BirthdayCheckReceiver.ACTION_CHECK_BIRTHDAYS);
    	PendingIntent operation = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    	
    	AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, AlarmManager.INTERVAL_DAY, operation);
    }
}
