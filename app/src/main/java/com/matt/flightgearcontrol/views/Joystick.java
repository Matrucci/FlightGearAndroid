package com.matt.flightgearcontrol.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;

import com.matt.flightgearcontrol.Interfaces.Changeable;
import com.matt.flightgearcontrol.R;

public class Joystick extends View {
    public Changeable onChange;

    private int boundCircleX;
    private int boundCircleY;
    private double boundCircleRadius;

    private int joystickCircleX;
    private int joystickCircleY;
    private double joystickCircleRadius;

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

    public Joystick(Context context, @Nullable AttributeSet attrs,
                    int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.paint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.joystickCircleRadius = 0.14f * Math.min(w, h);
        this.joystickCircleX = w / 2;
        this.joystickCircleY = h / 2;

        this.boundCircleRadius = 0.42 * Math.min(w, h);
        this.boundCircleX = w / 2;
        this.boundCircleY = h / 2;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        //Getting the X and Y coordinates of the touch input.
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        int eventaction = event.getAction();

        switch (eventaction) {
            //The user moved the joystick.
            case MotionEvent.ACTION_MOVE:
                moveOnTouch(touchX, touchY);
                this.invalidate();
                break;

            //The user let go of the joystick
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

    /*************************************************
     * Moving the joystick according to the touch.
     * @param x - The X coordinate of the touch input.
     * @param y - The Y coordinate of the touch input.
     *************************************************/
    private void moveOnTouch(int x, int y) {

        double dis = Math.sqrt(Math.pow(x - this.boundCircleX, 2) +
                Math.pow(y - this.boundCircleY, 2));
        //Checking if the input is inside the circle.
        if (dis <= this.boundCircleRadius) {
            this.joystickCircleX = x;
            this.joystickCircleY = y;
        } else { //If it's outside the circle
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
                if (angle <= 90) {
                    this.joystickCircleY = (int)(this.boundCircleY + this.boundCircleRadius *
                            Math.sin(Math.toRadians(angle)));
                    this.joystickCircleX = findX(this.joystickCircleY, true);
                } else if (angle <= 180) {
                    this.joystickCircleY = (int)(this.boundCircleY + this.boundCircleRadius *
                            Math.sin(Math.toRadians(angle)));
                    this.joystickCircleX = findX(this.joystickCircleY, false);
                } else if (angle <= 270) {
                    this.joystickCircleY = (int)(this.boundCircleY - this.boundCircleRadius *
                            Math.sin(Math.toRadians(angle - 180)));
                    this.joystickCircleX = findX(this.joystickCircleY, false);
                } else {
                    this.joystickCircleY = (int)(this.boundCircleY - this.boundCircleRadius *
                            Math.sin(Math.toRadians(360 - angle)));
                    this.joystickCircleX = findX(this.joystickCircleY, true);
                }
            }
        }

        //Activating the onChange method to notify that the values have changed.
        try {
            double a = (this.joystickCircleX - this.boundCircleX) / (this.boundCircleRadius);
            double e = (this.joystickCircleY - this.boundCircleY) / (this.boundCircleRadius);
            e = -1 * e;
            onChange.onChange(a,e);
        } catch (Exception e) {

        }
    }

    /*****************************************************************************************
     * Returning the X value of the circle circumference given the Y value
     * and a flag to specify if the X we're looking for is above or below the center.
     * @param y - The Y value of the point on the circumference.
     * @param isPositive - A flag to specify if the X we're looking for is above the center.
     * @return - The X value on the circumference.
     ******************************************************************************************/
    private int findX(int y, boolean isPositive) {
        double x = Math.sqrt(Math.abs(
                Math.pow(this.boundCircleRadius, 2) - Math.pow(y - this.boundCircleY, 2)));
        if (isPositive) {
            x = x + this.boundCircleX;
            return (int)x;
        }
        x = this.boundCircleX - x;
        return (int)x;
    }

    /*****************************************************************************************
     * Finds the angle between the line that starts at (x,y) and ends at the joystick circle
     * and the x axis.
     * @param x - The X coordinate.
     * @param y - The Y coordinate.
     * @return - The angle between the lines.
     *****************************************************************************************/
    private double findAngle(int x, int y) {
        //Checking the incline
        double incline = (double)(this.boundCircleY - y) / (double)(this.boundCircleX - x);
        //Using arctan to get the angle.
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

    /***************************************
     * Draws the view on the canvas.
     * @param canvas - The drawing canvas.
     **************************************/
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        //Outer circle (bounds).
        paint.setColor(Color.parseColor("#2a0575"));
        canvas.drawCircle(this.boundCircleX, this.boundCircleY,
                (float)this.boundCircleRadius, paint);

        //Inner circle (joystick itself).
        paint.setColor(Color.parseColor("#0aaaf5"));
        canvas.drawCircle(this.joystickCircleX, this.joystickCircleY,
                (float)this.joystickCircleRadius, paint);

        //Joystick arrows.
        paint.setColor(Color.parseColor("#0aaaf5"));
        paint.setStrokeWidth(5);
        float partRad = (float)(7 * boundCircleRadius) / 8;

        //Top arrow.
        canvas.drawLine(this.boundCircleX, this.boundCircleY - partRad,
                this.boundCircleX - 70, this.boundCircleY - partRad + 70, paint);
        canvas.drawLine(this.boundCircleX, this.boundCircleY - partRad,
                this.boundCircleX + 70, this.boundCircleY - partRad + 70, paint);

        //Bottom arrow.
        canvas.drawLine(this.boundCircleX, this.boundCircleY + partRad,
                this.boundCircleX - 70, this.boundCircleY + partRad - 70, paint);
        canvas.drawLine(this.boundCircleX, this.boundCircleY + partRad,
                this.boundCircleX + 70, this.boundCircleY + partRad - 70, paint);

        //Right arrow.
        canvas.drawLine(this.boundCircleX + partRad, this.boundCircleY,
                this.boundCircleX - 70 + partRad, this.boundCircleY - 70, paint);
        canvas.drawLine(this.boundCircleX + partRad, this.boundCircleY,
                this.boundCircleX - 70 + partRad, this.boundCircleY + 70, paint);

        //Left arrow.
        canvas.drawLine(this.boundCircleX - partRad, this.boundCircleY,
                this.boundCircleX + 70 - partRad, this.boundCircleY - 70, paint);
        canvas.drawLine(this.boundCircleX - partRad, this.boundCircleY,
                this.boundCircleX + 70 - partRad, this.boundCircleY + 70, paint);

    }

}
