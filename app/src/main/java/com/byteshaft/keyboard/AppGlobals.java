package com.byteshaft.keyboard;


import android.app.Application;

public class AppGlobals extends Application {

    public static final int FIVE_SECONDS = 5000;
    private static boolean sIsDebugModeOn;
    private static boolean sIsDebugShiftOn;

    public static void setDebugModeOn(boolean enable) {
        sIsDebugModeOn = enable;
    }

    public static boolean isDebugModeOn() {
        return sIsDebugModeOn;
    }

    public static void setDebugShiftOn(boolean on) {
        sIsDebugShiftOn = on;
    }

    public static boolean isDebugShiftOn() {
        return sIsDebugShiftOn;
    }

}
