package org.pointstone.cugappplat.net;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      23:37
 * QQ:             1981367757
 */

public interface OnResultListener<T> {
        void onSuccess(T result);
        void onError(int errorCode,String errorMsg);
        void onFailed(String message);

}
