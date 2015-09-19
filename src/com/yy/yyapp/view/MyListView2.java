package com.yy.yyapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView2 extends ListView
{
    public MyListView2(Context context, AttributeSet attrs)
    {
        
        super(context, attrs);
        
    }
    
    public MyListView2(Context context)
    {
        
        super(context);
        
    }
    
    public MyListView2(Context context, AttributeSet attrs, int defStyle)
    {
        
        super(context, attrs, defStyle);
        
    }
    
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    /**
     * 拦截TouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                
                if (xDistance > yDistance)
                {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
