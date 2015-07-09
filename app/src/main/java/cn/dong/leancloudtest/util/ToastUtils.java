package cn.dong.leancloudtest.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import cn.dong.leancloudtest.App;

public class ToastUtils {
    private static Toast toast;

    public static void shortT(@StringRes int resId) {
        shortT(App.getInstance(), App.getInstance().getResources().getString(resId));
    }

    public static void shortT(String msg) {
        shortT(App.getInstance(), msg);
    }

    public static void shortT(Context context, int resId) {
        shortT(context, context.getResources().getString(resId));
    }

    public static void shortT(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void longT(Context context, int msgResId) {
        longT(context, context.getResources().getString(msgResId));
    }

    public static void longT(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

}