/*
 * 文 件 名:  LocationActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-27
 
 */
package com.yy.yyapp.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.WebviewActivity;
import com.yy.yyapp.ui.shop.ShopTypeActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MapActivity extends FragmentActivity implements LocationSource, AMapLocationListener, OnClickListener,
    UICallBack, OnMarkerClickListener, OnInfoWindowClickListener
{
    private AMap aMap;
    
    private OnLocationChangedListener mListener;
    
    private LocationManagerProxy mAMapLocationManager;
    
    private LinearLayout back,typeButton,nextButton;
    
    private TextView title,typeTxt;
    
    private MarkerOptions markerOption;
    
    private List<Marker> marker2 = new ArrayList<Marker>();// 有跳动效果的marker对象
    
    private int page = -1;
    
    private List<ShopBean> mList;
    
    private String type = null;
    
    private LatLng myMarker;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        YYApplication.yyApplication.addActivity(this);
        //        mapView = (MapView)findViewById(R.id.map);
        //        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initData();
    }
    
    /**
    * 初始化AMap对象
    */
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("地图");
        typeButton = (LinearLayout)findViewById(R.id.typeButton);
        nextButton = (LinearLayout)findViewById(R.id.nextButton);
        typeTxt = (TextView)findViewById(R.id.typeTxt);
        
        
        if (aMap == null)
        {
            aMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            //            aMap = mapView.getMap();
            setUpMap();
        }
        
        back.setOnClickListener(this);
        typeButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }
    
    private void initData()
    {
        mList = (List<ShopBean>)getIntent().getSerializableExtra("list");
        addMarkersToMap(mList);
    }
    
    /**
    * 设置一些amap的属性
    */
    private void setUpMap()
    {
        LatLng marker1 = new LatLng(32.041544, 118.767413);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setScaleControlsEnabled(true); //设置地图默认的比例尺是否显示
        aMap.getUiSettings().setZoomControlsEnabled(true); //设置地图默认的缩放按钮是否显示
        aMap.getUiSettings().setCompassEnabled(true); //设置地图默认的指南针是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        
        setUpLocation();
    }
    
    private void setUpLocation()
    {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
    }
    
    private void addMarkersToMap(List<ShopBean> list)
    {
        for (Marker m : marker2)
        {
            m.remove();
        }
        for (ShopBean bean : list)
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
                marker2.add(aMap.addMarker(markerOption));
            }
        }
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        if(myMarker != null)
        {
            String str = String.valueOf(myMarker.longitude)+","+String.valueOf(myMarker.latitude);
            param.put("current_position", str);
        }
        else
        {
            ToastUtil.makeText(this, "定位失败，无法查询数据");
            return;
        }
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(type))
        {
            param.put("org_type", type);
        }
        
        param.put("page_no", String.valueOf(page));
        ConnectService.instance().connectServiceReturnResponse(MapActivity.this,
            param,
            MapActivity.this,
            URLUtil.SHOP_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        if (URLUtil.SHOP_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                if (array.length() < 10)
                {
                    ToastUtil.makeText(this, "您附近没有更多商家了");
                    nextButton.setClickable(false);
                    if (array.length() != 0)
                    {
                        mList.clear();
                    }
                }
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    ShopBean bean = new ShopBean();
                    bean.setOrg_id(ob.getString("org_id"));
                    bean.setOrg_name(ob.getString("org_name"));
                    bean.setOrg_addr(ob.getString("org_addr"));
                    bean.setOrg_city(ob.getString("org_city"));
                    bean.setOrg_position(ob.getString("org_position"));
                    bean.setOrg_pic_url(ob.getString("org_pic_url"));
                    bean.setOrg_content(ob.getString("org_content"));
                    bean.setOrg_type(ob.getString("org_type"));
                    bean.setOrg_tel(ob.getString("org_tel"));
                    mList.add(bean);
                }
                addMarkersToMap(mList);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Intent intent = new Intent(this, WebviewActivity.class);
        String url =
            "http://m.amap.com/navi/?start=" + myMarker.longitude + "," + myMarker.latitude + "&dest="
                + marker.getPosition().longitude + "," + marker.getPosition().latitude + "&destName="
                + marker.getTitle() + "&naviBy=car&key=" + URLUtil.AMAP_KEY;
        intent.putExtra("url", url);
        intent.putExtra("title", marker.getTitle());
        startActivity(intent);
    }
    
    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        return false;
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onResume()
    {
        super.onResume();
        //        mapView.onResume();
        YYApplication.currentActivity = this.getClass().getName();
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onPause()
    {
        super.onPause();
        //        mapView.onPause();
        deactivate();
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //        mapView.onSaveInstanceState(outState);
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //        mapView.onDestroy();
        YYApplication.yyApplication.deleteActivity(this);
    }
    
    /**
    * 此方法已经废弃
    */
    @Override
    public void onLocationChanged(Location location)
    {
    }
    
    @Override
    public void onProviderDisabled(String provider)
    {
    }
    
    @Override
    public void onProviderEnabled(String provider)
    {
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
    
    /**
    * 定位成功后回调函数
    */
    @Override
    public void onLocationChanged(AMapLocation aLocation)
    {
        if (mListener != null && aLocation != null)
        {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
            myMarker = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(myMarker));
        }
    }
    
    /**
    * 激活定位
    */
    @Override
    public void activate(OnLocationChangedListener listener)
    {
        mListener = listener;
        if (mAMapLocationManager == null)
        {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            /*
             * mAMapLocManager.setGpsEnable(false);
             * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
             * API定位采用GPS和网络混合定位方式
             * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
             */
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 1000, this);
        }
    }
    
    /**
    * 停止定位
    */
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            case R.id.typeButton:
                Intent intent1 = new Intent(this, ShopTypeActivity.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.nextButton:
                ++page;
                reqList();
                break;
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
            case Constants.TYPE_SUCCESS_CODE:
                type = data.getStringExtra("type");
                typeTxt.setText(type);
                page = 0;
                nextButton.setClickable(true);
                mList.clear();
                reqList();
                break;
        }
    }
}