/*
 * 文 件 名:  ProductTypeActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-9-28
 
 */
package com.yy.yyapp.ui.home;

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
import com.yy.yyapp.bean.shop.CircleBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.ui.goods.adapter.TypeListAdapter;
import com.yy.yyapp.ui.home.adapter.ShopCircleAdapter;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-9-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ShopCircleActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title;
    
    private List<CircleBean> typeList;
    
    private ListView typeListView;
    
    private ShopCircleAdapter typeListAdapter;
    
    private NetLoadingDailog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_type);
        init();
        reqList();
    }
    
    private void init()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("商圈");
        
        typeList = new ArrayList<CircleBean>();
        typeListView = (ListView)findViewById(R.id.goods_type_listview);
        typeListAdapter = new ShopCircleAdapter(this, typeList, this);
        typeListView.setAdapter(typeListAdapter);
        
        back.setOnClickListener(this);
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("user_id", Global.getUserId());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(Constants.cityTxt))
        {
            param.put("city_name", Constants.cityTxt);
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            URLUtil.CIRCLE_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (URLUtil.CIRCLE_LIST.equals(service))
        {
            if (dialog != null)
            {
                dialog.dismissDialog();
            }
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
                    
                    CircleBean bean = new CircleBean();
                    bean.setComm_id(ob.getString("comm_id"));
                    bean.setComm_title(ob.getString("comm_title"));
                    bean.setComm_pic_url(ob.getString("comm_pic_url"));
                    typeList.add(bean);
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
        typeListView.setVisibility(View.VISIBLE);
        typeListAdapter.notifyDataSetChanged();
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
