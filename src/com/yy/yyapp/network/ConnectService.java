package com.yy.yyapp.network;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;

import com.yy.yyapp.callback.INetCallBack;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.util.CMLog;
import com.yy.yyapp.util.SecurityUtils;

/**
 * <数据封装层>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ConnectService
{
    /**
     * 日志
     */
    
    private static ConnectService connectServiceSingleton = null;
    
    private ConnectService()
    {
    }
    
    public static ConnectService instance()
    {
        if (connectServiceSingleton == null)
        {
            connectServiceSingleton = new ConnectService();
        }
        return connectServiceSingleton;
    }
    
    /**
     * 
     * <访问网络,返回体为T，根据T再去判断T里面retcode是否为空/NULL> 
     * <调用方法可以参考MainFragment>
     * 
     * @param param
     * @param handler
     * @param t
     * @see [类、类#方法、类#成员]
     */
    public <T> void connectServiceReturnResponse(final Context context, Map<String, String> param,
        final UICallBack callback, final Class<T> t, final String serviceName, final String encrypt)
    {
        try
        {
            // 封装输入参数
            NetWork netWork = new NetWork();
            String url = URLUtil.SERVER + serviceName;// 加上具体的访问地址
            netWork.startPost(url, param, new INetCallBack()
            {
                @Override
                public void onComplete(String result)
                {
                    try
                    {
                        if (result != null && !"".equals(result.trim()))
                        {
//                            if (Constants.ENCRYPT_SIMPLE.equals(encrypt))
//                            {
//                                result = SecurityUtils.decode2Str(result, Global.getBaseKey());//解密
//                            }
                            CMLog.i("info", "result:" + result);
                        }
                        JSONArray array = new JSONArray(result);
                        result = array.getJSONObject(0).toString();
                        final T res = GsonHelper.toType(result, t);
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (res != null)
                                {
                                    callback.netBack(res);
                                }
                                else
                                {
                                    try
                                    {
                                        callback.netBack(t.newInstance());
                                    }
                                    catch (InstantiationException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    catch (IllegalAccessException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    callback.netBack(t.newInstance());
                                }
                                catch (InstantiationException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                        e.printStackTrace();
                    }
                    
                }
            });
        }
        catch (Exception e)
        {
            ((Activity)context).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        callback.netBack(t.newInstance());
                    }
                    catch (InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * <访问网络,上传文件> <功能详细描述>
     * 
     * @param param
     * @param handler
     * @param t
     * @see [类、类#方法、类#成员]
     */
    public <T> void connectServiceUploadFile(final Context context, Map<String, String> param,
        Map<String, List<File>> fileParameters, final UICallBack callback, final Class<T> t, final String serviceName,
        final String encrypt)
    {
        try
        {
            // 封装输入参数
            NetWork netWork = new NetWork();
            String url = URLUtil.SERVER + serviceName + "?encrypt=" + encrypt;// 加上具体的访问地址
            netWork.startPost(url, param, fileParameters, new INetCallBack()
            {
                @Override
                public void onComplete(String result)
                {
                    try
                    {
                        if (result != null && !"".equals(result.trim()))
                        {
//                            if (Constants.ENCRYPT_SIMPLE.equals(encrypt))
//                            {
//                                result = SecurityUtils.decode2Str(result, Global.getBaseKey());//解密
//                            }
                            CMLog.i("info", "result:" + result);
                        }
                        final T res = GsonHelper.toType(result, t);
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (res != null)
                                {
                                    callback.netBack(res);
                                }
                                else
                                {
                                    try
                                    {
                                        callback.netBack(t.newInstance());
                                    }
                                    catch (InstantiationException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    catch (IllegalAccessException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        ((Activity)context).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    callback.netBack(t.newInstance());
                                }
                                catch (InstantiationException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                        e.printStackTrace();
                    }
                    
                }
            });
        }
        catch (Exception e)
        {
            ((Activity)context).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        callback.netBack(t.newInstance());
                    }
                    catch (InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            e.printStackTrace();
        }
    }
    
}
