/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.coupon.adapter.CouponListAdapter;
import com.yy.yyapp.util.ToastUtil;
import com.yy.yyapp.view.PullToRefreshView;
import com.yy.yyapp.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserCouponActivity extends BaseActivity implements OnClickListener, OnHeaderRefreshListener
{
    private PullToRefreshView mPullToRefreshView;
    
    private LinearLayout loadingLayout;
    
    private ListView couponListView;
    
    private CouponListAdapter couponListAdapter;
    
    private List<CouponBean> couponList;
    
    private LinearLayout back;
    
    private TextView title;
    
    private String channel = "1";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_coupon_list);
        init();
        reqList();
    }
    
    private void init()
    {
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("我的现金券");
        
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        couponListView = (ListView)findViewById(R.id.coupon_listview);
        
        couponList = new ArrayList<CouponBean>();
        couponListAdapter = new CouponListAdapter(this, couponList, this);
        couponListView.setAdapter(couponListAdapter);
        
        back.setOnClickListener(this);
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            UserCouponActivity.this,
            URLUtil.MY_COUPON,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.MY_COUPON.equals(service))
        {
            JSONArray array;
            try
            {
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
                    bean.setTicket_remain(ob.getString("ticket_remain"));
                    couponList.add(bean);
                }
                showList();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    private void showList()
    {
        mPullToRefreshView.setVisibility(View.VISIBLE);
        couponListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        couponListAdapter.notifyDataSetChanged();
        mPullToRefreshView.onHeaderRefreshComplete();
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
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        couponList.clear();
        reqList();
    }
}
