package ryl.alarm;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MyCustomerView extends View {

    /**
     * 圆点画笔
     */
    Paint pointPaint;
    /**
     * 圆弧画笔
     */
    Paint arcPaint;
    /**
     * 图片画笔
     */
    Paint bitmapPaint;
    /**
     * 彩色画笔
     */
    Paint shaderPaint;

    /**
     * 自定义的Bitmap
     */
    private Bitmap mBitmap;
    /**
     * 自定义的画布，目的是为了能画出重叠的效果
     */
    private Canvas mCanvas;
    /**
     * 箭头图片
     */
    Bitmap mArrowBitmap;

    /**
     * 控件宽度
     */
    int width;
    /**
     * 控件高度
     */
    int height;
    /**
     * 圆弧外边半径
     */
    int radius;
    /**
     * 圆弧画笔宽度
     */
    int paintWidth;

    /**
     * 圆点间隔的角度
     */
    float spaceAngle = 15;
    /**
     * 当前的角度
     */
    float currentAgent = 90;
    /**
     * 当前百分比
     */
    float currentPercent = 0;

    ValueAnimator valueAnimator;

    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    // 默认的动效周期 2s
    private int defaultDuration = 2000;


    public MyCustomerView(Context context) {
        super(context);
        initView(context);
    }

    public MyCustomerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyCustomerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        pointPaint = new Paint();
        pointPaint.setStrokeWidth(3);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setAntiAlias(true);

        bitmapPaint = new Paint();
        bitmapPaint.setStyle(Paint.Style.FILL);
        bitmapPaint.setAntiAlias(true);

        shaderPaint = new Paint();

        mArrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.point);
        mMatrix = new Matrix();
    }

    /**
     * 设置进度百分比
     *
     * @param percent 百分比
     */
    public void setPercent(float percent) {
        //两边监测
        if (percent < 1) {
            percent = 1;
        } else if (percent > 200) {
            percent = 200;
        }
        currentPercent = percent;

        valueAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(defaultDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                currentAgent = value * currentPercent * 1.8f;
                invalidate();
            }
        });
        valueAnimator.start();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();

        paintWidth = width / 4;
        radius = width / 4 + paintWidth / 2;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        drawPoint(mCanvas);

        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE); //设置空心
        arcPaint.setStrokeWidth(paintWidth);
        arcPaint.setAntiAlias(true);
        arcPaint.setColor((Color.parseColor("#B9B9B9")));

        RectF rectF = new RectF(paintWidth / 2, paintWidth / 2, width - paintWidth / 2, width - paintWidth / 2);
        canvas.drawArc(rectF, 179, 182, false, arcPaint); //绘制半圆底色

        mMatrix.reset();
        mMatrix.postTranslate(width / 2 - 90, radius + paintWidth / 2 - 30);   // 将图片绘制中心调整到与当前点重合
        mCanvas.rotate(currentAgent, width / 2, radius + paintWidth / 2);
        mCanvas.drawBitmap(mArrowBitmap, mMatrix, bitmapPaint);
        mCanvas.save();
        drawPercentColor(canvas, rectF);
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    /**
     * 绘制过渡色背景
     *
     * @param canvas
     * @param rectF
     */
    private void drawPercentColor(Canvas canvas, RectF rectF) {
        shaderPaint.setAntiAlias(true);
        shaderPaint.setStyle(Paint.Style.STROKE);
        shaderPaint.setStrokeWidth(paintWidth);
        shaderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));//shaderPaint.setColor(yellowColor);
        SweepGradient sweepGradient = new SweepGradient(width / 2, radius + paintWidth / 2 + 10, new int[]{
                Color.parseColor("#ccffff"), Color.parseColor("#00ff99"),
        }, new float[]{0, 1F,
        });
        shaderPaint.setShader(sweepGradient);

        canvas.drawArc(rectF, 179, currentAgent + 2, false, shaderPaint);

    }

    private void drawPoint(Canvas canvas) {
        for (int i = 0; i <= 10; i++) {
            canvas.save();
            canvas.rotate(i * spaceAngle - 89.5f + spaceAngle, width / 2, paintWidth / 2 + radius);
            canvas.drawCircle(width / 2, paintWidth / 2, 8, pointPaint);
            canvas.restore();
        }
    }
}
