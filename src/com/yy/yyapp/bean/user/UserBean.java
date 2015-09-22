/*
 * 文 件 名:  UserBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-22
 
 */
package com.yy.yyapp.bean.user;

import com.yy.yyapp.bean.BaseBean;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-22]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserBean extends BaseBean
{
    private String user_id;
    
    private String user_mobile;
    
    private String user_sex;
    
    private String user_city;
    
    private String user_email;
    
    private String user_org_id;
    
    private String user_type;
    
    private String qqAccount;
    
    private String wxAccount;
    
    private String user_name;
    
    private String user_password;
    
    public String getUser_password()
    {
        return user_password;
    }

    public void setUser_password(String user_password)
    {
        this.user_password = user_password;
    }

    public String getUser_id()
    {
        return user_id;
    }
    
    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }
    
    public String getUser_mobile()
    {
        return user_mobile;
    }
    
    public void setUser_mobile(String user_mobile)
    {
        this.user_mobile = user_mobile;
    }
    
    public String getUser_sex()
    {
        return user_sex;
    }
    
    public void setUser_sex(String user_sex)
    {
        this.user_sex = user_sex;
    }
    
    public String getUser_city()
    {
        return user_city;
    }
    
    public void setUser_city(String user_city)
    {
        this.user_city = user_city;
    }
    
    public String getUser_email()
    {
        return user_email;
    }
    
    public void setUser_email(String user_email)
    {
        this.user_email = user_email;
    }
    
    public String getUser_org_id()
    {
        return user_org_id;
    }
    
    public void setUser_org_id(String user_org_id)
    {
        this.user_org_id = user_org_id;
    }
    
    public String getUser_type()
    {
        return user_type;
    }
    
    public void setUser_type(String user_type)
    {
        this.user_type = user_type;
    }
    
    public String getQqAccount()
    {
        return qqAccount;
    }
    
    public void setQqAccount(String qqAccount)
    {
        this.qqAccount = qqAccount;
    }
    
    public String getWxAccount()
    {
        return wxAccount;
    }
    
    public void setWxAccount(String wxAccount)
    {
        this.wxAccount = wxAccount;
    }
    
    public String getUser_name()
    {
        return user_name;
    }
    
    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }
}
