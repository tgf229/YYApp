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

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
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
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.qr_codescan.MipcaActivityCapture;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.active.ActiveBean;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.home.HomeBannerBean;
import com.yy.yyapp.bean.home.HomeIconBean;
import com.yy.yyapp.bean.home.HomeIconPageBean;
import com.yy.yyapp.bean.shop.CircleBean;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.HomeFragmentActivity;
import com.yy.yyapp.ui.active.ActiveActivity;
import com.yy.yyapp.ui.active.ActiveDetailActivity;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.coupon.CouponActivity;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.ui.home.adapter.FreshNewsAdapter;
import com.yy.yyapp.ui.home.adapter.HomeBannerPagerAdapter;
import com.yy.yyapp.ui.home.adapter.HomeIconPagerAdapter;
import com.yy.yyapp.ui.shop.RegisterShopActivity;
import com.yy.yyapp.ui.shop.ShopDetailActivity;
import com.yy.yyapp.ui.user.LoginActivity;
import com.yy.yyapp.util.DialogUtil;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;
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
    AMapLocationListener, OnMarkerClickListener
{
    private View view;
    
    private Button city;
    
    private String circle = null;
    
    private RelativeLayout titleBar;
    
    private TextView titleName, hot_shop_more, hot_goods_more, hot_active_more, hot_coupon_more;
    
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
    
    private ImageView title_img;
    
    private LinearLayout icon_default_img, hotCouponPager, hotShopPager, hotShopPicPager, hotGoodsPager,
        hotActivePager, hotActivePicPager;
    
    private ArrayList<HomeBannerBean> bannerList;
    
    private ArrayList<HomeIconBean> iconList = new ArrayList<HomeIconBean>();
    
    private ArrayList<HomeIconPageBean> iconPageList;
    
    private ArrayList<GoodsBean> hotProductList;
    
    private ArrayList<GoodsBean> gussList;
    
    private ArrayList<CouponBean> hotCouponList;
    
    private ArrayList<ShopBean> hotShopList;
    
    private ArrayList<ActiveBean> hotActiveList;
    
    private HomeBannerPagerAdapter homeBannerPagerAdapter;
    
    private HomeIconPagerAdapter homeIconPagerAdapter;
    
    private CirclePageIndicator banner_indicator;
    
    private CirclePageIndicator icon_indicator;
    
    private List<Map<String, List<GoodsBean>>> freshNewsList = new ArrayList<Map<String, List<GoodsBean>>>();
    
    private FreshNewsAdapter freshNewsAdapter;
    
    private TitleBroard titleBroard;
    
    private View listview_head;
    
    private ArrayList<CircleBean> circleList = new ArrayList<CircleBean>();
    
    private ImageView hotCoupon1, hotCoupon2, hotCoupon3, hotShop1, hotShop2, hotShop3, hotGoods1, hotGoods2,
        hotGoods3, hotActive1, hotActive2;
    
    private String[] homeTag = new String[] {Constants.HOME_TAG_GUSS};
    
    //    private List<String> homeTag = new ArrayList<String>();
    
    /**
     * 跳转时间
     */
    private final int SKIP_TIME = 5 * 1000;
    
    private MarkerOptions markerOption;
    
    private Marker marker2;// 有跳动效果的marker对象
    
    private View footView;
    
    private NetLoadingDailog dialog;
    
    private String org_id;
    
    private int num = 0;
    
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
    
    private PendingIntent mPendingIntent;
    
    private String circleId;
    
    NotificationCompat.Builder mBuilder;
    
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
        initNotify();
        registreBroadcast();
        
        reqIcon();
        reqBanner();
        reqHotGoods();
        reqHotShop();
        reqHotActive();
        reqHotCoupon();
        reqGuss();
        
        reqAutoLogin();
        
        reqShopCircle();
    }
    
    private void init(Bundle savedInstanceState)
    {
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        titleBar = (RelativeLayout)view.findViewById(R.id.title_bar);
        titleName = (TextView)view.findViewById(R.id.title_name);
        title_img = (ImageView)view.findViewById(R.id.title_img);
        
        city = (Button)view.findViewById(R.id.city);
        city.setText(Constants.cityTxt);
        if (Global.isLogin() && GeneralUtils.isNotNullOrZeroLenght(Global.getOrgName()))
        {
            titleName.setText(Global.getOrgName());
            titleName.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Intent intent = new Intent(getActivity(),ShopDetailActivity.class);
                    intent.putExtra("id", Global.getUserOrgId());
                    startActivity(intent);
                }
            });
            if(GeneralUtils.isNotNullOrZeroLenght(Global.getOrgImg()))
            {
                ImageLoader.getInstance().displayImage(Global.getOrgImg(),
                    title_img,
                    YYApplication.setAllDisplayImageOptions(getActivity(), "vip", "vip", "vip"));
                title_img.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            titleName.setText("首页");
        }
        titleBar.setClickable(false);
        titleBar.setOnClickListener(this);
        city.setOnClickListener(this);
        
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
        hot_coupon_more = (TextView)listview_head.findViewById(R.id.hot_coupon_more);
        hot_coupon_more.setOnClickListener(this);
        
        //HOTSHOP
        hotShopPager = (LinearLayout)listview_head.findViewById(R.id.hot_shop_content);
        hotShopPicPager = (LinearLayout)listview_head.findViewById(R.id.hot_shop_content_pic);
        hotShop1 = (ImageView)listview_head.findViewById(R.id.hot_shop_pic1);
        hotShop2 = (ImageView)listview_head.findViewById(R.id.hot_shop_pic2);
        hotShop3 = (ImageView)listview_head.findViewById(R.id.hot_shop_pic3);
        int hotShopHeight = (displayWidth - 20) / 3 + 7;
        LinearLayout.LayoutParams hotShopParams = new LinearLayout.LayoutParams(displayWidth, hotShopHeight);
        hotShopPicPager.setLayoutParams(hotShopParams);
        hot_shop_more = (TextView)listview_head.findViewById(R.id.hot_shop_more);
        hot_shop_more.setOnClickListener(this);
        
        //HOTGOODS
        hotGoodsPager = (LinearLayout)listview_head.findViewById(R.id.hot_goods_content);
        hotGoods1 = (ImageView)listview_head.findViewById(R.id.hot_goods_pic1);
        hotGoods2 = (ImageView)listview_head.findViewById(R.id.hot_goods_pic2);
        hotGoods3 = (ImageView)listview_head.findViewById(R.id.hot_goods_pic3);
        hot_goods_more = (TextView)listview_head.findViewById(R.id.hot_goods_more);
        hot_goods_more.setOnClickListener(this);
        
        //HOTACTIVE
        hotActivePager = (LinearLayout)listview_head.findViewById(R.id.hot_active_content);
        hotActivePicPager = (LinearLayout)listview_head.findViewById(R.id.hot_active_content_pic);
        hotActive1 = (ImageView)listview_head.findViewById(R.id.hot_active_pic1);
        hotActive2 = (ImageView)listview_head.findViewById(R.id.hot_active_pic2);
        int hotActiveHeight = (displayWidth - 20) / 3 + 7;
        LinearLayout.LayoutParams hotActiveParams = new LinearLayout.LayoutParams(displayWidth, hotActiveHeight);
        hotActivePicPager.setLayoutParams(hotActiveParams);
        hot_active_more = (TextView)listview_head.findViewById(R.id.hot_active_more);
        hot_active_more.setOnClickListener(this);
        
        //MAP
        mapView = (MapView)listview_head.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        
        //GUESS
        for (String str : homeTag)
        {
            HashMap<String, List<GoodsBean>> map = new HashMap<String, List<GoodsBean>>();
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
    
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            if (Global.isLogin() && GeneralUtils.isNotNullOrZeroLenght(Global.getOrgName()))
            {
                titleName.setText(Global.getOrgName());
                titleName.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        Intent intent = new Intent(getActivity(),ShopDetailActivity.class);
                        intent.putExtra("id", Global.getUserOrgId());
                        startActivity(intent);
                    }
                });
                if(GeneralUtils.isNotNullOrZeroLenght(Global.getOrgImg()))
                {
                    ImageLoader.getInstance().displayImage(Global.getOrgImg(),
                        title_img,
                        YYApplication.setAllDisplayImageOptions(getActivity(), "vip", "vip", "vip"));
                    title_img.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                titleName.setText("首页");
                title_img.setVisibility(View.GONE);
            }
        }
    }
    
    private void initNotify()
    {
        mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setContentTitle("测试标题").setContentText("测试内容")
        //.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
            .setTicker("测试通知来啦")
            //通知首次出现在通知栏，带上升动画效果的
            .setWhen(System.currentTimeMillis())
            //通知产生的时间，会在通知信息里显示
            .setPriority(Notification.PRIORITY_DEFAULT)
            //设置该通知优先级
            .setAutoCancel(true)
            //设置这个标志当用户单击面板就可以让通知将自动取消  
            .setOngoing(false)
            //ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
            //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
            .setSmallIcon(R.drawable.ic_launcher);
    }
    
    private void reqBanner()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            URLUtil.BANNER,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqIcon()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            URLUtil.ICON,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotGoods()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("is_recomment", "推荐商品");
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.PRODUCT_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotShop()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(circleId))
        {
            param.put("org_comm", circleId);
        }
        else
        {
            param.put("is_recomment", "推荐商家");
        }
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.SHOP_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotActive()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("is_recomment", "推荐活动");
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.ACTIVE_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotCoupon()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("is_recomment", "推荐现金券");
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.COUPON_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqGuss()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("is_recomment", "推荐商品");
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.GUSS_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqAutoLogin()
    {
        if (Global.isLogin())
        {
            Map<String, String> param = new HashMap<String, String>();
            param.put("user_mobile", Global.getUserMobile());
            param.put("user_password", Global.getUserPassword());
            ConnectService.instance().connectServiceReturnResponse(getActivity(),
                param,
                MainFragment.this,
                URLUtil.LOGIN,
                Constants.ENCRYPT_NONE);
        }
    }
    
    private void reqDetail(String org_id)
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.SHOP_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqShopCircle()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
        {
            param.put("city_name", Constants.cityTxt);
        }
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.CIRCLE_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void bind()
    {
        num = num + 1;
        if (num <= 1)
        {
            dialog = new NetLoadingDailog(getActivity());
            dialog.loading();
            Map<String, String> param = new HashMap<String, String>();
            if (Global.isLogin())
            {
                param.put("user_id", Global.getUserId());
            }
            param.put("org_id", org_id);
            ConnectService.instance().connectServiceReturnResponse(getActivity(),
                param,
                this,
                URLUtil.BIND,
                Constants.ENCRYPT_NONE);
        }
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.BANNER.equals(service))
        {
            bannerList.clear();
            
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    HomeBannerBean bean = new HomeBannerBean();
                    bean.setPic_url(ob.getString("pic_url"));
                    bean.setTitle(ob.getString("title"));
                    bean.setUrl(ob.getString("url"));
                    bean.setOrg_id(ob.getString("org_id"));
                    bannerList.add(bean);
                }
                showBanner();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.ICON.equals(service))
        {
            iconPageList.clear();
            iconList.clear();
            
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    HomeIconBean bean = new HomeIconBean();
                    bean.setPic_name(ob.getString("pic_name"));
                    bean.setPic_title(ob.getString("pic_title"));
                    iconList.add(bean);
                }
                showIcon();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.COUPON_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                hotCouponList = new ArrayList<CouponBean>();
                array = new JSONArray(res);
                for (int i = 0; i < (array.length() > 3 ? 3 : array.length()); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    CouponBean bean = new CouponBean();
                    bean.setTicket_id(ob.getString("ticket_id"));
                    bean.setTicket_pic_url(ob.getString("ticket_pic_url"));
                    hotCouponList.add(bean);
                }
                showHotCoupon();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.GUSS_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                gussList = new ArrayList<GoodsBean>();
                array = new JSONArray(res);
                for (int i = 0; i < (array.length() > 4 ? 4 : array.length()); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    GoodsBean bean = new GoodsBean();
                    bean.setProduct_id(ob.getString("product_id"));
                    bean.setProduct_name(ob.getString("product_name"));
                    bean.setProduct_pic_url(ob.getString("product_pic_url"));
                    bean.setProduct_type(ob.getString("product_type"));
                    bean.setProduct_content(ob.getString("product_content"));
                    bean.setProduct_price(ob.getString("product_price"));
                    bean.setActivity_price(ob.getString("activity_price"));
                    bean.setProduct_org_id(ob.getString("org_id"));
                    bean.setView_count(ob.getString("view_count"));
                    gussList.add(bean);
                }
                showGuss();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.PRODUCT_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                hotProductList = new ArrayList<GoodsBean>();
                array = new JSONArray(res);
                for (int i = 0; i < (array.length() > 3 ? 3 : array.length()); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    GoodsBean bean = new GoodsBean();
                    bean.setProduct_id(ob.getString("product_id"));
                    bean.setProduct_pic_url(ob.getString("product_pic_url"));
                    hotProductList.add(bean);
                }
                showHotGoods();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.SHOP_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                hotShopList = new ArrayList<ShopBean>();
                array = new JSONArray(res);
                for (int i = 0; i < (array.length() > 3 ? 3 : array.length()); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    ShopBean bean = new ShopBean();
                    bean.setOrg_id(ob.getString("org_id"));
                    bean.setOrg_pic_url(ob.getString("org_pic_url"));
                    bean.setOrg_name(ob.getString("org_name"));
                    bean.setOrg_addr(ob.getString("org_addr"));
                    bean.setOrg_position(ob.getString("org_position"));
                    hotShopList.add(bean);
                }
                showHotShop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.ACTIVE_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                hotActiveList = new ArrayList<ActiveBean>();
                array = new JSONArray(res);
                for (int i = 0; i < (array.length() > 2 ? 2 : array.length()); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    ActiveBean bean = new ActiveBean();
                    bean.setActivity_id(ob.getString("activity_id"));
                    bean.setActivity_pic_url(ob.getString("activity_pic_url"));
                    hotActiveList.add(bean);
                }
                showHotActive();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        if (URLUtil.LOGIN.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    UserBean user = NetResponse.loginResponse(getActivity(), bean);
                    Global.setIsLogin(true);
                    Global.saveData(user, Global.getUserPassword());
                    if (GeneralUtils.isNotNullOrZeroLenght(user.getUser_org_id()))
                    {
                        if (!Global.getUserOrgId().equals(user.getUser_org_id()))
                        {
                            reqDetail(user.getUser_org_id());
                        }
                    }
                    else
                    {
                        goToScan();
                        getActivity().setResult(Constants.LOGIN_SUCCESS_CODE);
                    }
                }
                else
                {
                    Global.setIsLogin(false);
                    Global.logout();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.BIND.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject ob = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(ob.getString("result")))
                {
                    reqDetail(org_id);
                    num = 0;
                }
                else
                {
                    if (dialog != null)
                    {
                        dialog.dismissDialog();
                    }
                    ToastUtil.makeText(getActivity(), "很抱歉，扫描信息有误");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (dialog != null)
                {
                    dialog.dismissDialog();
                }
                ToastUtil.makeText(getActivity(), "很抱歉，扫描信息有误");
            }
        }
        if (URLUtil.SHOP_DETAIL.equals(service))
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
                    Global.saveUserOrgId(ob.getString("org_id"));
                    Global.saveOrgName(ob.getString("org_name"));
                    
                    String is_recomment = ob.getString("is_recomment");
                    if(GeneralUtils.isNotNullOrZeroLenght(is_recomment) && "推荐商家".equals(is_recomment))
                    {
                        Global.saveOrgImg(ob.getString("recomment_pic_url"));
                        ImageLoader.getInstance().displayImage(Global.getOrgImg(),
                            title_img,
                            YYApplication.setAllDisplayImageOptions(getActivity(), "vip", "vip", "vip"));
                        title_img.setVisibility(View.VISIBLE);
                    }

                    titleName.setText(Global.getOrgName());
                    titleName.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(getActivity(),ShopDetailActivity.class);
                            intent.putExtra("id", Global.getUserOrgId());
                            startActivity(intent);
                        }
                    });
                    Intent intent = new Intent(Constants.BIND_TITLE_BROADCAST);
                    YYApplication.yyApplication.sendBroadcast(intent);
                    getActivity().setResult(Constants.LOGIN_SUCCESS_CODE);
                }
                else
                {
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (URLUtil.CIRCLE_LIST.equals(service))
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
                    
                    CircleBean bean = new CircleBean();
                    bean.setComm_id(ob.getString("comm_id"));
                    bean.setComm_title(ob.getString("comm_title"));
                    bean.setComm_latitude(ob.getString("comm_latitude"));
                    bean.setComm_longitude(ob.getString("comm_longitude"));
                    bean.setComm_radius(ob.getString("comm_radius"));
                    circleList.add(bean);
                }
                showCircleList();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void showHotActive()
    {
        if(GeneralUtils.isNullOrZeroSize(hotActiveList))
        {
            hotActive1.setClickable(false);
            hotActive2.setClickable(false);
            return;
        }
        for (int i = 0; i < hotActiveList.size(); i++)
        {
            if (i == 0)
            {
                imageLoader.displayImage(hotActiveList.get(i).getActivity_pic_url(),
                    hotActive1,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotActive1.setOnClickListener(this);
            }
            else
            {
                imageLoader.displayImage(hotActiveList.get(i).getActivity_pic_url(),
                    hotActive2,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotActive2.setOnClickListener(this);
            }
        }
    }
    
    private void showHotGoods()
    {
        if(GeneralUtils.isNullOrZeroSize(hotProductList))
        {
            hotGoods1.setClickable(false);
            hotGoods2.setClickable(false);
            hotGoods3.setClickable(false);
            return;
        }
        for (int i = 0; i < hotProductList.size(); i++)
        {
            if (i == 0)
            {
                imageLoader.displayImage(hotProductList.get(i).getProduct_pic_url(),
                    hotGoods1,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotGoods1.setOnClickListener(this);
            }
            else if (i == 1)
            {
                imageLoader.displayImage(hotProductList.get(i).getProduct_pic_url(),
                    hotGoods2,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotGoods2.setOnClickListener(this);
            }
            else
            {
                imageLoader.displayImage(hotProductList.get(i).getProduct_pic_url(),
                    hotGoods3,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotGoods3.setOnClickListener(this);
            }
        }
    }
    
    private void showHotShop()
    {
        if(GeneralUtils.isNullOrZeroSize(hotShopList))
        {
            hotShop1.setClickable(false);
            hotShop2.setClickable(false);
            hotShop3.setClickable(false);
            return;
        }
        if (GeneralUtils.isNotNullOrZeroLenght(circleId))
        {
            
        }
        else
        {
            for (int i = 0; i < hotShopList.size(); i++)
            {
                if (i == 0)
                {
                    imageLoader.displayImage(hotShopList.get(i).getOrg_pic_url(),
                        hotShop1,
                        YYApplication.setAllDisplayImageOptions(getActivity(),
                            "default_banner",
                            "default_banner",
                            "default_banner"));
                    hotShop1.setOnClickListener(this);
                }
                else if (i == 1)
                {
                    imageLoader.displayImage(hotShopList.get(i).getOrg_pic_url(),
                        hotShop2,
                        YYApplication.setAllDisplayImageOptions(getActivity(),
                            "default_banner",
                            "default_banner",
                            "default_banner"));
                    hotShop2.setOnClickListener(this);
                }
                else
                {
                    imageLoader.displayImage(hotShopList.get(i).getOrg_pic_url(),
                        hotShop3,
                        YYApplication.setAllDisplayImageOptions(getActivity(),
                            "default_banner",
                            "default_banner",
                            "default_banner"));
                    hotShop3.setOnClickListener(this);
                }
            }
        }
        addMarkersToMap();
    }
    
    private void showHotCoupon()
    {
        if(GeneralUtils.isNullOrZeroSize(hotCouponList))
        {
            hotCoupon1.setClickable(false);
            hotCoupon2.setClickable(false);
            hotCoupon3.setClickable(false);
            return;
        }
        for (int i = 0; i < hotCouponList.size(); i++)
        {
            if (i == 0)
            {
                imageLoader.displayImage(hotCouponList.get(i).getTicket_pic_url(),
                    hotCoupon1,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotCoupon1.setOnClickListener(this);
            }
            else if (i == 1)
            {
                imageLoader.displayImage(hotCouponList.get(i).getTicket_pic_url(),
                    hotCoupon2,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotCoupon2.setOnClickListener(this);
            }
            else
            {
                imageLoader.displayImage(hotCouponList.get(i).getTicket_pic_url(),
                    hotCoupon3,
                    YYApplication.setAllDisplayImageOptions(getActivity(),
                        "default_banner",
                        "default_banner",
                        "default_banner"));
                hotCoupon3.setOnClickListener(this);
            }
        }
    }
    
    private void showBanner()
    {
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
        
        for (String str : homeTag)
        {
            HashMap<String, List<GoodsBean>> map = new HashMap<String, List<GoodsBean>>();
            map.put(str, gussList);
            freshNewsList.add(map);
        }
        
        freshNewsAdapter.notifyDataSetChanged();
        mPullToRefreshView.onHeaderRefreshComplete();
    }
    
    private void showCircleList()
    {
        if (GeneralUtils.isNotNullOrZeroSize(circleList))
        {
            registreCircleBroadcast();
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.city:
                Intent cIntent = new Intent(getActivity(), CityActivity.class);
                cIntent.putExtra("city", Constants.cityTxt);
                getActivity().startActivityForResult(cIntent, 0);
                break;
            case R.id.hot_goods_pic1:
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("id", hotProductList.get(0).getProduct_id());
                getActivity().startActivity(intent);
                break;
            case R.id.hot_goods_pic2:
                Intent intent1 = new Intent(getActivity(), ProductDetailActivity.class);
                intent1.putExtra("id", hotProductList.get(1).getProduct_id());
                getActivity().startActivity(intent1);
                break;
            case R.id.hot_goods_pic3:
                Intent intent2 = new Intent(getActivity(), ProductDetailActivity.class);
                intent2.putExtra("id", hotProductList.get(2).getProduct_id());
                getActivity().startActivity(intent2);
                break;
            case R.id.hot_shop_pic1:
                Intent shopIntent = new Intent(getActivity(), ShopDetailActivity.class);
                shopIntent.putExtra("id", hotShopList.get(0).getOrg_id());
                getActivity().startActivity(shopIntent);
                break;
            case R.id.hot_shop_pic2:
                Intent shopIntent2 = new Intent(getActivity(), ShopDetailActivity.class);
                shopIntent2.putExtra("id", hotShopList.get(1).getOrg_id());
                getActivity().startActivity(shopIntent2);
                break;
            case R.id.hot_shop_pic3:
                Intent shopIntent3 = new Intent(getActivity(), ShopDetailActivity.class);
                shopIntent3.putExtra("id", hotShopList.get(2).getOrg_id());
                getActivity().startActivity(shopIntent3);
                break;
            case R.id.hot_shop_more:
                ((HomeFragmentActivity)getActivity()).setTabSelection(getString(R.string.home_tabbar_business));
                break;
            case R.id.hot_goods_more:
                ((HomeFragmentActivity)getActivity()).setTabSelection(getString(R.string.home_tabbar_goods));
                break;
            case R.id.hot_active_more:
                Intent activeIntent = new Intent(getActivity(), ActiveActivity.class);
                getActivity().startActivity(activeIntent);
                break;
            case R.id.hot_coupon_more:
                Intent couponIntent = new Intent(getActivity(), CouponActivity.class);
                getActivity().startActivity(couponIntent);
                break;
            case R.id.hot_active_pic1:
                Intent activeIntent1 = new Intent(getActivity(), ActiveDetailActivity.class);
                activeIntent1.putExtra("id", hotActiveList.get(0).getActivity_id());
                getActivity().startActivity(activeIntent1);
                break;
            case R.id.hot_active_pic2:
                Intent activeIntent2 = new Intent(getActivity(), ActiveDetailActivity.class);
                activeIntent2.putExtra("id", hotActiveList.get(1).getActivity_id());
                getActivity().startActivity(activeIntent2);
                break;
            case R.id.hot_coupon_pic1:
                Intent couponIntent1 = new Intent(getActivity(), CouponDetailActivity.class);
                couponIntent1.putExtra("id", hotCouponList.get(0).getTicket_id());
                getActivity().startActivity(couponIntent1);
                break;
            case R.id.hot_coupon_pic2:
                Intent couponIntent2 = new Intent(getActivity(), CouponDetailActivity.class);
                couponIntent2.putExtra("id", hotCouponList.get(1).getTicket_id());
                getActivity().startActivity(couponIntent2);
                break;
            case R.id.hot_coupon_pic3:
                Intent couponIntent3 = new Intent(getActivity(), CouponDetailActivity.class);
                couponIntent3.putExtra("id", hotCouponList.get(2).getTicket_id());
                getActivity().startActivity(couponIntent3);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        reqBanner();
        reqIcon();
        reqHotCoupon();
        reqHotShop();
        reqHotGoods();
        reqHotActive();
        reqGuss();
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
    
    private void goToScan()
    {
        DialogUtil.scanTwoButtonDialog(getActivity(), new DialogCallBack()
        {
            @Override
            public void dialogBack()
            {
                Intent intent = new Intent(getActivity(), RegisterShopActivity.class);
                startActivityForResult(intent, 1234);
            }
        });
    }
    
    //==================MAP====================
    private void initMap()
    {
        if (aMap == null)
        {
            aMap = mapView.getMap();
            setUpMap();
            aMap.setOnMapClickListener(new OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng arg0)
                {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("list", hotShopList);
                    getActivity().startActivity(intent);
                }
            });
        }
    }
    
    private void setUpMap()
    {
        LatLng marker1 = new LatLng(32.041544, 118.767413);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        
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
    
    private void addMarkersToMap()
    {
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        
        for (ShopBean bean : hotShopList)
        {
            if (GeneralUtils.isNotNullOrZeroLenght(bean.getOrg_position()))
            {
                markerOption = new MarkerOptions();
                String lng = bean.getOrg_position().split(",")[0];
                String lat = bean.getOrg_position().split(",")[1];
                markerOption.position(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
                markerOption.title(bean.getOrg_name()).snippet(bean.getOrg_addr());
                markerOption.draggable(true);
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_point));
                marker2 = aMap.addMarker(markerOption);
            }
        }
    }
    
    @Override
    public boolean onMarkerClick(Marker arg0)
    {
        // TODO Auto-generated method stub
        return false;
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
            Constants.cityTxt = aLocation.getCity().replaceAll("市", "");
            city.setText(Constants.cityTxt);
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
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 1000, this);
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
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Constants.REGISTER_BIND_CODE:
                Bundle bundle1 = data.getExtras();
                String result = bundle1.getString("id");
                if(GeneralUtils.isNotNullOrZeroLenght(result))
                {
                    org_id = result.trim();
                    bind();
                }
                else
                {
                    ToastUtil.makeText(getActivity(), "很抱歉，未获取到商家信息");
                }
                break;
            case Constants.CITY_SUCCESS_CODE:
                Constants.cityTxt = data.getStringExtra("city");
                city.setText(Constants.cityTxt);
                break;
            default:
                break;
        }
    }
    
    private void registreBroadcast()
    {
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(Constants.BIND_TITLE_BROADCAST);
        titleBroard = new TitleBroard();
        getActivity().registerReceiver(titleBroard, loginFilter);
        
        // 将地理围栏添加到地图上显示
        //        LatLng latLng = new LatLng(32.00487349, 118.74203682);
        //        CircleOptions circleOptions = new CircleOptions();
        //        circleOptions.center(latLng).radius(1000)
        //                .fillColor(Color.argb(180, 224, 171, 10))
        //                .strokeColor(Color.RED);
        //        aMap.addCircle(circleOptions);
    }
    
    private void registreCircleBroadcast()
    {
        IntentFilter fliter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        fliter.addAction(Constants.GEOFENCE_BROADCAST_ACTION);
        getActivity().registerReceiver(mGeoFenceReceiver, fliter);
        
        Intent intent = new Intent(Constants.GEOFENCE_BROADCAST_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        
        for (CircleBean b : circleList)
        {
            mAMapLocationManager.addGeoFenceAlert(Double.valueOf(b.getComm_longitude()),
                Double.valueOf(b.getComm_latitude()),
                Float.valueOf(b.getComm_radius()),
                1000 * 60 * 30,
                mPendingIntent);
        }
    }
    
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // 接受广播
            if (intent.getAction().equals(Constants.GEOFENCE_BROADCAST_ACTION))
            {
                Bundle bundle = intent.getExtras();
                // 根据广播的status来确定是在区域内还是在区域外
                int status = bundle.getInt("status");
                if (status == 1)
                {
                    getActivity().unregisterReceiver(mGeoFenceReceiver);
                    for (CircleBean b : circleList)
                    {
                        if (bundle.getString("fence").split("#")[0].equals(b.getComm_latitude()))
                        {
                            mBuilder.setContentTitle("亲爱的用户")
                            .setContentText("欢迎来到" + b.getComm_title())
                            //                          .setNumber(number)//显示数量
                                .setTicker("亲爱的用户，您进入"+b.getComm_title()+"啦");//通知首次出现在通知栏，带上升动画效果的
                            ((HomeFragmentActivity)getActivity()).mNotificationManager.notify(1, mBuilder.build());
                            circleId = b.getComm_id();
                            reqHotShop();
                        }
                    }
                }
                else
                {
                    
                }
            }
        }
    };
    
    class TitleBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (Constants.BIND_TITLE_BROADCAST.equals(intent.getAction()))
            {
                if (GeneralUtils.isNullOrZeroLenght(Global.getOrgName()))
                {
                    titleName.setText("首页");
                }
                else
                {
                    titleName.setText(Global.getOrgName());
                    titleName.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(getActivity(),ShopDetailActivity.class);
                            intent.putExtra("id", Global.getUserOrgId());
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }
}
