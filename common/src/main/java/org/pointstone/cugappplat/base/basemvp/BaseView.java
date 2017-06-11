package org.pointstone.cugappplat.base.basemvp;

import com.trello.rxlifecycle.LifecycleTransformer;

import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      19:05
 * QQ:             1981367757
 */

public interface BaseView {


        void showLoading(String loadingMsg);

        void hideLoading();

        void showError(String errorMsg, EmptyLayout.OnRetryListener listener);

        <T> LifecycleTransformer<T> bindLife();

}
