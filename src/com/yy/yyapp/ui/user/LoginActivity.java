/*
 * 文 件 名:  LoginActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-20
 
 */
package com.yy.yyapp.ui.user;

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
 * @version  [版本号, 2015-9-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoginActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title, titleRight;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }
    
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        titleRight = (TextView)findViewById(R.id.title_btn_call);
        title.setText(getString(R.string.home_tabbar_user));
        titleRight.setText("注册");
        
    }
    
    @Override
    public void onClick(View arg0)
    {
        // TODO Auto-generated method stub
        
    }
}
