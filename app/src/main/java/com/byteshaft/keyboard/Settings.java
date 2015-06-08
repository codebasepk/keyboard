package com.byteshaft.keyboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class Settings extends PreferenceActivity {

    private Preference myPrefs;
    private Preference mSeekBarPrefrence;
    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myPrefs = findPreference("resetKey");
        mSeekBarPrefrence = findPreference("sound");
        String seekPosition = mPreferences.getString("sound", "5");
        mSeekBarPrefrence.setSummary(String.valueOf(Integer.valueOf(seekPosition) * 10));
        myPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Settings.this);
                alertDialog.setMessage("Are you sure?");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mPreferences.edit().putString("textColor", null).apply();
                        mPreferences.edit().putString("buttonColor", null).apply();
                        mPreferences.edit().putString("backgroundColor", null).apply();
                        mPreferences.edit().putString("popupColor", null).apply();
                        Toast.makeText(getApplicationContext(), "Settings Reset", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.create();
                alertDialog.show();
                return false;
            }
        });


    }

}

