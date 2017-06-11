package chen.testchat.extendnet;

import com.google.gson.Gson;

import chen.testchat.util.LogUtil;
import rx.Observable;
import rx.functions.Func1;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      18:59
 * QQ:             1981367757
 */

public abstract class BaseApi<T> implements Func1<T, String> {

        //        每一个请求的共同属性
//        是否该请求需要缓存
        private boolean isCache = true;


        /**
         * 是否可以取消加载框
         */
        private boolean canCancel = false;


        /**
         * 是否应该显示加载框
         */
        private boolean shouldShowLoadDialog = true;
        /**
         * 该请求的主要url
         */
        private String baseUrl;
        /**
         * 连接超时时间，默认5秒
         */
        private int connectedTime = 5;


        /**
         * 数据写入超时的时间
         */
        private int writedOutTime = 60;


        /**
         * 有网的情况的数据缓存的时间
         */
        private int netWork_cookieTime = 60;
        /**
         * 没网情况下数据的缓存时间
         */
        private int noNetWork_cookieTime = 60 * 60 * 24 * 30;


        /**
         * 该请求的方法名,用于缓存的键值
         */
        private String methodName;


        /**
         * 根据API接口获取被观察者
         *
         * @param httpService api接口
         * @return 被观察者
         * 每个具体子类自己实现
         */
        public abstract Observable<T> getObservable(HttpService httpService);


        /**
         * 这里把发送过来的数据同意进行错误处理，返回需要关心的数据
         *
         * @param t
         * @return
         */
        @Override
        public String call(T t) {
                LogUtil.e("发送过来的数据格式：" + t.toString());
                Gson gson = new Gson();
//                把异常信息提前处理，使用户只关心需要的数据
                BaseResultEntity baseResultEntity = gson.fromJson(t.toString(), BaseResultEntity.class);
                if (baseResultEntity.getFlag() != 0) {
                        throw new RuntimeException(baseResultEntity.getErrorMsg());
                }
                return baseResultEntity.getData();
        }

        public boolean isCache() {
                return isCache;
        }

        public void setCache(boolean cache) {
                isCache = cache;
        }

        public String getBaseUrl() {
                return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
        }

        public int getConnectedTime() {
                return connectedTime;
        }

        public void setConnectedTime(int connectedTime) {
                this.connectedTime = connectedTime;
        }

        public int getNetWork_cookieTime() {
                return netWork_cookieTime;
        }

        public void setNetWork_cookieTime(int netWork_cookieTime) {
                this.netWork_cookieTime = netWork_cookieTime;
        }

        public int getNoNetWork_cookieTime() {
                return noNetWork_cookieTime;
        }

        public void setNoNetWork_cookieTime(int noNetWork_cookieTime) {
                this.noNetWork_cookieTime = noNetWork_cookieTime;
        }

        public String getMethodName() {
                return methodName;
        }

        public void setMethodName(String methodName) {
                this.methodName = methodName;
        }

        public int getWritedOutTime() {
                return writedOutTime;
        }

        public void setWritedOutTime(int writedOutTime) {
                this.writedOutTime = writedOutTime;
        }

        public boolean isCanCancel() {
                return canCancel;
        }

        public void setCanCancel(boolean canCancel) {
                this.canCancel = canCancel;
        }

        public boolean isShouldShowLoadDialog() {
                return shouldShowLoadDialog;
        }

        public void setShouldShowLoadDialog(boolean shouldShowLoadDialog) {
                this.shouldShowLoadDialog = shouldShowLoadDialog;
        }

        public String getUrl() {
                return baseUrl + methodName;
        }
}
