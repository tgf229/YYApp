/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.coupon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.coupon.adapter.CouponListAdapter;
import com.yy.yyapp.ui.home.CityActivity;
import com.yy.yyapp.ui.shop.ShopTypeActivity;
import com.yy.yyapp.util.GeneralUtils;
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
public class CouponActivity extends BaseActivity implements OnClickListener, OnHeaderRefreshListener
{
    private View view, footView;
    
    private PullToRefreshView mPullToRefreshView;
    
    private LinearLayout loadingLayout;
    
    private ListView couponListView;
    
    private CouponListAdapter couponListAdapter;
    
    private List<CouponBean> couponList;
    
    /**
     * 显示加载进度的view，当listview向下滚动的时候显示此view
     */
    private View loadingFooterView;
    
    /**
     * 加载布局
     */
    private LinearLayout loadingMore, title_back_layout;
    
    /**
     * 结束标志
     */
    private RelativeLayout endTips;
    
    /**
     * 是否有更多消息
     */
    private boolean anyMore = true;
    
    /**
     * 记录滚动列表的状态，是否已刷新
     */
    private boolean isRefreshing = false;
    
    /**
     * 当前页
     */
    private int page = 0;
    
    private EditText edit;
    
    private String keyword;
    
    private String org_id;
    
    private boolean isSearching = false;
    
    private Button title_tag_type, title_tag_price, title_tag_city, title_tag_order;
    
    private String[] orderStrs = {"热门","品牌","类别"};
    
    private String type;
    private String city;
    private String order;
    private String price_range;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_list);
        org_id = getIntent().getStringExtra("org_id");
        init();
        initData();
    }
    
    private void init()
    {
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        title_tag_type = (Button)findViewById(R.id.title_tag_type);
        title_tag_price = (Button)findViewById(R.id.title_tag_price);
        title_tag_city = (Button)findViewById(R.id.title_tag_city);
        title_tag_order = (Button)findViewById(R.id.title_tag_order);
        
        title_tag_type.setOnClickListener(this);
        title_tag_price.setOnClickListener(this);
        title_tag_city.setOnClickListener(this);
        title_tag_order.setOnClickListener(this);
        
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        title_back_layout = (LinearLayout)findViewById(R.id.title_back_layout);
        
        edit = (EditText)findViewById(R.id.edit);
        edit.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    edit.setText("");
                }
                else if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    if (!isSearching)
                    {
                        isSearching = true;
                        if (GeneralUtils.isNotNullOrZeroLenght(edit.getText().toString().trim()))
                        {
                            keyword = edit.getText().toString().trim();
                        }
                        else
                        {
                            keyword = null;
                        }
                        page = 0;
                        couponList.clear();
                        anyMore = true;
                        reqList();
                        hideSoftInput();
                    }
                }
                else if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    CouponActivity.this.finish();
                }
                
                return true;
            }
        });
        
        couponList = new ArrayList<CouponBean>();
        couponListView = (ListView)findViewById(R.id.coupon_listview);
        loadingFooterView =
            ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        
        couponListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        title_back_layout.setOnClickListener(this);
    }
    
    private void initData()
    {
        couponListView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && anyMore && !isRefreshing
                    && view.getLastVisiblePosition() == view.getCount() - 1)
                {
                    loadingMore.setVisibility(View.VISIBLE);
                    isRefreshing = true;
                    page++;
                    reqList();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        couponListAdapter = new CouponListAdapter(this, couponList, this);
        couponListView.setAdapter(couponListAdapter);
        reqList();
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(keyword))
        {
            param.put("ticket_name", keyword);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(org_id))
        {
            param.put("ticket_org_id", org_id);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(type))
        {
            param.put("ticket_trade_type", type);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(city))
        {
            param.put("org_city", city);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(order))
        {
            param.put("orderfield", order);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
        {
            param.put("org_city", Constants.cityTxt);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(price_range))
        {
            param.put("price_range", price_range);
        }
        
        param.put("page_no", String.valueOf(page));
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            CouponActivity.this,
            URLUtil.COUPON_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.COUPON_LIST.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                if (array.length() < 10)
                {
                    anyMore = false;
                }
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
        isSearching = false;
        isRefreshing = false;
        mPullToRefreshView.setVisibility(View.VISIBLE);
        couponListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        loadingMore.setVisibility(View.GONE);
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
            case R.id.title_tag_type:
                Intent intent = new Intent(this, ShopTypeActivity.class);
                startActivityForResult(intent, Constants.TYPE_SUCCESS_CODE);
                break;
            case R.id.title_tag_price:
                Intent intent1 = new Intent(this, CouponPriceActivity.class);
                startActivityForResult(intent1, Constants.PRICE_TYPE_SUCCESS_CODE);
                break;
            case R.id.title_tag_city:
                Intent cIntent = new Intent(this, CityActivity.class);
                if(GeneralUtils.isNotNullOrZeroLenght(city))
                {
                    cIntent.putExtra("city", city);
                }
                else
                {
                    cIntent.putExtra("city", Constants.cityTxt);
                }
                startActivityForResult(cIntent, 0);
                break;
            case R.id.title_tag_order:
                ArrayList<String> orderList = new ArrayList<String>();
                Collections.addAll(orderList, orderStrs);
                Intent oIntent = new Intent(this, TypeActivity.class);
                oIntent.putStringArrayListExtra("list", orderList);
                startActivityForResult(oIntent, 1);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Constants.TYPE_SUCCESS_CODE:
                type = data.getStringExtra("type");
                title_tag_type.setText(GeneralUtils.isNullOrZeroLenght(type)? "行业": type);
                page = 0;
                couponList.clear();
                anyMore = true;
                reqList();
                break;
            case Constants.CITY_SUCCESS_CODE:
                city = data.getStringExtra("city");
                title_tag_city.setText(city);
                page = 0;
                couponList.clear();
                anyMore = true;
                reqList();
                break;
            case Constants.ORDER_TYPE_SUCCESS_CODE:
                String str = data.getStringExtra("type");
                if("品牌".equals(str))
                {
                    order = "ticket_brand";
                    title_tag_order.setText("品牌");
                }else if("类别".equals(str))
                {
                    order = "ticket_type";
                    title_tag_order.setText("类别");
                }
                else
                {
                    title_tag_order.setText("热门");
                }
                page = 0;
                couponList.clear();
                anyMore = true;
                reqList();
                break;
            case Constants.PRICE_TYPE_SUCCESS_CODE:
                price_range = data.getStringExtra("type");
                if("".equals(price_range))
                {
                    title_tag_price.setText("全部");
                }else
                {
                    title_tag_price.setText(price_range);
                }
                page = 0;
                couponList.clear();
                anyMore = true;
                reqList();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 0;
        couponList.clear();
        anyMore = true;
        reqList();
    }
    
    /**
     * 隐藏软键盘
     * 
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void hideSoftInput()
    {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
        {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
