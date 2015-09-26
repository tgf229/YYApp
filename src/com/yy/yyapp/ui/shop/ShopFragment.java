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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.qr_codescan.MipcaActivityCapture;
import com.yy.yyapp.R;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.shop.adapter.ShopListAdapter;
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
    
    private ImageView scan;
    
    private Handler handler = new Handler()
    {
        
    };
    
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
        initData();
    }
    
    private void init()
    {
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        scan = (ImageView)view.findViewById(R.id.scan);
        
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
        param.put("is_recomment", "N");
        param.put("page_no", String.valueOf(page));
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            ShopFragment.this,
            URLUtil.SHOP_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.SHOP_LIST.equals(service))
        {
            //shopList.clear();
            
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
                    if(!Constants.SUCESS_CODE.equals(ob.get("result")))
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
    }
    
    private void showList()
    {
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
                Intent intent = new Intent(getActivity(), MipcaActivityCapture.class);
                startActivityForResult(intent, 0);
                break;
            
            default:
                break;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 0:
            if(resultCode == 1001){
                Bundle bundle = data.getExtras();
                String star = bundle.getString("result");
                System.out.println(star);
                
            }
            break;
        }
    }   
}
