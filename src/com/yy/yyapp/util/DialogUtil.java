package com.yy.yyapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yy.yyapp.R;
import com.yy.yyapp.callback.DialogCallBack;
import com.yy.yyapp.constant.Constants;
import com.yy.yyapp.global.Global;
import com.yy.yyapp.ui.user.LoginActivity;

/**
 * 
 * <弹出框公共类> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DialogUtil
{
    
    /**
     * 
     * <登录两个按钮的弹出框>
     * <功能详细描述>
     * @param context
     * @param title
     * @param content
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog loginTwoButtonDialog(Context context, final DialogCallBack callBack)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.two_button_dialog, null);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        Button cancel = (Button)layout.findViewById(R.id.dialog_cancel_bt);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                callBack.dialogBack();
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
 
}
