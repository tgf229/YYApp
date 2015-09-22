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
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.ui.base.BaseFragment;

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
    
    private TextView loginBtn, registerBtn, logout;
    
    private LinearLayout unlogin, inlogin;
    
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
        logout = (TextView)view.findViewById(R.id.logout);
        unlogin = (LinearLayout)view.findViewById(R.id.unlogin);
        inlogin = (LinearLayout)view.findViewById(R.id.inlogin);
        
        if (Global.isLogin())
        {
            inlogin.setVisibility(View.VISIBLE);
            unlogin.setVisibility(View.GONE);
        }
        else
        {
            inlogin.setVisibility(View.GONE);
            unlogin.setVisibility(View.VISIBLE);
        }
        
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        logout.setOnClickListener(this);
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
                startActivity(intent1);
                break;
            case R.id.logout:
                inlogin.setVisibility(View.GONE);
                unlogin.setVisibility(View.VISIBLE);
                Global.logout();
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
            case Constants.LOGIN_SUCCESS_CODE:
                inlogin.setVisibility(View.VISIBLE);
                unlogin.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
