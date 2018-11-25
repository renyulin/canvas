package ryl.alarm;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class RunningView extends View {
    /**
     * 圆弧的宽度
     */
    private float borderWidth = 20f;
    /**
     * 画步数的数值的字体大小
     */
    private float numberTextSize = 0;
    /**
     * 步数
     */
    private String stepNumber = "0";
    /**
     * 开始绘制圆弧的角度
     */
    private float startAngle = -90;
    /**
     * 终点对应的角度和起始点对应的角度的夹角
     */
    private float angleLength = 360;
    /**
     * 所要绘制的当前步数的红色圆弧终点到起点的夹角
     */
    private float currentAngleLength = 0;
    /**
     * 动画时长
     */
    private int animationLength = 1000;

    public RunningView(Context context) {
        super(context);
    }

    public RunningView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RunningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**中心点的x坐标*/
        float centerX = (getWidth()) / 2;
        //绘制中心圆的颜色
        drawCenterArcGray(canvas, centerX);
        /**指定圆弧的外轮廓矩形区域*/
        RectF rectF = new RectF(0 + borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);
        /**【第一步】绘制整体的黄色圆弧*/
        drawArcGray(canvas, rectF);
        /**【第二步】绘制当前进度的蓝色圆弧*/
        drawArcRed(canvas, rectF);
        /**【第三步】绘制"步数"的红色数字*/
        drawTextStepString(canvas, centerX);
        /**【第四步】绘制当前进度的红色数字*/
        drawTextNumber(canvas, centerX);
//        canvas.restore();
    }

    /**
     * 1.绘制中心圆颜色
     *
     * @param canvas  画笔
     * @param centerX
     */
    private void drawCenterArcGray(Canvas canvas, float centerX) {
        RectF rectF = new RectF(0 + borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.center_color));
        /** 结合处为圆弧*/
        paint.setStrokeJoin(Paint.Join.ROUND);
        /** 设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形*/
        paint.setStrokeCap(Paint.Cap.ROUND);
        /** 设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE  ：填充内部和描边;  Paint.Style.STROKE  ：仅描边*/
        paint.setStyle(Paint.Style.FILL);
        /**抗锯齿功能*/
        paint.setAntiAlias(true);
        /**设置画笔宽度*/
        paint.setStrokeWidth((centerX - borderWidth) / 2);
        /**绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);
    }

    /**
     * 1.绘制总步数的灰色圆弧
     *
     * @param canvas 画笔
     * @param rectF  参考的矩形
     */
    private void drawArcGray(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.running));
        /** 结合处为圆弧*/
        paint.setStrokeJoin(Paint.Join.ROUND);
        /** 设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形*/
        paint.setStrokeCap(Paint.Cap.ROUND);
        /** 设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE  ：填充内部和描边;  Paint.Style.STROKE  ：仅描边*/
        paint.setStyle(Paint.Style.STROKE);
        /**抗锯齿功能*/
        paint.setAntiAlias(true);
        /**设置画笔宽度*/
        paint.setStrokeWidth(borderWidth);
        /**绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);
    }

    /**
     * 2.绘制当前步数的绿色圆弧
     */
    private void drawArcRed(Canvas canvas, RectF rectF) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStrokeJoin(Paint.Join.ROUND);
        paintCurrent.setStrokeCap(Paint.Cap.ROUND);//圆角弧度
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(borderWidth);//设置画笔宽度
        paintCurrent.setColor(getResources().getColor(R.color.running_green));//设置画笔颜色
        canvas.drawArc(rectF, startAngle, currentAngleLength, false, paintCurrent);
    }

    /**
     * 3.圆环中心的步数
     */
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setTextSize(dipToPx(15));
        vTextPaint.setColor(getResources().getColor(R.color.little_gray));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        vTextPaint.setTypeface(font);//字体风格
        Rect bounds_Number = new Rect();
        vTextPaint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        canvas.drawText(stepNumber, centerX - bounds_Number.left, getHeight() / 2 + bounds_Number.height() / 2 - bounds_Number.top, vTextPaint);
    }

    /**
     * 4.圆环中心[步数]的文字
     */
    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dipToPx(15));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.little_gray));
        String stepString = "步数";
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, centerX - bounds.left, getHeight() / 2 - bounds.height() - bounds.top, vTextPaint);
    }

    /**
     * 获取当前步数的数字的高度
     *
     * @param fontSize 字体大小
     * @return 字体高度
     */
    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 所走的步数进度
     *
     * @param totalStepNum  设置的步数
     * @param currentCounts 所走步数
     */
    public void setCurrentCount(int startAngleNum, int totalStepNum, int currentCounts) {
        stepNumber = currentCounts + "";
        setTextSize(currentCounts);
        /**如果当前走的步数超过总步数则还是圆*/
        if (currentCounts > totalStepNum) {
            currentCounts = totalStepNum;
        }
        /**所走步数占用总共步数的百分比*/
        float scale = (float) currentCounts / totalStepNum;
        /**开始所走的步数占总步速的百分比*/
        float start = (float) startAngleNum / totalStepNum;
        float startAngleLength = start * angleLength;
        /**换算成弧度最后要到达的角度的长度-->弧长*/
        float currentAngleLength = scale * angleLength;
        /**开始执行动画*/
        setAnimation(startAngleLength, currentAngleLength, animationLength);
    }

    /**
     * 为进度设置动画
     * ValueAnimator是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现的，
     * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
     * 它的内部使用一种时间循环的机制来计算值与值之间的动画过渡，
     * 我们只需要将初始值和结束值提供给ValueAnimator，并且告诉它动画所需运行的时长，
     * 那么ValueAnimator就会自动帮我们完成从初始值平滑地过渡到结束值这样的效果。
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * 设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
     *
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dipToPx(15);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dipToPx(12);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dipToPx(10);
        } else if (length > 8) {
            numberTextSize = dipToPx(5);
        }
    }
}