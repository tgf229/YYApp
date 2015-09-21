/*
 * 文 件 名:  LoginActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-20
 
 */
package com.yy.yyapp.ui.user;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
    private LinearLayout back, titleRight;
    
    private TextView title, titleRightTxt, forgetPwdTxt;
    
    private EditText usernameTxt, pwdTxt;
    
    private Button loginBtn;
    
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
        title.setText(getString(R.string.login));
        titleRight = (LinearLayout)findViewById(R.id.title_call_layout);
        titleRightTxt = (TextView)findViewById(R.id.title_btn_call);
        titleRightTxt.setText("注册");
        
        usernameTxt = (EditText)findViewById(R.id.username_txt);
        pwdTxt = (EditText)findViewById(R.id.pwd_txt);
        forgetPwdTxt = (TextView)findViewById(R.id.forget_pwd_txt);
        forgetPwdTxt.setText(Html.fromHtml("<u><b>忘记密码</b></u>" + "?"));
        loginBtn = (Button)findViewById(R.id.login_btn);
        
        //自动弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager =
                (InputMethodManager)usernameTxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(usernameTxt, 0);
            }
        },
        500);
        
        back.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        forgetPwdTxt.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            case R.id.title_call_layout:
                Intent intent = new Intent(this, RegisterOneActivity.class);
                startActivityForResult(intent, 0);
            default:
                break;
        }
    }
}
