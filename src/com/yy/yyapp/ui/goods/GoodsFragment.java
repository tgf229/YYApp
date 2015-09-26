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
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.bean.home.HomeIconBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseFragment;
import com.yy.yyapp.ui.goods.adapter.GoodsListAdapter;
import com.yy.yyapp.ui.home.MainFragment;
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
public class GoodsFragment extends BaseFragment implements OnClickListener, OnHeaderRefreshListener
{
    private View view, footView;
    
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
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.goods_fragment, null);
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
        
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        
        goodsList = new ArrayList<GoodsBean>();
        goodsListView = (ListView)view.findViewById(R.id.goods_listview);
        loadingFooterView =
            ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading,
                null);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        
        goodsListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
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
        goodsListAdapter = new GoodsListAdapter(getActivity(), goodsList, this);
        goodsListView.setAdapter(goodsListAdapter);
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
            GoodsFragment.this,
            URLUtil.PRODUCT_LIST,
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
                if(array.length() < 10)
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
        
        mPullToRefreshView.setVisibility(View.VISIBLE);
        goodsListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        
        loadingMore.setVisibility(View.GONE);
        
        goodsListAdapter.notifyDataSetChanged();
        
        mPullToRefreshView.onHeaderRefreshComplete();
    }
    
    @Override
    public void onClick(View arg0)
    {
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 0;
        goodsList.clear();
        anyMore = true;
        reqList();
    }
}
