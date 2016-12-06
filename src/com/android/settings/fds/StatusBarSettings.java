/*Copyright (C) 2015 The ResurrectionRemix Project
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
          http://www.apache.org/licenses/LICENSE-2.0
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/
package com.android.settings.fds;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.app.Fragment;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v7.preference.PreferenceCategory;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import cyanogenmod.providers.CMSettings;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "StatusBarSettings";
	private static final String KEY_FDS_LOGO_COLOR = "status_bar_fds_logo_color";
    private static final String KEY_FDS_LOGO_STYLE = "status_bar_fds_logo_style";

	
    private ColorPickerPreference mFdsLogoColor;
    private ListPreference mFdsLogoStyle;
		
    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fds_statusbar);
		
	    PreferenceScreen prefSet = getPreferenceScreen();
		
    mFdsLogoStyle = (ListPreference) findPreference(KEY_FDS_LOGO_STYLE);
    int fdsLogoStyle = Settings.System.getIntForUser(getContentResolver(),
            Settings.System.STATUS_BAR_FDS_LOGO_STYLE, 0,
            UserHandle.USER_CURRENT);
    mFdsLogoStyle.setValue(String.valueOf(fdsLogoStyle));
    mFdsLogoStyle.setSummary(mFdsLogoStyle.getEntry());
    mFdsLogoStyle.setOnPreferenceChangeListener(this);

    // Fds logo color
    mFdsLogoColor =
        (ColorPickerPreference) prefSet.findPreference(KEY_FDS_LOGO_COLOR);
    mFdsLogoColor.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getContentResolver(),
        Settings.System.STATUS_BAR_FDS_LOGO_COLOR, 0xffffffff);
       	String hexColor = String.format("#%08x", (0xffffffff & intColor));
    mFdsLogoColor.setSummary(hexColor);
    mFdsLogoColor.setNewPreviewColor(intColor);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFdsLogoColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_FDS_LOGO_COLOR, intHex);
            return true;
        } else if (preference == mFdsLogoStyle) {
            int fdsLogoStyle = Integer.valueOf((String) newValue);
            int index = mFdsLogoStyle.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(
                    getContentResolver(), Settings.System.STATUS_BAR_FDS_LOGO_STYLE, fdsLogoStyle,
                    UserHandle.USER_CURRENT);
            mFdsLogoStyle.setSummary(
                    mFdsLogoStyle.getEntries()[index]);
                return true;
        }
            return false;
    }
	
    @Override
    public void onResume() {
        super.onResume();
    }
}