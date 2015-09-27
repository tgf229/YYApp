package com.yy.yyapp.ui.active.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.active.ActiveBean;
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.util.GeneralUtils;

public class ActiveListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ActiveBean> mList;
    
    private UICallBack callBack;
    
    public ActiveListAdapter(Context context, List<ActiveBean> freshNewsList, UICallBack callBack)
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
        final ActiveBean entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.active_listview_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.address = (TextView)convertView.findViewById(R.id.address);
            
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        mHolder.name.setText(entity.getActivity_title());
        mHolder.time.setText(entity.getActivity_time());
        mHolder.address.setText(entity.getActivity_addr());
        ImageLoader.getInstance().displayImage(entity.getActivity_pic_url(),
            mHolder.img,
            YYApplication.setAllDisplayImageOptions(context, "default_pic", "default_pic", "default_pic"));
//        convertView.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(context, ProductDetailActivity.class);
//                intent.putExtra("id", entity.getProduct_id());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        TextView name;
        TextView time;
        TextView address;
    }
}
