package com.yy.yyapp.ui.goods.adapter;

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
import com.yy.yyapp.bean.goods.GoodsBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.ui.goods.ProductDetailActivity;
import com.yy.yyapp.util.GeneralUtils;

public class GoodsListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<GoodsBean> mList;
    
    private UICallBack callBack;
    
    public GoodsListAdapter(Context context, List<GoodsBean> freshNewsList, UICallBack callBack)
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
        final GoodsBean entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_listview_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.price = (TextView)convertView.findViewById(R.id.price);
            mHolder.priceTag = (TextView)convertView.findViewById(R.id.priceTag);
            mHolder.priceTag.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.content = (TextView)convertView.findViewById(R.id.content);
            mHolder.viewCount = (TextView)convertView.findViewById(R.id.viewCount);
            
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        mHolder.name.setText(entity.getProduct_name());
        //若活动价 为空 为null 或与市场价相等 则不展示活动价 只展示市场价
        String activityPrice = entity.getActivity_price();
        if(GeneralUtils.isNullOrZeroLenght(activityPrice) || "null".equals(activityPrice) || activityPrice.equals(entity.getProduct_price()))
        {
            mHolder.price.setText("￥ "+entity.getProduct_price());
            mHolder.priceTag.setText("");
        }
        else
        {
            mHolder.price.setText("￥ "+entity.getActivity_price());
            mHolder.priceTag.setText("￥ "+entity.getProduct_price());
        }
        mHolder.content.setText(entity.getProduct_content());
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getView_count()))
        {
            mHolder.viewCount.setText("已被浏览："+entity.getView_count()+" 次");
        }
        else
        {
            mHolder.viewCount.setText("");
        }
        
        ImageLoader.getInstance().displayImage(entity.getProduct_pic_url(),
            mHolder.img,
            YYApplication.setAllDisplayImageOptions(context, "default_pic", "default_pic", "default_pic"));
        
        mHolder.content.setMovementMethod(null);
        convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("id", entity.getProduct_id());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        TextView name;
        
        ImageView img;
        
        TextView priceTag; //吊牌价
        
        TextView price; //现价
        
        TextView content;
        
        TextView viewCount;
    }
}
