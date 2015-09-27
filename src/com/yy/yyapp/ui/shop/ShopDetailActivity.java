/*
 * 文 件 名:  ProductDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-23
 
 */
package com.yy.yyapp.ui.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;
import com.yy.yyapp.view.MyImageView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ShopDetailActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title, content;
    
    private String org_id;
    
    private Button address;
    
    private MyImageView img;
    
    private NetLoadingDailog dialog;
    
    private List<GoodsBean> hotProductList;
    
    private LinearLayout hotProductLayout;
    
    private RelativeLayout hotProductLayoutTitle,call_layout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail);
        org_id = getIntent().getStringExtra("id");
        if (GeneralUtils.isNullOrZeroLenght(org_id))
        {
            ToastUtil.makeText(this, "很抱歉，获取商家信息失败");
            this.finish();
            return;
        }
        init();
        reqDetail();
        reqHotProduct();
    }
    
    private void init()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("商家详情");
        
        address = (Button)findViewById(R.id.address);
        img = (MyImageView)findViewById(R.id.img);
        content = (TextView)findViewById(R.id.content);
        call_layout = (RelativeLayout)findViewById(R.id.call_layout);
        
        hotProductLayoutTitle = (RelativeLayout)findViewById(R.id.hot_product_layout_title);
        hotProductLayout = (LinearLayout)findViewById(R.id.hot_product_layout);
        
        back.setOnClickListener(this);
    }
    
    private void reqDetail()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.SHOP_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotProduct()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.PRODUCT_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.SHOP_DETAIL.equals(service))
        {
            if (dialog != null)
            {
                dialog.dismissDialog();
            }
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject ob = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(ob.getString("result")))
                {
                    ShopBean bean = new ShopBean();
                    bean.setOrg_id(ob.getString("org_id"));
                    bean.setOrg_name(ob.getString("org_name"));
                    bean.setOrg_pic_url(ob.getString("org_pic_url"));
                    bean.setOrg_content(ob.getString("org_content"));
                    bean.setOrg_addr(ob.getString("org_addr"));
                    bean.setOrg_city(ob.getString("org_city"));
                    bean.setOrg_position(ob.getString("org_position"));
                    //bean.setOrg_tel(ob.getString("org_tel"));   接口还没返回  TODO
                    
                    showDetail(bean);
                }
                else
                {
                    ToastUtil.showError(this);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.PRODUCT_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                hotProductList = new ArrayList<GoodsBean>();
                array = new JSONArray(res);
                for (int i = 0; i < (array.length() > 3 ? 3 : array.length()); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if(!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    GoodsBean bean = new GoodsBean();
                    bean.setProduct_id(ob.getString("product_id"));
                    bean.setProduct_name(ob.getString("product_name"));
                    bean.setProduct_pic_url(ob.getString("product_pic_url"));
                    hotProductList.add(bean);
                }
                showHotProduct();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    private void showDetail(final ShopBean bean)
    {
        ImageLoader.getInstance().displayImage(bean.getOrg_pic_url(),
            img,
            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
        address.setText(bean.getOrg_addr());
        content.setText(bean.getOrg_content());
        title.setText(bean.getOrg_name());
        call_layout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getOrg_tel()));
                startActivity(intent);                
            }
        });
    }
    
    private void showHotProduct()
    {
        hotProductLayout.removeAllViews();
        
        if(GeneralUtils.isNullOrZeroSize(hotProductList))
        {
            hotProductLayoutTitle.setVisibility(View.GONE);
            hotProductLayout.setVisibility(View.GONE);
        }
        else
        {
            for (final GoodsBean g : hotProductList)
            {
                View item = (View)LayoutInflater.from(this).inflate(R.layout.shop_detail_hot_product_item, null);
                ImageView pic = (ImageView)item.findViewById(R.id.hot_product_pic);
                TextView name = (TextView)item.findViewById(R.id.hot_product_name);
                
                ImageLoader.getInstance().displayImage(g.getProduct_pic_url(),
                    pic,
                    YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                if (GeneralUtils.isNotNullOrZeroLenght(g.getProduct_name()))
                {
                    if(g.getProduct_name().length() > 11)
                    {
                        name.setText(g.getProduct_name().substring(0, 10)+"...");
                    }
                    else
                    {
                        name.setText(g.getProduct_name());
                    }
                }
                item.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        Intent intent = new Intent(ShopDetailActivity.this,ProductDetailActivity.class);
                        intent.putExtra("id", g.getProduct_id());
                        startActivity(intent);
                    }
                });
                hotProductLayout.addView(item);
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            
            default:
                break;
        }
    }
}