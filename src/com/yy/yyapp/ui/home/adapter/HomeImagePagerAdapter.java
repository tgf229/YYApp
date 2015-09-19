package com.yy.yyapp.ui.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.viewpagerindicator.IconPagerAdapter;
import com.yy.yyapp.R;
import com.yy.yyapp.bean.home.LoopHomeBean;
import com.yy.yyapp.util.ToastUtil;

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
public class HomeImagePagerAdapter extends PagerAdapter implements IconPagerAdapter
{
    
    private Context mContext;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    DisplayImageOptions options;
    
    /**
     * 图片相对路径
     */
    private ArrayList<LoopHomeBean> mPaths;
    
    public HomeImagePagerAdapter(Context context, ArrayList<LoopHomeBean> paths)
    {
        mContext = context;
        mPaths = paths;
        setDisplayImageOptions();
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        View pageView = LayoutInflater.from(mContext).inflate(R.layout.home_loop_advert_viewpager_item, null);
        
        final ImageView adImageView = (ImageView)pageView.findViewById(R.id.iv_banner);
        imageLoader.displayImage(mPaths.get(position).getImageUrl(), adImageView, options);
        pageView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (null != mPaths && null != mPaths.get(position))
                {
                    
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, AdvertiseDetailActivity.class);
//                    intent.putExtra("id", mPaths.get(position).getId());
//                    mContext.startActivity(intent);
                }
                else
                {
//                    ToastUtil.makeText(mContext,
//                        mContext.getResources().getString(R.string.home_shop_event_none));
                }
            }
        });
        
        container.addView(pageView, 0);
        
        return pageView;
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
        
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.start_logo) // 设置图片下载期间显示的图片  
            .showImageForEmptyUri(R.drawable.start_logo)
            // 设置图片Uri为空或是错误的时候显示的图片  
            .showImageOnFail(R.drawable.start_logo)
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
        if(count > 0)//null != mPaths && mPaths.size() == 0)
        {
            count--;
            return  POSITION_NONE;
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
