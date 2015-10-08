/*
 * 文 件 名:  NetResponse.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-25
 
 */
package com.yy.yyapp.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yy.yyapp.bean.user.UserBean;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NetResponse
{
    
    public static UserBean loginResponse(Context context, JSONObject bean)
        throws Exception
    {
        try
        {
            UserBean user = new UserBean();
            user.setUser_id((String)bean.get("user_id"));
            user.setUser_name((String)bean.get("user_name"));
            user.setUser_name((String)bean.get("user_name"));
            user.setUser_mobile((String)bean.get("user_mobile"));
            user.setUser_sex((String)bean.get("user_sex"));
            user.setUser_city((String)bean.get("user_city"));
            user.setScore((String)bean.get("score"));
            user.setUser_email((String)bean.get("user_email"));
            user.setUser_org_id((String)bean.get("user_org_id"));
            user.setUser_type((String)bean.get("user_type"));
            user.setTotal_user((String)bean.get("total_user"));
            return user;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
