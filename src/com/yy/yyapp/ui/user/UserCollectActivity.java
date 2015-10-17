/*
 * 文 件 名:  UserCard.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-9
 
 */
package com.yy.yyapp.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.user.CollectBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.active.ActiveDetailActivity;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.ui.shop.ShopDetailActivity;
import com.yy.yyapp.ui.user.adapter.UserCollectAdapter;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;

/**
 * <我的收藏>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-9]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserCollectActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private ImageView shop_tag_content_1, shop_tag_content_2, shop_tag_content_3, shop_tag_content_4,
        product_tag_content_1, product_tag_content_2, product_tag_content_3, product_tag_content_4,
        active_tag_content_1, active_tag_content_2, active_tag_content_3, active_tag_content_4, coupon_tag_content_1,
        coupon_tag_content_2, coupon_tag_content_3, coupon_tag_content_4;
    
    private TextView title, shop_tag_tips, product_tag_tips, active_tag_tips, coupon_tag_tips,shop_tag_more,product_tag_more,active_tag_more,coupon_tag_more;
    
    private List<CollectBean> list = new ArrayList<CollectBean>();
    
    ArrayList<CollectBean> shopList = new ArrayList<CollectBean>();
    
    ArrayList<CollectBean> productList = new ArrayList<CollectBean>();
    
    ArrayList<CollectBean> activeList = new ArrayList<CollectBean>();
    
    ArrayList<CollectBean> couponList = new ArrayList<CollectBean>();
    private UserCollectAdapter userCollectAdapter;
    
    private NetLoadingDailog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_collect);
        init();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        reqList();
    }
    
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        
        shop_tag_tips = (TextView)findViewById(R.id.shop_tag_tips);
        product_tag_tips = (TextView)findViewById(R.id.product_tag_tips);
        active_tag_tips = (TextView)findViewById(R.id.active_tag_tips);
        coupon_tag_tips = (TextView)findViewById(R.id.coupon_tag_tips);
        
        shop_tag_more = (TextView)findViewById(R.id.shop_tag_more);
        product_tag_more = (TextView)findViewById(R.id.product_tag_more);
        active_tag_more = (TextView)findViewById(R.id.active_tag_more);
        coupon_tag_more = (TextView)findViewById(R.id.coupon_tag_more);
        
        shop_tag_content_1 = (ImageView)findViewById(R.id.shop_tag_content_1);
        shop_tag_content_2 = (ImageView)findViewById(R.id.shop_tag_content_2);
        shop_tag_content_3 = (ImageView)findViewById(R.id.shop_tag_content_3);
        shop_tag_content_4 = (ImageView)findViewById(R.id.shop_tag_content_4);
//        ListView shop_tag_listview = (ListView)findViewById(R.id.shop_tag_listview);
//        userCollectAdapter = new UserCollectAdapter(this, shopList, this);
//        shop_tag_listview.setAdapter(userCollectAdapter);
        
        product_tag_content_1 = (ImageView)findViewById(R.id.product_tag_content_1);
        product_tag_content_2 = (ImageView)findViewById(R.id.product_tag_content_2);
        product_tag_content_3 = (ImageView)findViewById(R.id.product_tag_content_3);
        product_tag_content_4 = (ImageView)findViewById(R.id.product_tag_content_4);
        
        active_tag_content_1 = (ImageView)findViewById(R.id.active_tag_content_1);
        active_tag_content_2 = (ImageView)findViewById(R.id.active_tag_content_2);
        active_tag_content_3 = (ImageView)findViewById(R.id.active_tag_content_3);
        active_tag_content_4 = (ImageView)findViewById(R.id.active_tag_content_4);
        
        coupon_tag_content_1 = (ImageView)findViewById(R.id.coupon_tag_content_1);
        coupon_tag_content_2 = (ImageView)findViewById(R.id.coupon_tag_content_2);
        coupon_tag_content_3 = (ImageView)findViewById(R.id.coupon_tag_content_3);
        coupon_tag_content_4 = (ImageView)findViewById(R.id.coupon_tag_content_4);
        
        title.setText("我的收藏");
        back.setOnClickListener(this);
        
        shop_tag_more.setOnClickListener(this);
        product_tag_more.setOnClickListener(this);
        active_tag_more.setOnClickListener(this);
        coupon_tag_more.setOnClickListener(this);
    }
    
    private void reqList()
    {
        list.clear();
        shopList.clear();
        productList.clear();
        activeList.clear();
        couponList.clear();
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", Global.getUserId());
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.MY_COLLECT,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (dialog != null)
        {
            dialog.dismissDialog();
        }
        if (URLUtil.MY_COLLECT.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject ob = array.getJSONObject(i);
                    CollectBean bean = new CollectBean();
                    bean.setCollect_item_id(ob.getString("collect_item_id"));
                    bean.setCollect_type(ob.getString("collect_type"));
                    bean.setCollect_pic(ob.getString("collect_pic"));
                    list.add(bean);
                }
                praiseList();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    private void praiseList()
    {
        for (CollectBean b : list)
        {
            if ("商家".equals(b.getCollect_type()))
            {
                shopList.add(b);
            }
            else if ("商品".equals(b.getCollect_type()))
            {
                productList.add(b);
            }
            else if ("活动".equals(b.getCollect_type()))
            {
                activeList.add(b);
            }
            else if ("现金券".equals(b.getCollect_type()))
            {
                couponList.add(b);
            }
        }
        
        showShopList();
        showProductList();
        showActiveList();
        showCouponList();
    }
    
//    private void showShopList()
//    {
//        userCollectAdapter.notifyDataSetChanged();
//    }
    
    
    
    private void showShopList()
    {
        shop_tag_content_1.setVisibility(View.GONE);
        shop_tag_content_2.setVisibility(View.GONE);
        shop_tag_content_3.setVisibility(View.GONE);
        shop_tag_content_4.setVisibility(View.GONE);
        if (GeneralUtils.isNullOrZeroSize(shopList))
        {
            shop_tag_tips.setVisibility(View.VISIBLE);
            shop_tag_more.setVisibility(View.GONE);
        }
        else
        {
            for (int i = 0; i < shopList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(shopList.get(i).getCollect_pic(),
                        shop_tag_content_1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    shop_tag_content_1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ShopDetailActivity.class);
                            intent.putExtra("id", shopList.get(0).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    shop_tag_content_1.setVisibility(View.VISIBLE);
                    shop_tag_content_2.setVisibility(View.INVISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(shopList.get(i).getCollect_pic(),
                        shop_tag_content_2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    shop_tag_content_2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ShopDetailActivity.class);
                            intent.putExtra("id", shopList.get(1).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    shop_tag_content_2.setVisibility(View.VISIBLE);
                }
                else if (i == 2)
                {
                    ImageLoader.getInstance().displayImage(shopList.get(i).getCollect_pic(),
                        shop_tag_content_3,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    shop_tag_content_3.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ShopDetailActivity.class);
                            intent.putExtra("id", shopList.get(2).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    shop_tag_content_3.setVisibility(View.VISIBLE);
                    shop_tag_content_4.setVisibility(View.INVISIBLE);
                }
                else if (i == 3)
                {
                    ImageLoader.getInstance().displayImage(shopList.get(i).getCollect_pic(),
                        shop_tag_content_4,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    shop_tag_content_4.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ShopDetailActivity.class);
                            intent.putExtra("id", shopList.get(3).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    shop_tag_content_4.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    
    private void showProductList()
    {
        product_tag_content_1.setVisibility(View.GONE);
        product_tag_content_2.setVisibility(View.GONE);
        product_tag_content_3.setVisibility(View.GONE);
        product_tag_content_4.setVisibility(View.GONE);
        if (GeneralUtils.isNullOrZeroSize(productList))
        {
            product_tag_tips.setVisibility(View.VISIBLE);
            product_tag_more.setVisibility(View.GONE);
        }
        else
        {
            for (int i = 0; i < productList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(productList.get(i).getCollect_pic(),
                        product_tag_content_1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    product_tag_content_1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", productList.get(0).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    product_tag_content_1.setVisibility(View.VISIBLE);
                    product_tag_content_2.setVisibility(View.INVISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(productList.get(i).getCollect_pic(),
                        product_tag_content_2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    product_tag_content_2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", productList.get(1).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    product_tag_content_2.setVisibility(View.VISIBLE);
                }
                else if (i == 2)
                {
                    ImageLoader.getInstance().displayImage(productList.get(i).getCollect_pic(),
                        product_tag_content_3,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    product_tag_content_3.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", productList.get(2).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    product_tag_content_3.setVisibility(View.VISIBLE);
                    product_tag_content_4.setVisibility(View.INVISIBLE);
                }
                else if (i == 3)
                {
                    ImageLoader.getInstance().displayImage(productList.get(i).getCollect_pic(),
                        product_tag_content_4,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    product_tag_content_4.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ProductDetailActivity.class);
                            intent.putExtra("id", productList.get(3).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    product_tag_content_4.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    
    private void showActiveList()
    {
        active_tag_content_1.setVisibility(View.GONE);
        active_tag_content_2.setVisibility(View.GONE);
        active_tag_content_3.setVisibility(View.GONE);
        active_tag_content_4.setVisibility(View.GONE);
        if (GeneralUtils.isNullOrZeroSize(activeList))
        {
            active_tag_tips.setVisibility(View.VISIBLE);
            active_tag_more.setVisibility(View.GONE);
        }
        else
        {
            for (int i = 0; i < activeList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(activeList.get(i).getCollect_pic(),
                        active_tag_content_1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    active_tag_content_1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ActiveDetailActivity.class);
                            intent.putExtra("id", activeList.get(0).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    active_tag_content_1.setVisibility(View.VISIBLE);
                    active_tag_content_2.setVisibility(View.INVISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(activeList.get(i).getCollect_pic(),
                        active_tag_content_2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    active_tag_content_2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ActiveDetailActivity.class);
                            intent.putExtra("id", activeList.get(1).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    active_tag_content_2.setVisibility(View.VISIBLE);
                }
                else if (i == 2)
                {
                    ImageLoader.getInstance().displayImage(activeList.get(i).getCollect_pic(),
                        active_tag_content_3,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    active_tag_content_3.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ActiveDetailActivity.class);
                            intent.putExtra("id", activeList.get(2).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    active_tag_content_3.setVisibility(View.VISIBLE);
                    active_tag_content_4.setVisibility(View.INVISIBLE);
                }
                else if (i == 3)
                {
                    ImageLoader.getInstance().displayImage(activeList.get(i).getCollect_pic(),
                        active_tag_content_4,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    active_tag_content_4.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, ActiveDetailActivity.class);
                            intent.putExtra("id", activeList.get(3).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    active_tag_content_4.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    
    private void showCouponList()
    {
        coupon_tag_content_1.setVisibility(View.GONE);
        coupon_tag_content_2.setVisibility(View.GONE);
        coupon_tag_content_3.setVisibility(View.GONE);
        coupon_tag_content_4.setVisibility(View.GONE);
        if (GeneralUtils.isNullOrZeroSize(couponList))
        {
            coupon_tag_tips.setVisibility(View.VISIBLE);
            coupon_tag_more.setVisibility(View.GONE);
        }
        else
        {
            for (int i = 0; i < couponList.size(); i++)
            {
                if (i == 0)
                {
                    ImageLoader.getInstance().displayImage(couponList.get(i).getCollect_pic(),
                        coupon_tag_content_1,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    coupon_tag_content_1.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, CouponDetailActivity.class);
                            intent.putExtra("id", couponList.get(0).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    coupon_tag_content_1.setVisibility(View.VISIBLE);
                    coupon_tag_content_2.setVisibility(View.INVISIBLE);
                }
                else if (i == 1)
                {
                    ImageLoader.getInstance().displayImage(couponList.get(i).getCollect_pic(),
                        coupon_tag_content_2,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    coupon_tag_content_2.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, CouponDetailActivity.class);
                            intent.putExtra("id", couponList.get(1).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    coupon_tag_content_2.setVisibility(View.VISIBLE);
                }
                else if (i == 2)
                {
                    ImageLoader.getInstance().displayImage(couponList.get(i).getCollect_pic(),
                        coupon_tag_content_3,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    coupon_tag_content_3.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, CouponDetailActivity.class);
                            intent.putExtra("id", couponList.get(2).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    coupon_tag_content_3.setVisibility(View.VISIBLE);
                    coupon_tag_content_4.setVisibility(View.INVISIBLE);
                }
                else if (i == 3)
                {
                    ImageLoader.getInstance().displayImage(couponList.get(i).getCollect_pic(),
                        coupon_tag_content_4,
                        YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
                    coupon_tag_content_4.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(UserCollectActivity.this, CouponDetailActivity.class);
                            intent.putExtra("id", couponList.get(3).getCollect_item_id());
                            UserCollectActivity.this.startActivity(intent);
                        }
                    });
                    coupon_tag_content_4.setVisibility(View.VISIBLE);
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
            case R.id.shop_tag_more:
                Intent intent = new Intent(this,CollectMoreActivity.class);
                intent.putExtra("list", shopList);
                startActivity(intent);
                break;
            case R.id.product_tag_more:
                Intent intent1 = new Intent(this,CollectMoreActivity.class);
                intent1.putExtra("list", productList);
                startActivity(intent1);
                break;
            case R.id.active_tag_more:
                Intent intent2 = new Intent(this,CollectMoreActivity.class);
                intent2.putExtra("list", activeList);
                startActivity(intent2);
                break;
            case R.id.coupon_tag_more:
                Intent intent3 = new Intent(this,CollectMoreActivity.class);
                intent3.putExtra("list", couponList);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
}
