package applico.googlezlpreview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import applico.googlezlpreview.R;

/**
 * This class is a glorified CircleView class that we can use to create FAB buttons
 * in our applications. Its intended to have full attribute and getter/setter support
 */
public class FabView extends View {
    private static String LOG_TAG = FabView.class.getSimpleName();

    //Initailize the object variables
    private Paint mCirclePaint;
    private Paint mStrokePaint;
    private RectF mCircleArc;
    private int mCircleRadius;
    private int mStartAngle;
    private int mEndAngle;
    private int mCircleFillColor;
    private int mCircleStrokeColor;
    private int mCircleStrokeWidth;
    private int mFabSize;
    private int mScreenDensity;
    private int mDrawableHeight;
    private int mDrawableWidth;

    private float mScreenDensityFloat;
    private Drawable mDrawable;


    //Default values
    //TODO change this to pull from the dimensions file.
    private static final int DEFAULT_RADIUS_CHECK = -10;
    private static final int DEFAULT_RADIUS = 100;
    private static final int DEFAULT_FILL_COLOR = Color.BLACK;
    private static final int DEFAULT_STROKE_COLOR = Color.BLUE;
    private static final int DEFAULT_START_ANGLE = 0;
    private static final int DEFAULT_END_ANGLE = 360;
    private static final int DEFAULT_STROKE_WIDTH = 2;

    //Default FAB sizes according to the material design
    //docs: http://www.google.com/design/spec/components/buttons.html#buttons-main-buttons

    private int DEFAULT_FAB_SIZE = 56;
    private int DEFAULT_FAB_MINI_SIZE = 40;

    private static final int FAB_SIZE_NORMAL = 0;
    private static final int FAB_SIZE_MINI= 1;



    //TODO - support the context only constructor
    public FabView(Context context) {
        super(context);
    }

    public FabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Read all the attributes
        init(attrs);

        //Initialize the stroke and paint objects
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mCircleStrokeWidth);
        mStrokePaint.setColor(mCircleStrokeColor);
    }

    //TODO - support the context attribute set and defstyle constructor
    public FabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(1, 1);

        mCirclePaint.setColor(mCircleFillColor);
        canvas.drawArc(mCircleArc, mStartAngle, mEndAngle, true, mCirclePaint);
        canvas.drawArc(mCircleArc, mStartAngle, mEndAngle, true, mStrokePaint);

        if (mDrawable != null) {
            mDrawable.setBounds(50,50,100,100);
            mDrawable.draw(canvas);
        }


    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int measuredWidth = measureWidth(widthMeasureSpec);

        //We are using this method to provide a default size based on the layout size
        if(mCircleRadius == 0)
        {
            mCircleRadius = measuredWidth/2;
            int tempRadiusHeight = measureHeight(heightMeasureSpec)/2;
            if(tempRadiusHeight < mCircleRadius)
            {
                mCircleRadius = tempRadiusHeight;
            }
        }

        int circleDiameter = mCircleRadius * 2 - mCircleStrokeWidth;
        mCircleArc = new RectF(0, 0, circleDiameter, circleDiameter);
        int measuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);

    }


    /**
     * measure the height of the screen
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            result = mCircleRadius * 2;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }


    /**
     * measure the width of the screen
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            result = mCircleRadius * 2;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }


    /**
     * initialize all the custom attributes
     * TODO - some of the paint is not supported by attributes
     * @param attrs
     */
    public void init(AttributeSet attrs) {
        Log.e(LOG_TAG,"Init");
        //Convert the fab sizes to DP
        mScreenDensity = (int)getResources().getDisplayMetrics().density;
        mScreenDensityFloat = getResources().getDisplayMetrics().density;

        DEFAULT_FAB_SIZE = DEFAULT_FAB_SIZE * (int)mScreenDensity;
        DEFAULT_FAB_MINI_SIZE = DEFAULT_FAB_MINI_SIZE * (int)mScreenDensity;

        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.fab);
        mCircleRadius = attrsArray.getInteger(R.styleable.fab_cRadius, DEFAULT_RADIUS);
        mCircleFillColor = attrsArray.getColor(R.styleable.fab_cFillColor, DEFAULT_FILL_COLOR);
        mCircleStrokeColor = attrsArray.getColor(R.styleable.fab_cStrokeColor, DEFAULT_STROKE_COLOR);
        mCircleStrokeWidth = attrsArray.getInteger(R.styleable.fab_cStrokeWidth, DEFAULT_STROKE_WIDTH);
        mFabSize = attrsArray.getInteger(R.styleable.fab_cSize, DEFAULT_RADIUS_CHECK);
        mDrawable = attrsArray.getDrawable(R.styleable.fab_cDrawable);
        mDrawableHeight = mDrawable.getIntrinsicHeight();
        mDrawableWidth = mDrawable.getIntrinsicWidth();

        Log.e(LOG_TAG,"Height: " + mDrawableHeight);
        Log.e(LOG_TAG,"Width: " + mDrawableWidth);

        mStartAngle = DEFAULT_START_ANGLE;
        mEndAngle = DEFAULT_END_ANGLE;

        if(mCircleRadius != DEFAULT_RADIUS) {
            if (mFabSize == FAB_SIZE_NORMAL) {

                mCircleRadius = (DEFAULT_FAB_SIZE / 2);
            } else if (mFabSize == FAB_SIZE_MINI) {
                mCircleRadius = DEFAULT_FAB_MINI_SIZE / 2;
            }
        }

        attrsArray.recycle();
        this.bringToFront();
    }

        /**
         * 1.  User doesn't set a radius, we want to use the default
         * 2.  User doesn't set the radius, but sets the Fab size, we want to use the fab size
         * 3.  User sets the radius and the fab size, we want to use the radius as an override
         */
        /*if(mCircleRadius == DEFAULT_RADIUS_CHECK)
        {
            mCircleRadius = DEFAULT_FAB_SIZE/2;
        }

        if(mFabSize != DEFAULT_RADIUS_CHECK && mFabSize == FAB_SIZE_MINI)
        {
            mFabSize = DEFAULT_FAB_MINI_SIZE;
            mCircleRadius = mFabSize/2;
        }
        else if(mFabSize != DEFAULT_RADIUS_CHECK && mFabSize == FAB_SIZE_NORMAL)
        {
            mFabSize = DEFAULT_FAB_SIZE;
            mCircleRadius = mFabSize/2;
        }

        //Don't want to allow the fab to be controlled with a start and end angle
        //mStartAngle = attrsArray.getInteger(R.styleable.fab_cAngleStart, DEFAULT_START_ANGLE);
        //mEndAngle = attrsArray.getInteger(R.styleable.fab_cAngleEnd, DEFAULT_END_ANGLE);
        attrsArray.recycle();
    }

    /**
     * Paint used for the view.
     * @return
     */
    public Paint getPaint()
    {
        return mCirclePaint;
    }

    /**
     * Set the paint of the circleview
     * @param p
     */
    public void setPaint(Paint p)
    {
        mCirclePaint = p;
    }

    /**
     * Get the stroke paint
     * @return
     */
    public Paint getStrokePaint()
    {
        return mStrokePaint;
    }

    /**
     * Set the stroke paint on the view
     * @param p
     */
    public void setStrokePaint(Paint p)
    {
        mStrokePaint = p;
    }

    /**
     * set the Circle Arc for the view
     * @param arc
     */
    public void setCircleArc(RectF arc)
    {
        mCircleArc = arc;
    }

    /**
     * Get the circle arc
     * @return
     */
    public RectF getCircleArc()
    {
        return mCircleArc;
    }

    /**
     * Set the circle radius on the view..have fun creating Pacman
     * @param r
     */

    public void setCircleRadius(int r)
    {
        mCircleRadius = r;
    }

    /**
     * Get the radius of the view
     * @return
     */
    public int getCircleRadius()
    {
        return mCircleRadius;
    }

    /**
     * Get the start angle for the view
     * @return
     */
    /*
    public int getStartAngle()
    {
        return mStartAngle;
    }

    /**
     * Set the start angle for the view
     * @param angle
     */
    /*
    public void setStartAngle(int angle)
    {
        mStartAngle = angle;
    }

    /**
     * Get the end angle of the view
     * @return
     */
    /*
    public int getEndAngle()
    {
        return mEndAngle;
    }
    */

    /**
     * Set the fill color
     * @param color
     */
    public void setFillColor(int color)
    {
        mCircleFillColor = color;
    }

    /**
     * Get the fill color
     * @return
     */
    public int getFillColor()
    {
        return mCircleFillColor;
    }

    /**
     * Get the stroke color
     * @return
     */
    public int getStrokeColor()
    {
        return mCircleStrokeColor;
    }

    /**
     * Set the stroke color
     * @param color
     */
    public void setStrokeColor(int color)
    {
        mCircleStrokeColor = color;
    }

    /**
     * Get the stroke width
     * @return
     */
    public int getStrokeWidth()
    {
        return mCircleStrokeWidth;
    }

    /**
     * Set the stroke width
     * @param width
     */
    public void setStrokeWidth(int width)
    {
        mCircleStrokeWidth = width;
    }

    /**
     * Gets the example drawable attribute value.
     * @return The example drawable attribute value.
     */
    public Drawable getDrawable() {

        return mDrawable;
    }

    /**
     * Sets the view's drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     * @param drawable attribute value to use.
     */
    public void setDrawable(Drawable drawable) {

        mDrawable = drawable;
    }



    /**
     private void init(AttributeSet attrs, int defStyle) {
     // Load attributes
     final TypedArray a = getContext().obtainStyledAttributes(
     attrs, R.styleable.FabView, defStyle, 0);

     mExampleString = a.getString(
     R.styleable.FabView_exampleString);
     mExampleColor = a.getColor(
     R.styleable.FabView_exampleColor,
     mExampleColor);
     // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
     // values that should fall on pixel boundaries.
     mExampleDimension = a.getDimension(
     R.styleable.FabView_exampleDimension,
     mExampleDimension);

     if (a.hasValue(R.styleable.FabView_exampleDrawable)) {
     mExampleDrawable = a.getDrawable(
     R.styleable.FabView_exampleDrawable);
     mExampleDrawable.setCallback(this);
     }

     a.recycle();

     // Set up a default TextPaint object
     mTextPaint = new TextPaint();
     mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
     mTextPaint.setTextAlign(Paint.Align.LEFT);

     // Update TextPaint and text measurements from attributes
     invalidateTextPaintAndMeasurements();
     }**/


    /**
     *
     private void invalidateTextPaintAndMeasurements() {
     mTextPaint.setTextSize(mExampleDimension);
     mTextPaint.setColor(mExampleColor);
     mTextWidth = mTextPaint.measureText(mExampleString);

     Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
     mTextHeight = fontMetrics.bottom;
     }

     /**
     * Gets the example string attribute value.
     * @return The example string attribute value.
     */
    /*
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     * @param exampleString The example string attribute value to use.
     */
    /*
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     * @return The example color attribute value.
     */
    /*
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     * @param exampleColor The example color attribute value to use.
     */
    /*
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     * @return The example dimension attribute value.
     */
    /*
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     * @param exampleDimension The example dimension attribute value to use.
     */
    /*
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    */

}
