package org.pointstone.cugappplat.net.download;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      11:00
 * QQ:             1981367757
 */
public abstract class DownLoadCallBack<T> {


        public abstract void onNext(T t);


      public   abstract void onStart();


        public abstract void onCompleted(T t);


       public abstract void onError(Throwable error);


      public   abstract void onUpdateProgress(long totalLength, long readLength);


       public abstract void onStop();

       public abstract void onPasue();
}
