/*
 * 文 件 名:  ProductDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-23
 
 */
package com.yy.yyapp.ui.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.goods.adapter.GoodsPicAdapter;
import com.yy.yyapp.ui.shop.adapter.ShopPicAdapter;
import com.yy.yyapp.ui.user.LoginActivity;
import com.yy.yyapp.util.DialogUtil;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;
import com.yy.yyapp.view.MyImageView;
import com.yy.yyapp.view.MyImageView2;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ProductDetailActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    private TextView title,name,price,priceTag,content,collect_btn;
    private MyImageView2 img;
    private NetLoadingDailog dialog;
    private String product_id;
    private String is_collect;
    
    private ViewPager banner_Pager;
    private MyImageView2 default_img;
    private CirclePageIndicator banner_indicator;
    private ArrayList<String> bannerList;
    private GoodsPicAdapter goodsPicAdapter;
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                int postion = banner_Pager.getCurrentItem() + 1;
                if (null != bannerList && bannerList.size() > 0)
                    banner_Pager.setCurrentItem(postion % bannerList.size(), true);
                handler.sendEmptyMessageDelayed(0, 5*1000);
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_product_detail);
        product_id = getIntent().getStringExtra("id");
        if(GeneralUtils.isNullOrZeroLenght(product_id))
        {
            ToastUtil.makeText(this, "很抱歉，获取商品信息失败");
            this.finish();
            return;
        }
        init();
        reqDetail();
    }
    
    private void init()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
//        img = (MyImageView2)findViewById(R.id.img);
        title.setText("商品详情");
        
        //BANNER
        banner_Pager = (ViewPager)findViewById(R.id.circlepager);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img = (MyImageView2)findViewById(R.id.img);
        default_img.setVisibility(View.VISIBLE);
        bannerList = new ArrayList<String>();
        goodsPicAdapter = new GoodsPicAdapter(this, bannerList);
        banner_Pager.setAdapter(goodsPicAdapter);
        banner_indicator = (CirclePageIndicator)findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        handler.sendEmptyMessageDelayed(0, 5*1000);
        
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);
        priceTag = (TextView)findViewById(R.id.priceTag);
        priceTag.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        content = (TextView)findViewById(R.id.content);
        
        collect_btn = (TextView)findViewById(R.id.collect_btn);
        collect_btn.setOnClickListener(this);
        
        back.setOnClickListener(this);
    }
    
    private void showBanner()
    {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int height = displayWidth;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayWidth, height);
        banner_Pager.setLayoutParams(params);
        
        banner_indicator.setVisibility(View.VISIBLE);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img.setVisibility(View.GONE);
        
        goodsPicAdapter.notifyDataSetChanged();
        banner_Pager.setCurrentItem(0);
        banner_indicator.notifyDataSetChanged();
    }
    
    private void reqDetail()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("product_id", product_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            ProductDetailActivity.this,
            URLUtil.PRODUCT_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqCollect()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("collect_item_id", product_id);
        param.put("collect_type", "商品");
        if("1".equals(is_collect))
        {
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                ProductDetailActivity.this,
                URLUtil.DEL_COLLECT,
                Constants.ENCRYPT_NONE);
        }
        else
        {
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                ProductDetailActivity.this,
                URLUtil.ADD_COLLECT,
                Constants.ENCRYPT_NONE);
        }
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if(URLUtil.PRODUCT_DETAIL.equals(service))
        {
            if(dialog != null)
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
                    GoodsBean bean = new GoodsBean();
                    bean.setProduct_id(ob.getString("product_id"));
                    bean.setProduct_pic_url(ob.getString("product_pic_url"));
                    bean.setProduct_name(ob.getString("product_name"));
                    bean.setProduct_price(ob.getString("product_price"));
                    bean.setActivity_price(ob.getString("activity_price"));
                    bean.setProduct_content(ob.getString("product_content"));
                    is_collect = ob.getString("is_collect");
                    for(String str: ob.getString("filepath").split(","))
                    {
                        bannerList.add(str);
                    }
                    if(GeneralUtils.isNotNullOrZeroSize(bannerList))
                    {
                        showBanner();
                    }
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
        if (URLUtil.ADD_COLLECT.equals(service))
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
                    ToastUtil.makeText(this, "恭喜您，收藏成功");
                }
                else
                {
                    ToastUtil.makeText(this, "您已收藏");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.DEL_COLLECT.equals(service))
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
                    Intent intent = new Intent(Constants.DEL_COLLECT_BROADCAST);
                    intent.putExtra("id", product_id);
                    YYApplication.yyApplication.sendBroadcast(intent);
                    ToastUtil.makeText(this, "取消成功");
                }
                else
                {
                    ToastUtil.makeText(this, "取消失败");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    private void showDetail(GoodsBean bean)
    {
//        ImageLoader.getInstance().displayImage(bean.getProduct_pic_url(),
//            img,
//            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
        name.setText(bean.getProduct_name());
        String activityPrice = bean.getActivity_price();
        if(GeneralUtils.isNullOrZeroLenght(activityPrice) || "null".equals(activityPrice) || activityPrice.equals(bean.getProduct_price()))
        {
            price.setText("￥ "+bean.getProduct_price());
            priceTag.setText("");
        }
        else
        {
            price.setText("￥ "+bean.getActivity_price());
            priceTag.setText("市场价  ￥ "+bean.getProduct_price());
        }
        if("1".equals(is_collect))
        {
            collect_btn.setText("取消收藏");
        }
        content.setText(bean.getProduct_content());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            case R.id.collect_btn:
                if(Global.isLogin())
                {
                    dialog.loading();
                    reqCollect();
                }
                else
                {
                    goToLogin();
                }
                break;
            default:
                break;
        }
    }
    
    private void goToLogin()
    {
        DialogUtil.loginTwoButtonDialog(this, new DialogCallBack()
        {
            @Override
            public void dialogBack()
            {
                Intent i = new Intent(ProductDetailActivity.this, LoginActivity.class);
                ProductDetailActivity.this.startActivity(i);
            }
        });
    }
}
