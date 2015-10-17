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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.active.ActiveBean;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.home.HomeBannerBean;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.active.ActiveActivity;
import com.yy.yyapp.ui.active.ActiveDetailActivity;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.coupon.CouponActivity;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.goods.GoodsListActivity;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.ui.home.LocationActivity;
import com.yy.yyapp.ui.home.MapActivity;
import com.yy.yyapp.ui.home.adapter.HomeBannerPagerAdapter;
import com.yy.yyapp.ui.shop.adapter.ShopPicAdapter;
import com.yy.yyapp.ui.user.LoginActivity;
import com.yy.yyapp.util.DialogUtil;
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
    
    private TextView title, content, collect_btn,hot_product_more,hot_coupon_more_my,hot_coupon_more_all,hot_active_more;
    
    private String org_id;
    
    private Button address, wifi;
    
    private MyImageView img;
    
    private NetLoadingDailog dialog;
    
    private List<GoodsBean> hotProductList;
    private List<CouponBean> hotCouponList;
    private List<ActiveBean> hotActiveList;
    
    private LinearLayout hotProductLayout, wifiLayout,hotCouponLayout,hotActiveLayout;
    
    private RelativeLayout hotProductLayoutTitle, call_layout,hotCouponLayoutTitle,hotActiveLayoutTitle;
    
    private ShopBean bean = new ShopBean();;
    private ViewPager banner_Pager;
    private MyImageView default_img;
    private CirclePageIndicator banner_indicator;
    private ArrayList<String> bannerList;
    private ShopPicAdapter shopPicAdapter;
    private String is_collect;
    
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
        reqHotCoupon();
        reqHotActive();
    }
    
    private void init()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("商家详情");
        
        wifiLayout = (LinearLayout)findViewById(R.id.wifiLayout);
        wifi = (Button)findViewById(R.id.wifi);
        
        address = (Button)findViewById(R.id.address);
//        img = (MyImageView)findViewById(R.id.img);
        content = (TextView)findViewById(R.id.content);
        call_layout = (RelativeLayout)findViewById(R.id.call_layout);
        
        //BANNER
        banner_Pager = (ViewPager)findViewById(R.id.circlepager);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img = (MyImageView)findViewById(R.id.img);
        default_img.setVisibility(View.VISIBLE);
        bannerList = new ArrayList<String>();
        shopPicAdapter = new ShopPicAdapter(this, bannerList);
        banner_Pager.setAdapter(shopPicAdapter);
        banner_indicator = (CirclePageIndicator)findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        handler.sendEmptyMessageDelayed(0, 5*1000);
        
        hot_product_more = (TextView)findViewById(R.id.hot_product_more);
        hotProductLayoutTitle = (RelativeLayout)findViewById(R.id.hot_product_layout_title);
        hotProductLayout = (LinearLayout)findViewById(R.id.hot_product_layout);
        
        hot_coupon_more_my = (TextView)findViewById(R.id.hot_coupon_more_my);
        hot_coupon_more_all = (TextView)findViewById(R.id.hot_coupon_more_all);
        hotCouponLayoutTitle = (RelativeLayout)findViewById(R.id.hot_coupon_layout_title);
        hotCouponLayout = (LinearLayout)findViewById(R.id.hot_coupon_layout);
        
        hot_active_more= (TextView)findViewById(R.id.hot_active_more);
        hotActiveLayoutTitle = (RelativeLayout)findViewById(R.id.hot_active_layout_title);
        hotActiveLayout = (LinearLayout)findViewById(R.id.hot_active_layout);
        
        collect_btn = (TextView)findViewById(R.id.collect_btn);
        collect_btn.setOnClickListener(this);
        
        back.setOnClickListener(this);
        address.setOnClickListener(this);
        hot_product_more.setOnClickListener(this);
        hot_coupon_more_my.setOnClickListener(this);
        hot_coupon_more_all.setOnClickListener(this);
        hot_active_more.setOnClickListener(this);
    }
    
    private void showBanner()
    {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int height = displayWidth/2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(displayWidth, height);
        banner_Pager.setLayoutParams(params);
        
        banner_indicator.setVisibility(View.VISIBLE);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img.setVisibility(View.GONE);
        
        shopPicAdapter.notifyDataSetChanged();
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
        if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
        {
            param.put("org_city", Constants.cityTxt);
        }
        param.put("org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.PRODUCT_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotCoupon()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
        {
            param.put("org_city", Constants.cityTxt);
        }
        param.put("ticket_org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.COUPON_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqHotActive()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
        {
            param.put("org_city", Constants.cityTxt);
        }
        param.put("org_id", org_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.ACTIVE_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqCollect()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("collect_item_id", org_id);
        param.put("collect_type", "商家");
        if("1".equals(is_collect))
        {
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                ShopDetailActivity.this,
                URLUtil.DEL_COLLECT,
                Constants.ENCRYPT_NONE);
        }
        else
        {
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                ShopDetailActivity.this,
                URLUtil.ADD_COLLECT,
                Constants.ENCRYPT_NONE);
        }
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
                    bean.setOrg_id(ob.getString("org_id"));
                    bean.setOrg_name(ob.getString("org_name"));
                    bean.setOrg_pic_url(ob.getString("org_pic_url"));
                    bean.setOrg_content(ob.getString("org_content"));
                    bean.setOrg_addr(ob.getString("org_addr"));
                    bean.setOrg_city(ob.getString("org_city"));
                    bean.setOrg_position(ob.getString("org_position"));
                    bean.setOrg_tel(ob.getString("org_tel"));
                    bean.setOrg_wifiname(ob.getString("org_wifiname"));
                    bean.setOrg_wifipwd(ob.getString("org_wifipwd"));
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
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
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
        if (URLUtil.COUPON_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                hotCouponList = new ArrayList<CouponBean>();
                array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                    {
                        break;
                    }
                    CouponBean bean = new CouponBean();
                    bean.setTicket_id(ob.getString("ticket_id"));
                    bean.setTicket_title(ob.getString("ticket_name"));
                    bean.setTicket_pic_url(ob.getString("ticket_pic_url"));
                    bean.setTicket_brand(ob.getString("ticket_brand"));
                    bean.setTicket_money(ob.getString("ticket_money"));
                    bean.setTicket_number(ob.getString("ticket_number"));
                    bean.setTicket_limit(ob.getString("ticket_limit"));
                    hotCouponList.add(bean);
                }
                showHotCoupon();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.ACTIVE_LIST.equals(service))
        {
                JSONArray array;
                try
                {
                    hotActiveList = new ArrayList<ActiveBean>();
                    array = new JSONArray(res);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject ob = array.getJSONObject(i);
                        if (!Constants.SUCESS_CODE.equals(ob.get("result")))
                        {
                            break;
                        }
                        ActiveBean bean = new ActiveBean();
                        bean.setActivity_id(ob.getString("activity_id"));
                        bean.setActivity_title(ob.getString("activity_title"));
                        bean.setActivity_addr(ob.getString("activity_addr"));
                        bean.setActivity_time(ob.getString("activity_time"));
                        bean.setActivity_pic_url(ob.getString("activity_pic_url"));
                        hotActiveList.add(bean);
                    }
                    showHotActive();
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
                    intent.putExtra("id", org_id);
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
    
    private void showDetail(final ShopBean bean)
    {
//        ImageLoader.getInstance().displayImage(bean.getOrg_pic_url(),
//            img,
//            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
        address.setText(bean.getOrg_addr());
        String cont = bean.getOrg_content();
        if (GeneralUtils.isNotNullOrZeroLenght(cont))
        {
            cont = cont.replaceAll("rn", "\n");
            content.setText(cont);
        }
        else
        {
            content.setText("");
        }
        title.setText(bean.getOrg_name());
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getOrg_wifiname()))
        {
            wifi.setText("本店提供免费wifi：" + bean.getOrg_wifiname() + "  密码：" + bean.getOrg_wifipwd());
            wifiLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            wifiLayout.setVisibility(View.GONE);
        }
        if("1".equals(is_collect))
        {
            collect_btn.setText("取消收藏");
        }
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
        if (GeneralUtils.isNullOrZeroSize(hotProductList))
        {
            hotProductLayoutTitle.setVisibility(View.GONE);
            hotProductLayout.setVisibility(View.GONE);
        }
        else
        {
            ImageView pic1 = (ImageView)findViewById(R.id.hot_product_pic1);
            TextView name1 = (TextView)findViewById(R.id.hot_product_name1);
            ImageView pic2 = (ImageView)findViewById(R.id.hot_product_pic2);
            TextView name2 = (TextView)findViewById(R.id.hot_product_name2);
            ImageView pic3 = (ImageView)findViewById(R.id.hot_product_pic3);
            TextView name3 = (TextView)findViewById(R.id.hot_product_name3);
            pic1.setVisibility(View.GONE);
            name1.setVisibility(View.GONE);
            pic2.setVisibility(View.GONE);
            name2.setVisibility(View.GONE);
            pic3.setVisibility(View.GONE);
            name3.setVisibility(View.GONE);
            for (int i = 0; i < hotProductList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(hotProductList.get(i).getProduct_pic_url(),
                        pic1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    if (GeneralUtils.isNotNullOrZeroLenght(hotProductList.get(i).getProduct_name()))
                    {
                        name1.setText(hotProductList.get(i).getProduct_name());
                    }
                    pic1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", hotProductList.get(0).getProduct_id());
                            startActivity(intent);
                        }
                    });
                    pic1.setVisibility(View.VISIBLE);
                    name1.setVisibility(View.VISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(hotProductList.get(i).getProduct_pic_url(),
                        pic2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    if (GeneralUtils.isNotNullOrZeroLenght(hotProductList.get(i).getProduct_name()))
                    {
                        name2.setText(hotProductList.get(i).getProduct_name());
                    }
                    pic2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", hotProductList.get(1).getProduct_id());
                            startActivity(intent);
                        }
                    });
                    pic2.setVisibility(View.VISIBLE);
                    name2.setVisibility(View.VISIBLE);
                }
                else if (i == 2)
                {
                    ImageLoader.getInstance().displayImage(hotProductList.get(i).getProduct_pic_url(),
                        pic3,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    if (GeneralUtils.isNotNullOrZeroLenght(hotProductList.get(i).getProduct_name()))
                    {
                        name3.setText(hotProductList.get(i).getProduct_name());
                    }
                    pic3.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", hotProductList.get(2).getProduct_id());
                            startActivity(intent);
                        }
                    });
                    pic3.setVisibility(View.VISIBLE);
                    name3.setVisibility(View.VISIBLE);
                }
            }
            //            for (final GoodsBean g : hotProductList)
            //            {
            //                
            //                View item = (View)LayoutInflater.from(this).inflate(R.layout.shop_detail_hot_product_item, null);
            //                ImageView pic = (ImageView)item.findViewById(R.id.hot_product_pic);
            //                TextView name = (TextView)item.findViewById(R.id.hot_product_name);
            //                
            //                ImageLoader.getInstance().displayImage(g.getProduct_pic_url(),
            //                    pic,
            //                    YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
            //                if (GeneralUtils.isNotNullOrZeroLenght(g.getProduct_name()))
            //                {
            //                    if(g.getProduct_name().length() > 11)
            //                    {
            //                        name.setText(g.getProduct_name().substring(0, 10)+"...");
            //                    }
            //                    else
            //                    {
            //                        name.setText(g.getProduct_name());
            //                    }
            //                }
            //                item.setOnClickListener(new OnClickListener()
            //                {
            //                    @Override
            //                    public void onClick(View arg0)
            //                    {
            //                        Intent intent = new Intent(ShopDetailActivity.this,ProductDetailActivity.class);
            //                        intent.putExtra("id", g.getProduct_id());
            //                        startActivity(intent);
            //                    }
            //                });
            //                hotProductLayout.addView(item);
            //            }
        }
    }
    
    private void showHotCoupon()
    {
        if (GeneralUtils.isNullOrZeroSize(hotCouponList))
        {
            hotCouponLayoutTitle.setVisibility(View.GONE);
            hotCouponLayout.setVisibility(View.GONE);
        }
        else
        {
            ImageView pic1 = (ImageView)findViewById(R.id.hot_coupon_pic1);
            ImageView pic2 = (ImageView)findViewById(R.id.hot_coupon_pic2);
            pic1.setVisibility(View.GONE);
            pic2.setVisibility(View.GONE);
            for (int i = 0; i < hotCouponList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(hotCouponList.get(i).getTicket_pic_url(),
                        pic1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    pic1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, CouponDetailActivity.class);
                            intent.putExtra("id", hotCouponList.get(0).getTicket_id());
                            startActivity(intent);
                        }
                    });
                    pic1.setVisibility(View.VISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(hotCouponList.get(i).getTicket_pic_url(),
                        pic2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    pic2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, CouponDetailActivity.class);
                            intent.putExtra("id", hotCouponList.get(1).getTicket_id());
                            startActivity(intent);
                        }
                    });
                    pic2.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    
    private void showHotActive()
    {
        if (GeneralUtils.isNullOrZeroSize(hotActiveList))
        {
            hotActiveLayoutTitle.setVisibility(View.GONE);
            hotActiveLayout.setVisibility(View.GONE);
        }
        else
        {
            ImageView pic1 = (ImageView)findViewById(R.id.hot_active_pic1);
            ImageView pic2 = (ImageView)findViewById(R.id.hot_active_pic2);
            pic1.setVisibility(View.GONE);
            pic2.setVisibility(View.GONE);
            for (int i = 0; i < hotActiveList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(hotActiveList.get(i).getActivity_pic_url(),
                        pic1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    pic1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, ActiveDetailActivity.class);
                            intent.putExtra("id", hotActiveList.get(0).getActivity_id());
                            startActivity(intent);
                        }
                    });
                    pic1.setVisibility(View.VISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(hotActiveList.get(i).getActivity_pic_url(),
                        pic2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    pic2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View arg0)
                        {
                            Intent intent = new Intent(ShopDetailActivity.this, ActiveDetailActivity.class);
                            intent.putExtra("id", hotActiveList.get(1).getActivity_id());
                            startActivity(intent);
                        }
                    });
                    pic2.setVisibility(View.VISIBLE);
                }
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
            case R.id.collect_btn:
                if (Global.isLogin())
                {
                    dialog.loading();
                    reqCollect();
                }
                else
                {
                    goToLogin();
                }
                break;
            case R.id.address:
                Intent intent = new Intent(this, LocationActivity.class);
                ArrayList<ShopBean> list = new ArrayList<ShopBean>();
                list.add(bean);
                intent.putExtra("list", list);
                this.startActivity(intent);
                break;
            case R.id.hot_product_more:
                Intent intent1 = new Intent(this, GoodsListActivity.class);
                intent1.putExtra("id", org_id);
                startActivity(intent1);
                break;
            case R.id.hot_coupon_more_my:
                Intent intent2 = new Intent(this, CouponActivity.class);
                intent2.putExtra("org_id", org_id);
                startActivity(intent2);
                break;
            case R.id.hot_coupon_more_all:
                Intent intent3 = new Intent(this, CouponActivity.class);
                startActivity(intent3);
                break;
            case R.id.hot_active_more:
                Intent intent4 = new Intent(this, ActiveActivity.class);
                intent4.putExtra("org_id", org_id);
                startActivity(intent4);
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
                Intent i = new Intent(ShopDetailActivity.this, LoginActivity.class);
                ShopDetailActivity.this.startActivity(i);
            }
        });
    }
}
