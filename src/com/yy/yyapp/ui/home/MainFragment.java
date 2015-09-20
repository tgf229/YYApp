/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.bean.home.HomeBannerBean;
import com.yy.yyapp.bean.home.HomeIconBean;
import com.yy.yyapp.bean.home.HomeIconPageBean;
import com.yy.yyapp.bean.home.HotGoods;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.home.adapter.FreshNewsAdapter;
import com.yy.yyapp.ui.home.adapter.HomeBannerPagerAdapter;
import com.yy.yyapp.ui.home.adapter.HomeIconPagerAdapter;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.view.MyImageView;
import com.yy.yyapp.view.PullToRefreshView;
import com.yy.yyapp.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MainFragment extends BaseFragment implements OnClickListener, OnHeaderRefreshListener
{
    private View view;
    
    private RelativeLayout titleBar;
    
    private TextView titleName;
    
    /**
     * 上下拉刷新
     */
    private PullToRefreshView mPullToRefreshView;
    
    /**
     * 滑动的图片
     */
    private ViewPager banner_Pager;
    
    private ViewPager icon_Pager;
    
    /**
     * 广告位的默认图片
     */
    private MyImageView default_img;
    
    private LinearLayout icon_default_img;
    
    private ArrayList<HomeBannerBean> bannerList;
    
    private ArrayList<HomeIconBean> iconList = new ArrayList<HomeIconBean>();
    
    private ArrayList<HomeIconPageBean> iconPageList;
    
    private HomeBannerPagerAdapter homeBannerPagerAdapter;
    
    private HomeIconPagerAdapter homeIconPagerAdapter;
    
    private CirclePageIndicator banner_indicator;
    
    private CirclePageIndicator icon_indicator;
    
    private List<Map<String, List<HotGoods>>> freshNewsList = new ArrayList<Map<String, List<HotGoods>>>();
    
    private FreshNewsAdapter freshNewsAdapter;
    
    private Button icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8;
    
    private String[] homeTag = new String[] {Constants.HOME_TAG_COUPON, Constants.HOME_TAG_BUSINESS,
        Constants.HOME_TAG_GOODS, Constants.HOME_TAG_ACTIVE, Constants.HOME_TAG_PREFER};
    
    //    private List<String> homeTag = new ArrayList<String>();
    
    /**
     * 跳转时间
     */
    private final int SKIP_TIME = 5 * 1000;
    
    private View footView;
    
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
                handler.sendEmptyMessageDelayed(0, SKIP_TIME);
            }
        }
    };
    
    private Handler handler1 = new Handler()
    {
        
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.home_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
        
        handler1.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                showBanner();
                showIcon();
                showTagContent();
            }
        }, 2000);
    }
    
    private void init()
    {
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        titleBar = (RelativeLayout)view.findViewById(R.id.title_bar);
        titleName = (TextView)view.findViewById(R.id.title_name);
        titleName.setText(getString(R.string.app_title));
        titleBar.setClickable(false);
        titleBar.setOnClickListener(this);
        
        View listview_head = LayoutInflater.from(getActivity()).inflate(R.layout.home_fragment_listview_head, null);
        banner_Pager = (ViewPager)listview_head.findViewById(R.id.circlepager);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img = (MyImageView)listview_head.findViewById(R.id.default_load_img);
        default_img.setVisibility(View.VISIBLE);
        bannerList = new ArrayList<HomeBannerBean>();
        homeBannerPagerAdapter = new HomeBannerPagerAdapter(getActivity(), bannerList);
        banner_Pager.setAdapter(homeBannerPagerAdapter);
        banner_indicator = (CirclePageIndicator)listview_head.findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        handler.sendEmptyMessageDelayed(0, SKIP_TIME);
        
        icon_Pager = (ViewPager)listview_head.findViewById(R.id.icon_circlepager);
        icon_Pager.setVisibility(View.VISIBLE);
        icon_default_img = (LinearLayout)listview_head.findViewById(R.id.icon_default_load_img);
        icon_default_img.setVisibility(View.VISIBLE);
        iconPageList = new ArrayList<HomeIconPageBean>();
        homeIconPagerAdapter = new HomeIconPagerAdapter(getActivity(), iconPageList);
        icon_Pager.setAdapter(homeIconPagerAdapter);
        icon_indicator = (CirclePageIndicator)listview_head.findViewById(R.id.icon_circleindicator);
        icon_indicator.setViewPager(icon_Pager);
        
        for (String str : homeTag)
        {
            HashMap<String, List<HotGoods>> map = new HashMap<String, List<HotGoods>>();
            map.put(str, null);
            freshNewsList.add(map);
        }
        
        ListView freshNewsListView = (ListView)view.findViewById(R.id.fresh_news_listview);
        freshNewsListView.addHeaderView(listview_head);
        freshNewsAdapter = new FreshNewsAdapter(getActivity(), freshNewsList, this);
        freshNewsListView.setAdapter(freshNewsAdapter);
        //解决listview和scrollview抢焦点问题
        //                freshNewsListView2.setFocusable(false);
        
        //底部地图TAG
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.end_tips_layout, null);
        footView.setVisibility(View.VISIBLE);
        freshNewsListView.addFooterView(footView);
    }
    
    private void showBanner()
    {
        bannerList.clear();
        
        HomeBannerBean b = new HomeBannerBean();
        b.setId("1");
        b.setName("a");
        b.setDesc("AA");
        b.setImageUrl("http://img.zcool.cn/community/01a87a5530a8cd0000003cceec7ce8.jpg");
        bannerList.add(b);
        HomeBannerBean b1 = new HomeBannerBean();
        b1.setId("1");
        b1.setName("a");
        b1.setDesc("AA");
        b1.setImageUrl("http://img.zcool.cn/community/01d2055530a8db0000003cce2db55e.jpg");
        bannerList.add(b1);
        
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = width * 200 / 640;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        banner_Pager.setLayoutParams(params);
        
        banner_indicator.setVisibility(View.VISIBLE);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img.setVisibility(View.GONE);
        
        homeBannerPagerAdapter.notifyDataSetChanged();
        banner_Pager.setCurrentItem(0);
        banner_indicator.notifyDataSetChanged();
    }
    
    private void showIcon()
    {
        iconPageList.clear();
        
        HomeIconBean icon1 = new HomeIconBean();
        icon1.setName("测试按钮");
        HomeIconBean icon2 = new HomeIconBean();
        icon2.setName("测试按钮");
        HomeIconBean icon3 = new HomeIconBean();
        icon3.setName("测试按钮");
        HomeIconBean icon4 = new HomeIconBean();
        icon4.setName("测试按钮");
        HomeIconBean icon5 = new HomeIconBean();
        icon5.setName("测试按钮");
        HomeIconBean icon6 = new HomeIconBean();
        icon6.setName("测试按钮");
        HomeIconBean icon7 = new HomeIconBean();
        icon7.setName("测试按钮");
        HomeIconBean icon8 = new HomeIconBean();
        icon8.setName("测试按钮");
        HomeIconBean icon9 = new HomeIconBean();
        icon9.setName("测试按钮");
        iconList.add(icon9);
        iconList.add(icon8);
        iconList.add(icon7);
        iconList.add(icon6);
        iconList.add(icon5);
        iconList.add(icon4);
        iconList.add(icon1);
        iconList.add(icon2);
        iconList.add(icon3);
        
        if (GeneralUtils.isNotNullOrZeroSize(iconList))
        {
            int size = iconList.size();
            int page = (size - 1) / 8 + 1;
            for (int i = 0; i < page; i++)
            {
                HomeIconPageBean pageBean = new HomeIconPageBean();
                pageBean.setList(iconList.subList(i * 8, size > (i + 1) * 8 ? (i + 1) * 8 : size));
                iconPageList.add(pageBean);
            }
        }
        
        int width = getResources().getDisplayMetrics().widthPixels;
        //        int height = width * 300 / 640;
        int height = icon_default_img.getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        icon_Pager.setLayoutParams(params);
        
        icon_indicator.setVisibility(View.VISIBLE);
        icon_Pager.setVisibility(View.VISIBLE);
        icon_default_img.setVisibility(View.GONE);
        
        homeIconPagerAdapter.notifyDataSetChanged();
        icon_Pager.setCurrentItem(0);
        icon_indicator.notifyDataSetChanged();
    }
    
    private void showTagContent()
    {
        freshNewsList.clear();
        
        List<HotGoods> list = new ArrayList<HotGoods>();
        HotGoods g = new HotGoods();
        g.setId("1");
        g.setDistance("<500m");
        g.setContent("六鲜特色面5选1，免费wifi，免预约");
        g.setImg("http://imgsrc.baidu.com/forum/w%3D580/sign=9d1f50f9252dd42a5f0901a3333a5b2f/5726b9dda144ad3413b78217d0a20cf431ad851f.jpg");
        g.setName("[营苑北路/墨香路]上海");
        g.setPrice("￥13");
        g.setPriceTag("￥16");
        g.setSold("已售181");
        list.add(g);
        HotGoods g1 = new HotGoods();
        g1.setId("1");
        g1.setDistance("<5000m");
        g1.setContent("六鲜特色面5选1，免费wifi，免预约六鲜特色面5选1，免费wifi，免预约");
        g1.setImg("http://imgsrc.baidu.com/forum/w%3D580/sign=8ddfbab3632762d0803ea4b790ed0849/341a90096b63f6244f533c938744ebf81a4ca31d.jpg");
        g1.setName("[营苑北路/墨香路]上海阿姨奶茶");
        g1.setPrice("￥13.5");
        g1.setPriceTag("￥16");
        g1.setSold("已售18121");
        list.add(g1);
        
        for (String str : homeTag)
        {
            HashMap<String, List<HotGoods>> map = new HashMap<String, List<HotGoods>>();
            map.put(str, list);
            freshNewsList.add(map);
        }
        
        freshNewsAdapter.notifyDataSetChanged();
        
        //待删除
        handler1.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mPullToRefreshView.onHeaderRefreshComplete();
            }
        }, 2000);
        
    }
    
    @Override
    public void onClick(View arg0)
    {
        
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        showTagContent();
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
