package com.merise.net.heart.view.ClipImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout {

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	/***
	 * 屏幕边缘离截图区的宽度
	 */
	private int mHorizontalPadding = 60;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 *
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 *
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

	/**
	 * 设置裁剪的图片
	 *
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		mZoomImageView.setImageBitmap(bitmap);
	}

	/**
	 * 设置圆环的border的宽
	 *
	 * @param mBorderWidth
	 */
	public void setmBorderWidth(int mBorderWidth) {
		mClipImageView.setmBorderWidth(mBorderWidth);
	}

	/**
	 * 设置画笔颜色
	 *
	 * @param mPaintColor
	 */
	public void setmPaintColor(int mPaintColor) {
		mClipImageView.setmPaintColor(mPaintColor);
	}
}
