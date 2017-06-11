package org.pointstone.cugappplat.net.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      12:32
 * QQ:             1981367757
 */
class DownLoadInterceptor implements Interceptor {


        private OnProgressUpdateListener mListener;

        DownLoadInterceptor(OnProgressUpdateListener listener) {
                this.mListener = listener;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return new Response.Builder().body(new DownLoadResponse(response.body(), mListener)).build();
        }
}
