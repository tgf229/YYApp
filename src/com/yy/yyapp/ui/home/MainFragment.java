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

import android.graphics.Color;
import android.location.Location;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.active.ActiveBean;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.bean.home.HomeBannerBean;
import com.yy.yyapp.bean.home.HomeIconBean;
import com.yy.yyapp.bean.home.HomeIconPageBean;
import com.yy.yyapp.bean.home.Guss;
import com.yy.yyapp.bean.shop.ShopBean;
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
public class MainFragment extends BaseFragment implements OnClickListener, OnHeaderRefreshListener, LocationSource,
    AMapLocationListener
{
    private View view;
    
    private RelativeLayout titleBar;
    
    private TextView titleName;
    
    private int displayWidth;
    
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
    
    private LinearLayout icon_default_img, hotCouponPager, hotShopPager, hotShopPicPager,hotGoodsPager,hotActivePager,hotActivePicPager;
    
    private ArrayList<HomeBannerBean> bannerList;
    
    private ArrayList<HomeIconBean> iconList = new ArrayList<HomeIconBean>();
    
    private ArrayList<HomeIconPageBean> iconPageList;
    
    private HomeBannerPagerAdapter homeBannerPagerAdapter;
    
    private HomeIconPagerAdapter homeIconPagerAdapter;
    
    private CirclePageIndicator banner_indicator;
    
    private CirclePageIndicator icon_indicator;
    
    private List<Map<String, List<Guss>>> freshNewsList = new ArrayList<Map<String, List<Guss>>>();
    
    private FreshNewsAdapter freshNewsAdapter;
    
    private View listview_head;
    
    private ImageView hotCoupon1, hotCoupon2, hotCoupon3, hotShop1, hotShop2, hotShop3,hotGoods1,hotGoods2,hotGoods3,hotActive1,hotActive2;
    
    private String[] homeTag = new String[] {Constants.HOME_TAG_GUSS};
    
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
    
    private AMap aMap;
    
    private MapView mapView;
    
    private OnLocationChangedListener mListener;
    
    private LocationManagerProxy mAMapLocationManager;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.home_fragment, null);
        displayWidth = getResources().getDisplayMetrics().widthPixels;
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
        
        handler1.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                showBanner();
                showIcon();
                showHotCoupon();
                showHotShop();
                showHotGoods();
                showHotActive();
                showGuss();
            }
        }, 2000);
    }
    
    private void init(Bundle savedInstanceState)
    {
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        titleBar = (RelativeLayout)view.findViewById(R.id.title_bar);
        titleName = (TextView)view.findViewById(R.id.title_name);
        titleName.setText(getString(R.string.app_title));
        titleBar.setClickable(false);
        titleBar.setOnClickListener(this);
        
        listview_head = LayoutInflater.from(getActivity()).inflate(R.layout.home_fragment_listview_head, null);
        //BANNER
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
        
        //ICON
        icon_Pager = (ViewPager)listview_head.findViewById(R.id.icon_circlepager);
        icon_Pager.setVisibility(View.VISIBLE);
        icon_default_img = (LinearLayout)listview_head.findViewById(R.id.icon_default_load_img);
        icon_default_img.setVisibility(View.VISIBLE);
        int height = displayWidth / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayWidth, height);
        icon_default_img.setLayoutParams(params);
        iconPageList = new ArrayList<HomeIconPageBean>();
        homeIconPagerAdapter = new HomeIconPagerAdapter(getActivity(), iconPageList);
        icon_Pager.setAdapter(homeIconPagerAdapter);
        icon_indicator = (CirclePageIndicator)listview_head.findViewById(R.id.icon_circleindicator);
        icon_indicator.setViewPager(icon_Pager);
        
        //HOTCOUPON
        hotCouponPager = (LinearLayout)listview_head.findViewById(R.id.hot_coupon_content);
        hotCoupon1 = (ImageView)listview_head.findViewById(R.id.hot_coupon_pic1);
        hotCoupon2 = (ImageView)listview_head.findViewById(R.id.hot_coupon_pic2);
        hotCoupon3 = (ImageView)listview_head.findViewById(R.id.hot_coupon_pic3);
        
        //HOTSHOP
        hotShopPager = (LinearLayout)listview_head.findViewById(R.id.hot_shop_content);
        hotShopPicPager = (LinearLayout)listview_head.findViewById(R.id.hot_shop_content_pic);
        hotShop1 = (ImageView)listview_head.findViewById(R.id.hot_shop_pic1);
        hotShop2 = (ImageView)listview_head.findViewById(R.id.hot_shop_pic2);
        hotShop3 = (ImageView)listview_head.findViewById(R.id.hot_shop_pic3);
        int hotShopHeight = (displayWidth - 20) / 3 + 7;
        LinearLayout.LayoutParams hotShopParams = new LinearLayout.LayoutParams(displayWidth, hotShopHeight);
        hotShopPicPager.setLayoutParams(hotShopParams);
        
        //HOTGOODS
        hotGoodsPager = (LinearLayout)listview_head.findViewById(R.id.hot_goods_content);
        hotGoods1 = (ImageView)listview_head.findViewById(R.id.hot_goods_pic1);
        hotGoods2 = (ImageView)listview_head.findViewById(R.id.hot_goods_pic2);
        hotGoods3 = (ImageView)listview_head.findViewById(R.id.hot_goods_pic3);
        
        //HOTACTIVE
        hotActivePager = (LinearLayout)listview_head.findViewById(R.id.hot_active_content);
        hotActivePicPager = (LinearLayout)listview_head.findViewById(R.id.hot_active_content_pic);
        hotActive1 = (ImageView)listview_head.findViewById(R.id.hot_active_pic1);
        hotActive2 = (ImageView)listview_head.findViewById(R.id.hot_active_pic2);
        int hotActiveHeight = (displayWidth - 20) / 3 + 7;
        LinearLayout.LayoutParams hotActiveParams = new LinearLayout.LayoutParams(displayWidth, hotActiveHeight);
        hotActivePicPager.setLayoutParams(hotActiveParams);
        
        for (String str : homeTag)
        {
            HashMap<String, List<Guss>> map = new HashMap<String, List<Guss>>();
            map.put(str, null);
            freshNewsList.add(map);
        }
        
        mapView = (MapView)listview_head.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        
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
    
    private void showHotActive()
    {
        ActiveBean  c1 = new ActiveBean();
        c1.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/08/1/20140817072951317.jpg");
        ShopBean c2 = new ShopBean();
//        c2.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/07/1/20140720035655174.jpg");
        c2.setImgUrl("http://img.zcool.cn/community/01ebc9559a18e832f87598b54416f9.jpg");
        
        imageLoader.displayImage(c1.getImgUrl(),
            hotActive1,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c2.getImgUrl(),
            hotActive2,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
    }
    
    private void showHotGoods()
    {
        CouponBean c1 = new CouponBean();
        c1.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/08/1/20140817072951317.jpg");
        CouponBean c2 = new CouponBean();
        c2.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/07/1/20140720035655174.jpg");
        CouponBean c3 = new CouponBean();
        c3.setImgUrl("http://img.zcool.cn/community/01ebc9559a18e832f87598b54416f9.jpg");
        
        imageLoader.displayImage(c1.getImgUrl(),
            hotGoods1,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c2.getImgUrl(),
            hotGoods2,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c3.getImgUrl(),
            hotGoods3,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
    }
    
    private void showHotShop()
    {
        ShopBean c1 = new ShopBean();
        c1.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/08/1/20140817072951317.jpg");
        ShopBean c2 = new ShopBean();
        c2.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/07/1/20140720035655174.jpg");
        ShopBean c3 = new ShopBean();
        c3.setImgUrl("http://img.zcool.cn/community/01ebc9559a18e832f87598b54416f9.jpg");
        
        imageLoader.displayImage(c1.getImgUrl(),
            hotShop1,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c2.getImgUrl(),
            hotShop2,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c3.getImgUrl(),
            hotShop3,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
    }
    
    private void showHotCoupon()
    {
        CouponBean c1 = new CouponBean();
        c1.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/08/1/20140817072951317.jpg");
        CouponBean c2 = new CouponBean();
        c2.setImgUrl("http://www.qqzhuangban.com/uploadfile/2014/07/1/20140720035655174.jpg");
        CouponBean c3 = new CouponBean();
        c3.setImgUrl("http://img.zcool.cn/community/01ebc9559a18e832f87598b54416f9.jpg");
        
        imageLoader.displayImage(c1.getImgUrl(),
            hotCoupon1,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c2.getImgUrl(),
            hotCoupon2,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
        imageLoader.displayImage(c3.getImgUrl(),
            hotCoupon3,
            YYApplication.setAllDisplayImageOptions(getActivity(), "default_banner", "default_banner", "default_banner"));
    }
    
    private void showBanner()
    {
        bannerList.clear();
        
        HomeBannerBean b = new HomeBannerBean();
        b.setId("1");
        b.setName("a");
        b.setDesc("AA");
        b.setImageUrl("http://files.18touch.com/uploads/2014/06/101_20140620151756383.jpg");
        bannerList.add(b);
        HomeBannerBean b1 = new HomeBannerBean();
        b1.setId("1");
        b1.setName("a");
        b1.setDesc("AA");
        b1.setImageUrl("http://img0.imgtn.bdimg.com/it/u=196414304,3042212512&fm=21&gp=0.jpg");
        bannerList.add(b1);
        
        //        int height = width * 200 / 640;
        int height = displayWidth / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayWidth, height);
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
        
        int height = displayWidth / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayWidth, height);
        icon_Pager.setLayoutParams(params);
        
        icon_indicator.setVisibility(View.VISIBLE);
        icon_Pager.setVisibility(View.VISIBLE);
        icon_default_img.setVisibility(View.GONE);
        
        homeIconPagerAdapter.notifyDataSetChanged();
        icon_Pager.setCurrentItem(0);
        icon_indicator.notifyDataSetChanged();
    }
    
    private void showGuss()
    {
        freshNewsList.clear();
        
        List<Guss> list = new ArrayList<Guss>();
        Guss g = new Guss();
        g.setId("1");
        g.setDistance("<500m");
        g.setContent("六鲜特色面5选1，免费wifi，免预约");
        g.setImg("http://imgsrc.baidu.com/forum/w%3D580/sign=9d1f50f9252dd42a5f0901a3333a5b2f/5726b9dda144ad3413b78217d0a20cf431ad851f.jpg");
        g.setName("[营苑北路/墨香路]上海");
        g.setPrice("￥13");
        g.setPriceTag("￥16");
        g.setSold("已售181");
        list.add(g);
        Guss g1 = new Guss();
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
            HashMap<String, List<Guss>> map = new HashMap<String, List<Guss>>();
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
        showBanner();
        showIcon();
        showHotCoupon();
        showHotShop();
        showHotGoods();
        showHotActive();
        showGuss();
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
        deactivate();
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
    
    //==================MAP====================
    private void initMap()
    {
        if (aMap == null)
        {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
    
    private void setUpMap()
    {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
    }
    
    @Override
    public void onLocationChanged(Location arg0)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onProviderDisabled(String arg0)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onProviderEnabled(String arg0)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onLocationChanged(AMapLocation aLocation)
    {
        if (mListener != null && aLocation != null)
        {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
        }
        
    }
    
    @Override
    public void activate(OnLocationChangedListener listener)
    {
        mListener = listener;
        if (mAMapLocationManager == null)
        {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            /*
             * mAMapLocManager.setGpsEnable(false);
             * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
             * API定位采用GPS和网络混合定位方式
             * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
             */
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 100, this);
        }
        
    }
    
    @Override
    public void deactivate()
    {
        mListener = null;
        if (mAMapLocationManager != null)
        {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }
}
