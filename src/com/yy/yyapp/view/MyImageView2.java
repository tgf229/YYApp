package com.yy.yyapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView2 extends ImageView
{

    public MyImageView2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getResources().getDisplayMetrics().widthPixels;
//        int height=width*200/640;
        int height=width;
        setMeasuredDimension(width, height);
    }
    
    
}
