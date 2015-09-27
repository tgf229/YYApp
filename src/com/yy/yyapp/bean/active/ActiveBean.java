/*
 * 文 件 名:  HotCouponBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-21
 
 */
package com.yy.yyapp.bean.active;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ActiveBean
{
    private String activity_id;
    private String activity_title;
    private String activity_addr;
    private String activity_position;
    private String activity_time;
    private String activity_pic_url;
    public String getActivity_id()
    {
        return activity_id;
    }
    public void setActivity_id(String activity_id)
    {
        this.activity_id = activity_id;
    }
    public String getActivity_title()
    {
        return activity_title;
    }
    public void setActivity_title(String activity_title)
    {
        this.activity_title = activity_title;
    }
    public String getActivity_addr()
    {
        return activity_addr;
    }
    public void setActivity_addr(String activity_addr)
    {
        this.activity_addr = activity_addr;
    }
    public String getActivity_position()
    {
        return activity_position;
    }
    public void setActivity_position(String activity_position)
    {
        this.activity_position = activity_position;
    }
    public String getActivity_time()
    {
        return activity_time;
    }
    public void setActivity_time(String activity_time)
    {
        this.activity_time = activity_time;
    }
    public String getActivity_pic_url()
    {
        return activity_pic_url;
    }
    public void setActivity_pic_url(String activity_pic_url)
    {
        this.activity_pic_url = activity_pic_url;
    }
}
