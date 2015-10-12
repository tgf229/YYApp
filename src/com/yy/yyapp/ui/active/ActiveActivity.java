/*
 * 文 件 名:  MainFragment.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-17
 
 */
package com.yy.yyapp.ui.active;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.active.ActiveBean;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.active.adapter.ActiveListAdapter;
import com.yy.yyapp.ui.base.BaseActivity;
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
public class ActiveActivity extends BaseActivity implements OnClickListener, OnHeaderRefreshListener
{
    private View view, footView;
    
    private PullToRefreshView mPullToRefreshView;
    
    private LinearLayout loadingLayout;
    
    private ListView activeListView;
    
    private ActiveListAdapter activeListAdapter;
    
    private List<ActiveBean> activeList;
    
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_list);
        org_id =  getIntent().getStringExtra("org_id");
        init();
        initData();
    }
    
    private void init()
    {
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        title_back_layout = (LinearLayout)findViewById(R.id.title_back_layout);
        
        edit = (EditText)findViewById(R.id.edit);
        edit.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_DEL) { 
                    edit.setText("");
                }
                else if(keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    if(!isSearching)
                    {
                        isSearching = true;
                        if(GeneralUtils.isNotNullOrZeroLenght(edit.getText().toString().trim()))
                        {
                            keyword = edit.getText().toString().trim();
                        }
                        else
                        {
                            keyword = null;
                        }
                        page = 0;
                        activeList.clear();
                        anyMore = true;
                        reqList();
                        hideSoftInput();
                    }
                }
                else if(keyCode == KeyEvent.KEYCODE_BACK) { 
                    ActiveActivity.this.finish();
                }
                    
                return true;
            }
        });
        
        activeList = new ArrayList<ActiveBean>();
        activeListView = (ListView)findViewById(R.id.active_listview);
        loadingFooterView =
            ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        
        activeListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        title_back_layout.setOnClickListener(this);
    }
    
    private void initData()
    {
        activeListView.setOnScrollListener(new OnScrollListener()
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
        activeListAdapter = new ActiveListAdapter(this, activeList, this);
        activeListView.setAdapter(activeListAdapter);
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
        if (GeneralUtils.isNotNullOrZeroLenght(keyword))
        {
            param.put("activity_title", keyword);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(org_id))
        {
            param.put("org_id", org_id);
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            ActiveActivity.this,
            URLUtil.ACTIVE_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.ACTIVE_LIST.equals(service))
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
                        ActiveBean bean = new ActiveBean();
                        bean.setActivity_id(ob.getString("activity_id"));
                        bean.setActivity_title(ob.getString("activity_title"));
                        bean.setActivity_addr(ob.getString("activity_addr"));
                        bean.setActivity_time(ob.getString("activity_time"));
                        bean.setActivity_pic_url(ob.getString("activity_pic_url"));
                        activeList.add(bean);
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
        activeListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        loadingMore.setVisibility(View.GONE);
        activeListAdapter.notifyDataSetChanged();
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
        page = 0;
        activeList.clear();
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
