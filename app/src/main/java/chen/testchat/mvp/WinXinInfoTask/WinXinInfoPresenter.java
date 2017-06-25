package chen.testchat.mvp.WinXinInfoTask;

import com.google.gson.Gson;

import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;
import org.pointstone.cugappplat.net.NetManager;
import org.pointstone.cugappplat.util.NetUtil;

import chen.testchat.api.TxApi;
import chen.testchat.base.Constant;
import chen.testchat.bean.TxResponse;
import chen.testchat.bean.WinXinBean;
import chen.testchat.util.LogUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      0:14
 * QQ:             1981367757
 */

public class WinXinInfoPresenter extends WinXinInfoContacts.Presenter {
        private Gson mGson = new Gson();
        private int mPage;

        @Override
        public void getWinXinInfo( int page) {
                this.mPage=page;
                LogUtil.e("测试拉拉");
                if (page == 1) {
                        LogUtil.e("这里清除数据困中的缓存数据");
                        mView.showLoading("正在加载..........");
                        mModel.clearAllData();
                }
                if (!NetUtil.isNetWorkAvailable()) {
                        mView.hideLoading();
                        mView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getWinXinInfo(mPage);
                                }
                        });
                        return;
                }

                Subscription subscription=  ((TxApi) NetManager.getInstance().getApi("https://api.tianapi.com", TxApi.class)).getWinXinInfo(page)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<TxResponse>() {
                                @Override
                                public void onCompleted() {
                                        mView.hideLoading();
                                }

                                @Override
                                public void onError(Throwable e) {
                                        LogUtil.e(e.getMessage());
                                        mView.hideLoading();
                                        mView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                @Override
                                                public void onRetry() {
                                                        getWinXinInfo(mPage);
                                                }
                                        });
                                        mView.updateData(mModel.getCacheWeiXinInfo(Constant.WEI_XIN_KEY + mPage));
                                }

                                @Override
                                public void onNext(TxResponse txResponse) {
                                        LogUtil.e("接收到的数据" + mGson.toJson(txResponse));
                                        if (txResponse.getCode() == 200) {
                                                mView.updateData(txResponse.getNewslist());
//                                                这个缓存用来存储读取状态
                                                for (WinXinBean bean :
                                                        txResponse.getNewslist()) {
                                                        mModel.saveCacheWeiXinInfo(bean.getUrl(), mGson.toJson(bean));
                                                }
                                                mModel.saveCacheWeiXinInfo(Constant.WEI_XIN_KEY + mPage, mGson.toJson(txResponse));
                                        } else {
                                                LogUtil.e("服务器解析数据出错" + txResponse.getCode());
                                        }
                                }
                        });
                addSubscription(subscription);
//                Subscription subscription = HttpServiceManager.getInstance().getTxApi().getWinXinInfo(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<TxResponse>() {
//                                @Override
//                                public void onCompleted() {
//                                        mView.hideLoading();
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                        LogUtil.e(e.getMessage());
//                                        mView.hideLoading();
//                                        mView.showError(e.getMessage());
//                                        mView.updateData(mModel.getCacheWeiXinInfo(Constant.WEI_XIN_KEY + page));
//                                }
//
//                                @Override
//                                public void onNext(TxResponse txResponse) {
//                                        LogUtil.e("接收到的数据" + mGson.toJson(txResponse));
//                                        if (txResponse.getCode() == 200) {
//                                                mView.updateData(txResponse.getNewslist());
////                                                这个缓存用来存储读取状态
//                                                for (WinXinBean bean :
//                                                        txResponse.getNewslist()) {
//                                                        mModel.saveCacheWeiXinInfo(bean.getUrl(), mGson.toJson(bean));
//                                                }
//                                                mModel.saveCacheWeiXinInfo(Constant.WEI_XIN_KEY + page, mGson.toJson(txResponse));
//                                        } else {
//                                                LogUtil.e("服务器解析数据出错" + txResponse.getCode());
//                                        }
//                                }
//                        });
//                addSubscription(subscription);
        }
}
