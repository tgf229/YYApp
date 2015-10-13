package com.yy.yyapp.ui.home.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.home.MainFragment;
import com.yy.yyapp.util.GeneralUtils;

/**
 * 
 * <选择>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015年3月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CityAdapter extends BaseAdapter
{
    private Context context;
    
    private ArrayList<String> mList;
    
    public CityAdapter(Context context, ArrayList<String> mList)
    {
        this.context = context;
        this.mList = mList;
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final String entity = mList.get(position);
        ViewHolder mHolder;
        if (convertView == null)
        {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.city_listview_item, null);
            mHolder.layout = (RelativeLayout)convertView.findViewById(R.id.select_community_layout);
            mHolder.city = (TextView)convertView.findViewById(R.id.city);
            mHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder)convertView.getTag();
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity))
        {
            mHolder.city.setText(entity);
        }
        else
        {
            mHolder.city.setText("");
        }
        mHolder.layout.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                
                Intent i = new Intent(context, MainFragment.class);
                i.putExtra("city", entity);//小区所属的城市
                Constants.cityTxt = entity;
                ((Activity)context).setResult(Constants.CITY_SUCCESS_CODE, i);
                ((Activity)context).finish();
            }
        });
        return convertView;
    }
    
    class ViewHolder
    {
        RelativeLayout layout;
        
        TextView city;
        
        View line;
    }
}
