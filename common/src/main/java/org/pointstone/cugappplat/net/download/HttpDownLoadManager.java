package org.pointstone.cugappplat.net.download;

import org.pointstone.cugappplat.util.FileUtil;
import org.pointstone.cugappplat.util.LogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      10:28
 * QQ:             1981367757
 */

public class HttpDownLoadManager {
        /**
         * 缓存下载消息实体列表
         */
        private Set<DownInfo> mDownInfoSet;
        /**
         * 存储正在下载的DownInfo
         */
        private Map<String, DownLoadSubscriber> mDownInfoMap;


        private static HttpDownLoadManager instance;

        private static final Object LOCK = new Object();


        private HttpDownLoadManager() {
                mDownInfoSet = new HashSet<>();
                mDownInfoMap = new HashMap<>();
        }


        public static HttpDownLoadManager getInstance() {
                if (instance == null) {
                        synchronized (LOCK) {
                                if (instance == null) {
                                        instance = new HttpDownLoadManager();
                                }
                        }
                }
                return instance;
        }


        public void start(final DownInfo info) {
//                如果消息实体为空，或正在下载，则返回
                LogUtil.e("开始下载1");
                if (info == null || mDownInfoMap.get(info.getUrl()) != null) return;
                DownLoadSubscriber downLoadSubscriber = new DownLoadSubscriber(info);
                mDownInfoMap.put(info.getUrl(), downLoadSubscriber);
//                HttpService service;
//                增加一个拦截器，用于获取数据的进度回调
                DownLoadInterceptor interceptor = new DownLoadInterceptor(downLoadSubscriber);
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(info.getConnectedTime(), TimeUnit.SECONDS).addInterceptor(interceptor);
                Retrofit retrofit = new Retrofit.Builder().
                        addConverterFactory(ScalarsConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                        baseUrl("http://bmob-cdn-7375.b0.upaiyun.com/").client(builder.build()).build();
                HttpService service = retrofit.create(HttpService.class);
//                info.setHttpService(service);
                service.download().subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io()).map(new Func1<ResponseBody, DownInfo>() {
                        @Override
                        public DownInfo call(ResponseBody responseBody) {
                                LogUtil.e("call");
                                if (responseBody != null) {
                                        LogUtil.e("ewsponseBody  is not null");
                                }
                                FileUtil.writeToCache(responseBody, info.getSavedFilePath(), info.getReadLength(), info.getTotalLength());
//                                这里进行转化
                                return info;
                        }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(downLoadSubscriber);
        }


        public void stop(DownInfo info) {
                if (info == null) return;
                info.setDownLoadStatus(DownState.STOP.getState());
                info.getCallBack().onStop();
                if (mDownInfoMap.containsKey(info.getUrl()) && !mDownInfoMap.get(info.getUrl()).isUnsubscribed()) {
                        mDownInfoMap.get(info.getUrl()).unsubscribe();
                }
                remove(info);
        }

        private void pause(DownInfo info) {
                if (info == null) return;
                info.setDownLoadStatus(DownState.STOP.getState());
                info.getCallBack().onStop();
                if (mDownInfoMap.containsKey(info.getUrl()) && !mDownInfoMap.get(info.getUrl()).isUnsubscribed()) {
                        mDownInfoMap.get(info.getUrl()).unsubscribe();
                }
                remove(info);
        }


        /**
         * 停止所有的下载任务
         */
        public void stopAll() {
                for (DownInfo info :
                        mDownInfoSet) {
                        pause(info);
                }
                mDownInfoMap.clear();
                mDownInfoSet.clear();
        }

        void remove(DownInfo downInfo) {
                if (!mDownInfoSet.contains(downInfo)) {
                        mDownInfoSet.remove(downInfo);
                }
                if (!mDownInfoMap.containsKey(downInfo.getUrl())) {
                        mDownInfoMap.remove(downInfo.getUrl());
                }
        }

}
