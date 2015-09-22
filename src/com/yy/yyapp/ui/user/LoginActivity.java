/*
 * 文 件 名:  LoginActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-20
 
 */
package com.yy.yyapp.ui.user;

import java.util.HashMap;
import java.util.Map;
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
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;

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
    
    /**
     * 加载框
     */
    private NetLoadingDailog dialog;
    
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
        dialog = new NetLoadingDailog(this);
        
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
    
    private void reqLogin()
    {
        
        String username = usernameTxt.getText().toString().trim();
        String password = pwdTxt.getText().toString().trim();
        
        if (GeneralUtils.isNullOrZeroLenght(password))
        {
            pwdTxt.setText(Constants.DEFAULT_PWD);
        }
        
        if (GeneralUtils.isNullOrZeroLenght(username) || GeneralUtils.isNullOrZeroLenght(password))
        {
            ToastUtil.makeText(this, "手机号或密码不可为空");
            return;
        }
        
        if (!GeneralUtils.isTel(username))
        {
            ToastUtil.makeText(this, "请输入正确格式的手机号码");
            return;
        }
        
        if (!GeneralUtils.IsPassword(password))
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_mobile", username);
        param.put("user_password", password);
        ConnectService.instance().connectServiceReturnResponse(LoginActivity.this,
            param,
            LoginActivity.this,
            UserBean.class,
            URLUtil.LOGIN,
            Constants.ENCRYPT_NONE);
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
                break;
            case R.id.login_btn:
                reqLogin();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        
        if (dialog != null)
        {
            dialog.dismissDialog();
        }
        if (ob instanceof UserBean)
        {
            UserBean res = (UserBean)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getResult()))
            {
                if (Constants.SUCESS_CODE.equals(res.getResult()))
                {
                    Global.setIsLogin(true);
                    Global.saveData(res, pwdTxt.getText().toString().trim());
                    setResult(Constants.LOGIN_SUCCESS_CODE);
                    finish();
                }
                else
                {
                    ToastUtil.makeText(this, "用户名或密码错误");
                }
            }
            else
            {
                ToastUtil.showError(this);
            }
        }
    }
}
