package org.pointstone.cugappplat.net;

import org.pointstone.cugappplat.util.NetUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      23:01
 * QQ:             1981367757
 */

public class CacheInterceptor implements Interceptor {
        private int netWorkCacheTime = 60;
        private int outOfNetCacheTime = 60 * 60 * 24 * 7;
        @Override
        public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                if (NetUtil.isNetWorkAvailable()) {
                        return response.newBuilder().removeHeader("program").removeHeader("Cache_Control")
                                .addHeader("Cache_Control", "public, max-age=" + netWorkCacheTime).build();
                } else {
                        return response.newBuilder().removeHeader("program").removeHeader("Cache_Control")
                                .addHeader("Cache_Control", "public, only-if-cached, max-stale=" + outOfNetCacheTime).build();
                }
        }
}
