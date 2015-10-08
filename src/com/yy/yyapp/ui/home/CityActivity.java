/*
 * 文 件 名:  CityActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-7
 
 */
package com.yy.yyapp.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.home.adapter.CityAdapter;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-7]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CityActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout loadingLayout;
    /**
     * 城市名
     */
    private TextView currentCity;
    
    private ArrayList<String> mList;
    
    private CityAdapter adapter;
    
    private ListView mListView;
    
    private String city;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_listview);
        
        city = getIntent().getStringExtra("city");
        init();
        reqList();
    }
    
    private void init()
    {
        LinearLayout back = (LinearLayout)findViewById(R.id.title_back_layout);
        back.setOnClickListener(this);
        loadingLayout = (LinearLayout)findViewById(R.id.loading);
        loadingLayout.setVisibility(View.VISIBLE);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("选择城市");
        
        mListView = (ListView)findViewById(R.id.list_view);
        View headView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.city_listview_head, null);
        RelativeLayout currentLay = (RelativeLayout)headView.findViewById(R.id.current_layout);
        currentCity = (TextView)headView.findViewById(R.id.current_c_city);
        currentCity.setText(city);
        mListView.addHeaderView(headView);
        currentLay.setOnClickListener(this);
        mList = new ArrayList<String>();
        adapter = new CityAdapter(this,mList);
        mListView.setAdapter(adapter);
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
            this,
            URLUtil.CITY_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.CITY_LIST.equals(service))
        {
            loadingLayout.setVisibility(View.GONE);
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
                    if(GeneralUtils.isNotNullOrZeroLenght(ob.getString("city_name")))
                    {
                        if(!city.equals(ob.getString("city_name")))
                        {
                            mList.add(ob.getString("city_name"));
                        }
                    }
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
        mListView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
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
