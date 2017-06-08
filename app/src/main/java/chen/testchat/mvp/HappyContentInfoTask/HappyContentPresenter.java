package chen.testchat.mvp.HappyContentInfoTask;

import com.google.gson.Gson;

import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;
import org.pointstone.cugappplat.net.NetManager;
import org.pointstone.cugappplat.util.NetUtil;

import chen.testchat.api.HappyApi;
import chen.testchat.base.Constant;
import chen.testchat.bean.HappyContentResponse;
import chen.testchat.util.LogUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:28
 * QQ:             1981367757
 */
public class HappyContentPresenter extends HappyContentContacts.Presenter {
        private Gson mGson = new Gson();
        private int mPage;

        @Override
        public void getHappyContentInfo(int page) {
                mPage = page;
                if (mPage == 1) {
                        mModel.clearAllCacheHappyContentData();
                        mView.showLoading("正在加载数据，请稍候..........");
                }
                if (!NetUtil.isNetWorkAvailable()) {
                        mView.hideLoading();
                        mView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getHappyContentInfo(mPage);
                                }
                        });
                        return;
                }


                Subscription subscription = ((HappyApi) NetManager.getInstance().getApi("http://japi.juhe.cn", HappyApi.class)).getHappyContentInfo(page, 20).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<HappyContentResponse>() {
                                @Override
                                public void onCompleted() {
                                        mView.hideLoading();
                                }

                                @Override
                                public void onError(Throwable e) {
                                        mView.hideLoading();
                                        if (mPage == 1 && mModel.getHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage) == null) {
                                                mView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                        @Override
                                                        public void onRetry() {
                                                                getHappyContentInfo(mPage);
                                                        }
                                                });
                                        }
                                        mView.onUpdateHappyContentInfo(mModel.getHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage));
                                }

                                @Override
                                public void onNext(HappyContentResponse happyContentResponse) {
                                        LogUtil.e("接收到的笑话数据" + mGson.toJson(happyContentResponse));
                                        if (happyContentResponse.getError_code() == 0) {
                                                mView.onUpdateHappyContentInfo(happyContentResponse.getResult().getData());
                                                mModel.saveHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage, mGson.toJson(happyContentResponse));
                                        } else {
                                                LogUtil.e("服务器出错拉" + happyContentResponse.getReason() + happyContentResponse.getError_code());
                                                mView.onUpdateHappyContentInfo(mModel.getHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage));
                                        }
                                }
                        });
                addSubscription(subscription);
        }
}
