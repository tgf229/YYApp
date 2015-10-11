/*
 * 文 件 名:  UserCard.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-9
 
 */
package com.yy.yyapp.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.bean.user.UserBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.network.NetResponse;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-9]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserScoreActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back;
    
    private TextView title,score;
    
    private NetLoadingDailog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_score);
        init();
        reqScore();
    }
    
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        score = (TextView)findViewById(R.id.score);
        
        title.setText("我的积分");
        back.setOnClickListener(this);
    }
    
    private void reqScore()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", Global.getUserId());
        ConnectService.instance().connectServiceReturnResponse(this, param, this,
            URLUtil.MY_SCORE,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (dialog != null)
        {
            dialog.dismissDialog();
        }
        if (URLUtil.MY_SCORE.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    if(GeneralUtils.isNullOrZeroLenght(bean.getString("user_score")))
                    {
                        Global.saveScore("");
                        score.setText("0");
                    }
                    else
                    {
                        Global.saveScore(bean.getString("user_score"));
                        score.setText(bean.getString("user_score"));
                    }
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
