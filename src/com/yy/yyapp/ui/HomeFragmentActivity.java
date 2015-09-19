/*
 * 文 件 名:  HomeFragmentActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.ui.business.BusinessFragment;
import com.yy.yyapp.ui.goods.GoodsFragment;
import com.yy.yyapp.ui.home.MainFragment;
import com.yy.yyapp.ui.user.UserFragment;
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
public class HomeFragmentActivity extends FragmentActivity implements UICallBack, OnClickListener
{
    /**
     * TabBar标签
     */
    private RelativeLayout home_tabbar_home, home_tabbar_goods, home_tabbar_business, home_tabbar_user;
    
    private ImageView home_tabbar_home_img, home_tabbar_goods_img, home_tabbar_business_img, home_tabbar_user_img;
    
    private TextView home_tabbar_home_txt, home_tabbar_goods_txt, home_tabbar_business_txt, home_tabbar_user_txt;
    
    private FragmentManager fragmentManager;
    
    private String[] tags;
    
    private String curFragmentTag;
    
    /**
     * 上次退出时间
     */
    private long downTime;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(arg0);
        setContentView(R.layout.activity_home_fragment);
        YYApplication.yyApplication.addActivity(this);
        init();
        initData();
        setTabSelection(getString(R.string.home_tabbar_home));
    }
    
    private void init()
    {
        home_tabbar_home = (RelativeLayout)findViewById(R.id.home_tabbar_home);
        home_tabbar_goods = (RelativeLayout)findViewById(R.id.home_tabbar_goods);
        home_tabbar_business = (RelativeLayout)findViewById(R.id.home_tabbar_business);
        home_tabbar_user = (RelativeLayout)findViewById(R.id.home_tabbar_user);
        
        home_tabbar_home_img = (ImageView)findViewById(R.id.home_tabbar_home_img);
        home_tabbar_goods_img = (ImageView)findViewById(R.id.home_tabbar_goods_img);
        home_tabbar_business_img = (ImageView)findViewById(R.id.home_tabbar_business_img);
        home_tabbar_user_img = (ImageView)findViewById(R.id.home_tabbar_user_img);
        
        home_tabbar_home_txt = (TextView)findViewById(R.id.home_tabbar_home_txt);
        home_tabbar_goods_txt = (TextView)findViewById(R.id.home_tabbar_goods_txt);
        home_tabbar_business_txt = (TextView)findViewById(R.id.home_tabbar_business_txt);
        home_tabbar_user_txt = (TextView)findViewById(R.id.home_tabbar_user_txt);
        
        home_tabbar_home.setOnClickListener(this);
        home_tabbar_goods.setOnClickListener(this);
        home_tabbar_business.setOnClickListener(this);
        home_tabbar_user.setOnClickListener(this);
    }
    
    @Override
    protected void onResume()
    {
        YYApplication.currentActivity = this.getClass().getName();
        super.onResume();
    }
    
    private void initData()
    {
        fragmentManager = getSupportFragmentManager();
        tags =
            new String[] {getString(R.string.home_tabbar_home), getString(R.string.home_tabbar_goods),
                getString(R.string.home_tabbar_business), getString(R.string.home_tabbar_user)};
    }
    
    /**
     * 
     * <设置选中的Tab>
     * <功能详细描述>
     * @param tag tab的名称
     * @see [类、类#方法、类#成员]
     */
    public void setTabSelection(String tag)
    {
        if (TextUtils.equals(tag, curFragmentTag))
        {
            return;
        }
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        Fragment mFragment = fragmentManager.findFragmentByTag(tag);
        if (TextUtils.equals(tag, getString(R.string.home_tabbar_home)))
        {
            // 当点击了消息tab时，改变控件的图片和文字颜色
            setSelection(R.id.home_tabbar_home);
            if (mFragment == null)
            {
                mFragment = new MainFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.home_tabbar_goods)))
        {
            setSelection(R.id.home_tabbar_goods);
            if (mFragment == null)
            {
                mFragment = new GoodsFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.home_tabbar_business)))
        {
            setSelection(R.id.home_tabbar_business);
            if (mFragment == null)
            {
                mFragment = new BusinessFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.home_tabbar_user)))
        {
            setSelection(R.id.home_tabbar_user);
            if (mFragment == null)
            {
                mFragment = new UserFragment();
            }
        }
        switchFragment(mFragment, tag);
    }
    
    /**
     * 
     * <根据传入的tag切换fragment>
     * <功能详细描述>
     * @param tag
     * @see [类、类#方法、类#成员]
     */
    private void switchFragment(Fragment fragment, String tag)
    {
        detachFragment();
        attachFragment(fragment, tag);
        curFragmentTag = tag;
    }
    
    /**
     * 
     * <隐藏当前Fragment>
     * <功能详细描述>
     * @param f
     * @see [类、类#方法、类#成员]
     */
    private void detachFragment()
    {
        for (int i = 0; i < tags.length; i++)
        {
            Fragment fragment = fragmentManager.findFragmentByTag(tags[i]);
            if (fragment != null && !fragment.isDetached())
            {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.hide(fragment);
                ft.commit();
            }
        }
    }
    
    /**
     * 
     * <加入Fragment>
     * <功能详细描述>
     * @param layout
     * @param f
     * @param tag
     * @see [类、类#方法、类#成员]
     */
    private void attachFragment(Fragment fragment, String tag)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isHidden())
        {
            ft.show(fragment);
        }
        else if (fragment.isDetached())
        {
            ft.attach(fragment);
        }
        else if (!fragment.isAdded())
        {
            ft.add(R.id.content, fragment, tag);
        }
        ft.commit();
    }
    
    /**
     * 
     * <设置控件的选择状态>
     * <功能详细描述>
     * @param id   //传入父视图的id
     * @see [类、类#方法、类#成员]
     */
    private void setSelection(int id)
    {
        switch (id)
        {
            case R.id.home_tabbar_home:
                home_tabbar_home_img.setImageResource(R.drawable.bottom1_p);
                home_tabbar_home_txt.setTextColor(getResources().getColor(R.color.home_menu_selected));
                break;
            case R.id.home_tabbar_goods:
                home_tabbar_goods_img.setImageResource(R.drawable.bottom3_p);
                home_tabbar_goods_txt.setTextColor(getResources().getColor(R.color.home_menu_selected));
                break;
            case R.id.home_tabbar_business:
                home_tabbar_business_img.setImageResource(R.drawable.bottom4_p);
                home_tabbar_business_txt.setTextColor(getResources().getColor(R.color.home_menu_selected));
                break;
            case R.id.home_tabbar_user:
                home_tabbar_user_img.setImageResource(R.drawable.bottom5_p);
                home_tabbar_user_txt.setTextColor(getResources().getColor(R.color.home_menu_selected));
                break;
        }
    }
    
    /**
     * 
     * <清楚所有选中状态>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void clearSelection()
    {
        home_tabbar_home_img.setImageResource(R.drawable.bottom1_n);
        home_tabbar_home_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
        home_tabbar_goods_img.setImageResource(R.drawable.bottom3_n);
        home_tabbar_goods_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
        home_tabbar_business_img.setImageResource(R.drawable.bottom4_n);
        home_tabbar_business_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
        home_tabbar_user_img.setImageResource(R.drawable.bottom5_n);
        home_tabbar_user_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.home_tabbar_home:
                setTabSelection(getString(R.string.home_tabbar_home));
                break;
            case R.id.home_tabbar_goods:
                setTabSelection(getString(R.string.home_tabbar_goods));
                break;
            case R.id.home_tabbar_business:
                setTabSelection(getString(R.string.home_tabbar_business));
                break;
            case R.id.home_tabbar_user:
                setTabSelection(getString(R.string.home_tabbar_user));
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (curFragmentTag.equals(getString(R.string.home_tabbar_home)))
        {
            if (keyCode == KeyEvent.KEYCODE_BACK)
            {
                if (event.getDownTime() - downTime <= 2000)
                {
                    YYApplication.yyApplication.onTerminate();
                }
                else
                {
                    ToastUtil.makeText(HomeFragmentActivity.this, getString(R.string.home_back));
                    downTime = event.getDownTime();
                    return true;
                }
            }
        }
        else
        {
            setTabSelection(getString(R.string.home_tabbar_home));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        YYApplication.yyApplication.deleteActivity(this);
    }
    
    @Override
    public void netBack(Object ob)
    {
        // TODO Auto-generated method stub
        
    }
}
