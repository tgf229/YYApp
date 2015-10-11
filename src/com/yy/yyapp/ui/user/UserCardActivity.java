/*
 * 文 件 名:  UserCard.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-10-9
 
 */
package com.yy.yyapp.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yy.yyapp.R;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.constant.URLUtil;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.network.ConnectService;
import com.yy.yyapp.ui.base.BaseActivity;
import com.yy.yyapp.util.GeneralUtils;
import com.yy.yyapp.util.NetLoadingDailog;
import com.yy.yyapp.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-10-9]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UserCardActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout back,card_bg;
    
    private TextView title,card_no;
    
    private NetLoadingDailog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_card2);
        init();
        reqCard();
    }
    
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setText("我的会员卡");
        
        card_bg = (LinearLayout)findViewById(R.id.card_bg);
        card_no = (TextView)findViewById(R.id.card_no);
        back.setOnClickListener(this);
    }
    
    @SuppressLint("NewApi")
    private void initData(String level)
    {
        try
        {
            if("金卡会员".equals(level))
            {
                card_bg.setBackground(getResources().getDrawable(R.drawable.card_gold));
            }
            else
            {
                card_bg.setBackground(getResources().getDrawable(R.drawable.card_silver));
            }
            Bitmap qrcode = CreateOneDCode(Global.getUserId());
            card_no.setText("No："+Global.getUserId());
            
            ImageView imageView = (ImageView)findViewById(R.id.code_image);
            imageView.setImageBitmap(qrcode);
            
            Bitmap qrcode2 = CreateTwoDCode(Global.getUserId());
            ImageView imageView2 = (ImageView)findViewById(R.id.qr_image);
            imageView2.setImageBitmap(qrcode2);
        }
        catch (Exception e)
        {
        }
    }
    
    private void reqCard()
    {
        dialog = new NetLoadingDailog(this);
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", Global.getUserId());
        ConnectService.instance().connectServiceReturnResponse(this, param, this,
            URLUtil.MY_CARD,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(String service, String res)
    {
        super.netBack(service, res);
        if (dialog != null)
        {
            dialog.dismissDialog();
        }
        if (URLUtil.MY_CARD.equals(service))
        {
            JSONArray array;
            try
            {
                array = new JSONArray(res);
                JSONObject bean = array.getJSONObject(0);
                if (Constants.SUCESS_CODE.equals(bean.getString("result")))
                {
                    initData(bean.getString("user_level"));
                }
                else
                {
                    ToastUtil.showError(this);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ToastUtil.showError(this);
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            default:
                break;
        }
    }
    
    public Bitmap CreateTwoDCode(String content)
        throws WriterException
    {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败  
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 200, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了  
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (matrix.get(x, y))
                {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api  
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    
    /** 
     * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容 
     * 
     * @param content 将要生成一维码的内容 
     * @return 返回生成好的一维码bitmap 
     * @throws WriterException WriterException异常 
     */
    public Bitmap CreateOneDCode(String content)
        throws WriterException
    {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败  
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 260, 90);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (matrix.get(x, y))
                {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api  
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    
}
