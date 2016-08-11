package com.farina.circleprogressdemo.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.farina.circleprogressdemo.util.DensityUtil;
import com.farina.circleprogressdemo.R;

/**   
 * @Description: TODO
 * @author SpencerWang  
 * @date 2015年7月8日 下午2:47:13 
 * @version V1.0   
 */
public class CustomCircleProgress extends View{

	private static final String INSTANCE_STATE = "saved_instance";
	private static final String FINISHED_STROKE_COLOR = "finished_stroke_color";
	private static final String UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
	private static final String FINISHED_STROKE_WIDTH = "finished_stroke_width";
	private static final String UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
	private static final String MAX = "max";
	private static final String PROGRESS = "progress";

	private float finishedStrokeWidth;
	private float unfinishedStrokeWidth;
	private int finishedStrokeColor;
	private int unfinishedStrokeColor;

	private int progress = 0;
	private int style;
	private int max = 600;
	private int minSize;

	private Paint finishedPain;
	private Paint unfinishedPain;

	private final float defaultFinishedStrokeWidth = 15f;
	private final float defaultUnFinishedStrokeWidth = 15f;
	private final int defaultFinishedStrokeColor = Color.rgb(66, 145, 241);
	private final int defaultUnFinishedStrokeColor = Color.rgb(204, 204, 204);

	private RectF finishedRect = new RectF();
	private RectF unFinishedRect = new RectF();
	
	private CallBack mCallBack;

	public interface CallBack{
		public void onStart();
		//public int onInterrupt();
		public void onFinished();
	}
	
	public CustomCircleProgress(Context context) {
		// TODO Auto-generated constructor stub
		this(context,null);
	}


	public CustomCircleProgress(Context context, AttributeSet attrs) {
		this(context,attrs,0);
		// TODO Auto-generated constructor stub
	}


	public CustomCircleProgress(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		TypedArray mTypes = context.obtainStyledAttributes(attrs, R.styleable.CustomCircleProgress);

		finishedStrokeWidth = DensityUtil.dip2px(context, mTypes.getDimension(R.styleable.CustomCircleProgress_finishedStrokeWidth, defaultFinishedStrokeWidth));
		unfinishedStrokeWidth = DensityUtil.dip2px(context, mTypes.getDimension(R.styleable.CustomCircleProgress_unfinishedStrokeWidth, defaultUnFinishedStrokeWidth));

		finishedStrokeColor = mTypes.getColor(R.styleable.CustomCircleProgress_finishedStrokeColor, defaultFinishedStrokeColor);
		unfinishedStrokeColor = mTypes.getColor(R.styleable.CustomCircleProgress_unfinishedStrokeColor, defaultUnFinishedStrokeColor);
		
		max = mTypes.getInt(R.styleable.CustomCircleProgress_max,max);
		setMax(max);
		progress = mTypes.getInt(R.styleable.CustomCircleProgress_progress, progress);
		setProgress(progress);
		minSize = (int)DensityUtil.dip2px(context, 100);

		mTypes.recycle();
		initPaint();
	}

	private void initPaint(){

		finishedPain = new Paint();
		finishedPain.setColor(finishedStrokeColor);
		finishedPain.setStyle(Style.STROKE);
		finishedPain.setAntiAlias(true);
		finishedPain.setStrokeWidth(finishedStrokeWidth);

		unfinishedPain = new Paint();
		unfinishedPain.setColor(unfinishedStrokeColor);
		unfinishedPain.setStyle(Style.STROKE);
		unfinishedPain.setStrokeWidth(unfinishedStrokeWidth);

	}

	public void setFinishStrokeColor(int rgb){
		finishedStrokeColor = rgb;
		invalidate();
	}

	public void setUnFinishStrokeColor(int rgb){
		unfinishedStrokeColor = rgb;
		invalidate();
	}

	public void setFinishStrokeWidth(float width){
		finishedStrokeWidth = width;
		invalidate();
	}

	public void setunFinishStrokeWidth(float width){
		unfinishedStrokeWidth = width;
		invalidate();
	}

	private int getFinishedStrokeColor(){
		return finishedStrokeColor;
	}

	private int getunFinishedStrokeColor(){
		return unfinishedStrokeColor;
	}

	private float getFinishedStrokeWidth(){
		return finishedStrokeWidth;
	}

	private float getunFinishedStrokeWidth(){
		return unfinishedStrokeWidth;
	}

	private int getMax(){
		return max;
	}

	public int getProgress(){
		return progress;
	}

	public void setMax(int max){
		if(max > 0){
			this.max = max;
			invalidate();
		}
	}

	public void setProgress(int progress){
		this.progress = progress;
		if(progress >= getMax()){
			if(mCallBack != null){
				mCallBack.onFinished();
			}
			progress %= getMax(); 
		}
		invalidate();
	}

	
	
	public void setCallBack(CallBack callBack){
		if(callBack != null){
			mCallBack = callBack;
		}
	}
	
	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		initPaint();
		super.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
	}

	private int measure(int measure){
		int mode = MeasureSpec.getMode(measure);
		int size = MeasureSpec.getSize(measure);
		int result;
		if(mode == MeasureSpec.EXACTLY){
			result = size;
		}else if(mode == MeasureSpec.AT_MOST){
			result = Math.min(size, minSize);
		}else{
			result = minSize;
		}
		return result;
	}

	private float getProgressAngle(){
		return getProgress() / (float) 600 * 360f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float delta = Math.max(finishedStrokeWidth, unfinishedStrokeWidth);

		finishedRect.set(delta, delta, getWidth()-delta, getHeight()-delta);
		unFinishedRect.set(delta, delta, getWidth()-delta, getHeight()-delta);
		canvas.drawArc(finishedRect, 0, getProgressAngle(), false, finishedPain);
		canvas.drawArc(unFinishedRect, getProgressAngle(), 360 - getProgressAngle(), false, unfinishedPain);
		if(mCallBack != null){
			mCallBack.onStart();
		}

	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());

		bundle.putInt(FINISHED_STROKE_COLOR, getFinishedStrokeColor());
		bundle.putInt(UNFINISHED_STROKE_COLOR, getunFinishedStrokeColor());

		bundle.putFloat(FINISHED_STROKE_WIDTH, getFinishedStrokeWidth());
		bundle.putFloat(UNFINISHED_STROKE_WIDTH, getunFinishedStrokeWidth());

		bundle.putInt(MAX, getMax());
		bundle.putInt(PROGRESS,getProgress());

		return bundle;
	}



	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		if(state instanceof Bundle){
			Bundle bundle = (Bundle)state;

			finishedStrokeWidth = bundle.getFloat(FINISHED_STROKE_WIDTH);
			unfinishedStrokeWidth = bundle.getFloat(UNFINISHED_STROKE_WIDTH);

			finishedStrokeColor = bundle.getInt(FINISHED_STROKE_COLOR);
			unfinishedStrokeColor = bundle.getInt(UNFINISHED_STROKE_COLOR);

			max = bundle.getInt(MAX);
			progress = bundle.getInt(PROGRESS);

			initPaint();

			setMax(max);
			setProgress(progress);

			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
			return;
		}
		super.onRestoreInstanceState(state);

	}

}
