package com.amaker.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

// 自定义跑马灯
public class MyMarqueeTextView extends TextView{

	public MyMarqueeTextView(Context context) {  
        super(context);  
    }  
  
    public MyMarqueeTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public MyMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
    }  
    
    @Override
    public boolean isFocused() {
    	return true;
    }
    
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    	
    }
}
