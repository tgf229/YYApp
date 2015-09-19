package com.yy.yyapp.ui.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.yy.yyapp.YYApplication;
import com.yy.yyapp.callback.UICallBack;


public class BaseActivity extends Activity implements UICallBack
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //动态设置屏幕方向 强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        
        YYApplication.yyApplication.addActivity(this);
    }
    
    
    
    @Override
    public void netBack(Object ob)
    {
        
    }
    
    @Override
    protected void onResume()
    {
        YYApplication.currentActivity = this.getClass().getName();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    @Override
    protected void onDestroy()
    {
        YYApplication.yyApplication.deleteActivity(this);
        super.onDestroy();
    }
    
}
