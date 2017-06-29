package chen.testchat.api;

import chen.testchat.base.Constant;
import chen.testchat.bean.HappyContentResponse;
import chen.testchat.bean.HappyResponse;
import chen.testchat.bean.PictureResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:18
 * QQ:             1981367757
 */

public interface HappyApi {

        @GET("/joke/img/text.from?key=" + Constant.JU_HE_KEY)
        Observable<HappyResponse> getHappyInfo(@Query("page") int page, @Query("pagesize") int pagesize);

        @GET("/joke/content/text.from?key=" + Constant.JU_HE_KEY)
        Observable<HappyContentResponse> getHappyContentInfo(@Query("page") int page, @Query("pagesize") int pagesize);

        Observable<PictureResponse> getPictureInfo(int page, int i);

}
