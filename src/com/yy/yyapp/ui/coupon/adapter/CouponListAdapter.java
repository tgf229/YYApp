package com.yy.yyapp.ui.coupon.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.yy.yyapp.bean.coupon.CouponBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.ui.coupon.CouponDetailActivity;
import com.yy.yyapp.ui.user.UserCouponActivity;
import com.yy.yyapp.util.GeneralUtils;

public class CouponListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<CouponBean> mList;
    
    private UICallBack callBack;
    
    public CouponListAdapter(Context context, List<CouponBean> freshNewsList, UICallBack callBack)
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
        final CouponBean entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.coupon_listview_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.brand = (TextView)convertView.findViewById(R.id.brand);
            mHolder.price = (TextView)convertView.findViewById(R.id.price);
            mHolder.number = (TextView)convertView.findViewById(R.id.number);
            mHolder.limit =  (TextView)convertView.findViewById(R.id.limit);
            
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        mHolder.name.setText(entity.getTicket_title());
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTicket_brand()))
        {
            mHolder.brand.setText("品牌："+entity.getTicket_brand());
        }
        else
        {
            mHolder.brand.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTicket_money()))
        {
            mHolder.price.setText("￥ "+entity.getTicket_money());
        }
        else
        { 
            mHolder.price.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTicket_limit()))
        {
            mHolder.limit.setText("已领"+entity.getTicket_limit()+"张");
        }
        else
        { 
            mHolder.limit.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTicket_number()))
        {
            mHolder.number.setText("总计"+entity.getTicket_number()+"张");
        }
        else
        { 
            mHolder.number.setText("");
        }
       
        
        ImageLoader.getInstance().displayImage(entity.getTicket_pic_url(),
            mHolder.img,
            YYApplication.setAllDisplayImageOptions(context, "default_pic", "default_pic", "default_pic"));
        convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, CouponDetailActivity.class);
                intent.putExtra("id", entity.getTicket_id());
                if("com.yy.yyapp.ui.user.UserCouponActivity".equals(YYApplication.currentActivity))
                {
                    intent.putExtra("channel", "1");
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        TextView name;
        TextView brand;
        TextView price;
        TextView number,limit;
    }
}
