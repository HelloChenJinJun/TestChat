package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      17:00
 * QQ:             1981367757
 */
public interface OnNextListener {


        /**
         * @param result 返回结果
         * @param method 请求的方法类型
         */
        void onNext(String result, String method);


//        /**
//         * 数据全部发送完成后调用
//         */
//        void onCompleted();

        /**
         * 出现错误的时候调用
         *
         * @param throwable 异常
         */
        void onError(Throwable throwable);
}
