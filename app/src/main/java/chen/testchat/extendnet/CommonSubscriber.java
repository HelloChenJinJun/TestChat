package chen.testchat.extendnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import chen.testchat.util.CommonUtils;
import rx.Subscriber;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      20:09
 * QQ:             1981367757
 */
class CommonSubscriber<T> extends Subscriber<T> {
        private SoftReference<OnNextListener> mOnNextListenerSoftReference;
        private SoftReference<Context> mContextSoftReference;
        private BaseApi mBaseApi;
        private ProgressDialog mDialog;

        CommonSubscriber(BaseApi baseApi, SoftReference<OnNextListener> onNextListenerSoftReference, SoftReference<Context> contextSoftReference) {
                this.mBaseApi = baseApi;
                this.mOnNextListenerSoftReference = onNextListenerSoftReference;
                this.mContextSoftReference = contextSoftReference;
                initLoadDialog();
        }

        private void initLoadDialog() {
                if (mContextSoftReference.get() != null) {
                        mDialog = new ProgressDialog(mContextSoftReference.get());
                        mDialog.setMessage("正在加载..........请稍候..........");
                        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                        dialog.cancel();
                                        unsubscribe();
                                }
                        });
                }
        }

        @Override
        public void onStart() {
                showLoadingDialog();
                if (mBaseApi.isCache()) {
                        CookieDbUtil cookieDbUtil = CookieDbUtil.getInstance();
                        CookieResultEntity cookieResultEntity = cookieDbUtil.queryCookieData(mBaseApi.getUrl());
                        if (cookieResultEntity != null) {
                                long time = cookieResultEntity.getTime();
                                long currentTime = System.currentTimeMillis();
                                long intervalTime = currentTime - time;
                                if (!CommonUtils.isNetWorkAvailable()) {
                                        //                                这里进行查询数据，并判断无网络时过期时间,并且不会传到onNext()方法中
                                        if (mBaseApi.getNoNetWork_cookieTime() < intervalTime) {
                                                //                                        在缓存期内
                                                if (mOnNextListenerSoftReference.get() != null) {
                                                        mOnNextListenerSoftReference.get().onNext(cookieResultEntity.getData(), mBaseApi.getMethodName());
                                                }
                                                onError(new Throwable("网络连接失败，请检查网络配置"));
                                        } else {
                                                onError(new Throwable("网络连接失败，并且缓存数据过期"));
                                        }
                                        unsubscribe();
                                } else {
                                        //                                这里进行查询数据，并判断有网络时的过期时间
                                        if (mBaseApi.getNetWork_cookieTime() < intervalTime) {
                                                //                                        在缓存期内
                                                if (mOnNextListenerSoftReference.get() != null) {
                                                        mOnNextListenerSoftReference.get().onNext(cookieResultEntity.getData(), mBaseApi.getMethodName());
                                                        onCompleted();
                                                        unsubscribe();
                                                }
                                        }
                                }
                        }
                }
        }

        private void showLoadingDialog() {
                if (!mDialog.isShowing()) {
                        mDialog.show();
                }
        }

        @Override
        public void onCompleted() {
                dismissLoadingDialog();

        }

        @Override
        public void onError(Throwable e) {
                dismissLoadingDialog();
                if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                        Toast.makeText(mContextSoftReference.get(), "网络连接失败，请检查网络配置", Toast.LENGTH_SHORT).show();
                } else {
                        Toast.makeText(mContextSoftReference.get(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (mOnNextListenerSoftReference.get() != null) {
                        mOnNextListenerSoftReference.get().onError(e);
                }
        }

        private void dismissLoadingDialog() {
                if (!isUnsubscribed()) {
                        unsubscribe();
                }
                if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                }
        }

        @Override
        public void onNext(T t) {
//                到这里1、第一次请求   2、有网缓存过期   3、该请求不缓存
                if (mBaseApi.isCache()) {
//                        请求缓存的情况下
//                        判断是否是第一次请求
                        long currentTime = System.currentTimeMillis();

                        CookieResultEntity cookieResultEntity = CookieDbUtil.getInstance().queryCookieData(mBaseApi.getUrl());
                        if (cookieResultEntity == null) {
//                                第一次请求
                                cookieResultEntity = new CookieResultEntity(mBaseApi.getUrl(), t.toString(), currentTime);
                                CookieDbUtil.getInstance().saveCookieData(cookieResultEntity);
                        } else {
//                                更新数据
                                cookieResultEntity.setData(t.toString());
                                cookieResultEntity.setTime(currentTime);
                                cookieResultEntity.setUrl(mBaseApi.getUrl());
                                CookieDbUtil.getInstance().updateCookieData(cookieResultEntity);
                        }
                }

                if (mOnNextListenerSoftReference.get() != null) {
                        mOnNextListenerSoftReference.get().onNext(t.toString(), mBaseApi.getMethodName());
                }
        }

}
