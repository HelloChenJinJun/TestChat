package chen.testchat.extendnet;

import java.io.IOException;

import chen.testchat.util.CommonUtils;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      19:26
 * QQ:             1981367757
 */


/**
 * 在这里截获请求和响应实体，并根据是否有网来进行相应的缓存处理
 * <p>
 * 这里根据实际项目需要来设置缓存时间
 */
public class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!CommonUtils.isNetWorkAvailable()) {
//                        没网情况下，写入强迫缓存策略
                        request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                Response response = chain.proceed(request);
                if (!CommonUtils.isNetWorkAvailable()) {
//                        没网的情况下，响应的实体缓存时间为3个小时
                        int maxSize = 3 * 60 * 60;
                        response = response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxSize).build();
                } else {
//                        有网的情况下，缓存一分钟
                        int maxAge = 60;
                        response = response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                                .header("Cache-Control", "public, only-if-cached, max-age=" + maxAge).build();
                }
                return response;
        }
}
