package com.yy.yyapp.ui.home.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.home.HotGoods;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.util.GeneralUtils;

public class FreshNewsAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Map<String, List<HotGoods>>> mList;
    
    private UICallBack callBack;
    
    public FreshNewsAdapter(Context context, List<Map<String, List<HotGoods>>> freshNewsList, UICallBack callBack)
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
        final Map<String, List<HotGoods>> entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_listview_item, null);
            mHolder.title = (TextView)convertView.findViewById(R.id.tag_title);
            mHolder.content = (LinearLayout)convertView.findViewById(R.id.tag_content);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        String title = handleTitleName(entity.keySet().toString());
        mHolder.title.setText(title);
        
        @SuppressWarnings("unchecked")
        List<HotGoods> list = (List<HotGoods>)entity.get(title);
        
        if (GeneralUtils.isNotNullOrZeroSize(list))
        {
            mHolder.content.removeAllViews();
            
            for (HotGoods goods : list)
            {
                View item = (View)LayoutInflater.from(context).inflate(R.layout.home_hotgoods_listview_item, null);
                
                ItemView itemViewHolder = new ItemView();
                itemViewHolder.name = (TextView)item.findViewById(R.id.name);
                itemViewHolder.img = (ImageView)item.findViewById(R.id.img);
                itemViewHolder.content = (TextView)item.findViewById(R.id.content);
                itemViewHolder.price = (TextView)item.findViewById(R.id.price);
                itemViewHolder.priceTag = (TextView)item.findViewById(R.id.priceTag);
                itemViewHolder.distance = (TextView)item.findViewById(R.id.distance);
                itemViewHolder.sold = (TextView)item.findViewById(R.id.sold);
                itemViewHolder.priceTag.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                
                itemViewHolder.name.setText(goods.getName());
                itemViewHolder.content.setText(goods.getContent());
                itemViewHolder.priceTag.setText(goods.getPriceTag());
                itemViewHolder.price.setText(goods.getPrice());
                itemViewHolder.distance.setText(goods.getDistance());
                itemViewHolder.sold.setText(goods.getSold());
                ImageLoader.getInstance().displayImage(goods.getImg(),
                    itemViewHolder.img,
                    YYApplication.setAllDisplayImageOptions(context, "default_pic", "default_pic", "default_pic"));
                mHolder.content.addView(item);
            }
        }
        
        return convertView;
    }
    
    class HolderView
    {
        TextView title;
        
        LinearLayout content;
    }
    
    class ItemView
    {
        TextView name;
        
        ImageView img;
        
        TextView content;
        
        TextView priceTag; //吊牌价
        
        TextView price; //现价
        
        TextView distance;
        
        TextView sold;
    }
    
    private String handleTitleName(String title)
    {
        title = title.replaceAll("\\[", "");
        title = title.replaceAll("\\]", "");
        return title;
    }
}
