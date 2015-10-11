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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qr_codescan.MipcaActivityCapture;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.shop.RegisterShopActivity;
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
    private LinearLayout back,scan;
    
    private TextView title;
    
    private EditText phoneTxt, pwdTxt;
    
    private Button confirmBtn;
    
    private boolean isUserTouchPwd = false;
    
    /**
     * 加载框
     */
    private NetLoadingDailog dialog;
    
    private String scanResult;
    
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
        scan = (LinearLayout)findViewById(R.id.scan);
        
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
        scan.setOnClickListener(this);
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
        if(GeneralUtils.isNotNullOrZeroLenght(scanResult))
        {
            param.put("user_org_id", scanResult);
        }
        ConnectService.instance().connectServiceReturnResponse(RegisterOneActivity.this,
            param,
            RegisterOneActivity.this,
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
            case R.id.scan:
                Intent intent = new Intent(this, RegisterShopActivity.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Constants.SCAN_SUCCESS_CODE:
                Bundle bundle = data.getExtras();
                String star = bundle.getString("result");
                if(GeneralUtils.isNotNullOrZeroLenght(star))
                {
                    scanResult = star.trim();
                }
                else
                {
                    ToastUtil.makeText(this, "很抱歉，未扫描到信息");
                }
                break;
            case Constants.REGISTER_BIND_CODE:
                Bundle bundle1 = data.getExtras();
                String result = bundle1.getString("id");
                if(GeneralUtils.isNotNullOrZeroLenght(result))
                {
                    scanResult = result.trim();
                }
                else
                {
                    ToastUtil.makeText(this, "很抱歉，获取到商家信息");
                }
                break;
            default:
                break;
        }
    }
    
    private void reqLogin()
    {
        String phone = phoneTxt.getText().toString().trim();
        String pwd = pwdTxt.getText().toString().trim();
        
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_mobile", phone);
        param.put("user_password", pwd);
        ConnectService.instance()
            .connectServiceReturnResponse(this, param, this, URLUtil.LOGIN, Constants.ENCRYPT_NONE);
    }

    private void reqDetail(String org_id)
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.SHOP_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        if (URLUtil.REGISTER.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    reqLogin();
                }
                else
                {
                    if (dialog != null)
                    {
                        dialog.dismissDialog();
                    }
                    ToastUtil.makeText(this, "很抱歉，注册失败");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                if (dialog != null)
                {
                    dialog.dismissDialog();
                }
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.LOGIN.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    UserBean user = NetResponse.loginResponse(this, bean);
                    Global.setIsLogin(true);
                    Global.saveData(user, pwdTxt.getText().toString().trim());
                    if(GeneralUtils.isNotNullOrZeroLenght(user.getUser_org_id()))
                    {
                        reqDetail(user.getUser_org_id());
                    }
                    else
                    {
                        if (dialog != null)
                        {
                            dialog.dismissDialog();
                        }
                        this.setResult(Constants.LOGIN_SUCCESS_CODE);
                        finish();
                    }
                }
                else
                {
                    if (dialog != null)
                    {
                        dialog.dismissDialog();
                    }
                    ToastUtil.makeText(this, "用户名或密码错误");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (dialog != null)
                {
                    dialog.dismissDialog();
                }
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.SHOP_DETAIL.equals(service))
        {
            if (dialog != null)
            {
                dialog.dismissDialog();
            }
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject ob = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(ob.getString("result")))
                {
                    Global.saveUserOrgId(ob.getString("org_id"));
                    Global.saveOrgName(ob.getString("org_name"));
                    
                    Intent intent = new Intent(Constants.BIND_TITLE_BROADCAST);
                    YYApplication.yyApplication.sendBroadcast(intent);
                    
                    this.setResult(Constants.LOGIN_SUCCESS_CODE);
                    finish();
                }
                else
                {
                    ToastUtil.showError(this);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
}
