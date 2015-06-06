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
import android.inputmethodservice.*;
import android.inputmethodservice.Keyboard;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.util.List;

public class CustomKeyboardView extends KeyboardView {

    private Context mContext;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());

        String textColor = preferences.getString("textColor", "#ffffff");
        String buttonColor = preferences.getString("buttonColor", "#333333");
        String backgroundColor = preferences.getString("backgroundColor", "#000000");
        if (textColor == "") {
            textColor = "#ffffff";
        }
        if (buttonColor == "") {
            buttonColor = "#83838b";
        }
        if (backgroundColor == "") {
            backgroundColor = "#000000";
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
        if (textColor.length() < 2) {
            textColor = "#ffffff";
        }

        System.out.println(textColor);
        System.out.println(buttonColor);
        System.out.println(backgroundColor);

        ShapeDrawable background = new ShapeDrawable(new RectShape());
        background.getPaint().setColor(Color.parseColor(backgroundColor));
        background.setBounds((int) getX(), (int) getY(), (int) getX() + getWidth(), (int) getY() + getHeight());
        background.draw(canvas);

        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.parseColor(buttonColor));
        shape.getPaint().setStyle(Paint.Style.FILL);

        ShapeDrawable shape1 = new ShapeDrawable(new RectShape());
        shape1.getPaint().setColor(Color.parseColor(backgroundColor));
        shape1.getPaint().setStyle(Paint.Style.STROKE);
        shape1.getPaint().setStrokeWidth(getDensityPixels(5));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(getDip(40));
        paint.setColor(Color.parseColor(textColor));
        
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            System.out.println(key.label);
            if (key.label.equals("space") || key.label.equals("delete")) {
                shape.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                shape1.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                shape.draw(canvas);
                shape1.draw(canvas);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2), paint);
            } else if (key.label != null) {
                shape.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                shape1.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                shape.draw(canvas);
                shape1.draw(canvas);
                Rect areaRect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
                RectF bounds = new RectF(areaRect);
                bounds.right = paint.measureText(key.label.toString(), 0, key.label.toString().length());
                bounds.bottom = paint.descent() - paint.ascent();
                bounds.left += (areaRect.width() - bounds.right) / 2.0f;
                bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;
                paint.setColor(Color.parseColor(textColor));
                canvas.drawText(key.label.toString(), bounds.left + paint.descent(), bounds.top - paint.ascent(), paint);
            }
        }
    }

    float getDensityPixels(int pixels) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    int getDip(int MY_DIP_VALUE) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                MY_DIP_VALUE, getResources().getDisplayMetrics());
    }
}
