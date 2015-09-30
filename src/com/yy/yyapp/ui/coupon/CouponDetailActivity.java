/*
 * 文 件 名:  ProductDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-23
 
 */
package com.yy.yyapp.ui.coupon;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
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
public class CouponDetailActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    private TextView title,name,price,priceTag,content,number;
    private MyImageView img;
    private NetLoadingDailog dialog;
    private String coupon_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_detail);
        coupon_id = getIntent().getStringExtra("id");
        if(GeneralUtils.isNullOrZeroLenght(coupon_id))
        {
            ToastUtil.makeText(this, "很抱歉，获取信息失败");
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
        img = (MyImageView)findViewById(R.id.img);
        title.setText("券详情");
        
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);
        number = (TextView)findViewById(R.id.number);
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
        param.put("ticket_id", coupon_id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            CouponDetailActivity.this,
            URLUtil.COUPON_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if(URLUtil.COUPON_DETAIL.equals(service))
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
                    CouponBean bean = new CouponBean();
                    bean.setTicket_id(ob.getString("ticket_id"));
                    bean.setTicket_title(ob.getString("ticket_name"));
                    bean.setTicket_pic_url(ob.getString("ticket_pic_url"));
                    bean.setTicket_money(ob.getString("ticket_money"));
                    bean.setTicket_number(ob.getString("ticket_number"));
                    bean.setTicket_content(ob.getString("ticket_content"));
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
    
    private void showDetail(CouponBean bean)
    {
        ImageLoader.getInstance().displayImage(bean.getTicket_pic_url(),
            img,
            YYApplication.setAllDisplayImageOptions(this, "default_pic", "default_pic", "default_pic"));
        name.setText(bean.getTicket_title());
        content.setText(bean.getTicket_content());
        
        if(GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_money()))
        {
            price.setText("￥ "+bean.getTicket_money());
        }
        else
        { 
            price.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_number()))
        {
            number.setText("发行数量："+bean.getTicket_number());
        }
        else
        { 
            number.setText("");
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