package org.pointstone.cugappplat.net;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      22:59
 * QQ:             1981367757
 */

public class LoggerInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                logForRequest(request);
                Response response = chain.proceed(request);
                return logForResponse(response);
        }

        private Response logForResponse(Response response) {
                try {
                        Logger.d("type:response" + "------------code:" + response.code() + "-----------protocol" + response.protocol());
                        ResponseBody body = response.body();
                        if (body != null && body.contentType() != null) {
                                if (isTextType(body.contentType())) {
                                        if (body.contentType().subtype() != null) {
                                                String result = body.string();
                                                switch (body.contentType().subtype()) {
                                                        case "json":
                                                                Logger.json(result);
                                                                break;
                                                        case "xml":
                                                                Logger.xml(result);
                                                        default:
                                                                Logger.d(result);
                                                }
                                                return response.newBuilder().body(ResponseBody.create(body.contentType(), result)).build();
                                        }
                                }
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return response;
        }

        private boolean isTextType(MediaType contentType) {
                return contentType.type() != null && contentType.type().equals("text") || contentType.subtype() != null && (contentType.subtype().equals("json") || contentType.subtype().equals("xml") || contentType.subtype().equals("html") || contentType.subtype().equals("webviewhtml"));
        }
        private void logForRequest(Request request) {
                Logger.d("type：request" + "----------method:" + request.method() + "----------url:" + request.url());
                Headers headers = request.headers();
                if (headers != null && headers.size() > 0) {
                        Logger.d("headers:" + headers.toString());
                }
                RequestBody body = request.body();
                if (body != null) {
                        MediaType contentType = body.contentType();
                        if (contentType != null) {
                                if (isTextType(contentType)) {
                                        Logger.d(parseContent(request));
                                } else {
                                        Logger.d("不是纯文本类型");
                                }
                        }
                }
        }

        private String parseContent(Request request) {
                Buffer buffer = new Buffer();
                try {
                        request.newBuilder().build().body().writeTo(buffer);
                        return buffer.readUtf8();
                } catch (IOException e) {
                        e.printStackTrace();
                        return "解析请求数据流异常" + e.getMessage();
                }
        }
}
