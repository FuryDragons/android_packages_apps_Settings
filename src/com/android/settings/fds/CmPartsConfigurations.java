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

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import cyanogenmod.providers.CMSettings;

public class CmPartsConfigurations extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "CmPartsConfigurations";
	private static final String STATUS_BAR_QUICK_QS_PULLDOWN = "qs_quick_pulldown";
	
	protected Context mContext;
	protected ContentResolver mContentRes;
	private ListPreference mQuickPulldown;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fds_cmparts);

	
			mContext = getActivity().getApplicationContext();

        mContentRes = getActivity().getContentResolver();
		final Resources res = getResources();

		PreferenceScreen prefs = getPreferenceScreen();
		ContentResolver resolver = getActivity().getContentResolver();

	    mQuickPulldown = (ListPreference) findPreference(STATUS_BAR_QUICK_QS_PULLDOWN);
        int quickPulldown = CMSettings.System.getInt(resolver,
                CMSettings.System.STATUS_BAR_QUICK_QS_PULLDOWN, 1);
        mQuickPulldown.setValue(String.valueOf(quickPulldown));
        if (quickPulldown == 0) {
            // quick pulldown deactivated
            mQuickPulldown.setSummary(res.getString(R.string.status_bar_quick_qs_pulldown_off));
        } else {
            String direction = res.getString(quickPulldown == 2
                    ? R.string.status_bar_quick_qs_pulldown_left
                    : R.string.status_bar_quick_qs_pulldown_right);
            mQuickPulldown.setSummary(
                    res.getString(R.string.status_bar_quick_qs_pulldown_summary, direction));
        }
        mQuickPulldown.setOnPreferenceChangeListener(this);
    }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
		ContentResolver resolver = getActivity().getContentResolver();
		final Resources res = getResources();
            if (preference == mQuickPulldown) {
            int quickPulldown = Integer.valueOf((String) newValue);
            CMSettings.System.putInt(resolver, CMSettings.System.STATUS_BAR_QUICK_QS_PULLDOWN,
                    quickPulldown);
            if (quickPulldown == 0) {
                // quick pulldown deactivated
                mQuickPulldown.setSummary(res.getString(R.string.status_bar_quick_qs_pulldown_off));
            } else {
                String direction = res.getString(quickPulldown == 2
                        ? R.string.status_bar_quick_qs_pulldown_left
                        : R.string.status_bar_quick_qs_pulldown_right);
                mQuickPulldown.setSummary(
                        res.getString(R.string.status_bar_quick_qs_pulldown_summary, direction));
            }
            return true;
		} 
		return false;
    }


}