package com.matt.flightgearcontrol.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.Nullable;

import com.matt.flightgearcontrol.R;

public class Joystick extends View {
    private Paint paint;

    public Joystick(Context context) {
        super(context);
        this.paint = new Paint();
    }

    public Joystick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
    }

    public Joystick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paint = new Paint();
    }

    public Joystick(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        int radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = pl + (usableWidth / 2);
        int cy = pt + (usableHeight / 2);


        paint.setColor(getResources().getColor(R.color.design_default_color_primary_dark));
        canvas.drawCircle(cx, cy, radius, paint);
    }
}
