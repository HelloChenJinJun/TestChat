package org.pointstone.cugappplat.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.pointstone.cugappplat.base.BaseApplication;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      20:59
 * QQ:             1981367757
 */

public class NetUtil {
        public static boolean isNetWorkAvailable() {
                ConnectivityManager manager = (ConnectivityManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo == null) {
                        return false;
                }
                return networkInfo.isAvailable();
        }
}
