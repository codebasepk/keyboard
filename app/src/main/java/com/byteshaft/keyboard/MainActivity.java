package com.byteshaft.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class MainActivity extends InputMethodService implements
        KeyboardView.OnKeyboardActionListener {

    private KeyboardView mKeyboardView;
    private Keyboard mKeyboard;
    private boolean isCapsLockEnabled;

    @Override
    public View onCreateInputView() {
        mKeyboardView = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        mKeyboardView.setOnKeyboardActionListener(this);
        return mKeyboardView;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
                mKeyboard = new Keyboard(this, R.xml.numeric);
                break;
            case EditorInfo.TYPE_CLASS_PHONE:
                mKeyboard = new Keyboard(this, R.xml.alphanumaric);
                break;
            default:
                mKeyboard = new Keyboard(this, R.xml.alpha);
        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.closing();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                isCapsLockEnabled = !isCapsLockEnabled;
                mKeyboard.setShifted(isCapsLockEnabled);
                mKeyboardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if(Character.isLetter(code) && isCapsLockEnabled){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
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
