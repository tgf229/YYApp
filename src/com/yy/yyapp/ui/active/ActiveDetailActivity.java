/*
 * 文 件 名:  ActiveDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-28
 
 */
package com.yy.yyapp.ui.active;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
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
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.ui.shop.adapter.ShopPicAdapter;
import com.yy.yyapp.ui.user.LoginActivity;
import com.yy.yyapp.util.DialogUtil;
import com.yy.yyapp.util.GeneralUtils;
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
    private String is_collect;
    
    private ViewPager banner_Pager;
    private MyImageView default_img;
    private CirclePageIndicator banner_indicator;
    private ArrayList<String> bannerList;
    private ShopPicAdapter shopPicAdapter;
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                int postion = banner_Pager.getCurrentItem() + 1;
                if (null != bannerList && bannerList.size() > 0)
                    banner_Pager.setCurrentItem(postion % bannerList.size(), true);
                handler.sendEmptyMessageDelayed(0, 5*1000);
            }
        }
    };
    
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
        
        //BANNER
        banner_Pager = (ViewPager)findViewById(R.id.circlepager);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img = (MyImageView)findViewById(R.id.img);
        default_img.setVisibility(View.VISIBLE);
        bannerList = new ArrayList<String>();
        shopPicAdapter = new ShopPicAdapter(this, bannerList);
        banner_Pager.setAdapter(shopPicAdapter);
        banner_indicator = (CirclePageIndicator)findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        handler.sendEmptyMessageDelayed(0, 5*1000);
        
//        img = (MyImageView)findViewById(R.id.img);
        name = (TextView)findViewById(R.id.name);
        time = (TextView)findViewById(R.id.time);
        addr = (TextView)findViewById(R.id.addr);
        content = (TextView)findViewById(R.id.content);
        
        collect_btn = (TextView)findViewById(R.id.collect_btn);
        collect_btn.setOnClickListener(this);
        
        back.setOnClickListener(this);
    }
    
    private void showBanner()
    {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int height = displayWidth/2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayWidth, height);
        banner_Pager.setLayoutParams(params);
        
        banner_indicator.setVisibility(View.VISIBLE);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img.setVisibility(View.GONE);
        
        shopPicAdapter.notifyDataSetChanged();
        banner_Pager.setCurrentItem(0);
        banner_indicator.notifyDataSetChanged();
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
        if("1".equals(is_collect))
        {
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                ActiveDetailActivity.this,
                URLUtil.DEL_COLLECT,
                Constants.ENCRYPT_NONE);
        }
        else
        {
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                ActiveDetailActivity.this,
                URLUtil.ADD_COLLECT,
                Constants.ENCRYPT_NONE);
        }
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
                    is_collect = ob.getString("is_collect");
                    for(String str: ob.getString("filepath").split(","))
                    {
                        bannerList.add(str);
                    }
                    if(GeneralUtils.isNotNullOrZeroSize(bannerList))
                    {
                        showBanner();
                    }
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
        if (URLUtil.DEL_COLLECT.equals(service))
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
                    Intent intent = new Intent(Constants.DEL_COLLECT_BROADCAST);
                    intent.putExtra("id", active_id);
                    YYApplication.yyApplication.sendBroadcast(intent);
                    ToastUtil.makeText(this, "取消成功");
                }
                else
                {
                    ToastUtil.makeText(this, "取消失败");
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
//        ImageLoader.getInstance().displayImage(bean.getActivity_pic_url(),
//            img,
//            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
        name.setText(bean.getActivity_title());
        time.setText(bean.getActivity_time());
        addr.setText(bean.getActivity_addr());
        content.setText(bean.getActivity_content());
        if("1".equals(is_collect))
        {
            collect_btn.setText("取消收藏");
        }
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
