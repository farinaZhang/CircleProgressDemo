/* add by farina zhang 2016-04-06
 * 
 * 结果转圈 打钩或打叉 动画
 *  
 * */

package com.farina.circleprogressdemo.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.farina.circleprogressdemo.R;
import com.farina.circleprogressdemo.message.MessageConst;

public class DrawHookView extends View{
	private Handler mHandler;
	//绘制圆弧的进度值  
    private int progress = 0;  
    //线1
    private int line1_x = 0;  
    private int line1_y = 0;  
    private int lineNext1_x,lineNext1_y;
    //线2 
    private int line2_x = 0;  

    private int line2_y = 0; 
    
    private int penWidth,penLineWidth ;  //画笔粗细
    private int penColor;  //画笔颜色
    private Paint paint,paintLine;   //画笔
    private int center,radius; //圆心，半径
    private RectF rect;
    private int startAngle ,sweepAngle;
    private boolean beHook=false;//是否画钩
    private boolean beHook2=false;
    private int PER_VALUE=2;
    
    
	public DrawHookView(Context context){
		this(context,null);
	}
	
	public DrawHookView(Context context, AttributeSet attrs){
		this(context, attrs,0);
	}
	
	public DrawHookView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		TypedArray a= context.obtainStyledAttributes(attrs, R.styleable.DrawHookType);
		penColor= a.getColor(R.styleable.DrawHookType_pen_color,Color.parseColor("#ffffff"));
       	        
		penWidth = a.getDimensionPixelOffset(R.styleable.DrawHookType_pen_width, 3);
		
		a.recycle();
		
		//创建画笔
		paint = new Paint();
		paint.setColor(penColor);
		paint.setStrokeWidth(penWidth);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true); 
		
		penLineWidth = penWidth*3/2;
		paintLine = new Paint();
		paintLine.setColor(penColor);
		paintLine.setStrokeWidth(penLineWidth);
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setAntiAlias(true);


		clearMyAnim();
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		//super.onDraw(canvas);

		if(radius==0) {
			center = getWidth() / 2;
			radius = this.getMeasuredWidth() / 2 - penWidth * 3 / 2;
			rect = new RectF(center - radius, center - radius, center + radius, center + radius);
		}

		if(progress<100){
			progress += PER_VALUE;
			Log.v("farina","DrawHookView--onDraw  progress ="+progress);
			startAngle =(int) (270+(progress *(270/100.0f)));
			if(startAngle>=360)startAngle-=360;
			
			sweepAngle=(int)(progress*(270/100.0f));
			if(sweepAngle>=145) sweepAngle = 270 - sweepAngle;
		}
		//根据进度画圆弧
		canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
		
		if(startAngle>=270){
			if(startAngle+sweepAngle-270 >=180){
				beHook=true;
			}else{
				beHook=false;
			}
			
		}else{
			if(startAngle+sweepAngle >=180){
				beHook=true;
			}else{
				beHook=false;
			}
		}

		if(beHook){
			if(line1_x < radius ) {  
                line1_x += PER_VALUE;  
                line1_y+= PER_VALUE;  
            } 
			
			//画第一根线
			if(beHook2 == true && line2_x > 3*radius/4.0f){
				//钩的第二笔和第一笔 同时画
				if(lineNext1_x < center/2.0f){
					lineNext1_x++;
					lineNext1_y++;
				}
				canvas.drawLine(lineNext1_x, lineNext1_y, line1_x, center + line1_y, paintLine);
			}else{
				//画钩的第一笔
				lineNext1_x =penLineWidth;
				lineNext1_y=center;
				canvas.drawLine(penLineWidth, center, line1_x, center + line1_y, paintLine);  
			}
  
            if (line1_x >= radius&& beHook2 == false) {
            	beHook2 = true; //开始画钩的第二条线
                line2_x = line1_x;  
                line2_y = line1_y+center; 
            }
			//画第二根线
            if(beHook2 == true){
                if(line2_y >= 2*radius/3.0f){
                	line2_x+= PER_VALUE;
                    line2_y-= PER_VALUE;
  
                }else{
					//打钩完成后，清空状态，下次重新绘制
					mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_SINGN_ANIM_CLEAR);
                }
                canvas.drawLine(line1_x, center + line1_y, line2_x, line2_y, paintLine);
            }  
  
		}
		mHandler.sendEmptyMessage(MessageConst.CLIENT_ACTION_SINGN_ANIM_INVALIDATE);

		
	}
	
	public void setHandler(Handler handler){
		mHandler=handler;
	}
	
	public void clearMyAnim(){
		progress=0;
		beHook=false;
		beHook2=false;
		line1_x=0;
		line1_y=0;
		lineNext1_x=0;
		lineNext1_y=0;
		line2_x=0;
		line2_y=0;
		
	}
}

