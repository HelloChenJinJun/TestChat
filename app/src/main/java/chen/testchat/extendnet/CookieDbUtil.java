package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      20:32
 * QQ:             1981367757
 */


/**
 * 缓存cookie数据的数据库工具
 */
public class CookieDbUtil {

        private static CookieDbUtil instance;
        private static final Object LOCK = new Object();


        /**
         * 双重锁定单例模式
         *
         * @return 单例
         */
        public static CookieDbUtil getInstance() {
                if (instance == null) {
                        synchronized (LOCK) {
                                if (instance == null) {
                                        instance = new CookieDbUtil();
                                }
                        }
                }
                return instance;
        }


        public void saveCookieData(CookieResultEntity cookieResultEntity) {
//                这里进行保存cookie数据
        }


        /**
         * 根据URL查询数据
         *
         * @param url
         */
        public CookieResultEntity queryCookieData(String url) {
                return null;
        }


        public long updateCookieData(CookieResultEntity cookieResultEntity) {
                return 0;
        }


}
