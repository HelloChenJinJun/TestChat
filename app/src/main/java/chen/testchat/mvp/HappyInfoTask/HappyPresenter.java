package chen.testchat.mvp.HappyInfoTask;

import com.google.gson.Gson;

import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;
import org.pointstone.cugappplat.net.NetManager;
import org.pointstone.cugappplat.util.NetUtil;

import chen.testchat.api.HappyApi;
import chen.testchat.base.Constant;
import chen.testchat.bean.HappyBean;
import chen.testchat.bean.HappyResponse;
import chen.testchat.util.LogUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

// FIXME generate failure  field _$Result_code243
// FIXME generate failure  field _$Req_id88
// FIXME generate failure  field _$Result238
// FIXME generate failure  field _$Err_msg170
// FIXME generate failure  field _$Timestamp45
// FIXME generate failure  field _$Is_ios268
/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:12
 * QQ:             1981367757
 */

public class HappyPresenter extends HappyContacts.Presenter {

        private Gson mGson = new Gson();
        private int mPage;

        @Override
        public void getHappyInfo(int page) {
                mPage = page;
                if (mPage == 1) {
                        mModel.clearAllCacheHappyData();
                        mView.showLoading("正在加载数据，请稍候..........");

                }
                LogUtil.e("加载的页数" + mPage);
                if (!NetUtil.isNetWorkAvailable()) {
                      mView.hideLoading();
                        mView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getHappyInfo(mPage);
                                }
                        });
                        return;
                }
                Subscription subscription = (NetManager.getInstance().getApi("http://japi.juhe.cn", HappyApi.class)).getHappyInfo(page, 20).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<HappyResponse>() {
                                @Override
                                public void onCompleted() {
                                        mView.hideLoading();
                                }

                                @Override
                                public void onError(Throwable e) {
                                        mView.hideLoading();
                                        if (mPage == 1 && mModel.getHappyInfo(Constant.HAPPY_KEY + mPage) == null) {
                                                mView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                        @Override
                                                        public void onRetry() {
                                                                getHappyInfo(mPage);
                                                        }
                                                });
                                        } else {
                                                mView.showError(e.getMessage(), null);
                                        }
                                        mView.onUpdateHappyInfo(mModel.getHappyInfo(Constant.HAPPY_KEY + mPage));
                                }

                                @Override
                                public void onNext(HappyResponse happyResponse) {
                                        LogUtil.e("接收到的笑话数据" + mGson.toJson(happyResponse));
                                        if (happyResponse.getError_code() == 0) {
                                                mView.onUpdateHappyInfo(happyResponse.getResult().getData());
                                                for (HappyBean happyBean :
                                                        happyResponse.getResult().getData()) {
                                                        mModel.saveHappyInfo(happyBean.getUrl(), mGson.toJson(happyBean));
                                                }
                                                mModel.saveHappyInfo(Constant.HAPPY_KEY + mPage, mGson.toJson(happyResponse));
                                        } else {
                                                LogUtil.e("服务器出错拉" + happyResponse.getReason() + happyResponse.getError_code());
                                        }
                                }
                        });
                addSubscription(subscription);
        }
}
