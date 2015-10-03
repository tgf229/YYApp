/*
 * 文 件 名:  LocationActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-27
 
 */
package com.yy.yyapp.ui.home;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.yy.yyapp.R;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LocationActivity extends Activity implements LocationSource, AMapLocationListener, OnClickListener
{
    private AMap aMap;
    
    private MapView mapView;
    
    private OnLocationChangedListener mListener;
    
    private LocationManagerProxy mAMapLocationManager;
    
    private LinearLayout back;
    
    private TextView title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }
    
    /**
    * 初始化AMap对象
    */
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("我在哪");
        
        if (aMap == null)
        {
            aMap = mapView.getMap();
            setUpMap();
        }
        
        back.setOnClickListener(this);
    }
    
    
    
    /**
    * 设置一些amap的属性
    */
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
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
        deactivate();
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    
    /**
    * 方法必须重写
    */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
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
            
            default:
                break;
        }
        
    }
}