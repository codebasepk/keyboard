package com.byteshaft.keyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends InputMethodService implements
        KeyboardView.OnKeyboardActionListener {

    private CustomKeyboardView mKeyboardView;
    private Keyboard mKeyboard;
    private boolean isCapsLockEnabled;

    @Override
    public View onCreateInputView() {
        mKeyboardView = (CustomKeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        mKeyboardView.setOnKeyboardActionListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return mKeyboardView;
    }

    @Override
    public void onUpdateExtractingViews(EditorInfo ei) {
        ei.imeOptions |= EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        super.onUpdateExtractingViews(ei);
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        onUpdateExtractingViews(attribute);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String keyboardPreference = preferences.getString("keyboardType", "2");

        switch (keyboardPreference) {
            case "1":
                mKeyboard = new Keyboard(this, R.xml.alpha);
                break;
            case "2":
                mKeyboard = new Keyboard(this, R.xml.alphanumaric);
                break;
            case "3":
                mKeyboard = new Keyboard(this, R.xml.numeric);
                break;
        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.closing();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                inputConnection.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCapsLockEnabled = !isCapsLockEnabled;
                mKeyboard.setShifted(isCapsLockEnabled);
                mKeyboardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if(Character.isLetter(code) && isCapsLockEnabled){
                    code = Character.toUpperCase(code);
                }
                inputConnection.commitText(String.valueOf(code),1);
        }
    }



    @Override public void onPress(int primaryCode) {
        
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeUp() {

    }
}
