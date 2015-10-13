/*
 * 文 件 名:  ActiveDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-28
 
 */
package com.yy.yyapp.ui.active;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.active.ActiveBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.user.LoginActivity;
import com.yy.yyapp.util.DialogUtil;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;
import com.yy.yyapp.view.MyImageView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ActiveDetailActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title, name, time, addr, content,collect_btn;
    
    private NetLoadingDailog dialog;
    
    private String active_id;
    
    private MyImageView img;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_detail);
        active_id = getIntent().getStringExtra("id");
        init();
        reqDetail();
    }
    
    private void init()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("活动详情");
        
        img = (MyImageView)findViewById(R.id.img);
        name = (TextView)findViewById(R.id.name);
        time = (TextView)findViewById(R.id.time);
        addr = (TextView)findViewById(R.id.addr);
        content = (TextView)findViewById(R.id.content);
        
        collect_btn = (TextView)findViewById(R.id.collect_btn);
        collect_btn.setOnClickListener(this);
        
        back.setOnClickListener(this);
    }
    
    private void reqDetail()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("activity_id", active_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.ACTIVE_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqCollect()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("collect_item_id", active_id);
        param.put("collect_type", "活动");
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            ActiveDetailActivity.this,
            URLUtil.ADD_COLLECT,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.ACTIVE_DETAIL.equals(service))
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
                    ActiveBean bean = new ActiveBean();
                    bean.setActivity_id(ob.getString("activity_id"));
                    bean.setActivity_title(ob.getString("activity_title"));
                    bean.setActivity_addr(ob.getString("activity_addr"));
                    bean.setActivity_time(ob.getString("activity_time"));
                    bean.setActivity_content(ob.getString("activity_content"));
                    bean.setActivity_pic_url(ob.getString("activity_pic_url"));
                    
                    showDetail(bean);
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
        if (URLUtil.ADD_COLLECT.equals(service))
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
                    ToastUtil.makeText(this, "恭喜您，收藏成功");
                }
                else
                {
                    ToastUtil.makeText(this, "您已收藏");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    private void showDetail(ActiveBean bean)
    {
        ImageLoader.getInstance().displayImage(bean.getActivity_pic_url(),
            img,
            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
        name.setText(bean.getActivity_title());
        time.setText(bean.getActivity_time());
        addr.setText(bean.getActivity_addr());
        content.setText(bean.getActivity_content());
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            case R.id.collect_btn:
                if(Global.isLogin())
                {
                    dialog.loading();
                    reqCollect();
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
        DialogUtil.loginTwoButtonDialog(this, new DialogCallBack()
        {
            @Override
            public void dialogBack()
            {
                Intent i = new Intent(ActiveDetailActivity.this, LoginActivity.class);
                ActiveDetailActivity.this.startActivity(i);
            }
        });
    }
}
