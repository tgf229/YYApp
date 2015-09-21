package com.yy.yyapp.ui.home.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.home.Guss;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.util.GeneralUtils;

public class HomeListViewAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Guss> mList;
    
    private UICallBack callBack;
    
    public HomeListViewAdapter(Context context, List<Guss> mList, UICallBack callBack)
    {
        this.context = context;
        this.mList = mList;
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
        final Guss entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_hotgoods_listview_item, null);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.distance = (TextView)convertView.findViewById(R.id.distance);
            mHolder.content = (TextView)convertView.findViewById(R.id.content);
            mHolder.priceTag = (TextView)convertView.findViewById(R.id.priceTag);
            mHolder.price = (TextView)convertView.findViewById(R.id.price);
            mHolder.sold = (TextView)convertView.findViewById(R.id.sold);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(entity.getImg(),
            mHolder.img,
            YYApplication.setAllDisplayImageOptions(context,
                "default_pic",
                "default_pic",
                "default_pic"));
        mHolder.img.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
//                Intent personIntent = new Intent(context, CommunityPersonDetailActivity.class);
//                personIntent.putExtra("queryUId", entity.getuId());
//                context.startActivity(personIntent);
            }
        });
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getName()))
        {
            mHolder.name.setText(entity.getName());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getDistance()))
        {
            mHolder.distance.setText(entity.getDistance());
        }
        //mHolder.content.setMovementMethod(null);
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getContent()))
        {
            mHolder.content.setText(entity.getContent());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getPriceTag()))
        {
            mHolder.priceTag.setText(entity.getPriceTag());
        }
        mHolder.price.setText(entity.getPrice());
        mHolder.sold.setText(entity.getSold());
        
        convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
//                Intent topicIntent = new Intent(context, CommunityTopicDetailsActivity.class);
//                topicIntent.putExtra("id", entity.getArticleId());
//                context.startActivity(topicIntent);
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        TextView name;
        TextView distance;
        TextView content;
        TextView priceTag;
        TextView price;
        TextView sold;
    }
}
