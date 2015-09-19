package com.yy.yyapp.global;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract.Constants;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.network.NetWork;
import com.yy.yyapp.util.CMLog;

public class Global
{
    
    /**
     * <退出应用>
     * <功能详细描述>
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void logoutApplication()
    {
        try
        {
            //保存到配置文件
            for (Activity activity : YYApplication.activitys)
            {
                activity.finish();
                activity = null;
            }
            YYApplication.activitys.clear();
        }
        catch (Exception e)
        {
            CMLog.e("", "finish activity exception:" + e.getMessage());
        }
        finally
        {
            NetWork.shutdown();
            ImageLoader.getInstance().clearMemoryCache();
            System.exit(0);
        }
    }
}
