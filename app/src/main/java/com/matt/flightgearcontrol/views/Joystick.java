package com.matt.flightgearcontrol.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;

import com.matt.flightgearcontrol.Interfaces.Changeable;

public class Joystick extends View {
    private Paint paint;
    private int boundCircleX;
    private int boundCircleY;
    private double boundCircleRadius;
    private int joystickCircleX;
    private int joystickCircleY;
    private double joystickCircleRadius;
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

        //this.joystickCircleRadius = 0.175f * Math.min(w, h);
        this.joystickCircleRadius = 0.14f * Math.min(w, h);
        this.joystickCircleX = w / 2;
        this.joystickCircleY = h / 2;
        //this.boundCircleRadius = 0.4 * Math.min(w, h);
        this.boundCircleRadius = 0.42 * Math.min(w, h);
        this.boundCircleX = w / 2;
        this.boundCircleY = h / 2;
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
                this.joystickCircleY = this.boundCircleY;
                this.joystickCircleX = this.boundCircleX;
                try {
                    onChange.onChange(0,0);
                } catch (Exception e) {

                }
                this.invalidate();
                break;
        }

        return true;
    }

    private void moveOnTouch(int x, int y) {
        double dis = Math.sqrt(Math.pow(x - this.boundCircleX, 2) + Math.pow(y - this.boundCircleY, 2));
        if (dis <= this.boundCircleRadius) {
            this.joystickCircleX = x;
            this.joystickCircleY = y;
        } else {
            if (x == this.boundCircleX) {
                if (y > this.boundCircleY) {
                    this.joystickCircleY = this.boundCircleY + (int)this.boundCircleRadius;
                    this.joystickCircleX = this.boundCircleX;
                } else {
                    this.joystickCircleY = this.boundCircleY - (int)this.boundCircleRadius;
                    this.joystickCircleX = this.boundCircleX;
                }
            } else {
                double angle = findAngle(x, y);
                //Log.i("joystick", "" + angle);
                if (angle <= 90) {
                    this.joystickCircleY = (int)(this.boundCircleY + this.boundCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.joystickCircleX = findX(this.joystickCircleY, true);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(angle));
                } else if (angle <= 180) {
                    //Log.i("value", "" + this.bigCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.joystickCircleY = (int)(this.boundCircleY + this.boundCircleRadius * Math.sin(Math.toRadians(angle)));
                    this.joystickCircleX = findX(this.joystickCircleY, false);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(1  - angle));
                } else if (angle <= 270) {
                    //Log.i("ANGLE", "FUCK");
                    this.joystickCircleY = (int)(this.boundCircleY - this.boundCircleRadius * Math.sin(Math.toRadians(angle - 180)));
                    this.joystickCircleX = findX(this.joystickCircleY, false);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(angle - 1));
                } else {
                    this.joystickCircleY = (int)(this.boundCircleY - this.boundCircleRadius * Math.sin(Math.toRadians(360 - angle)));
                    this.joystickCircleX = findX(this.joystickCircleY, true);
                    //this.smallCircleX = (int)(this.bigCircleRadius * Math.cos(angle - 1.5));
                }
            }
        }


        try {
            double a = (this.joystickCircleX - this.boundCircleX) / (this.boundCircleRadius);
            double e = (this.joystickCircleY - this.boundCircleY) / (this.boundCircleRadius);
            e = -1 * e;
            //Log.i("A", "" + a);
            //Log.i("E", "" + e);
            onChange.onChange(a,e);
        } catch (Exception e) {

        }
    }

    private int findX(int y, boolean isPositive) {
        double x = Math.sqrt(Math.abs(Math.pow(this.boundCircleRadius, 2) - Math.pow(y - this.boundCircleY, 2)));
        if (isPositive) {
            x = x + this.boundCircleX;
            return (int)x;
        }
        x = this.boundCircleX - x;
        return (int)x;
    }

    private double findAngle(int x, int y) {
        double incline = (double)(this.boundCircleY - y) / (double)(this.boundCircleX - x);
        double deg = Math.toDegrees(Math.atan(Math.abs(incline)));
        if (x < this.boundCircleX && y > this.boundCircleY) {
            return 180 - deg;
        }
        if (x < this.boundCircleX && y < this.boundCircleY) {
            return 180 + deg;
        }
        if (x > this.boundCircleX && y < this.boundCircleY) {
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
        canvas.drawCircle(this.boundCircleX, this.boundCircleY, (float)this.boundCircleRadius, paint);

        //paint.setColor(getResources().getColor(R.color.design_default_color_primary_dark));
        paint.setColor(Color.parseColor("#0aaaf5"));
        canvas.drawCircle(this.joystickCircleX, this.joystickCircleY, (float)this.joystickCircleRadius, paint);

        paint.setColor(Color.parseColor("#0aaaf5"));
        paint.setStrokeWidth(5);
        float partRad = (float)(7 * boundCircleRadius) / 8;
        canvas.drawLine(this.boundCircleX, this.boundCircleY - partRad, this.boundCircleX - 70, this.boundCircleY - partRad + 70, paint);
        canvas.drawLine(this.boundCircleX, this.boundCircleY - partRad, this.boundCircleX + 70, this.boundCircleY - partRad + 70, paint);
        canvas.drawLine(this.boundCircleX, this.boundCircleY + partRad, this.boundCircleX - 70, this.boundCircleY + partRad - 70, paint);
        canvas.drawLine(this.boundCircleX, this.boundCircleY + partRad, this.boundCircleX + 70, this.boundCircleY + partRad - 70, paint);

        canvas.drawLine(this.boundCircleX + partRad, this.boundCircleY, this.boundCircleX - 70 + partRad, this.boundCircleY - 70, paint);
        canvas.drawLine(this.boundCircleX + partRad, this.boundCircleY, this.boundCircleX - 70 + partRad, this.boundCircleY + 70, paint);
        canvas.drawLine(this.boundCircleX - partRad, this.boundCircleY, this.boundCircleX + 70 - partRad, this.boundCircleY - 70, paint);
        canvas.drawLine(this.boundCircleX - partRad, this.boundCircleY, this.boundCircleX + 70 - partRad, this.boundCircleY + 70, paint);

    }

}
