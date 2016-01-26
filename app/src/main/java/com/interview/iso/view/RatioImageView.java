package com.interview.iso.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RatioImageView extends ImageView {

	private float mRatioWidth = 1;
	private float mRatioHeight = 0.5f;
	
	public RatioImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setRatio(float ratioWidth, float ratioHeight) {
		mRatioWidth = ratioWidth;
		mRatioHeight = ratioHeight;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int newHeight = (int) (originalWidth * mRatioHeight / mRatioWidth);
		//Log.d("RadioImg", "onMeasure @width=" + originalWidth + " @height=" + newHeight);
		super.onMeasure(
                MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY), 
                MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));
	}

	

}
