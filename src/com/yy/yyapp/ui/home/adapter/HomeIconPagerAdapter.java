package com.yy.yyapp.ui.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.IconPagerAdapter;
import com.yy.yyapp.R;
import com.yy.yyapp.bean.home.HomeBannerBean;
import com.yy.yyapp.bean.home.HomeIconBean;
import com.yy.yyapp.bean.home.HomeIconPageBean;

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
        
        Button icon1 = (Button)pageView.findViewById(R.id.icon1);
        Button icon2 = (Button)pageView.findViewById(R.id.icon2);
        Button icon3 = (Button)pageView.findViewById(R.id.icon3);
        Button icon4 = (Button)pageView.findViewById(R.id.icon4);
        Button icon5 = (Button)pageView.findViewById(R.id.icon5);
        Button icon6 = (Button)pageView.findViewById(R.id.icon6);
        Button icon7 = (Button)pageView.findViewById(R.id.icon7);
        Button icon8 = (Button)pageView.findViewById(R.id.icon8);
        Button[] btns = new Button[]{icon1,icon2,icon3,icon4,icon5,icon6,icon7,icon8};
        
        List<HomeIconBean> list = mPaths.get(position).getList();
        for(int i=0; i<list.size(); i++)
        {
            Drawable drawable=mContext.getResources().getDrawable(R.drawable.icon1); 
            btns[i].setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable,null,null);
            btns[i].setText(list.get(i).getName());
            btns[i].setVisibility(View.VISIBLE);
        }
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
