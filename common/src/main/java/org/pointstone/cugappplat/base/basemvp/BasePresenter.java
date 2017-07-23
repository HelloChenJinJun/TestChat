package org.pointstone.cugappplat.base.basemvp;

import org.pointstone.cugappplat.util.LogUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      19:07
 * QQ:             1981367757
 */

public abstract class BasePresenter<V, M> {
        protected V mView;
        protected M mModel;
        private CompositeSubscription mCompositeSubscription = null;


        public void setViewAndModel(V view, M model) {
                mView = view;
                mModel = model;
        }


        public void addSubscription(Subscription subscription) {
                if (mCompositeSubscription == null) {
                        mCompositeSubscription = new CompositeSubscription();
                }
                mCompositeSubscription.add(subscription);
        }

        public void unSubscrible() {
                if (mCompositeSubscription != null) {
                        mCompositeSubscription.clear();
                        mCompositeSubscription = null;
                }
        }


        public void onDestroy() {
                LogUtil.e("presenterOnDestroy");
                unSubscrible();
//                解绑定
                mView = null;
                mModel = null;
        }


}
