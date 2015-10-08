/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.HomeFragmentActivity;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.shop.adapter.ShopListAdapter;
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
public class ShopFragment extends BaseFragment implements OnClickListener, OnHeaderRefreshListener
{
    private View view, footView;
    
    private PullToRefreshView mPullToRefreshView;
    
    private ShopListAdapter shopListAdapter;
    
    private List<ShopBean> shopList;
    
    private ListView shopListView;
    
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
    
    private ImageView scan, shop_type;
    
    private EditText edit;
    
    private String type = null;
    
    private String keyword = null;
    
    private String circle1 = null;
    
    private boolean isSearching = false;
    
    private NetLoadingDailog dialog;
    
    private String org_id;
    
    private int num = 0;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.shop_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
//        circle1 = ((HomeFragmentActivity)getActivity()).circle;
        initData();
    }
    
    private void init()
    {
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        scan = (ImageView)view.findViewById(R.id.scan);
        edit = (EditText)view.findViewById(R.id.edit);
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
                        shopList.clear();
                        anyMore = true;
                        reqList();
                        hideSoftInput();
                    }
                }
                return true;
            }
        });
        
        shop_type = (ImageView)view.findViewById(R.id.shop_type);
        shop_type.setOnClickListener(this);
        
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        
        shopList = new ArrayList<ShopBean>();
        shopListView = (ListView)view.findViewById(R.id.shop_listview);
        loadingFooterView =
            ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading,
                null);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        
        shopListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        
        scan.setOnClickListener(this);
    }
    
    private void initData()
    {
        shopListView.setOnScrollListener(new OnScrollListener()
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
        shopListAdapter = new ShopListAdapter(getActivity(), shopList, this);
        shopListView.setAdapter(shopListAdapter);
        reqList();
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        param.put("page_no", String.valueOf(page));
        if (GeneralUtils.isNotNullOrZeroLenght(type))
        {
            param.put("org_type", type);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(keyword))
        {
            param.put("org_name", keyword);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(circle1))
        {
            param.put("org_comm", circle1);
        }
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            ShopFragment.this,
            URLUtil.SHOP_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    private void bind()
    {
        num = num + 1;
        if (num <= 1)
        {
            dialog = new NetLoadingDailog(getActivity());
            dialog.loading();
            Map<String, String> param = new HashMap<String, String>();
            if (Global.isLogin())
            {
                param.put("user_id", Global.getUserId());
            }
            param.put("org_id", org_id);
            ConnectService.instance().connectServiceReturnResponse(getActivity(),
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
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            this,
            URLUtil.SHOP_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.SHOP_LIST.equals(service))
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
                    ShopBean bean = new ShopBean();
                    bean.setOrg_id(ob.getString("org_id"));
                    bean.setOrg_name(ob.getString("org_name"));
                    bean.setOrg_addr(ob.getString("org_addr"));
                    bean.setOrg_city(ob.getString("org_city"));
                    bean.setOrg_position(ob.getString("org_position"));
                    bean.setOrg_pic_url(ob.getString("org_pic_url"));
                    bean.setOrg_content(ob.getString("org_content"));
                    bean.setOrg_type(ob.getString("org_type"));
                    bean.setOrg_tel(ob.getString("org_tel"));
                    shopList.add(bean);
                }
                showList();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(getActivity());
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
                    ToastUtil.makeText(getActivity(), "很抱歉，扫描信息有误");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (dialog != null)
                {
                    dialog.dismissDialog();
                }
                ToastUtil.makeText(getActivity(), "很抱歉，扫描信息有误");
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
                    ToastUtil.makeText(getActivity(), "恭喜您，扫码绑定成功");
                }
                else
                {
                    ToastUtil.showError(getActivity());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(getActivity());
            }
        }
    }
    
    private void showList()
    {
        circle1 = null;
        ((HomeFragmentActivity)getActivity()).circle = null;
        isSearching = false;
        isRefreshing = false;
        
        shopListAdapter.notifyDataSetChanged();
        mPullToRefreshView.setVisibility(View.VISIBLE);
        shopListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        loadingMore.setVisibility(View.GONE);
        mPullToRefreshView.onHeaderRefreshComplete();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.scan:
                if (Global.isLogin())
                {
                    Intent intent = new Intent(getActivity(), MipcaActivityCapture.class);
                    startActivityForResult(intent, 0);
                }
                else
                {
                    goToLogin();
                }
                break;
            case R.id.shop_type:
                Intent intent1 = new Intent(getActivity(), ShopTypeActivity.class);
                startActivityForResult(intent1, 0);
                break;
            default:
                break;
        }
    }
    
    private void goToLogin()
    {
        DialogUtil.loginTwoButtonDialog(getActivity(), new DialogCallBack()
        {
            @Override
            public void dialogBack()
            {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(i);
            }
        });
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 0;
        shopList.clear();
        anyMore = true;
        reqList();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
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
                    ToastUtil.makeText(getActivity(), "很抱歉，未扫描到信息");
                }
                break;
            case Constants.TYPE_SUCCESS_CODE:
                type = data.getStringExtra("type");
                page = 0;
                shopList.clear();
                anyMore = true;
                reqList();
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
        if (getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null)
        {
            ((InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus()
                .getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
