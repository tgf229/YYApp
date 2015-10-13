/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.goods;

import java.util.ArrayList;
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
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.qr_codescan.MipcaActivityCapture;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.goods.adapter.GoodsListAdapter;
import com.yy.yyapp.ui.user.LoginActivity;
import com.yy.yyapp.util.DialogUtil;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
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
public class GoodsListActivity extends BaseActivity implements OnClickListener, OnHeaderRefreshListener
{
    private View footView;
    
    private PullToRefreshView mPullToRefreshView;
    
    private GoodsListAdapter goodsListAdapter;
    
    private List<GoodsBean> goodsList;
    
    private ListView goodsListView;
    
    private LinearLayout loadingLayout;
    
    /**
     * 显示加载进度的view，当listview向下滚动的时候显示此view
     */
    private View loadingFooterView;
    
    /**
     * 加载布局
     */
    private LinearLayout loadingMore;
    
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
    
    private ImageView scan, goods_type;
    
    private EditText edit;
    
    private String keyword = null;
    
    private String type = null;
    
    private boolean isSearching = false;
    
    private NetLoadingDailog dialog;
    
    private String org_id;
    
    private int num = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_fragment);
        org_id = getIntent().getStringExtra("id");
        init();
        initData();
    }
    
    private void init()
    {
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        scan = (ImageView)findViewById(R.id.scan);
        scan.setVisibility(View.GONE);
        goods_type = (ImageView)findViewById(R.id.goods_type);
        goods_type.setImageResource(R.drawable.title_back);
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
                        goodsList.clear();
                        anyMore = true;
                        reqList();
                        hideSoftInput();
                    }
                }
                return true;
            }
        });
        
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        
        goodsList = new ArrayList<GoodsBean>();
        goodsListView = (ListView)findViewById(R.id.goods_listview);
        loadingFooterView =
            ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading,
                null);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        
        goodsListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        scan.setOnClickListener(this);
        goods_type.setOnClickListener(this);
    }
    
    private void initData()
    {
        goodsListView.setOnScrollListener(new OnScrollListener()
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
        goodsListAdapter = new GoodsListAdapter(this, goodsList, this);
        goodsListView.setAdapter(goodsListAdapter);
        reqList();
    }
    
    private void reqList()
    {
        if (num <= 1)
        {
            Map<String, String> param = new HashMap<String, String>();
            if (Global.isLogin())
            {
                param.put("user_id", Global.getUserId());
            }
            param.put("page_no", String.valueOf(page));
            if (GeneralUtils.isNotNullOrZeroLenght(keyword))
            {
                param.put("product_name", keyword);
            }
            if (GeneralUtils.isNotNullOrZeroLenght(type))
            {
                param.put("product_type", type);
            }
            if (GeneralUtils.isNotNullOrZeroLenght(org_id))
            {
                param.put("org_id", org_id);
            }
            if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
            {
                param.put("org_city", Constants.cityTxt);
            }
            
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                GoodsListActivity.this,
                URLUtil.PRODUCT_LIST,
                Constants.ENCRYPT_NONE);
        }
    }
    
    private void bind()
    {
        num = num + 1;
        if (num <= 1)
        {
            dialog = new NetLoadingDailog(this);
            dialog.loading();
            Map<String, String> param = new HashMap<String, String>();
            if (Global.isLogin())
            {
                param.put("user_id", Global.getUserId());
            }
            param.put("org_id", org_id);
            ConnectService.instance().connectServiceReturnResponse(this,
                param,
                this,
                URLUtil.BIND,
                Constants.ENCRYPT_NONE);
        }
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
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.PRODUCT_LIST.equals(service))
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
                    GoodsBean bean = new GoodsBean();
                    bean.setProduct_id(ob.getString("product_id"));
                    bean.setProduct_name(ob.getString("product_name"));
                    bean.setProduct_pic_url(ob.getString("product_pic_url"));
                    bean.setProduct_type(ob.getString("product_type"));
                    bean.setProduct_content(ob.getString("product_content"));
                    bean.setProduct_price(ob.getString("product_price"));
                    bean.setActivity_price(ob.getString("activity_price"));
                    bean.setProduct_org_id(ob.getString("org_id"));
                    bean.setView_count(ob.getString("view_count"));
                    goodsList.add(bean);
                }
                showList();
                num = 0;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
        if (URLUtil.BIND.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject ob = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(ob.getString("result")))
                {
                    reqDetail();
                    num = 0;
                }
                else
                {
                    if (dialog != null)
                    {
                        dialog.dismissDialog();
                    }
                    ToastUtil.makeText(this, "很抱歉，扫描信息有误");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (dialog != null)
                {
                    dialog.dismissDialog();
                }
                ToastUtil.makeText(this, "很抱歉，扫描信息有误");
            }
        }
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
                    Global.saveUserOrgId(ob.getString("org_id"));
                    Global.saveOrgName(ob.getString("org_name"));
                    
                    Intent intent = new Intent(Constants.BIND_TITLE_BROADCAST);
                    YYApplication.yyApplication.sendBroadcast(intent);
                    ToastUtil.makeText(this, "恭喜您，扫码绑定成功");
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
    
    private void showList()
    {
        isSearching = false;
        isRefreshing = false;
        
        mPullToRefreshView.setVisibility(View.VISIBLE);
        goodsListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        
        loadingMore.setVisibility(View.GONE);
        
        goodsListAdapter.notifyDataSetChanged();
        
        mPullToRefreshView.onHeaderRefreshComplete();
    }
    
    private void goToLogin()
    {
        DialogUtil.loginTwoButtonDialog(this, new DialogCallBack()
        {
            @Override
            public void dialogBack()
            {
                Intent i = new Intent(GoodsListActivity.this, LoginActivity.class);
                GoodsListActivity.this.startActivity(i);
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.scan:
                if (Global.isLogin())
                {
                    if(GeneralUtils.isNotNullOrZeroLenght(Global.getUserOrgId()))
                    {
                        ToastUtil.makeText(GoodsListActivity.this, "操作重复，您已经绑定了商户");
                    }
                    else
                    {
                        Intent intent = new Intent(GoodsListActivity.this, MipcaActivityCapture.class);
                        startActivityForResult(intent, 0);
                    }
                }
                else
                {
                    goToLogin();
                }
                break;
            case R.id.goods_type:
//                Intent intent1 = new Intent(GoodsListActivity.this, ProductTypeActivity.class);
//                startActivityForResult(intent1, 0);
                finish();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 0;
        goodsList.clear();
        anyMore = true;
        reqList();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Constants.TYPE_SUCCESS_CODE:
                type = data.getStringExtra("type");
                page = 0;
                goodsList.clear();
                anyMore = true;
                num = num + 1;
                reqList();
                break;
            case Constants.SCAN_SUCCESS_CODE:
                Bundle bundle = data.getExtras();
                String star = bundle.getString("result");
                if (GeneralUtils.isNotNullOrZeroLenght(star))
                {
                    org_id = star.trim();
                    bind();
                }
                else
                {
                    ToastUtil.makeText(GoodsListActivity.this, "很抱歉，未扫描到信息");
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 隐藏软键盘
     * 
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void hideSoftInput()
    {
        if (GoodsListActivity.this.getCurrentFocus() != null && GoodsListActivity.this.getCurrentFocus().getWindowToken() != null)
        {
            ((InputMethodManager)GoodsListActivity.this.getSystemService(GoodsListActivity.this.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(GoodsListActivity.this.getCurrentFocus()
                .getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
