package com.byteshaft.keyboard;


import android.app.Application;

public class AppGlobals extends Application {

    public static final int TEN_SECONDS = 10000;
    private static boolean sIsDebugModeOn;
    private static boolean sIsDebugShiftOn;
    private static boolean sLongPressed;

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

    public static boolean isLongPressed() {
        return sLongPressed;
    }

    public static void setIsLongPressed(boolean flag) {
        sLongPressed = flag;
    }
}
