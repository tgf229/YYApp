package com.yy.yyapp.ui.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.shop.ShopBean;
import com.yy.yyapp.callback.UICallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.home.MainFragment;
import com.yy.yyapp.ui.shop.ShopDetailActivity;
import com.yy.yyapp.ui.user.RegisterOneActivity;
import com.yy.yyapp.util.GeneralUtils;

public class RegisterShopAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ShopBean> mList;
    
    private UICallBack callBack;
    
    public RegisterShopAdapter(Context context, List<ShopBean> shopList, UICallBack callBack)
    {
        this.context = context;
        this.mList = shopList;
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
        final ShopBean entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.register_shop_listview_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.address = (TextView)convertView.findViewById(R.id.address);
            mHolder.tel = (TextView)convertView.findViewById(R.id.tel);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        mHolder.name.setText(entity.getOrg_name());
        mHolder.address.setText(entity.getOrg_addr());
        ImageLoader.getInstance().displayImage(entity.getOrg_pic_url(),
            mHolder.img,
            YYApplication.setAllDisplayImageOptions(context, "default_pic", "default_pic", "default_pic"));
        if (GeneralUtils.isNullOrZeroLenght(entity.getOrg_tel()))
        {
            mHolder.tel.setText("");
        }
        else
        {
            mHolder.tel.setText(entity.getOrg_tel());
        }
        convertView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, RegisterOneActivity.class);
                intent.putExtra("id", entity.getOrg_id());
                intent.putExtra("name", entity.getOrg_name());
                ((Activity)context).setResult(Constants.REGISTER_BIND_CODE, intent);
                ((Activity)context).finish();
            }
        });
        
        return convertView;
    }
    
    class HolderView
    {
        TextView name;
        
        ImageView img;
        
        TextView address, tel;
    }
}
