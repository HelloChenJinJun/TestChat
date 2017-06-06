package org.pointstone.cugappplat.base.basemvp;

import rx.functions.Action1;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/27      12:43
 * QQ:             1981367757
 */

public abstract class RxBusPresenter<V extends BaseView, M extends BaseModel> extends BasePresenter<V, M> {

        protected abstract void registerEvent(Class eventType, Action1 call);

        protected abstract void unRegisterEvent();

}
