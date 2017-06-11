package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      11:00
 * QQ:             1981367757
 */
abstract class DownLoadCallBack<T> {


        public abstract void onNext(T t);


        abstract void onStart();


        abstract void onCompleted();


        abstract void onError(Throwable error);


        abstract void onUpdateProgress(long totalLength, long readLength);


        abstract void onStop();

        abstract void onPasue();
}
