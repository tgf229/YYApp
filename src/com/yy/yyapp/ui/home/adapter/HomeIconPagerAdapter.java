package com.yy.yyapp.ui.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.IconPagerAdapter;
import com.yy.yyapp.R;
import com.yy.yyapp.YYApplication;
import com.yy.yyapp.bean.home.HomeIconBean;
import com.yy.yyapp.bean.home.HomeIconPageBean;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.ui.coupon.CouponActivity;
import com.yy.yyapp.ui.home.LocationActivity;
import com.yy.yyapp.ui.home.ShopCircleActivity;

@SuppressLint("NewApi")
public class HomeIconPagerAdapter extends PagerAdapter implements IconPagerAdapter
{
    
    private Context mContext;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    DisplayImageOptions options;
    
    /**
     * 图片相对路径
     */
    private ArrayList<HomeIconPageBean> mPaths;
    
    public HomeIconPagerAdapter(Context context, ArrayList<HomeIconPageBean> paths)
    {
        mContext = context;
        mPaths = paths;
        setDisplayImageOptions();
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        View pageView = LayoutInflater.from(mContext).inflate(R.layout.home_loop_icon_viewpager_item, null);
        
        ImageView icon1 = (ImageView)pageView.findViewById(R.id.icon1);
        ImageView icon2 = (ImageView)pageView.findViewById(R.id.icon2);
        ImageView icon3 = (ImageView)pageView.findViewById(R.id.icon3);
        ImageView icon4 = (ImageView)pageView.findViewById(R.id.icon4);
        ImageView icon5 = (ImageView)pageView.findViewById(R.id.icon5);
        ImageView icon6 = (ImageView)pageView.findViewById(R.id.icon6);
        ImageView icon7 = (ImageView)pageView.findViewById(R.id.icon7);
        ImageView icon8 = (ImageView)pageView.findViewById(R.id.icon8);
        TextView icon1_text = (TextView)pageView.findViewById(R.id.icon1_text);
        TextView icon2_text = (TextView)pageView.findViewById(R.id.icon2_text);
        TextView icon3_text = (TextView)pageView.findViewById(R.id.icon3_text);
        TextView icon4_text = (TextView)pageView.findViewById(R.id.icon4_text);
        TextView icon5_text = (TextView)pageView.findViewById(R.id.icon5_text);
        TextView icon6_text = (TextView)pageView.findViewById(R.id.icon6_text);
        TextView icon7_text = (TextView)pageView.findViewById(R.id.icon7_text);
        TextView icon8_text = (TextView)pageView.findViewById(R.id.icon8_text);
        ImageView[] btns = new ImageView[] {icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8};
        TextView[] btns_text = new TextView[] {icon1_text, icon2_text, icon3_text, icon4_text, icon5_text, icon6_text, icon7_text, icon8_text};
        
        List<HomeIconBean> list = mPaths.get(position).getList();
        for (int i = 0; i < list.size(); i++)
        {
            ImageLoader.getInstance().displayImage(list.get(i).getPic_name(),
                btns[i],
                YYApplication.setAllDisplayImageOptions(mContext, "default_icon", "default_icon", "default_icon"));
            btns_text[i].setText(list.get(i).getPic_title());
            bindClick(list.get(i).getPic_title(),btns[i]);
            btns[i].setVisibility(View.VISIBLE);
            btns_text[i].setVisibility(View.VISIBLE);
        }
        container.addView(pageView, 0);
        return pageView;
    }
    
    private void bindClick(String title, ImageView btn)
    {
        if(Constants.ICON_COUPON.equals(title))
        {
            btn.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Intent intent = new Intent(mContext,CouponActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
        else if(Constants.ICON_LOCATION.equals(title))
        {
            btn.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Intent intent = new Intent(mContext,LocationActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
        else if(Constants.ICON_BUSINESS.equals(title))
        {
            btn.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Intent intent = new Intent(mContext,ShopCircleActivity.class);
                    ((Activity)mContext).startActivityForResult(intent, Constants.CIRCLE_SUCCESS_CODE);
                }
            });
        }
    }
    
    @Override
    public int getCount()
    {
        return null != mPaths ? mPaths.size() : 0;
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == (View)object;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager)container).removeView((View)object);
    }
    
    @Override
    public int getIconResId(int index)
    {
        return index;
    }
    
    /**
     * banner显示配置
     */
    void setDisplayImageOptions()
    {
        
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_banner) // 设置图片下载期间显示的图片  
            .showImageForEmptyUri(R.drawable.default_banner)
            // 设置图片Uri为空或是错误的时候显示的图片  
            .showImageOnFail(R.drawable.default_banner)
            // 设置图片加载或解码过程中发生错误显示的图片      
            .cacheInMemory(true)
            // 设置下载的图片是否缓存在内存中  
            .cacheOnDisc(true)
            // 设置下载的图片是否缓存在SD卡中
            //      .displayer(new SimpleBitmapDisplayer())
            .build();
    }
    
    /**
     * 解决list数据清空后，视图不销毁的bug
     */
    @Override
    public int getItemPosition(Object object)
    {
        if (count > 0)//null != mPaths && mPaths.size() == 0)
        {
            count--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
    
    private int count;
    
    @Override
    public void notifyDataSetChanged()
    {
        count = getCount();
        super.notifyDataSetChanged();
    }
}
