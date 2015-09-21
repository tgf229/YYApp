/*

 * 文 件 名:  HotCoupon.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.bean.home;

/**
 * <热门商品>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Guss
{
    private String id;
    private String name;
    private String img;
    private String content;
    private String priceTag; //吊牌价
    private String price; //现价
    private String distance;
    private String sold;
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getImg()
    {
        return img;
    }
    public void setImg(String img)
    {
        this.img = img;
    }
    public String getPriceTag()
    {
        return priceTag;
    }
    public void setPriceTag(String priceTag)
    {
        this.priceTag = priceTag;
    }
    public String getPrice()
    {
        return price;
    }
    public void setPrice(String price)
    {
        this.price = price;
    }
    public String getDistance()
    {
        return distance;
    }
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    public String getSold()
    {
        return sold;
    }
    public void setSold(String sold)
    {
        this.sold = sold;
    }
}
