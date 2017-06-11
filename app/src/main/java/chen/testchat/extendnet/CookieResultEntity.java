package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      20:45
 * QQ:             1981367757
 */
public class CookieResultEntity {
        /**
         * cookieID
         */
        private int id;
        /**
         * 每一个请求所对应的url
         */
        private String url;
        /**
         * 缓存的用户数据
         */
        private String data;
        /**
         * 缓存时间
         */
        private long time;


        public CookieResultEntity(String url, String data, long time) {
                this.url = url;
                this.data = data;
                this.time = time;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
        }

        public long getTime() {
                return time;
        }

        public void setTime(long time) {
                this.time = time;
        }
}
