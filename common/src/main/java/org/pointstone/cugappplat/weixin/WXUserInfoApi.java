package org.pointstone.cugappplat.weixin;

/**
 * Created by Administrator on 2017/4/8.
 */


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WXUserInfoApi {
        @GET("sns/userinfo")
        Observable<WXUserInfoItem> getWXUserInfo(@Query("access_token") String access_token, @Query("openid") String openid);

        @GET("sns/oauth2/access_token")
        Observable<WXAccessTokenItem> getWXAccessToken(@Query("appid") String APPID, @Query("secret") String SECRET, @Query("code") String CODE, @Query("grant_type") String authorization_code);
}
