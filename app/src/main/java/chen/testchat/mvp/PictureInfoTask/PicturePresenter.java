package chen.testchat.mvp.PictureInfoTask;

import com.google.gson.Gson;

import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;
import org.pointstone.cugappplat.net.NetManager;
import org.pointstone.cugappplat.util.NetUtil;

import chen.testchat.api.PictureApi;
import chen.testchat.base.Constant;
import chen.testchat.bean.PictureBean;
import chen.testchat.bean.PictureResponse;
import chen.testchat.util.LogUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:07
 * QQ:             1981367757
 */
public class PicturePresenter extends PictureContacts.Presenter {
        private Gson mGson = new Gson();

        private int mPage;


        @Override
        public void getPictureInfo(int page) {
                mPage = page;
                if (mPage == 1) {
                        mModel.clearAllCacheData();
                        mView.showLoading("正在加载数据，请稍候..........");
                }
                LogUtil.e("加载的页数" + page);
                if (!NetUtil.isNetWorkAvailable()) {
                        mView.hideLoading();
                        mView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getPictureInfo(mPage);
                                }
                        });
                        return;
                }
                Subscription subscription = ((PictureApi) NetManager.getInstance().getApi("http://gank.io", PictureApi.class)).getPictureInfo(page).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<PictureResponse>() {
                                @Override
                                public void onCompleted() {
                                        mView.hideLoading();
                                }
                                @Override
                                public void onError(Throwable e) {
                                        mView.hideLoading();
//                                        下拉加载设置重试,下拉加载不设置
                                        if (mPage==1&&mModel.getPictureInfo(Constant.PICTURE_KEY+mPage)==null) {
                                                mView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                        @Override
                                                        public void onRetry() {
                                                                getPictureInfo(mPage);
                                                        }
                                                });
                                                mView.showError(e.getMessage(),null);
                                        }
                                        mView.onUpdatePictureInfo(mModel.getPictureInfo(Constant.PICTURE_KEY + mPage));
                                }

                                @Override
                                public void onNext(PictureResponse pictureResponse) {
                                        LogUtil.e("接收到的图片数据" + mGson.toJson(pictureResponse));
                                        if (!pictureResponse.isError()) {
                                                mView.onUpdatePictureInfo(pictureResponse.getResults());
                                                for (PictureBean bean :
                                                        pictureResponse.getResults()) {
                                                        mModel.savePictureInfo(bean.getUrl(), mGson.toJson(bean));
                                                }
                                                mModel.savePictureInfo(Constant.PICTURE_KEY + mPage, mGson.toJson(pictureResponse));
                                        } else {
                                                LogUtil.e("服务器出错拉");
                                        }
                                }
                        });
                addSubscription(subscription);
        }
}
