package com.yy.yyapp.util;


import com.yy.yyapp.YYApplication;

import android.content.Context;
import android.widget.Toast;

/**
 * <公共提示类>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class ToastUtil
{
    /**
     * 
     * <显示toast提示>
     * <功能详细描述>
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeText(Context context, String msg)
    {
        if (YYApplication.currentActivity.equals(context.getClass().getName()))
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 
     * <显示失败信息 > <功能详细描述>
     * 
     * @param ctx
     * @see [类、类#方法、类#成员]
     */
    public static void showError(Context context)
    {
        makeText(context, "请求失败，请稍后再试");
    }
}
