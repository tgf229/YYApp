/*
 * 文 件 名:  ProductDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-23
 
 */
package com.yy.yyapp.ui.goods;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.user.LoginActivity;
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
    private TextView title,name,price,priceTag,content;
    private MyImageView2 img;
    private NetLoadingDailog dialog;
    private String product_id;
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
        img = (MyImageView2)findViewById(R.id.img);
        title.setText("商品详情");
        
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);
        priceTag = (TextView)findViewById(R.id.priceTag);
        priceTag.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        content = (TextView)findViewById(R.id.content);
        
        back.setOnClickListener(this);
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
    }
    
    private void showDetail(GoodsBean bean)
    {
        ImageLoader.getInstance().displayImage(bean.getProduct_pic_url(),
            img,
            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
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
            
            default:
                break;
        }
    }
}
