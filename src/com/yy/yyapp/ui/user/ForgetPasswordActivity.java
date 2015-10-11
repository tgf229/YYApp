package com.yy.yyapp.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;

public class ForgetPasswordActivity extends BaseActivity implements OnClickListener
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 用户名,验证码
     */
    private EditText etUserName, etCode;
    private EditText etPassword, etPassAgain;
    private Button btSumbit, btCode;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 倒计时
     */
    private MyTime myTime;
    
    private String pNum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        initTitle();
        init();
        addListener();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText("忘记密码");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                ForgetPasswordActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        etUserName = (EditText)findViewById(R.id.forget_password_phone_et);
        etCode = (EditText)findViewById(R.id.forget_password_code_et);
        btCode = (Button)findViewById(R.id.forget_password_code_bt);
        btSumbit = (Button)findViewById(R.id.forget_password_sumbit_bt);
        
        etPassword = (EditText)findViewById(R.id.set_new_password_bt);
        etPassAgain = (EditText)findViewById(R.id.set_new_password_again_bt);
        
        dailog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                sureUserName();
            }
        });
        
        btCode.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String phoneNum = etUserName.getText().toString().trim();
                if (GeneralUtils.isNullOrZeroLenght(phoneNum))
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, "请输入手机号码");
                    return;
                }
                
                if (!GeneralUtils.isTel(phoneNum))
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, "请输入正确格式的手机号码!");
                }
                else
                {
                    getCode(phoneNum);
                }
                
            }
        });
    }
    
    /**
     * 验证用户名
     */
    private void sureUserName()
    {
        pNum = etUserName.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(pNum))
        {
            ToastUtil.makeText(ForgetPasswordActivity.this, "请输入手机号码");
            return;
        }
        
        if (!GeneralUtils.isTel(pNum))
        {
            ToastUtil.makeText(ForgetPasswordActivity.this, "请输入正确格式的手机号码");
            return;
        }
        
        String code = etCode.getText().toString().trim();

        if (GeneralUtils.isNullOrZeroLenght(code))
        {
            ToastUtil.makeText(this, "请输入验证码");
            return;
        }
        
        String password = etPassword.getText().toString().trim();
        String passAgain = etPassAgain.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(password))
        {
            ToastUtil.makeText(this, "请输入密码");
            return;
        }
        if (!GeneralUtils.IsPassword(password))
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        if (GeneralUtils.isNullOrZeroLenght(passAgain))
        {
            ToastUtil.makeText(this, "请再次输入密码");
            return;
        }
        if (!password.equals(passAgain))
        {
            ToastUtil.makeText(this, "您两次输入的密码不一致");
            return;
        }
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_mobile", pNum);
        param.put("user_smscode", code);
        param.put("user_password", password);
        
        ConnectService.instance().connectServiceReturnResponse(ForgetPasswordActivity.this,
            param,
            ForgetPasswordActivity.this,
            URLUtil.SET_NEW_PWD,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <获取验证码>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void getCode(String phone)
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_mobile", phone);
        ConnectService.instance().connectServiceReturnResponse(ForgetPasswordActivity.this,
            param,
            ForgetPasswordActivity.this,
            URLUtil.FORGET_PWD,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        if (URLUtil.FORGET_PWD.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    myTime = new MyTime(99000, 1000);
                    myTime.start();
                    btCode.setClickable(false);
                    ToastUtil.makeText(ForgetPasswordActivity.this, "您的短信验证码已发送至您的手机，请注意查收");
                }
                else
                {
                    ToastUtil.makeText(this, "很抱歉，获取验证码失败");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.SET_NEW_PWD.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, "恭喜您，设置新密码成功");
                    this.finish();
                }
                else
                {
                    ToastUtil.makeText(this, "很抱歉，设置新密码失败");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    //倒计时
    private class MyTime extends CountDownTimer
    {
        public MyTime(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        
        @Override
        public void onFinish()
        {
            btCode.setText("获取验证码");
            btCode.setTextColor(getResources().getColor(R.color.text_color));
            btCode.setClickable(true);
            btCode.setBackgroundResource(R.drawable.register_two_code_sumbit_bg_selector);
        }
        
        @Override
        public void onTick(long millisUntilFinished)
        {
            btCode.setBackgroundResource(R.drawable.register_two_code_grey_bg);
            btCode.setTextColor(getResources().getColor(R.color.person_register_choise_title));
            btCode.setText("重新获取" + millisUntilFinished / 1000);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                ForgetPasswordActivity.this.finish();
                break;
            
            default:
                break;
        }
    }
}
