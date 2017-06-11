package org.pointstone.cugappplat.net.download;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      11:59
 * QQ:             1981367757
 */

public interface HttpService {

        //        断点下载接口
//        @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
        @GET("2017/05/09/64f9e2a440d05c888079cf8d5c514cd8.so")
        Observable<ResponseBody> download();





}
