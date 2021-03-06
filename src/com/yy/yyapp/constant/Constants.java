package com.yy.yyapp.constant;

import java.util.ArrayList;

import com.yy.yyapp.bean.shop.ShopBean;

public class Constants
{
    
    public static String cityTxt = "";
    
    public static ArrayList<ShopBean> shopList = null;
    /**
     * 是否显示引导页，版本控制,需要引导页时，版本号变大1
     */
    public static final int GUIDE_VERSION_CODE = 1;
    
    public static final String ICON_COUPON = "现金券";
    public static final String ICON_CARD= "会员卡";
    public static final String ICON_LOCATION= "我在哪";
    public static final String ICON_SERVICE= "服务";
    public static final String ICON_HOUSE= "智能家居";
    public static final String ICON_MONEY= "返现宝";
    public static final String ICON_BUSINESS= "商圈";
    public static final String ICON_MOVIE= "看世界";
    public static final String ICON_GAME= "玩游戏";
    public static final String ICON_OLD= "养老";
    public static final String ICON_MEDICAL= "医疗";
    
    public static final String PAGE_LOAD_BROADCAST = "PAGE_LOAD_BROADCAST";
    public static final String BIND_TITLE_BROADCAST = "BIND_TITLE_BROADCAST";
    public static final String DEL_COLLECT_BROADCAST = "DEL_COLLECT_BROADCAST";
    
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    
    
    /**
     * 推出应用
     */
    public static final String EXIT_APP = "1";
    
    /**
     * 进入应用
     */
    public static final int IN_APP = 0;
    
    /**
     * sharedPreferences中保存用户的信息文件名
     */
    public static final String USER_INFO = "userInfo";
    public static final String ENCRYPT_NONE = "none";
    
    public static final String DEFAULT_PWD = "888888";
    
    /**
     *  成功码
     */
    public static final String SUCESS_CODE = "success";
    
    public static final int LOGIN_SUCCESS_CODE = 1001;
    public static final int TYPE_SUCCESS_CODE = 1002;
    public static final int CITY_SUCCESS_CODE = 1003;
    public static final int SCAN_SUCCESS_CODE = 1004;
    public static final int CIRCLE_SUCCESS_CODE = 1005;
    public static final int REGISTER_BIND_CODE = 1006;
    public static final int ORDER_TYPE_SUCCESS_CODE = 1007;
    public static final int PRICE_TYPE_SUCCESS_CODE = 1008;
    
    /**
     * 请求失败展示信息
     */
    public static final String ERROR_MESSAGE = "请求失败，请稍后再试";
    
    public static final String HOME_TAG_COUPON = "热门现金券";
    public static final String HOME_TAG_BUSINESS = "热门商家";
    public static final String HOME_TAG_GOODS = "热门商品";
    public static final String HOME_TAG_ACTIVE = "热门活动";
    public static final String HOME_TAG_GUSS = "猜你喜欢";
}

