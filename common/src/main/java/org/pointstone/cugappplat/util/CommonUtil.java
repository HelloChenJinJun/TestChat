package org.pointstone.cugappplat.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

import java.security.MessageDigest;
import java.util.List;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/16      14:09
 * QQ:             1981367757
 */

public class CommonUtil {
        public static String encode(String str) {
                return str+"cug";
        }

        public static boolean isSupportSdcard() {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return false;
                } else {
                        return true;
                }
        }

        public static String md5(String currentUserObjectId) {
                MessageDigest md5 = null;
                try {
                        md5 = MessageDigest.getInstance("MD5");
                } catch (Exception e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                        return "";
                }
                char[] charArray = currentUserObjectId.toCharArray();
                byte[] byteArray = new byte[charArray.length];

                for (int i = 0; i < charArray.length; i++)
                        byteArray[i] = (byte) charArray[i];
                byte[] md5Bytes = md5.digest(byteArray);
                StringBuffer hexValue = new StringBuffer();
                for (int i = 0; i < md5Bytes.length; i++) {
                        int val = ((int) md5Bytes[i]) & 0xff;
                        if (val < 16)
                                hexValue.append("0");
                        hexValue.append(Integer.toHexString(val));
                }
                return hexValue.toString();
        }

        public static boolean isAppOnForeground(Context context) {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo appProcessInfo :
                        appProcessInfos
                        ) {
                        if (appProcessInfo.processName.equals(context.getPackageName())) {
                                LogUtil.e("当前的APP存活");
                                return true;
                        }
                }
                return false;
        }
}
