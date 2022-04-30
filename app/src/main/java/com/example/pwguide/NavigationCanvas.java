package com.example.pwguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class NavigationCanvas extends View {

    private Paint paint;
    private Drawable mCustomImage;
    private int width, height;
    private float scale;
    private int translateX, translateY;
    private int translateXMax, translateXMin, translateYMax, translateYMin;
    private double dx,dy;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureListener;
    private boolean isScaled = false;

    public NavigationCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        scale = 1;
        translateX = 0;
        translateY = 0;
        loadImage(R.drawable.mini_0);
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureListener = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                scale = 1.0f;
                translateX = 0;
                translateY = 0;
                return super.onDoubleTap(e);
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        paint.setColor(Color.parseColor("#FAF6EB"));
        paint.setStrokeWidth(20);
        canvas.drawLine(50, 100, 600, 600, paint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        mCustomImage.setBounds(translateX + getWidth() / 2 - (int) (width * scale) / 2, translateY + getHeight() / 2 - (int) (height * scale) / 2,
                translateX + getWidth() / 2 + (int) (width * scale) / 2, translateY + getHeight() / 2 + (int) (height * scale) / 2);
        mCustomImage.draw(canvas);
    }

    private void loadImage(@DrawableRes int id) {
        mCustomImage = ResourcesCompat.getDrawable(getResources(), id, null);
        width = mCustomImage != null ? mCustomImage.getIntrinsicWidth() : 0;
        height = mCustomImage != null ? mCustomImage.getIntrinsicHeight() : 0;
        translateXMax = translateX + getWidth() / 2 + (int) (width * scale) / 2 + 100;
        translateXMin = translateX + getWidth() / 2 - (int) (width * scale) / 2 - 100;
        translateYMax = translateY + getHeight() / 2 + (int) (height * scale) / 2 + 100;
        translateYMin = translateY + getHeight() / 2 - (int) (height * scale) / 2 - 100;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        mGestureListener.onTouchEvent(motionEvent);
        if(motionEvent.getPointerCount() < 2) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dx = motionEvent.getRawX();
                    dy = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!isScaled) {
                        translateX += (int) (motionEvent.getRawX() - dx);
                        translateY += (int) (motionEvent.getRawY() - dy);
                        if (translateX < translateXMin || translateX > translateXMax) {
                            translateX -= (int) (motionEvent.getRawX() - dx);
                        }
                        if (translateY < translateYMin || translateY > translateYMax) {
                            translateY -= (int) (motionEvent.getRawY() - dy);
                        }
                        dx = motionEvent.getRawX();
                        dy = motionEvent.getRawY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isScaled = false;
            }
        } else {
            isScaled = true;
        }

        invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            scale *= scaleGestureDetector.getScaleFactor();
            scale = Math.max(0.1f,
                    Math.min(scale, 3.0f));
            return true;
        }
    }

}
