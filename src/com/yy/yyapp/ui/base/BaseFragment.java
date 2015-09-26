package com.yy.yyapp.ui.base;

import android.support.v4.app.Fragment;

import com.yy.yyapp.callback.UICallBack;


/**
 * 
 * <基础Fragment>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseFragment extends Fragment implements UICallBack
{
    
    @Override
    public void netBack(String service,String res)
    {
        
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
