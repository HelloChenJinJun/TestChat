package chen.testchat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.CustomApplication;
import chen.testchat.base.Constant;
import chen.testchat.listener.OnNetWorkChangedListener;
import chen.testchat.util.LogUtil;
import chen.testchat.util.TimeUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/12      20:40
 * QQ:             1981367757
 */

public class NetWorkChangedReceiver extends BroadcastReceiver {


        private List<OnNetWorkChangedListener> mOnNetWorkChangedListeners = new ArrayList<>();

        public void registerListener(OnNetWorkChangedListener listener) {
                if (!mOnNetWorkChangedListeners.contains(listener)) {
                        mOnNetWorkChangedListeners.add(listener);
                }
        }

        public void unregisterListener(OnNetWorkChangedListener listener) {
                if (mOnNetWorkChangedListeners.contains(listener)) {
                        mOnNetWorkChangedListeners.remove(listener);
                }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.NETWORK_CONNECTION_CHANGE)) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null) {
                                if (networkInfo.isConnected() && CustomApplication.getInstance().getSharedPreferencesUtil().getDeltaTime() == -1) {
                                        TimeUtil.getServerTime();
                                }
                                if (mOnNetWorkChangedListeners != null && mOnNetWorkChangedListeners.size() > 0) {
                                        for (OnNetWorkChangedListener listener :
                                                mOnNetWorkChangedListeners) {
                                                listener.OnNetWorkChanged(networkInfo.isConnected(), networkInfo.getType());
                                        }
                                }
                        } else {
                                LogUtil.e("当前没有网络连接");
                                if (mOnNetWorkChangedListeners != null && mOnNetWorkChangedListeners.size() > 0) {
                                        for (OnNetWorkChangedListener listener :
                                                mOnNetWorkChangedListeners) {
                                                listener.OnNetWorkChanged(false, 0);
                                        }
                                }

                        }
                }

        }
}
