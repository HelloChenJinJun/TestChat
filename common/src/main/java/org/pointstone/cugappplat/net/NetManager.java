package org.pointstone.cugappplat.net;

import com.orhanobut.logger.Logger;

import org.pointstone.cugappplat.base.BaseApplication;
import org.pointstone.cugappplat.util.LogUtil;
import org.pointstone.cugappplat.util.NetUtil;
import org.pointstone.cugappplat.weixin.CUGUserInfoApi;
import org.pointstone.cugappplat.weixin.WXUserInfoApi;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      19:24
 * QQ:             1981367757
 */

public class NetManager {


        private static final String CACHE_CONTROL = "Cache_Control";
        //        获取用户信息的网址
        private static final String BASE_USER_INFO_URL = "https://api.cugapp.com/public_api/CugApp/";
        private static final String BASE_WX_URL = "https://api.weixin.qq.com/";

        private static final String TAG = NetManager.class.getName();
        private static NetManager instance;
        private static Interceptor cacheInterceptor;
        private static OkHttpClient client;
        private int outOfNetCacheTime = 60 * 60 * 24 * 7;
        private static long maxCache = 1024 * 1024 * 10;
        private int netWorkCacheTime = 60;
        private static File cacheFileDir = new File(BaseApplication.getInstance().getCacheDir(), "tx_cache");
        private static Cache sCache = new Cache(cacheFileDir, maxCache);
        private static WXUserInfoApi sWXUserInfo;
        private static CUGUserInfoApi sCUGUserInfo;


        private static Interceptor sLoggerInterceptor;


        public static NetManager getInstance() {
                if (instance == null) {
                        instance = new NetManager();
                }
                return instance;
        }


        private NetManager() {
                LogUtil.e("NetManager");
                if (cacheInterceptor == null) {
                        cacheInterceptor = new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                        Response response = chain.proceed(chain.request());
                                        if (NetUtil.isNetWorkAvailable()) {
                                                return response.newBuilder().removeHeader("program").removeHeader(CACHE_CONTROL)
                                                        .addHeader(CACHE_CONTROL, "public, max-age=" + netWorkCacheTime).build();
                                        } else {
                                                return response.newBuilder().removeHeader("program").removeHeader(CACHE_CONTROL)
                                                        .addHeader(CACHE_CONTROL, "public, only-if-cached, max-stale=" + outOfNetCacheTime).build();
                                        }
                                }
                        };
                }
                if (sLoggerInterceptor == null) {
                        sLoggerInterceptor = new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                        Request request = chain.request();
                                        RequestBody requestBody = request.body();
                                        Buffer buffer = new Buffer();
                                        if (requestBody != null) {
                                                requestBody.writeTo(buffer);
                                                Logger.e(TAG, parseContent(requestBody, buffer));
                                        } else {
                                                Logger.e(TAG, "request_body is null");
                                        }
                                        return chain.proceed(request);
                                }
                        };
                }
                if (client == null) {
                        initClient();
                }
        }

        private String parseContent(RequestBody requestBody, Buffer buffer) {
                try {
                        if (requestBody.contentType() != null && requestBody.contentType().toString().equals("multipart")) {
                                return URLDecoder.decode(buffer.readUtf8(), "UTF-8");
                        }
                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                }
                return null;
        }

        private static final Object LOCK = new Object();


        public WXUserInfoApi getWXUserInfo() {
                if (sWXUserInfo == null) {
                        synchronized (LOCK) {
                                if (sWXUserInfo == null) {
                                        sWXUserInfo = new Retrofit.Builder().baseUrl(BASE_WX_URL)
                                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create()).client(client).build().create(WXUserInfoApi.class);
                                }
                        }
                }
                return sWXUserInfo;
        }

        public String getBaseUserInfoUrl() {
                return BASE_USER_INFO_URL;
        }


        public String getBaseWxUrl() {
                return BASE_WX_URL;
        }

        public OkHttpClient getClient() {
                if (client != null)
                        return client;
                initClient();
                return client;
        }

        private void initClient() {
                client = new OkHttpClient.Builder().addInterceptor(cacheInterceptor).addInterceptor(sLoggerInterceptor).addNetworkInterceptor(cacheInterceptor).cache(sCache).build();
        }

        public CUGUserInfoApi getCugUserInfo() {
                if (sCUGUserInfo == null) {
                        synchronized (LOCK) {
                                if (sCUGUserInfo == null) {
                                        sCUGUserInfo = new Retrofit.Builder().baseUrl(BASE_USER_INFO_URL)
                                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create()).client(client).build().create(CUGUserInfoApi.class);
                                }
                        }
                }
                return sCUGUserInfo;
        }


        public <T> T  getApi(String baseUrl, Class<T> api) {
                if (baseUrl == null) {
                        return null;
                }
                if (sClassRetrofitMap.get(api) == null) {
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(client).build();
                        sClassRetrofitMap.put(api, retrofit);
                }
                return sClassRetrofitMap.get(api).create(api);
        }


        public void removeApi(Class api) {
                if (sClassRetrofitMap.containsKey(api)) {
                        sClassRetrofitMap.remove(api);
                }
        }

        private static Map<Class, Retrofit> sClassRetrofitMap = new HashMap<>();


}
