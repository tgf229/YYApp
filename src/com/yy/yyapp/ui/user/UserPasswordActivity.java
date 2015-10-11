package com.yy.yyapp.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.SecurityUtils;
import com.yy.yyapp.util.ToastUtil;

/**
 * 
 * <修改密码>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserPasswordActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private EditText etPassword, etPassAgain, etOldPassword;
    
    private Button btSumbit;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    private String oldPassword, newPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_password);
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
        tvTitle.setText("修改密码");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                UserPasswordActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        etOldPassword = (EditText)findViewById(R.id.manage_old_password_bt);
        etPassword = (EditText)findViewById(R.id.manage_new_password_bt);
        etPassAgain = (EditText)findViewById(R.id.manage_new_password_again_bt);
        btSumbit = (Button)findViewById(R.id.manage_password_sumbit_bt);
        dailog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                setNewPassword();
            }
        });
    }
    
    private void setNewPassword()
    {
        oldPassword = etOldPassword.getText().toString().trim();
        newPassword = etPassword.getText().toString().trim();
        String passAgain = etPassAgain.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(oldPassword))
        {
            ToastUtil.makeText(this, "请输入旧密码");
            return;
        }
        if (GeneralUtils.isNullOrZeroLenght(newPassword))
        {
            ToastUtil.makeText(this, "请输入新密码");
            return;
        }
        
        if (!GeneralUtils.IsPassword(newPassword))
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        
        if (GeneralUtils.isNullOrZeroLenght(passAgain))
        {
            ToastUtil.makeText(this, "请再次输入新密码");
            return;
        }
        if (!newPassword.equals(passAgain))
        {
            ToastUtil.makeText(this, "您两次输入的密码不一致");
            return;
        }
        
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("user_id", Global.getUserId());
            param.put("old_pwd", oldPassword);
            param.put("new_pwd", newPassword);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(UserPasswordActivity.this,
            param,
            UserPasswordActivity.this,
            URLUtil.CHANGE_PWD,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        if (URLUtil.CHANGE_PWD.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    ToastUtil.makeText(UserPasswordActivity.this, "修改密码成功");
                    
                    Global.logout();
                    UserPasswordActivity.this.finish();
                    Intent intent = new Intent(UserPasswordActivity.this, LoginActivity.class);
                    UserPasswordActivity.this.startActivity(intent);
                }
                else
                {
                    ToastUtil.makeText(this, "修改密码失败");
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
