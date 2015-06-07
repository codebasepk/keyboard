package com.byteshaft.keyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.inputmethodservice.*;
import android.inputmethodservice.Keyboard;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.util.List;

public class CustomKeyboardView extends KeyboardView {

    private Context mContext;
    private ShapeDrawable mButtonInner;
    private ShapeDrawable mButtonStroke;
    private Paint mPaint;
    private SharedPreferences mPreferences;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
     }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String textColor = mPreferences.getString("textColor", "#ffffff");
        String buttonColor = mPreferences.getString("buttonColor", "#333333");
        String backgroundColor = mPreferences.getString("backgroundColor", "#000000");
        String popupColor = mPreferences.getString("popupColor", "#a8a8a8");

        if (textColor == "") {
            textColor = "#ffffff";
        }
        if (buttonColor == "") {
            buttonColor = "#333333";
        }
        if (backgroundColor == "") {
            backgroundColor = "#000000";
        }
        if (popupColor == "") {
            popupColor = "#a8a8a8";
        }
        if (!textColor.startsWith("#")) {
            textColor = "#" + textColor;
        }
        if (!buttonColor.startsWith("#")) {
            buttonColor = "#" + buttonColor;
        }
        if (!backgroundColor.startsWith("#")) {
            backgroundColor = "#" + backgroundColor;
        }
        if (!popupColor.startsWith("#")) {
            popupColor = "#" + popupColor;
        }
        try {
            drawKeyboardBackground(canvas, backgroundColor);
        } catch (Exception e) {
            e.printStackTrace();
            mPreferences.edit().putString("backgroundColor", null).apply();
        }

        if (mButtonInner == null) {
            mButtonInner = new ShapeDrawable(new RectShape());
            mButtonInner.getPaint().setStyle(Paint.Style.FILL);
        }
        try {
            mButtonInner.getPaint().setColor(Color.parseColor(buttonColor));
        } catch (Exception e) {
            e.printStackTrace();
            mPreferences.edit().putString("buttonColor", null).apply();
        }

        if (mButtonStroke == null) {
            mButtonStroke = new ShapeDrawable(new RoundRectShape(getEightEdgeArrayForCurve(), null, null));
            mButtonStroke.getPaint().setStyle(Paint.Style.STROKE);
            mButtonStroke.getPaint().setStrokeWidth(getDensityPixels(5));
            mButtonStroke.getPaint().setAntiAlias(true);
        }

        try {
            mButtonStroke.getPaint().setColor(Color.parseColor(backgroundColor));
        } catch (Exception e) {
            e.printStackTrace();
            mPreferences.edit().putString("backgroundColor", null).apply();
        }

        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setTextAlign(Paint.Align.CENTER);
            int scaledSize = getResources().getDimensionPixelSize(R.dimen.myFontSize);
            mPaint.setTextSize(scaledSize);
        }
        try {
            mPaint.setColor(Color.parseColor(textColor));
        } catch (Exception e) {
            e.printStackTrace();
            mPreferences.edit().putString("textColor", null).apply();
        }
        int scaledSize = getResources().getDimensionPixelSize(R.dimen.myFontSize);
        mPaint.setTextSize(scaledSize);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.label.equals("space")) {
                mButtonInner.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                mButtonStroke.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                mButtonInner.draw(canvas);
                mButtonStroke.draw(canvas);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + getDensityPixels(8), mPaint);
            } else if (key.label != null && !key.label.equals("←")) {
                mButtonInner.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                mButtonStroke.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                mButtonInner.draw(canvas);
                mButtonStroke.draw(canvas);
                RectF bounds = getTextAreaBoundsForKey(key);
                canvas.drawText(key.label.toString(), bounds.left + mPaint.descent(), bounds.top - mPaint.ascent(), mPaint);
            } else if (key.label.equals("←")) {
                mButtonInner.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                mButtonStroke.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                mButtonInner.draw(canvas);
                mButtonStroke.draw(canvas);
                mPaint.setTextSize(200);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + getDensityPixels(16), mPaint);
            }
            if (key.pressed) {
                ShapeDrawable buttonStateNormal = new ShapeDrawable(new RectShape());
                try {
                    buttonStateNormal.getPaint().setColor(Color.parseColor(popupColor));
                } catch (Exception e) {
                    e.printStackTrace();
                    mPreferences.edit().putString("popupColor", null).apply();
                }
                buttonStateNormal.getPaint().setStyle(Paint.Style.FILL);
                buttonStateNormal.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                buttonStateNormal.draw(canvas);
                mButtonStroke.draw(canvas);
                if (key.label.equals("space")) {
                    canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + getDensityPixels(8), mPaint);
                } else if (key.label.equals("←")) {
                    mPaint.setTextSize(200);
                    canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2) + getDensityPixels(16), mPaint);
                } else {
                    RectF bounds = getTextAreaBoundsForKey(key);
                    canvas.drawText(key.label.toString(), bounds.left + mPaint.descent(), bounds.top - mPaint.ascent(), mPaint);
                }
            }
        }
    }

    float getDensityPixels(int pixels) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    private void drawKeyboardBackground(Canvas canvas, String colour) {
        ShapeDrawable background = new ShapeDrawable(new RectShape());
        background.getPaint().setColor(Color.parseColor(colour));
        background.setBounds((int) getX(), (int) getY(), (int) getX() + getWidth(), (int) getY() + getHeight());
        background.draw(canvas);
    }

    private float[] getEightEdgeArrayForCurve() {
        int radius = (int) getDensityPixels(4);
        return new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
    }

    private RectF getTextAreaBoundsForKey(Keyboard.Key key) {
        Rect areaRect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
        RectF bounds = new RectF(areaRect);
        bounds.right = mPaint.measureText(key.label.toString(), 0, key.label.toString().length());
        bounds.bottom = mPaint.descent() - mPaint.ascent();
        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;
        return bounds;
    }
}
