/*
 * 文 件 名:  ProductTypeActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-28
 
 */
package com.yy.yyapp.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.user.CollectBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.user.adapter.UserCollectAdapter;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CollectMoreActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title;
    
    private List<CollectBean> typeList = new ArrayList<CollectBean>();
    
    private ListView typeListView;
    
    private UserCollectAdapter typeListAdapter;
    private DelCollectBroard delCollectBroard;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_type);
        typeList = (List<CollectBean>)getIntent().getSerializableExtra("list");
        init();
        registreBroadcast();
    }
    
    private void registreBroadcast()
    {
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(Constants.DEL_COLLECT_BROADCAST);
        delCollectBroard = new DelCollectBroard();
        registerReceiver(delCollectBroard, loginFilter);
    }
    
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("收藏列表");
        
        typeListView = (ListView)findViewById(R.id.goods_type_listview);
        typeListAdapter = new UserCollectAdapter(this, typeList, this);
        typeListView.setAdapter(typeListAdapter);
        
        typeListView.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
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
    
    class DelCollectBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String id = (String)intent.getExtras().get("id");
            if (Constants.DEL_COLLECT_BROADCAST.equals(intent.getAction()))
            {
                for(int i=0 ; i<typeList.size(); i++)
                {
                    if(id.equals(typeList.get(i).getCollect_item_id()))
                    {
                        typeList.remove(i);
                        break;
                    }
                }
                typeListAdapter.notifyDataSetChanged();
            }
        }
    }
}
