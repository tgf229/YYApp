/*
 * 文 件 名:  HotCouponBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-21
 
 */
package com.yy.yyapp.bean.goods;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GoodsBean
{
    private String product_id;
    
    private String product_name;
    
    private String product_pic_url; 
    
    private String product_type;
    
    private String product_content;
    
    private String product_price;
    
    private String activity_price;
    
    private String product_org_id;
    
    private String view_count;
    
    private String is_collect;
    
    public String getIs_collect()
    {
        return is_collect;
    }

    public void setIs_collect(String is_collect)
    {
        this.is_collect = is_collect;
    }

    public String getActivity_price()
    {
        return activity_price;
    }

    public void setActivity_price(String activity_price)
    {
        this.activity_price = activity_price;
    }

    public String getProduct_org_id()
    {
        return product_org_id;
    }

    public void setProduct_org_id(String product_org_id)
    {
        this.product_org_id = product_org_id;
    }

    public String getProduct_id()
    {
        return product_id;
    }
    
    public void setProduct_id(String product_id)
    {
        this.product_id = product_id;
    }
    
    public String getProduct_name()
    {
        return product_name;
    }
    
    public void setProduct_name(String product_name)
    {
        this.product_name = product_name;
    }
    
    public String getProduct_type()
    {
        return product_type;
    }
    
    public void setProduct_type(String product_type)
    {
        this.product_type = product_type;
    }
    
    public String getProduct_content()
    {
        return product_content;
    }
    
    public void setProduct_content(String product_content)
    {
        this.product_content = product_content;
    }
    
    public String getProduct_price()
    {
        return product_price;
    }
    
    public void setProduct_price(String product_price)
    {
        this.product_price = product_price;
    }
    
    public String getView_count()
    {
        return view_count;
    }
    
    public void setView_count(String view_count)
    {
        this.view_count = view_count;
    }
    
    public String getProduct_pic_url()
    {
        return product_pic_url;
    }
    
    public void setProduct_pic_url(String product_pic_url)
    {
        this.product_pic_url = product_pic_url;
    }
}
