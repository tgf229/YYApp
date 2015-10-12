/*
 * 文 件 名:  WaitingActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-12
 
 */
package com.yy.yyapp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.ui.base.BaseActivity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class WaitingActivity extends BaseActivity
{
    private LinearLayout back;
    
    private TextView title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("施工中...");
        
        back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
               WaitingActivity.this.finish();
                
            }
        });
    }
}
