package com.yy.yyapp.ui.goods.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.goods.GoodsFragment;
import com.yy.yyapp.ui.shop.ShopFragment;

public class TypeListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<String> mList;
    
    private UICallBack callBack;
    
    public TypeListAdapter(Context context, List<String> freshNewsList, UICallBack callBack)
    {
        this.context = context;
        this.mList = freshNewsList;
        this.callBack = callBack;
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final String entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.type_listview_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        mHolder.name.setText(entity);
        convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if("com.yy.yyapp.ui.shop.ShopTypeActivity".equals(YYApplication.currentActivity))
                {
                    Intent intent = new Intent(context, ShopFragment.class);
                    intent.putExtra("type", entity);
                    ((Activity)context).setResult(Constants.TYPE_SUCCESS_CODE, intent);
                    ((Activity)context).finish();
                }
                else
                {
                    Intent intent = new Intent(context, GoodsFragment.class);
                    intent.putExtra("type", entity);
                    ((Activity)context).setResult(Constants.TYPE_SUCCESS_CODE, intent);
                    ((Activity)context).finish();
                }
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        TextView name;
    }
}
