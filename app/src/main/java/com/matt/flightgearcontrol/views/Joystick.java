package com.matt.flightgearcontrol.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.matt.flightgearcontrol.Interfaces.Changeable;
import com.matt.flightgearcontrol.R;

public class Joystick extends View {
    private Paint paint;
    private int bigCircleX;
    private int bigCircleY;
    private double bigCircleRadius;
    private int smallCircleX;
    private int smallCircleY;
    private double smallCircleRadius;
    public Changeable onChange;

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.smallCircleRadius = 0.175f * Math.min(w, h);
        this.smallCircleX = w / 2;
        this.smallCircleY = h / 2;
        this.bigCircleRadius = 0.35 * Math.min(w, h);
        this.bigCircleX = w / 2;
        this.bigCircleY = h / 2;
        //Log.i("circle", "x : " + this.bigCircleX + " y: " + this.bigCircleY + " radius: " + this.bigCircleRadius);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_MOVE:
                moveOnTouch(touchX, touchY);
                this.invalidate();
                break;

            case MotionEvent.ACTION_UP:
                this.smallCircleY = this.bigCircleY;
                this.smallCircleX = this.bigCircleX;
                this.invalidate();
                break;
        }

        return true;
    }

    private void moveOnTouch(int x, int y) {
        double dis = Math.sqrt(Math.pow(x - this.bigCircleX, 2) + Math.pow(y - this.bigCircleY, 2));
        if (dis <= this.bigCircleRadius) {
            this.smallCircleX = x;
            this.smallCircleY = y;
        } else {
            if (x == this.bigCircleX) {
                if (y > this.bigCircleY) {
                    this.smallCircleY = this.bigCircleY + (int)this.bigCircleRadius;
                    this.smallCircleX = this.bigCircleX;
                } else {
                    this.smallCircleY = this.bigCircleY - (int)this.bigCircleRadius;
                    this.smallCircleX = this.bigCircleX;
                }
            } else {
                double angle = findAngle(x, y);
                //Log.i("joystick", "" + angle);
                if (angle <= 90) {
                    this.smallCircleY = (int)(this.bigCircleY + this.bigCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.smallCircleX = findX(this.smallCircleY, true);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(angle));
                } else if (angle <= 180) {
                    //Log.i("value", "" + this.bigCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.smallCircleY = (int)(this.bigCircleY + this.bigCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.smallCircleX = findX(this.smallCircleY, false);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(1  - angle));
                } else if (angle <= 270) {
                    //Log.i("ANGLE", "FUCK");
                    this.smallCircleY = (int)(this.bigCircleY - this.bigCircleRadius * Math.sin(Math.toRadians(angle - 180)));
                    this.smallCircleX = findX(this.smallCircleY, false);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(angle - 1));
                } else {
                    this.smallCircleY = (int)(this.bigCircleY - this.bigCircleRadius * Math.sin(Math.toRadians(360 - angle)));
                    this.smallCircleX = findX(this.smallCircleY, true);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(angle - 1.5));
                }
            }
        }

        //Log.i("joystick", )
        //TODO call onChange

        try {
            double a = (this.smallCircleX - this.bigCircleX) / (this.bigCircleRadius);
            double e = (this.smallCircleY - this.bigCircleY) / (this.bigCircleRadius);
            Log.i("A", "" + a);
            //Log.i("E", "" + e);
            onChange.onChange(a,e);
        } catch (Exception e) {

        }
    }

    private int findX(int y, boolean isPositive) {
        double x = Math.sqrt(Math.abs(Math.pow(this.bigCircleRadius, 2) - Math.pow(y - this.bigCircleY, 2)));
        if (isPositive) {
            x = x + this.bigCircleX;
            return (int)x;
        }
        x = this.bigCircleX - x;
        return (int)x;
    }

    private double findAngle(int x, int y) {
        double incline = (double)(this.bigCircleY - y) / (double)(this.bigCircleX - x);
        double deg = Math.toDegrees(Math.atan(Math.abs(incline)));
        if (x < this.bigCircleX && y > this.bigCircleY) {
            return 180 - deg;
        }
        if (x < this.bigCircleX && y < this.bigCircleY) {
            return 180 + deg;
        }
        if (x > this.bigCircleX && y < this.bigCircleY) {
            return 360 - deg;
        }
        return deg;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        //int color = ContextCompat.getColor(context, R.color.colorPrimary);
        //paint.setColor(getResources().getColor(R.color.material_on_background_disabled));
        paint.setColor(Color.parseColor("#2a0575"));
        canvas.drawCircle(this.bigCircleX, this.bigCircleY, (float)this.bigCircleRadius, paint);

        //paint.setColor(getResources().getColor(R.color.design_default_color_primary_dark));
        paint.setColor(Color.parseColor("#0aaaf5"));
        canvas.drawCircle(this.smallCircleX, this.smallCircleY, (float)this.smallCircleRadius, paint);

    }

}
