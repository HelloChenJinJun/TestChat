package org.pointstone.cugappplat.weixin;

/**
 * Created by Administrator on 2017/4/8.
 */


import org.pointstone.cugappplat.db.ResultEntity;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface CUGUserInfoApi {
@GET("userinfo_for_wechat_unionid")
Observable<ResultEntity<CUGUserInfoItem>> getCUGUserInfo(@Header("X-API-KEY") String key, @Query("unionid") String unionId);
}
