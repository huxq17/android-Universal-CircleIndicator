# android-Universal-CircleIndicator
universal circle indicator for android
##效果图
![效果图](http://img.my.csdn.net/uploads/201507/09/1436411793_4026.gif) 
##用法介绍
###简单用法

```
@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		......
		gridview.setOnPageChangedListener(new OnPageChangedListener() {            
            @Override
            public void onPageChanged(PagedDragDropGrid sender, int newPageNumber) {
            	//当页面切换的时候调用此方法改变被选中的圆点
            	indicator.setCurrentPage(newPageNumber);
            }
        });
		//设置总共的页数
		indicator.initData(adapter.pageCount(), 0);
		//设置当前的页面
		indicator.setCurrentPage(0);
		//注：如果需要在切换页面时同时改变pagecount，initData和setCurrentPage可以同时使用。
	}
```

按照上面做了以后就能正常使用了，如果你想定制圆点的大小，圆点间的间距，选中圆点和未选中圆点的颜色，那么请继续往下看吧。
###详细用法
可配置的属性：

```
<declare-styleable name="XCircleIndicator">
        <!-- 被选中圆点的颜色 -->
        <attr name="fillColor" format="color" />
        <!-- 未选中圆点的颜色 -->
        <attr name="strokeColor" format="color" />
        <!-- 圆点的大小 -->
        <attr name="radius" format="dimension" />
        <!-- 圆点间间距的大小 -->
        <attr name="circleInterval" format="dimension" />
    </declare-styleable>
```
属性在xml中的用法：

```
  <com.andwidget.xcircleindicator.XCircleIndicator
        android:id="@+id/xCircleIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="@dimen/indicator_bottom_margin"
        indicator:circleInterval="@dimen/circleInterval"
        indicator:fillColor="#F96A0E"
        indicator:radius="@dimen/radius"
        indicator:strokeColor="#cecece" />
```
当然设置参数不会局限于xml配置，也可以在java代码中动态设置

```
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

```
可以看到，在改变参数以后都调用了view.invalidate()方法，invalidate()函数的主要作用是请求View树进行重绘，然后在我们自定义view中会依次回调onMeasure(),onLayout()和onDraw()。在我们需要在onMeasure中完成测量，并在onDraw中完成了最终的绘制。

```
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

```
