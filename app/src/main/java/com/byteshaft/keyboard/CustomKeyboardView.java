package com.byteshaft.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.inputmethodservice.*;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.util.List;

public class CustomKeyboardView extends KeyboardView {

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ShapeDrawable background = new ShapeDrawable(new RectShape());
        background.getPaint().setColor(Color.BLACK);
        background.setBounds((int) getX(), (int) getY(), (int) getX() + getWidth(), (int) getY() + getHeight());
        background.draw(canvas);

        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.DKGRAY);
        shape.getPaint().setStyle(Paint.Style.FILL);

        ShapeDrawable shape1 = new ShapeDrawable(new RectShape());
        shape1.getPaint().setColor(Color.BLACK);
        shape1.getPaint().setStyle(Paint.Style.STROKE);
        shape1.getPaint().setStrokeWidth(getDensityPixels(10));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(75);
        paint.setColor(Color.WHITE);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.label != null) {
                shape.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                shape1.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                shape.draw(canvas);
                shape1.draw(canvas);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2), paint);
            } else {
                key.icon.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                key.icon.draw(canvas);
            }
        }
    }

    float getDensityPixels(int pixels) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }
}
