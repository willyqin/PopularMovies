package com.example.han.popularmovies.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.han.popularmovies.R;

/**
 * Created by Han on 2016/9/25.
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
