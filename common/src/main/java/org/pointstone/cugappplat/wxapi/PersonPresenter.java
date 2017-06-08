package org.pointstone.cugappplat.wxapi;


import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.pointstone.cugappplat.base.BaseApplication;
import org.pointstone.cugappplat.base.basemvp.ILoadDataView;
import org.pointstone.cugappplat.base.basemvp.RxBusPresenter;
import org.pointstone.cugappplat.rxbus.RxBus;
import org.pointstone.cugappplat.util.LogUtil;
import org.pointstone.cugappplat.weixin.CUGUserInfoItem;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/4/8.
 */

public class PersonPresenter extends RxBusPresenter {
        private RxBus mRxBus;
//        private ILoadDataView<CUGUserInfoItem> mView;


        public PersonPresenter(ILoadDataView<CUGUserInfoItem> view, RxBus rxBus) {
                mRxBus = rxBus;
//                mView=view;
        }


        @Override
        protected void registerEvent(Class eventType, Action1 call) {
                Subscription subscription = mRxBus.registerEvent(eventType, call
                        , new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                        LogUtil.e(throwable.getMessage());
                                }
                        });
                mRxBus.addSubscription(this, subscription);
        }

        @Override
        public void unRegisterEvent() {
                mRxBus.unSubcrible(this);
        }


        public void login() {
                LogUtil.e("login1112223334455");
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wx_login";
                BaseApplication.api.sendReq(req);
        }
}
