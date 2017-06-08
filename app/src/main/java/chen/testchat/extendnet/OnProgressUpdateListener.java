package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      12:13
 * QQ:             1981367757
 */
public interface OnProgressUpdateListener {

        /**
         * 下载进度回调
         *
         * @param totalLength 下载分总长度
         * @param readLength  目前已下载的长度
         * @param isFinished  是否已完成下载
         */
        void onUpdateProgress(long totalLength, long readLength, boolean isFinished);


}
