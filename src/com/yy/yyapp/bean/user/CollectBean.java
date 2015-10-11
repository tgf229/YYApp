/*
 * 文 件 名:  CollectBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-11
 
 */
package com.yy.yyapp.bean.user;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CollectBean
{
    private String collect_item_id;
    private String collect_type;    //中文  【商家、商品、活动、现金券】
    private String collect_pic;
    public String getCollect_item_id()
    {
        return collect_item_id;
    }
    public void setCollect_item_id(String collect_item_id)
    {
        this.collect_item_id = collect_item_id;
    }
    public String getCollect_type()
    {
        return collect_type;
    }
    public void setCollect_type(String collect_type)
    {
        this.collect_type = collect_type;
    }
    public String getCollect_pic()
    {
        return collect_pic;
    }
    public void setCollect_pic(String collect_pic)
    {
        this.collect_pic = collect_pic;
    }
}
