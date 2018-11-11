/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.car.settings.datetime;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;

import com.android.car.settings.common.FragmentController;
import com.android.car.settings.common.NoSetupPreferenceController;
import com.android.car.settings.common.PreferenceUtil;

/**
 * Business logic which controls the auto datetime toggle.
 */
public class AutoDatetimeTogglePreferenceController extends NoSetupPreferenceController
        implements Preference.OnPreferenceChangeListener {

    public AutoDatetimeTogglePreferenceController(Context context, String preferenceKey,
            FragmentController fragmentController) {
        super(context, preferenceKey, fragmentController);
    }

    @Override
    public void updateState(Preference preference) {
        PreferenceUtil.requirePreferenceType(preference, TwoStatePreference.class);
        ((TwoStatePreference) preference).setChecked(isEnabled());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        PreferenceUtil.requirePreferenceType(preference, TwoStatePreference.class);
        boolean isAutoDatetimeEnabled = (boolean) newValue;
        Settings.Global.putInt(
                mContext.getContentResolver(),
                Settings.Global.AUTO_TIME,
                isAutoDatetimeEnabled ? 1 : 0);

        mContext.sendBroadcast(new Intent(Intent.ACTION_TIME_CHANGED));
        return true;
    }

    private boolean isEnabled() {
        return Settings.Global.getInt(
                mContext.getContentResolver(), Settings.Global.AUTO_TIME, 0) > 0;
    }
}
