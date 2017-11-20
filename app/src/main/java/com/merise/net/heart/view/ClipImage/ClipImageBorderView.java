package com.merise.net.heart.view.ClipImage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View {
	private static final String TAG = "ClipImageBorderView";
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 2;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 画笔的颜色
	 */
	private int mPaintColor = Color.parseColor("#FFFFFF");

	public ClipImageBorderView(Context context) {
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i(TAG, "onDraw");
		// 绘制边框
		mPaint.setColor(mPaintColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		// 方形边框
		// canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()-
		// mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
		// 圆形边框
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2
				- mHorizontalPadding, mPaint);

	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;

	}

	public void setmBorderWidth(int mBorderWidth) {
		Log.i(TAG, "setmBorderWidth");
		this.mBorderWidth = mBorderWidth;
	}

	public void setmPaintColor(int mPaintColor) {
		Log.i(TAG, "setmPaintColor");
		this.mPaintColor = mPaintColor;
	}

}
