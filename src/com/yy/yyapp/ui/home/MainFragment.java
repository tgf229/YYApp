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
import java.util.Arrays;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.bean.home.HotGoods;
import com.yy.yyapp.bean.home.LoopHomeBean;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.home.adapter.FreshNewsAdapter;
import com.yy.yyapp.ui.home.adapter.HomeImagePagerAdapter;
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
    
    /**
     * 广告位的默认图片
     */
    private MyImageView default_img;
    
    private ArrayList<LoopHomeBean> businessPlans;
    
    private HomeImagePagerAdapter circleImagePagerAdapter;
    
    private CirclePageIndicator banner_indicator;
    
    private List<Map<String, List<HotGoods>>> freshNewsList = new ArrayList<Map<String,List<HotGoods>>>();
    
    private FreshNewsAdapter freshNewsAdapter;
    
    private Button icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8;
    
    private String[] homeTag = new String[] {"热门现金券", "热门商家", "热门商品", "热门活动", "高德地图", "猜你喜欢"};
    
    //    private List<String> homeTag = new ArrayList<String>();
    
    /**
     * 跳转时间
     */
    private final int SKIP_TIME = 5 * 1000;
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                int postion = banner_Pager.getCurrentItem() + 1;
                if (null != businessPlans && businessPlans.size() > 0)
                    banner_Pager.setCurrentItem(postion % businessPlans.size(), true);
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
        businessPlans = new ArrayList<LoopHomeBean>();
        circleImagePagerAdapter = new HomeImagePagerAdapter(getActivity(), businessPlans);
        banner_Pager.setAdapter(circleImagePagerAdapter);
        banner_indicator = (CirclePageIndicator)listview_head.findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        handler.sendEmptyMessageDelayed(0, SKIP_TIME);
        
        icon1 = (Button)listview_head.findViewById(R.id.icon1);
        icon1.setOnClickListener(this);
        icon2 = (Button)listview_head.findViewById(R.id.icon2);
        icon2.setOnClickListener(this);
        icon3 = (Button)listview_head.findViewById(R.id.icon3);
        icon3.setOnClickListener(this);
        icon4 = (Button)listview_head.findViewById(R.id.icon4);
        icon4.setOnClickListener(this);
        icon5 = (Button)listview_head.findViewById(R.id.icon5);
        icon5.setOnClickListener(this);
        icon6 = (Button)listview_head.findViewById(R.id.icon6);
        icon6.setOnClickListener(this);
        icon7 = (Button)listview_head.findViewById(R.id.icon7);
        icon7.setOnClickListener(this);
        icon8 = (Button)listview_head.findViewById(R.id.icon8);
        icon8.setOnClickListener(this);
        
        for (String str : homeTag)
        {
            HashMap<String, List<HotGoods>> map = new HashMap<String,List<HotGoods>>();
            map.put(str, null);
            freshNewsList.add(map);
        }
        
        ListView freshNewsListView = (ListView)view.findViewById(R.id.fresh_news_listview);
        freshNewsListView.addHeaderView(listview_head);
        freshNewsAdapter = new FreshNewsAdapter(getActivity(), freshNewsList, this);
        freshNewsListView.setAdapter(freshNewsAdapter);
        //解决listview和scrollview抢焦点问题
        //                freshNewsListView2.setFocusable(false);
    }
    
    public void showTagContent()
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
            HashMap<String, List<HotGoods>> map = new HashMap<String,List<HotGoods>>();
            map.put(str, list);
            freshNewsList.add(map);
        }
        
        freshNewsAdapter.notifyDataSetChanged();
        
        //待删除
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mPullToRefreshView.onHeaderRefreshComplete();                
            }
        }, 3000);
        
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
}
