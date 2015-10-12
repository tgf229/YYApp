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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
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
public class CouponDetailActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title, name, price, priceTag, content, number, limit, nameTxt, brandTxt, typeTxt, priceTxt,
        send_btn,collect_btn;
    
    private MyImageView img;
    
    private NetLoadingDailog dialog;
    
    private String coupon_id;
    
    private String channel = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_detail);
        coupon_id = getIntent().getStringExtra("id");
        channel = getIntent().getStringExtra("channel");
        if (GeneralUtils.isNullOrZeroLenght(coupon_id))
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
        limit = (TextView)findViewById(R.id.limit);
        
        nameTxt = (TextView)findViewById(R.id.nameTxt);
        brandTxt = (TextView)findViewById(R.id.brandTxt);
        typeTxt = (TextView)findViewById(R.id.typeTxt);
        priceTxt = (TextView)findViewById(R.id.priceTxt);
        
        send_btn = (TextView)findViewById(R.id.send_btn);
        collect_btn = (TextView)findViewById(R.id.collect_btn);
        
        if("1".equals(channel))
        {
            send_btn.setText("使    用");
            collect_btn.setVisibility(View.GONE);
        }
        
        back.setOnClickListener(this);
        send_btn.setOnClickListener(this);
        collect_btn.setOnClickListener(this);
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
    
    private void reqSend()
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
            URLUtil.COUPON_SEND,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqUse()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("ticket_id", coupon_id);
        param.put("ticket_count", "1");
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            CouponDetailActivity.this,
            URLUtil.USE_COUPON,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqCollect()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("collect_item_id", coupon_id);
        param.put("collect_type", "现金券");
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            CouponDetailActivity.this,
            URLUtil.ADD_COLLECT,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.COUPON_DETAIL.equals(service))
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
                    CouponBean bean = new CouponBean();
                    bean.setTicket_id(ob.getString("ticket_id"));
                    bean.setTicket_title(ob.getString("ticket_name"));
                    bean.setTicket_pic_url(ob.getString("ticket_pic_url"));
                    bean.setTicket_money(ob.getString("ticket_money"));
                    bean.setTicket_number(ob.getString("ticket_number"));
                    bean.setTicket_content(ob.getString("ticket_content"));
                    bean.setTicket_limit(ob.getString("ticket_limit"));
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
        if (URLUtil.COUPON_SEND.equals(service))
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
                    ToastUtil.makeText(this, "恭喜您，领取成功");
                }
                else
                {
                    ToastUtil.makeText(this, "很抱歉，领取失败");
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
                    ToastUtil.makeText(this, "很抱歉，收藏失败");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.USE_COUPON.equals(service))
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
                    ToastUtil.makeText(this, "恭喜您，使用成功");
                }
                else
                {
                    ToastUtil.makeText(this, "很抱歉，您无法使用此券");
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
        
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_money()))
        {
            price.setText("￥ " + bean.getTicket_money());
            priceTxt.setText("面值：" + bean.getTicket_money());
        }
        else
        {
            price.setText("");
            priceTxt.setText("");
        }
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_limit()))
        {
            limit.setText("已领" + bean.getTicket_number() + "张");
        }
        else
        {
            limit.setText("");
        }
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_number()))
        {
            number.setText("总计" + bean.getTicket_number() + "张");
        }
        else
        {
            number.setText("");
        }
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_title()))
        {
            nameTxt.setText("名称：" + bean.getTicket_title());
        }
        else
        {
            nameTxt.setText("名称： 无");
        }
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_brand()))
        {
            brandTxt.setText("品牌：" + bean.getTicket_brand());
        }
        else
        {
            brandTxt.setText("品牌： 无");
        }
        if (GeneralUtils.isNotNullOrZeroLenght(bean.getTicket_type()))
        {
            typeTxt.setText("类型：" + bean.getTicket_type());
        }
        else
        {
            typeTxt.setText("类型： 无");
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
            case R.id.send_btn:
                if(Global.isLogin())
                {
                    dialog.loading();
                    if("1".equals(channel))
                    {
                        reqUse();
                    }
                    else
                    {
                        reqSend();
                    }
                }
                else
                {
                    goToLogin();
                }
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
                Intent i = new Intent(CouponDetailActivity.this, LoginActivity.class);
                CouponDetailActivity.this.startActivity(i);
            }
        });
    }
}
