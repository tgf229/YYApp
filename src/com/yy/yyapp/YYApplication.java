/*
 * �� �� ��:  YYApplication.java
 * ��    Ȩ:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * ��    ��:  <����>
 * ��    ���� <�汾��> 
 * �� �� ��:  tgf
 * ����ʱ��:  2015-9-17
 
 */
package com.yy.yyapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.util.FileSystemManager;

/**
 * <应用初始化>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class YYApplication extends Application
{
    
    /**
     * APP实例
     */
    public static YYApplication yyApplication = null;
    
    /**
     * 本地activity栈
     */
    public static List<Activity> activitys = new ArrayList<Activity>();
    
    /**
     * 当前avtivity
     */
    public static String currentActivity = "";
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        Context contx = getApplicationContext();
        loadData(contx);
        yyApplication = this;
    }
    
    public static void loadData(Context context)
    {
        ImageLoaderConfiguration config =
            new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(4).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                .discCacheSize(50 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(new File(FileSystemManager.getCacheImgFilePath(context))))
                .build();
        ImageLoader.getInstance().init(config);
    }
    
    @Override
    public void onTerminate()
    {
        super.onTerminate();
        Global.logoutApplication();
    }
    
    /**
     * <增加>
     * <功能详细描述>
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void addActivity(Activity activity)
    {
        activitys.add(activity);
    }
    
    /**
     * <删除>
     * <功能详细描述>
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void deleteActivity(Activity activity)
    {
        if (activity != null)
        {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    
    /**
     * 初始化option
     * picFail--加载失败时显示
     * picLoading--正在加载图片时显示
     * picEmpty--uri为空的时候显示
     */
    public static DisplayImageOptions setAllDisplayImageOptions(Context context, String picFail, String picLoading,
        String picEmpty)
    {
        DisplayImageOptions options;
        //通过图片名调用图片ID
        int fail = context.getResources().getIdentifier(picFail, "drawable", "com.yy.yyapp");
        int loading = context.getResources().getIdentifier(picLoading, "drawable", "com.yy.yyapp");
        int empty = context.getResources().getIdentifier(picEmpty, "drawable", "com.yy.yyapp");
        options =
            new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageOnFail(fail)
                .showImageForEmptyUri(empty)
                .showImageOnLoading(loading)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(0))
                .build();
        
        return options;
    }
}
