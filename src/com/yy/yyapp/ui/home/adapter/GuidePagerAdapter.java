package com.yy.yyapp.ui.home.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jauker.widget.BadgeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.IconPagerAdapter;
import com.yy.yyapp.R;
import com.yy.yyapp.ui.HomeFragmentActivity;
import com.yy.yyapp.ui.WelcomeActivity;

/**
 * 
 * <滚动图片适配器> 
 * <功能详细描述>
 * 
 * @author  qiuqiaohua
 * @version  [版本号, Apr 30, 2014]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("NewApi")
public class GuidePagerAdapter extends PagerAdapter implements IconPagerAdapter
{
    
    private Context mContext;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    DisplayImageOptions options;
    
    private WelcomeActivity activity;
    
    /**
     * 图片相对路径
     */
    private List<Bitmap> mPaths;
    
    public GuidePagerAdapter(Context context, List<Bitmap> paths, WelcomeActivity activity)
    {
        mContext = context;
        mPaths = paths;
        setDisplayImageOptions();
        this.activity = activity;
    }
    
    /** {实例化条目}*/
    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        if (position == mPaths.size() - 1)
        {
            Intent intent = new Intent(mContext, HomeFragmentActivity.class);
            mContext.startActivity(intent);
            activity.finish();
            return null;
        }
        else
        {
            View pageView = LayoutInflater.from(mContext).inflate(R.layout.home_banner_item, null);
            ImageView adImageView = (ImageView)pageView.findViewById(R.id.iv_banner);
            
            BadgeView backgroundDrawableBadge = new BadgeView(mContext);
            backgroundDrawableBadge.setBadgeCount(position + 1);
            backgroundDrawableBadge.setBackgroundResource(R.drawable.skip);
            backgroundDrawableBadge.setTargetView(adImageView);
            
            BadgeView backgroundDrawableBadge1 = new BadgeView(mContext);
            backgroundDrawableBadge1.setBadgeCount(position+1);
            backgroundDrawableBadge1.setBackgroundResource(R.drawable.badge_blue);
            backgroundDrawableBadge1.setBadgeGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            backgroundDrawableBadge1.setTargetView(adImageView);
            
            adImageView.setBackground(new BitmapDrawable(mPaths.get(position)));
            if (position == mPaths.size() - 1)
            {
                adImageView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(mContext, HomeFragmentActivity.class);
                        mContext.startActivity(intent);
                        activity.finish();
                    }
                });
            }
            
            backgroundDrawableBadge.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mContext, HomeFragmentActivity.class);
                    mContext.startActivity(intent);
                    activity.finish();
                }
            });
            
            container.addView(pageView, 0);
            
            return pageView;
        }
        
    }
    
    /** {获取条目数量} */
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
    
    /** {销毁条目} */
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
        
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_pic) // 设置图片下载期间显示的图片  
            .showImageForEmptyUri(R.drawable.default_pic)
            // 设置图片Uri为空或是错误的时候显示的图片  
            .showImageOnFail(R.drawable.default_pic)
            // 设置图片加载或解码过程中发生错误显示的图片      
            .cacheInMemory(true)
            // 设置下载的图片是否缓存在内存中  
            .cacheOnDisc(true)
            // 设置下载的图片是否缓存在SD卡中
            //      .displayer(new SimpleBitmapDisplayer())
            .build();
        
        //        options =
        //            new DisplayImageOptions.Builder().cacheInMemory(true)
        //                .showImageForEmptyUri(R.drawable.default_load9)
        //                .showImageOnFail(R.drawable.default_load9) 
        //                .showImageOnLoading(R.drawable.default_load9)
        //                .cacheOnDisc(true)
        //                .displayer(new SimpleBitmapDisplayer())
        //                .build();
    }
    
    /**
     * 解决list数据清空后，视图不销毁的bug
     */
    @Override
    public int getItemPosition(Object object)
    {
        return null != mPaths && mPaths.size() == 0 ? POSITION_NONE : super.getItemPosition(object);
    }
}
