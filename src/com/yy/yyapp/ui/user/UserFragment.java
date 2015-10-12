/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.util.DialogUtil;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserFragment extends BaseFragment implements OnClickListener
{
    private View view;
    
    private TextView loginBtn, registerBtn, inLoginTxt,tips;
    
    private LinearLayout unlogin, inlogin,logout,my_card,my_score,my_about,my_pwd,my_coupon,my_bank,my_collect;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.user_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    private void init()
    {
        loginBtn = (TextView)view.findViewById(R.id.login_btn);
        registerBtn = (TextView)view.findViewById(R.id.register_btn);
        inLoginTxt = (TextView)view.findViewById(R.id.inLoginTxt);
        unlogin = (LinearLayout)view.findViewById(R.id.unlogin);
        inlogin = (LinearLayout)view.findViewById(R.id.inlogin);
        logout = (LinearLayout)view.findViewById(R.id.logout);
        
        my_card = (LinearLayout)view.findViewById(R.id.my_card);
        my_score = (LinearLayout)view.findViewById(R.id.my_score);
        my_about = (LinearLayout)view.findViewById(R.id.my_about);
        my_pwd = (LinearLayout)view.findViewById(R.id.my_pwd);
        my_coupon = (LinearLayout)view.findViewById(R.id.my_coupon);
        my_bank = (LinearLayout)view.findViewById(R.id.my_bank);
        my_collect = (LinearLayout)view.findViewById(R.id.my_collect);
        
        
        tips = (TextView)view.findViewById(R.id.tips);
        
        if (Global.isLogin())
        {
            inlogin.setVisibility(View.VISIBLE);
            unlogin.setVisibility(View.GONE);
            inLoginTxt.setText("已注册会员："+Global.getTotalUser()+" 人");
        }
        else
        {
            inlogin.setVisibility(View.GONE);
            unlogin.setVisibility(View.VISIBLE);
        }
        
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        logout.setOnClickListener(this);
        
        my_card.setOnClickListener(this);
        my_score.setOnClickListener(this);
        my_about.setOnClickListener(this);
        my_pwd.setOnClickListener(this);
        my_coupon.setOnClickListener(this);
        my_bank.setOnClickListener(this);
        my_collect.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_btn:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.register_btn:
                Intent intent1 = new Intent(getActivity(), RegisterOneActivity.class);
                startActivityForResult(intent1,2);
                break;
            case R.id.logout:
                inlogin.setVisibility(View.GONE);
                unlogin.setVisibility(View.VISIBLE);
                tips.setVisibility(View.VISIBLE);
                Global.logout();
                Intent intent2 = new Intent(Constants.BIND_TITLE_BROADCAST);
                YYApplication.yyApplication.sendBroadcast(intent2);
                break;
            case R.id.my_card:
                if(Global.isLogin())
                {
                    Intent i = new Intent(getActivity(), UserCardActivity.class);
                    getActivity().startActivity(i);
                }
                else
                {
                    goToLogin();
                }
                break;
            case R.id.my_score:
                if(Global.isLogin())
                {
                    Intent i = new Intent(getActivity(), UserScoreActivity.class);
                    getActivity().startActivity(i);
                }
                else
                {
                    goToLogin();
                }
                break;
            case R.id.my_about:
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                getActivity().startActivity(i);
                break;
            case R.id.my_pwd:
                if(Global.isLogin())
                {
                    Intent i1 = new Intent(getActivity(), UserPasswordActivity.class);
                    getActivity().startActivity(i1);
                }
                else
                {
                    goToLogin();
                }
                break; 
            case R.id.my_coupon:
                if(Global.isLogin())
                {
                    Intent i1 = new Intent(getActivity(), UserCouponActivity.class);
                    getActivity().startActivity(i1);
                }
                else
                {
                    goToLogin();
                }
                break;
            case R.id.my_bank:
                ToastUtil.makeText(getActivity(), "功能开发中，敬请期待！");
                break;
            case R.id.my_collect:
                if(Global.isLogin())
                {
                    Intent i1 = new Intent(getActivity(), UserCollectActivity.class);
                    getActivity().startActivity(i1);
                }
                else
                {
                    goToLogin();
                }
                break;
            default:
                break;
        }
    }
    
    private void goToLogin()
    {
        DialogUtil.loginTwoButtonDialog(getActivity(), new DialogCallBack()
        {
            @Override
            public void dialogBack()
            {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(i);
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Constants.LOGIN_SUCCESS_CODE:
                unlogin.setVisibility(View.GONE);
                tips.setVisibility(View.GONE);
                inlogin.setVisibility(View.VISIBLE);
                inLoginTxt.setText("已注册会员："+Global.getTotalUser()+"人");
                break;
            default:
                break;
        }
    }
}
