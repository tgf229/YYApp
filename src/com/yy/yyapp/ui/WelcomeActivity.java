/*
 * 文 件 名:  WelcomeActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-19
 
 */
package com.yy.yyapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.yy.yyapp.R;
import com.yy.yyapp.ui.base.BaseActivity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class WelcomeActivity extends BaseActivity
{
    private Handler handler = new Handler()
    {
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        init();
    }
    
    private void init()
    {
        ImageView imageView = (ImageView)findViewById(R.id.loading_welcome_iv);
        imageView.setVisibility(View.VISIBLE);
        
        //Android延后处理事件的方法  :  postDelayed 和   Handler和TimerTask相结合 
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(WelcomeActivity.this, HomeFragmentActivity.class);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 2000);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
