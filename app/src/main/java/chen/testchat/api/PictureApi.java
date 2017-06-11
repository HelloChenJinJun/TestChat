package chen.testchat.api;

import chen.testchat.bean.PictureResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:36
 * QQ:             1981367757
 */
public interface PictureApi {

        @GET("/api/data/福利/10/{page}")
        Observable<PictureResponse> getPictureInfo(@Path("page") int page);
}
