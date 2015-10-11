package com.yy.yyapp.bean.home;

/**
 * <轮播通告实体>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HomeBannerBean
{
    private String title;
    
    private String pic_url;
    
    private String url;
    
    private String org_id;
    
    public String getOrg_id()
    {
        return org_id;
    }
    
    public void setOrg_id(String org_id)
    {
        this.org_id = org_id;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getPic_url()
    {
        return pic_url;
    }
    
    public void setPic_url(String pic_url)
    {
        this.pic_url = pic_url;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
}
