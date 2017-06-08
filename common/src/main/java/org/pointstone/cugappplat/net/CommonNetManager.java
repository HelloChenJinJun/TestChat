package org.pointstone.cugappplat.net;

import android.text.TextUtils;

import org.pointstone.cugappplat.util.FileUtil;
import org.pointstone.cugappplat.util.LogUtil;
import org.pointstone.cugappplat.util.NetUtil;
import org.pointstone.cugappplat.util.ParseDataUtil;
import org.pointstone.cugappplat.util.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      20:32
 * QQ:             1981367757
 */

public class CommonNetManager {

        private static final long CONNECTION_MAX_TIME = 10000L;
        private static CommonNetManager instance;
        private static OkHttpClient sOkHttpClient;
        private static Interceptor sCacheInterceptor;
        private static Interceptor sLoggerInterceptor;
        private static Cache sCache;
        private Builder builder;
        private Call<ResponseBody> mCall;


        private static CommonNetManager getInstance() {
                if (instance == null) {
                        synchronized (CommonNetManager.class) {
                                if (instance == null) {
                                        instance = new CommonNetManager();
                                }
                        }
                }
                return instance;
        }


        private CommonNetManager() {
                sOkHttpClient = new OkHttpClient.Builder().connectTimeout(CONNECTION_MAX_TIME, TimeUnit.MILLISECONDS)
                        .addInterceptor(getCacheInterceptor()).addInterceptor(getLoggerInterceptor()).addNetworkInterceptor(getCacheInterceptor()).cache(getCache()).build();
        }

        private Cache getCache() {
                if (sCache == null) {
                        sCache = new Cache(FileUtil.getCacheDirs(), 1024 * 1024 * 10);
                }
                return null;
        }

        private Interceptor getLoggerInterceptor() {
                if (sLoggerInterceptor == null) {
                        sLoggerInterceptor = new LoggerInterceptor();
                }
                return sLoggerInterceptor;
        }


        private Interceptor getCacheInterceptor() {
                if (sCacheInterceptor == null) {
                        sCacheInterceptor = new CacheInterceptor();
                }
                return sCacheInterceptor;
        }


        public static class Builder {
                private String url;
                /**
                 * 内容的类型
                 */
                private int contentType;
                /**
                 * 参数集合
                 */
                private Map<String, String> paramMap = new HashMap<>();
                /**
                 * baseUrl
                 */
                private String baseUrl;
                /**
                 * 标签
                 */
                private String tag;

                /**
                 * 需要解析的类class
                 */
                private Class mClass;


                public String getBaseUrl() {
                        return baseUrl;
                }

                public String getUrl() {
                        return url;
                }

                public Map<String, String> getParamMap() {
                        return paramMap;
                }

                public Builder baseUrl(String baseUrl) {
                        this.baseUrl = baseUrl;
                        return this;
                }

                public Builder url(String url) {
                        this.url = url;
                        return this;
                }

                public Builder setParam(String key, String value) {
                        paramMap.put(key, value);
                        return this;
                }


                public Builder contentType(int type, Class typeClass) {
                        this.contentType = type;
                        this.mClass = typeClass;
                        return this;
                }
                public CommonNetManager build() {
                        CommonNetManager manager = getInstance();
                        manager.setBuilder(this);
                        manager.initRetrofit();
                        return manager;
                }


                public String getTag() {
                        return tag;
                }

                public int getContentType() {
                        return contentType;
                }

                public Class getContentClass() {
                        return mClass;
                }
        }


        private Map<String, Retrofit> mRetrofitMap;
        private Retrofit mRetrofit;

        private void initRetrofit() {
                if (mRetrofitMap == null) {
                        mRetrofitMap = new HashMap<>();
                }
                if (mRetrofitMap.containsKey(builder.getBaseUrl())) {
                        mRetrofit = mRetrofitMap.get(builder.getBaseUrl());
                } else {
                        mRetrofit = new Retrofit.Builder().baseUrl(builder.getBaseUrl()).client(sOkHttpClient).build();
                        mRetrofitMap.put(builder.getBaseUrl(), mRetrofit);
                }
        }

        private void setBuilder(Builder builder) {
                this.builder = builder;
        }


        public void get(OnResultListener listener) {
                if (builder == null || builder.getBaseUrl() == null) {
                        throw new NullPointerException("builder 不能为空，baseUrl也不能为空");
                }
                String paramValue = null;
                if (builder.getParamMap() != null && !builder.getParamMap().isEmpty()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        boolean isFirst = true;
                        for (Map.Entry<String, String> entry : builder.getParamMap().entrySet()) {
                                if (!isFirst) {
                                        stringBuilder.append("&");
                                } else {
                                        isFirst = false;
                                }
                                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                        }
                        paramValue = stringBuilder.toString();
                        if (!TextUtils.isEmpty(paramValue)) {
                                builder.url(builder.getUrl() + "?" + paramValue);
                        }
                }
                mCall = mRetrofit.create(ApiService.class).get(builder.getUrl());
                putCall(builder.getTag(), mCall);
                dealCall(listener);
        }

        private void dealCall(final OnResultListener listener) {
                if (!NetUtil.isNetWorkAvailable()){
                        ToastUtils.showLongToast("网络连接失败，请检查网络配置");
                        return;
                }else {
                        mCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                        if (response.code()==200){
                                                try {
                                                        String result=response.body().string();
                                                        parseContent(result,listener);
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                        }else if (!response.isSuccessful()){
                                                listener.onError(response.code(),response.message());
                                        }
                                        if (builder.getTag() != null && sCallMap.containsKey(builder.getTag())) {
                                                sCallMap.remove(builder.getTag());
                                        }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        t.printStackTrace();
                                        listener.onFailed(t.getMessage());
                                        if (builder.getTag() != null && sCallMap.containsKey(builder.getTag())) {
                                                sCallMap.remove(builder.getTag());
                                        }
                                }
                        });

                }
        }

        public static final int STRING=0;
        public static final int OBJECT=1;
        public static  final int OBJECT_ARRAY=2;
        public static final int XML=3;

        @SuppressWarnings("unchecked")
        private void parseContent(String result, OnResultListener listener) {
                if (builder != null) {
                      int   bodyType = builder.getContentType();
                        switch (bodyType){
                                case STRING:
                                        listener.onSuccess(result);
                                        break;
                                case OBJECT:
                                        listener.onSuccess(ParseDataUtil.parseObject(result,builder.getContentClass()));
                                        break;
                                case OBJECT_ARRAY:
                                        listener.onSuccess(ParseDataUtil.parseList(result,builder.getContentClass()));
                                        break;
                                case XML:
                                        listener.onSuccess(ParseDataUtil.parseXml(result,builder.getContentClass()));
                                        break;
                                default:
                                        LogUtil.e("找不到解析的类型");
                                        listener.onError(1000,"找不到解析的类型，解析失败");
                                        break;
                        }
                }
        }

        private static final Map<String,Call<ResponseBody>> sCallMap=new HashMap<>();

        private void putCall(String tag, Call<ResponseBody> call) {
                if (tag != null) {
                        synchronized (sCallMap){
                                sCallMap.put(tag,call);
                        }
                }
        }


}
