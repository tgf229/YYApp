package com.yy.yyapp.ui.user.adapter;

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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.user.CollectBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.ui.active.ActiveDetailActivity;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.ui.shop.ShopDetailActivity;

public class UserCollectAdapter extends BaseAdapter
{
    private Context context;
    
    private List<CollectBean> mList;
    
    private UICallBack callBack;
    
    public UserCollectAdapter(Context context, List<CollectBean> freshNewsList, UICallBack callBack)
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
        final CollectBean entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.collect_listview_item, null);
            mHolder.pic = (ImageView)convertView.findViewById(R.id.pic);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(entity.getCollect_pic(),
            mHolder.pic,
            YYApplication.setAllDisplayImageOptions(context, "default_pic", "default_pic", "default_pic"));
        
        convertView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if("商家".equals(entity.getCollect_type()))
                {
                    Intent intent = new Intent(context, ShopDetailActivity.class);
                    intent.putExtra("id", entity.getCollect_item_id());
                    ((Activity)context).startActivity(intent);
                }
                else if("商品".equals(entity.getCollect_type()))
                {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("id", entity.getCollect_item_id());
                    ((Activity)context).startActivity(intent);
                }
                else if("活动".equals(entity.getCollect_type()))
                {
                    Intent intent = new Intent(context, ActiveDetailActivity.class);
                    intent.putExtra("id", entity.getCollect_item_id());
                    ((Activity)context).startActivity(intent);
                }
                else if("现金券".equals(entity.getCollect_type()))
                {
                    Intent intent = new Intent(context, CouponDetailActivity.class);
                    intent.putExtra("id", entity.getCollect_item_id());
                    ((Activity)context).startActivity(intent);
                }
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        ImageView pic;
    }
}
