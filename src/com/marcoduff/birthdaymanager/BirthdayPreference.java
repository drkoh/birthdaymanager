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

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity per la gestione delle impostazioni.
 * 
 * @author Marco Palermo (http://www.marcoduff.com/)
 * @version 1.3
 */
public class BirthdayPreference extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.preferences);
	}
}
