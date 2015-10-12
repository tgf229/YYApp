/*
 * 文 件 名:  WelcomeActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-19
 
 */
package com.yy.yyapp.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.home.adapter.GuidePagerAdapter;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class WelcomeActivity extends BaseActivity
{
    private ImageView imageView;
    
    private List<String> list = new ArrayList<String>();
    
    private List<Bitmap> mList = new ArrayList<Bitmap>();
    
    private ViewPager banner_Pager;
    
    private GuidePagerAdapter circleImagePagerAdapter;
    
    private CirclePageIndicator banner_indicator;
    
    private PageLoadBroard pageLoadBroard;
    
    private RelativeLayout rl;
    
    private int num = 0;
    
    private Handler handler = new Handler()
    {
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        registreBroadcast();
        init();
//        reqPage();
    }
    
    private void reqPage()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.PAGE_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.PAGE_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    if (GeneralUtils.isNotNullOrZeroLenght(ob.getString("pic_url")))
                    {
                        list.add(ob.getString("pic_url"));
                    }
                }
                showList();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    private void registreBroadcast()
    {
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(Constants.PAGE_LOAD_BROADCAST);
        pageLoadBroard = new PageLoadBroard();
        registerReceiver(pageLoadBroard, loginFilter);
    }
    
    private void showList()
    {
        rl = (RelativeLayout)findViewById(R.id.circlepager_rl);
        
        for (String str : list)
        {
            ImageLoader.getInstance().loadImage(str,
                YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"),
                new ImageLoadingListener()
                {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1)
                    {
                    }
                    
                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
                    {
                    }
                    
                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
                    {
                        num = num + 1;
                        mList.add(arg2);
                        if (num == list.size())
                        {
                            Intent intent = new Intent(Constants.PAGE_LOAD_BROADCAST);
                            YYApplication.yyApplication.sendBroadcast(intent);
                        }
                    }
                    
                    @Override
                    public void onLoadingCancelled(String arg0, View arg1)
                    {
                    }
                });
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(pageLoadBroard);
    }
    
    private void init()
    {
        imageView = (ImageView)findViewById(R.id.loading_welcome_iv);
        imageView.setVisibility(View.VISIBLE);
        
        //Android延后处理事件的方法  :  postDelayed 和   Handler和TimerTask相结合 
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent(WelcomeActivity.this, HomeFragmentActivity.class);
                        WelcomeActivity.this.startActivity(intent);
                        WelcomeActivity.this.finish();
                    }
                }, 500);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    class PageLoadBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.PAGE_LOAD_BROADCAST.equals(intent.getAction()))
            {
                mList.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading_welcome));
                banner_Pager = (ViewPager)findViewById(R.id.circlepager);
                circleImagePagerAdapter = new GuidePagerAdapter(WelcomeActivity.this, mList, WelcomeActivity.this);
                banner_Pager.setAdapter(circleImagePagerAdapter);
                //实例化CirclePageIndicator 并设置与ViewPager关联
                banner_indicator = (CirclePageIndicator)findViewById(R.id.circleindicator);
                banner_indicator.setViewPager(banner_Pager);
                
                rl.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                
            }
        }
    }
}
