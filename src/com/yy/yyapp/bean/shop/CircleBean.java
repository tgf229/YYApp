/*
 * 文 件 名:  CircleBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-8
 
 */
package com.yy.yyapp.bean.shop;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CircleBean
{
    private String comm_id;
    
    private String comm_title;
    
    private String comm_content;
    
    private String comm_pic_url;
    
    private String comm_latitude;
    private String comm_longitude;
    private String comm_radius;
    
    public String getComm_latitude()
    {
        return comm_latitude;
    }

    public void setComm_latitude(String comm_latitude)
    {
        this.comm_latitude = comm_latitude;
    }

    public String getComm_longitude()
    {
        return comm_longitude;
    }

    public void setComm_longitude(String comm_longitude)
    {
        this.comm_longitude = comm_longitude;
    }

    public String getComm_radius()
    {
        return comm_radius;
    }

    public void setComm_radius(String comm_radius)
    {
        this.comm_radius = comm_radius;
    }

    public String getComm_id()
    {
        return comm_id;
    }
    
    public void setComm_id(String comm_id)
    {
        this.comm_id = comm_id;
    }
    
    public String getComm_title()
    {
        return comm_title;
    }
    
    public void setComm_title(String comm_title)
    {
        this.comm_title = comm_title;
    }
    
    public String getComm_content()
    {
        return comm_content;
    }
    
    public void setComm_content(String comm_content)
    {
        this.comm_content = comm_content;
    }
    
    public String getComm_pic_url()
    {
        return comm_pic_url;
    }
    
    public void setComm_pic_url(String comm_pic_url)
    {
        this.comm_pic_url = comm_pic_url;
    }
}
