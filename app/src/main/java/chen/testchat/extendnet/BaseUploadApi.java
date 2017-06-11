package chen.testchat.extendnet;

import okhttp3.MultipartBody;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      21:46
 * QQ:             1981367757
 */

public abstract class BaseUploadApi extends BaseApi {


        private MultipartBody.Part mPart;

        public MultipartBody.Part getPart() {
                return mPart;
        }

        public void setPart(MultipartBody.Part part) {
                mPart = part;
        }

        /**
         * 上传文件，默认可以取消加载框，显示加载框，不设置缓存
         */
        public BaseUploadApi() {
                setCanCancel(true);
                setShouldShowLoadDialog(true);
                setCache(false);
        }
}
