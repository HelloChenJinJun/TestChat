package chen.testchat.extendnet;

//import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import chen.testchat.CustomApplication;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      16:54
 * QQ:             1981367757
 */

public class CommonHttpManager {
        private SoftReference<OnNextListener> mOnNextListenerSoftReference;
        private SoftReference<RxAppCompatActivity> mRxAppCompatActivitySoftReference;


        public CommonHttpManager(OnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
                mOnNextListenerSoftReference = new SoftReference<>(listener);
                mRxAppCompatActivitySoftReference = new SoftReference<>(rxAppCompatActivity);
        }


        public void dealApiReqest(BaseApi baseApi) {
//                创建一个OkHttpClient
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                CommonSubscriber commonSubscriber = new CommonSubscriber(baseApi, mOnNextListenerSoftReference, mRxAppCompatActivitySoftReference);
                builder.addInterceptor(new CacheInterceptor()).connectTimeout(baseApi.getConnectedTime(), TimeUnit.SECONDS)
                        .writeTimeout(baseApi.getWritedOutTime(), TimeUnit.SECONDS).addNetworkInterceptor(new CacheInterceptor())
                        .cache(new Cache(CustomApplication.getInstance().getCacheDir(), 10 * 1024 * 1024));
                Retrofit retrofit = new Retrofit.Builder().client(builder.build()).baseUrl(baseApi.getBaseUrl()).addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
//                        .build();
//                这里原理是通过Java的动态代理创建接口的实例
                HttpService httpService = retrofit.create(HttpService.class);
                Observable observable = baseApi.getObservable(httpService);
                observable.retryWhen(new RetryWhenNetWorkException()).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).map(baseApi).subscribe(commonSubscriber);
        }


}
