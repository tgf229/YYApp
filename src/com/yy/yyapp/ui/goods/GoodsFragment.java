/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.goods;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
import com.yy.yyapp.ui.base.BaseFragment;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GoodsFragment extends BaseFragment implements OnClickListener, LocationSource, AMapLocationListener
{
    private View view;
    
    private AMap aMap;
    
    private MapView mapView;
    
    private OnLocationChangedListener mListener;
    
    private LocationManagerProxy mAMapLocationManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.goods_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        //init();
    }
    
    @Override
    public void onClick(View arg0)
    {
        
    }
    
    /**
     * 初始化AMap对象
     */
    private void init()
    {
        if (aMap == null)
        {
            aMap = mapView.getMap();
            setUpMap();
        }
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
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    
    /**
     * 方法必须重写
     */
    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
        deactivate();
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
    
    /**
     * 激活定位
     */
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
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
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
}
