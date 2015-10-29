/*
 * 文 件 名:  ShopBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-21
 
 */
package com.yy.yyapp.bean.shop;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ShopBean implements Serializable
{
    private String org_id;
    
    private String org_name;
    
    private String org_addr;
    
    private String org_position;
    
    private String org_pic_url;
    
    private String org_content;
    
    private String org_type;
    
    private String org_city;
    
    private String org_tel;
    
    private String org_wifiname;
    
    private String org_wifipwd;
    
    private String org_website;
    
    public String getOrg_website()
    {
        return org_website;
    }

    public void setOrg_website(String org_website)
    {
        this.org_website = org_website;
    }

    public String getOrg_wifiname()
    {
        return org_wifiname;
    }
    
    public void setOrg_wifiname(String org_wifiname)
    {
        this.org_wifiname = org_wifiname;
    }
    
    public String getOrg_wifipwd()
    {
        return org_wifipwd;
    }
    
    public void setOrg_wifipwd(String org_wifipwd)
    {
        this.org_wifipwd = org_wifipwd;
    }
    
    public String getOrg_tel()
    {
        return org_tel;
    }
    
    public void setOrg_tel(String org_tel)
    {
        this.org_tel = org_tel;
    }
    
    public String getOrg_city()
    {
        return org_city;
    }
    
    public void setOrg_city(String org_city)
    {
        this.org_city = org_city;
    }
    
    public String getOrg_id()
    {
        return org_id;
    }
    
    public void setOrg_id(String org_id)
    {
        this.org_id = org_id;
    }
    
    public String getOrg_name()
    {
        return org_name;
    }
    
    public void setOrg_name(String org_name)
    {
        this.org_name = org_name;
    }
    
    public String getOrg_addr()
    {
        return org_addr;
    }
    
    public void setOrg_addr(String org_addr)
    {
        this.org_addr = org_addr;
    }
    
    public String getOrg_position()
    {
        return org_position;
    }
    
    public void setOrg_position(String org_position)
    {
        this.org_position = org_position;
    }
    
    public String getOrg_pic_url()
    {
        return org_pic_url;
    }
    
    public void setOrg_pic_url(String org_pic_url)
    {
        this.org_pic_url = org_pic_url;
    }
    
    public String getOrg_content()
    {
        return org_content;
    }
    
    public void setOrg_content(String org_content)
    {
        this.org_content = org_content;
    }
    
    public String getOrg_type()
    {
        return org_type;
    }
    
    public void setOrg_type(String org_type)
    {
        this.org_type = org_type;
    }
}
