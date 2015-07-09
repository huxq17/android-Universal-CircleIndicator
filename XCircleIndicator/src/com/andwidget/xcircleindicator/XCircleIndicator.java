package com.andwidget.xcircleindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * A FlowIndicator which draws circles (one for each view). The current view
 * position is filled and others are only striked.<br/>
 * <br/>
 * Availables attributes are:<br/>
 * <ul>
 * fillColor: Define the color used to fill a circle (default to white)
 * </ul>
 * <ul>
 * strokeColor: Define the color used to stroke a circle (default to white)
 * </ul>
 * <ul>
 * radius: Define the circle radius (default to 4)
 * </ul>
 * <ul>
 * circleInterval: Define the circle interval between circles(default to 4)
 * </ul>
 * <ul>
 * pageTotalCount: Define the page total count
 * </ul>
 */
public class XCircleIndicator extends View {
	private int radius = 4;
	private final Paint mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int currentScroll = 0;
	private int flowWidth = 0;
	private int pageTotalCount = 1;
	private int currentPage = 0;
	private int circleInterval = radius;

	public XCircleIndicator(Context context) {
		super(context);
		initColors(0xFFFFFFFF, 0xFFFFFFFF);
	}

	/**
	 * The contructor used with an inflater
	 * 
	 * @param context
	 * @param attrs
	 */
	public XCircleIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Retrieve styles attributs
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.XCircleIndicator);

		try {
			// Retrieve the colors to be used for this view and apply them.
			int fillColor = a.getColor(R.styleable.XCircleIndicator_fillColor,
					0xFFFFFFFF);
			int strokeColor = a.getColor(
					R.styleable.XCircleIndicator_strokeColor, 0xFFFFFFFF);
			// Retrieve the radius
			radius = (int) a.getDimension(R.styleable.XCircleIndicator_radius,
					radius);
			circleInterval = (int) a.getDimension(
					R.styleable.XCircleIndicator_circleInterval, radius);

			initColors(fillColor, strokeColor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			a.recycle();
		}

	}

	/**
	 * init page count and some params that maybe use in the future
	 * 
	 * @param count
	 *            the total page count
	 * @param contentWidth
	 *            pass any value and we are not care now
	 */
	public void initData(int count, int contentWidth) {
		this.pageTotalCount = count;
		this.flowWidth = contentWidth;
		invalidate();
	}

	/**
	 * set the current selected circleIndicator
	 * 
	 * @param currentPage
	 *            current page number,start with zero
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		invalidate();
	}

	/**
	 * Sets the circle interval
	 * 
	 * @param circleInterval
	 *            unit px
	 */
	public void setPageTotalCount(int pageTotalCount) {
		this.pageTotalCount = pageTotalCount;
		invalidate();
	}

	private void initColors(int fillColor, int strokeColor) {
		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setColor(strokeColor);
		mPaintFill.setStyle(Style.FILL);
		mPaintFill.setColor(fillColor);
	}

	/**
	 * Sets the fill color
	 * 
	 * @param color
	 *            ARGB value for the text
	 */
	public void setFillColor(int color) {
		mPaintFill.setColor(color);
		invalidate();
	}

	/**
	 * Sets the stroke color
	 * 
	 * @param color
	 *            ARGB value for the text
	 */
	public void setStrokeColor(int color) {
		mPaintStroke.setColor(color);
		invalidate();
	}

	/**
	 * Sets the circle interval
	 * 
	 * @param circleInterval
	 *            unit px
	 */
	public void setCircleInterval(int circleInterval) {
		this.circleInterval = circleInterval;
		invalidate();
	}

	/**
	 * Sets the circle radius
	 * 
	 * @param circleInterval
	 *            unit px
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw stroked circles
		for (int iLoop = 0; iLoop < pageTotalCount; iLoop++) {
			canvas.drawCircle(getPaddingLeft() + radius
					+ (iLoop * (2 * radius + circleInterval)), getPaddingTop()
					+ radius, radius, mPaintStroke);
		}
		int cx = 0;
		// if (flowWidth != 0) {
		// // Draw the filled circle according to the current scroll
		// cx = (currentScroll * (2 * radius + radius)) / flowWidth;
		// }
		cx = (2 * radius + circleInterval) * currentPage;
		// The flow width has been upadated yet. Draw the default position
		canvas.drawCircle(getPaddingLeft() + radius + cx, getPaddingTop()
				+ radius, radius, mPaintFill);
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// We were told how big to be
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else { // Calculate the width according the views count
			result = getPaddingLeft() + getPaddingRight()
					+ (pageTotalCount * 2 * radius) + (pageTotalCount - 1)
					* circleInterval;
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// We were told how big to be
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		// Measure the height
		else {
			result = 2 * radius + getPaddingTop() + getPaddingBottom();
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	public void onScrolled(int h, int v, int oldh, int oldv) {
		currentScroll = h;
		invalidate();
	}
}