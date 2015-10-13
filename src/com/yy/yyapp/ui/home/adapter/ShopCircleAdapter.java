package com.yy.yyapp.ui.home.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.shop.CircleBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.HomeFragmentActivity;
import com.yy.yyapp.ui.goods.GoodsFragment;
import com.yy.yyapp.ui.home.MainFragment;
import com.yy.yyapp.ui.home.MapActivity;
import com.yy.yyapp.ui.shop.ShopFragment;

public class ShopCircleAdapter extends BaseAdapter
{
    private Context context;
    
    private List<CircleBean> mList;
    
    private UICallBack callBack;
    
    public ShopCircleAdapter(Context context, List<CircleBean> freshNewsList, UICallBack callBack)
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
        final String entity = mList.get(position).getComm_title();
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.circle_listview_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.location = (ImageView)convertView.findViewById(R.id.location);
            
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
                Intent intent = new Intent();
                intent.putExtra("circle", mList.get(position).getComm_id());
                ((Activity)context).setResult(Constants.CIRCLE_SUCCESS_CODE, intent);
                ((Activity)context).finish();
            }
        });
        mHolder.location.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("org_comm", mList.get(position).getComm_id());
                ((Activity)context).startActivity(intent);
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        TextView name;
        ImageView location;
    }
}
