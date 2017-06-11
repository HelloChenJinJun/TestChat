package org.pointstone.cugappplat.net;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      23:59
 * QQ:             1981367757
 */

public interface ApiService {
        @GET
        Call<ResponseBody> get(@Url String url);

        @FormUrlEncoded
        @POST
        Call<ResponseBody> post(@Url String url, @FieldMap Map<String,String>map);


        @Streaming
        @GET
        Call<ResponseBody> download(@Url String url, @HeaderMap Map<String,String> headerMap);

}
