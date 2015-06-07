package com.byteshaft.keyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.UnknownFormatConversionException;

class CustomSeekBar extends ListPreference implements SeekBar.OnSeekBarChangeListener,
         View.OnClickListener {

     private static final String androidns = "http://schemas.android.com/apk/res/android";
     private SeekBar mSeekBar;
     private TextView mSplashText, mValueText;
     private Context mContext;
     int mSeekBarProgress;
     CharSequence mTextToDisplay;

     public CustomSeekBar(Context context, AttributeSet attrs) {
         super(context, attrs);
         mContext = context;
     }

     @Override
     protected View onCreateDialogView() {
         LinearLayout.LayoutParams params;
         LinearLayout layout = new LinearLayout(mContext);
         layout.setOrientation(LinearLayout.VERTICAL);
         layout.setPadding(6, 6, 6, 6);
         mSplashText = new TextView(mContext);
         mSplashText.setPadding(30, 10, 30, 10);
         layout.addView(mSplashText);
         mValueText = new TextView(mContext);
         mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
         mValueText.setTextSize(32);
         params = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
         layout.addView(mValueText, params);
         mSeekBar = new SeekBar(mContext);
         mSeekBar.setOnSeekBarChangeListener(this);
         layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

         try {
             String previousSeekPosition = getPersistedString("sound");
             int intValue = Integer.parseInt(previousSeekPosition.replaceAll("[^0-9]", ""));
             mSeekBarProgress = intValue;
         }catch (UnknownFormatConversionException e) {
             e.printStackTrace();
         }
         return layout;
     }

     @Override
     protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
         // do not call super
     }

     private void setProgressBarValue() {
         final int max = this.getEntries().length - 1;
         mSeekBar.setMax(max);
         mSeekBar.setProgress(mSeekBarProgress);
     }

     @Override
     protected void onBindDialogView(View v) {
         super.onBindDialogView(v);
         setProgressBarValue();
     }

     @Override
     public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
         mTextToDisplay = getEntryFromValue(value);
         mValueText.setText(mTextToDisplay);
         setSummary(String.valueOf(value * 10));
     }

     private CharSequence getEntryFromValue(int value) {
         CharSequence[] entries = getEntries();
         return value >= 0 && entries != null ? entries[value] : null;
     }

     @Override
     public void onStartTrackingTouch(SeekBar seekBar) {
     }

     @Override
     public void onStopTrackingTouch(SeekBar seekBar) {
     }

     @Override
     public void showDialog(Bundle state) {
         super.showDialog(state);
         Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
         positiveButton.setOnClickListener(this);
     }

     @Override
     public void onClick(View v) {
         if (shouldPersist()) {
             final int progressChoice = mSeekBar.getProgress();
             setValueIndex(progressChoice);
             persistString(String.valueOf(progressChoice));
         }
         getDialog().dismiss();
     }
 }
