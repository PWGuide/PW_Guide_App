package com.example.pwguide.navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pwguide.R;
import com.example.pwguide.dijkstraAlgorithm.Vertex;

import java.util.List;

public class NavigationCanvas extends View {

    private Paint paint;
    private int width, height;
    private float scale;
    private int translateX, translateY;
    private double dx, dy;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureListener;
    private boolean isScaled = false;
    private List<Vertex> path;
    private Path pathToDraw;
    private Bitmap bitmap;
    private final BitmapFactory.Options options;
    private Rect src, dest;
    private int floor;
    private final int radius = 15;
    private Context context;
    private LinearGradient linearGradient;
    private Vertex startPoint, endPoint;
    private ImageButton up, down;
    private String building = "mini_";

    public NavigationCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        options = new BitmapFactory.Options();
        options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

        paint = new Paint();
        scale = 1;
        pathToDraw = new Path();
        up = new ImageButton(context);
        down = new ImageButton(context);
        up.setBackground(null);
        down.setBackground(null);
        down.setImageDrawable(context.getDrawable(R.drawable.arrow_down));
        up.setImageDrawable(context.getDrawable(R.drawable.arrow_up));

        up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                floor++;
                final int resourceId = context.getResources().getIdentifier(building + floor, "drawable",
                        context.getPackageName());

                if (resourceId != 0) {
                    loadImage(resourceId, context);
                    initBitmap();
                    restartDraw();
                    invalidate();
                } else {
                    floor--;
                }
            }
        });

        down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floor > 0) {
                    floor--;
                    final int resourceId = context.getResources().getIdentifier(building + floor, "drawable",
                            context.getPackageName());
                    loadImage(resourceId, context);
                    initBitmap();
                    restartDraw();
                    invalidate();
                }
            }
        });

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureListener = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                restartDraw();
                return super.onDoubleTap(e);
            }
        });

    }

    private void restartDraw() {
        scale = 1.0f;
        width = src.width();
        height = src.height();
        translateX = getWidth() / 2 - width / 2;
        translateY = getHeight() / 2 - height / 2;
        createPath();
        calculateBitmapCoor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (src == null) {
            initBitmap();
        }
        createPath();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#FAF6EB"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        canvas.drawBitmap(bitmap, null, dest, paint);

        if (pathToDraw != null) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8 * scale);
            paint.setShader(linearGradient);
            canvas.drawPath(pathToDraw, paint);
            paint.setShader(null);

            if (startPoint != null) {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(Color.parseColor("#e86413"));
                canvas.drawOval((float) (translateX + startPoint.getX() * scale - radius * scale / 2),
                        (float) (translateY + startPoint.getY() * scale - radius * scale / 2),
                        (float) (translateX + startPoint.getX() * scale + radius * scale / 2),
                        (float) (translateY + startPoint.getY() * scale + radius * scale / 2), paint);
            }
            if (endPoint != null) {
                paint.setColor(Color.parseColor("#7726d4"));
                canvas.drawOval((float) (translateX + endPoint.getX() * scale - radius * scale / 2),
                        (float) (translateY + endPoint.getY() * scale - radius * scale / 2),
                        (float) (translateX + endPoint.getX() * scale + radius * scale / 2),
                        (float) (translateY + endPoint.getY() * scale + radius * scale / 2), paint);
            }
        }
    }

    private void createPath() {
        pathToDraw.reset();

        if (path != null) {
            boolean first = true;
            startPoint = null;
            endPoint = null;
            for (Vertex v : path) {
                if (v.getFloor() == floor && v.getName().matches("^[skw]+[0-9]+$")) {
                    if (first) {
                        startPoint = v;
                        pathToDraw.moveTo(translateX + (float) v.getX() * scale, translateY + (float) v.getY() * scale);
                        first = false;
                    } else {
                        pathToDraw.lineTo(translateX + (float) v.getX() * scale, translateY + (float) v.getY() * scale);
                        endPoint = v;
                    }
                    if (v.getName().matches("^s[0-9]+$")) {
                        up.setScaleX(scale);
                        up.setScaleY(scale);
                        up.setX(translateX + (float) (v.getX() + 50) * scale + getX() - up.getLayoutParams().width / 2);
                        up.setY(translateY + (float) (v.getY() - 50) * scale + getY() - up.getLayoutParams().height / 2);
                        down.setScaleX(scale);
                        down.setScaleY(scale);
                        down.setX(translateX + (float) (v.getX() - 50) * scale + getX() - down.getLayoutParams().width / 2);
                        down.setY(translateY + (float) (v.getY() - 50) * scale + getY() - down.getLayoutParams().height / 2);
                    }
                }
            }
            if (first) {
                up.setScaleX(scale);
                up.setScaleY(scale);
                up.setX(getX() + 100);
                up.setY(getY());
                down.setScaleX(scale);
                down.setScaleY(scale);
                down.setX(getX());
                down.setY(getY());
            }

            if (startPoint != null && endPoint != null) {
                linearGradient = new LinearGradient(translateX + (float) startPoint.getX() * scale,
                        translateY + (float) startPoint.getY() * scale,
                        translateX + (float) endPoint.getX() * scale,
                        translateY + (float) endPoint.getY() * scale,
                        Color.parseColor("#e86413"), Color.parseColor("#7726d4"), Shader.TileMode.MIRROR);
            }
        }
    }

    private void initBitmap() {
        translateX = getWidth() / 2 - width / 2;
        translateY = getHeight() / 2 - height / 2;

        src = new Rect(translateX, translateY, translateX + width, translateY + height);

        calculateBitmapCoor();
    }

    private void calculateBitmapCoor() {
        dest = new Rect(translateX, translateY, translateX + width, translateY + height);
    }

    private void loadImage(@DrawableRes int id, Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        width = bitmap != null ? bitmap.getWidth() : 0;
        height = bitmap != null ? bitmap.getHeight() : 0;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        mGestureListener.onTouchEvent(motionEvent);
        if (motionEvent.getPointerCount() < 2) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dx = motionEvent.getRawX();
                    dy = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isScaled) {
                        translateX += (int) (motionEvent.getRawX() - dx);
                        translateY += (int) (motionEvent.getRawY() - dy);

                        dx = motionEvent.getRawX();
                        dy = motionEvent.getRawY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isScaled = false;
            }
            calculateDrawingPosition();
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
            scale = Math.max(0.5f,
                    Math.min(scale, 4.0f));
            int prevHeight = height;
            int prevWidth = width;
            width = (int) (src.width() * scale);
            height = (int) (src.height() * scale);

            translateX += (prevWidth - width) / 2;
            translateY += (prevHeight - height) / 2;

            calculateDrawingPosition();

            return true;
        }
    }

    private void calculateDrawingPosition() {
        dest.left = translateX;
        dest.right = translateX + width;
        dest.bottom = translateY + height;
        dest.top = translateY;
    }

    public void setPath(List<Vertex> path) {
        this.path = path;
        for (Vertex v : path) {
            System.out.println(v.getName());
        }
        floor = path.get(0).getFloor();

        final int resourceId = context.getResources().getIdentifier(building + floor, "drawable",
                context.getPackageName());

        loadImage(resourceId, context);
    }

    public void addButton(ConstraintLayout layout) {
        layout.addView(up);
        layout.addView(down);
        up.getLayoutParams().height = 130;
        up.getLayoutParams().width = 130;
        down.getLayoutParams().height = 130;
        down.getLayoutParams().width = 130;
        down.setScaleType(ImageView.ScaleType.FIT_XY);
        up.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
