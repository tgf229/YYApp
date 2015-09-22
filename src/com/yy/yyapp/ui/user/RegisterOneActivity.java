/*
 * 文 件 名:  RegisterOneActivity.java
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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.BaseBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
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
public class RegisterOneActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title;
    
    private EditText phoneTxt, pwdTxt;
    
    private Button confirmBtn;
    
    private boolean isUserTouchPwd = false;
    
    /**
     * 加载框
     */
    private NetLoadingDailog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);
        init();
    }
    
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText(getString(R.string.register));
        phoneTxt = (EditText)findViewById(R.id.phone_txt);
        pwdTxt = (EditText)findViewById(R.id.pwd_txt);
        confirmBtn = (Button)findViewById(R.id.confirm_btn);
        dialog = new NetLoadingDailog(this);
        
        //自动弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager =
                    (InputMethodManager)phoneTxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(phoneTxt, 0);
            }
        },
            500);
        
        pwdTxt.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View arg0, boolean arg1)
            {
                //第一次获取焦点 设置密码为默认密码 
                if (!isUserTouchPwd)
                {
                    pwdTxt.setText(Constants.DEFAULT_PWD);
                }
                //设置用户已经触碰了PWD
                isUserTouchPwd = true;
            }
        });
        
        back.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }
    
    private void reqRegister()
    {
        //若用户没有触碰过密码，则默认密码为888888
        if (!isUserTouchPwd)
        {
            pwdTxt.setText(Constants.DEFAULT_PWD);
        }
        
        String phone = phoneTxt.getText().toString().trim();
        String pwd = pwdTxt.getText().toString().trim();
        
        if (GeneralUtils.isNullOrZeroLenght(phone) || GeneralUtils.isNullOrZeroLenght(pwd))
        {
            ToastUtil.makeText(this, "手机号或密码不可为空");
            return;
        }
        
        if (!GeneralUtils.isTel(phone))
        {
            ToastUtil.makeText(this, "请输入正确格式的手机号码");
            return;
        }
        
        if (!GeneralUtils.IsPassword(pwd))
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_mobile", phone);
        param.put("user_password", pwd);
        ConnectService.instance().connectServiceReturnResponse(RegisterOneActivity.this,
            param,
            RegisterOneActivity.this,
            BaseBean.class,
            URLUtil.REGISTER,
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
            case R.id.confirm_btn:
                reqRegister();
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
        if (ob instanceof BaseBean)
        {
            BaseBean res = (BaseBean)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getResult()))
            {
                if (Constants.SUCESS_CODE.equals(res.getResult()))
                {
                    ToastUtil.makeText(this, "恭喜您，注册成功");
                    
                    //执行自动登录 ============TODO===========
                    
                    finish();
                }
                else
                {
                    ToastUtil.showError(this);
                }
            }
            else
            {
                ToastUtil.showError(this);
            }
        }
    }
}
