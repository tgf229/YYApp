/*
 * 文 件 名:  BaseListBean.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-24
 
 */
package com.yy.yyapp.bean;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseListBean
{
    private List<?> list;

    public List<?> getList()
    {
        return list;
    }

    public void setList(List<?> list)
    {
        this.list = list;
    }
}
