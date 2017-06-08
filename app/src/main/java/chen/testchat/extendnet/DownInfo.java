package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      10:29
 * QQ:             1981367757
 */


/**
 * 下载信息实体entity
 */
public class DownInfo {

        /**
         * 下载ID
         */
        private long id;
        /**
         * 下载保存的文件路径
         */
        private String savedFilePath;


        /**
         * 下载回调
         */
        private DownLoadCallBack mCallBack;
        /**
         * 下载的api
         */
        private HttpService mHttpService;

        public DownLoadCallBack getCallBack() {
                return mCallBack;
        }

        public void setCallBack(DownLoadCallBack callBack) {
                mCallBack = callBack;
        }

        /**
         * 下载文件的全部长度
         */
        private long totalLength;
        /**
         * 已下载的长度,用户断点下载
         */
        private long readLength;


        /**
         * 下载状态
         */
        private int downLoadStatus;

        /**
         * 下载的url
         */
        private String url;

        /**
         * 超时时间
         */
        private long connectedTime;


        public String getSavedFilePath() {
                return savedFilePath;
        }

        public void setSavedFilePath(String savedFilePath) {
                this.savedFilePath = savedFilePath;
        }

        public long getTotalLength() {
                return totalLength;
        }

        public void setTotalLength(long totalLength) {
                this.totalLength = totalLength;
        }

        public long getReadLength() {
                return readLength;
        }

        public void setReadLength(long readLength) {
                this.readLength = readLength;
        }

        public int getDownLoadStatus() {
                return downLoadStatus;
        }

        public void setDownLoadStatus(int downLoadStatus) {
                this.downLoadStatus = downLoadStatus;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public long getConnectedTime() {
                return connectedTime;
        }

        public void setConnectedTime(long connectedTime) {
                this.connectedTime = connectedTime;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public void setHttpService(HttpService httpService) {
                mHttpService = httpService;
        }

        public HttpService getHttpService() {
                return mHttpService;
        }
}
