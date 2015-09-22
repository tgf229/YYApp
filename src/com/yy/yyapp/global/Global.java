package com.yy.yyapp.global;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.network.NetWork;
import com.yy.yyapp.util.CMLog;

public class Global
{
    
    /**
     * 获取登录状态
     */
    public static boolean isLogin()
    {
        return SharePref.getBoolean(SharePref.LOGIN_TYPE, false);
    }
    
    public static void setIsLogin(boolean isLogin)
    {
        SharePref.saveBoolean(SharePref.LOGIN_TYPE, isLogin);
    }
    
    public static String getUserId()
    {
        return SharePref.getString(SharePref.USER_ID, "");
    }
    
    public static void saveUserId(String userId)
    {
        SharePref.saveString(SharePref.USER_ID, userId);
    }
    
    public static String getUserName()
    {
        return SharePref.getString(SharePref.USER_NAME, "");
    }
    
    public static void saveUserName(String userName)
    {
        SharePref.saveString(SharePref.USER_NAME, userName);
    }
    
    public static String getUserMobile()
    {
        return SharePref.getString(SharePref.USER_MOBILE, "");
    }
    
    public static void saveUserMobile(String userMobile)
    {
        SharePref.saveString(SharePref.USER_MOBILE, userMobile);
    }
    
    public static String getUserPassword()
    {
        return SharePref.getString(SharePref.USER_PASSWORD, "");
    }
    
    public static void saveUserPassword(String userPassword)
    {
        SharePref.saveString(SharePref.USER_PASSWORD, userPassword);
    }
    
    public static String getUserSex()
    {
        return SharePref.getString(SharePref.USER_SEX, "");
    }
    
    public static void saveUserSex(String userSex)
    {
        SharePref.saveString(SharePref.USER_SEX, userSex);
    }
    
    public static String getUserCity()
    {
        return SharePref.getString(SharePref.USER_CITY, "");
    }
    
    public static void saveUserCity(String userCity)
    {
        SharePref.saveString(SharePref.USER_CITY, userCity);
    }
    
    public static String getUserEmail()
    {
        return SharePref.getString(SharePref.USER_EMAIL, "");
    }
    
    public static void saveUserEmail(String userEmail)
    {
        SharePref.saveString(SharePref.USER_EMAIL, userEmail);
    }
    
    public static String getUserOrgId()
    {
        return SharePref.getString(SharePref.USER_ORG_ID, "");
    }
    
    public static void saveUserOrgId(String userOrgId)
    {
        SharePref.saveString(SharePref.USER_ORG_ID, userOrgId);
    }
    
    public static String getUserType()
    {
        return SharePref.getString(SharePref.USER_TYPE, "");
    }
    
    public static void saveUserType(String userType)
    {
        SharePref.saveString(SharePref.USER_TYPE, userType);
    }
    
    /**
     * 
     * <保存登陆信息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public static void saveData(UserBean userBean, String password)
    {
        Global.saveUserId(userBean.getUser_id());
        Global.saveUserName(userBean.getUser_name());
        Global.saveUserMobile(userBean.getUser_mobile());
        Global.saveUserPassword(password);
        Global.saveUserSex(userBean.getUser_sex());
        Global.saveUserCity(userBean.getUser_city());
        Global.saveUserEmail(userBean.getUser_email());
        Global.saveUserOrgId(userBean.getUser_org_id());
        Global.saveUserType(userBean.getUser_type());
    }
    
    public static void logout()
    {
        Global.setIsLogin(false);
        Global.saveUserId("");
        Global.saveUserName("");
        Global.saveUserMobile("");
        Global.saveUserPassword("");
        Global.saveUserSex("");
        Global.saveUserCity("");
        Global.saveUserEmail("");
        Global.saveUserOrgId("");
        Global.saveUserType("");
    }
    
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
