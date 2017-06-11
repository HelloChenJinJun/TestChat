package chen.testchat.extendnet;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      13:30
 * QQ:             1981367757
 */
public class RetryWhenNetWorkException implements rx.functions.Func1<Observable<? extends Throwable>, Observable<?>> {

        /**
         * 重试次数
         */
        private int retryCount = 3;


        /**
         * 每次重试的延迟时间
         */
        private long delayTime = 3000;


        @Override
        public Observable<?> call(Observable<? extends Throwable> observable) {

                return observable.zipWith(Observable.range(1, retryCount + 1), new Func2<Throwable, Integer, ExceptionWrapper>() {
                        @Override
                        public ExceptionWrapper call(Throwable throwable, Integer integer) {
                                return new ExceptionWrapper(integer, throwable);
                        }
                }).flatMap(new Func1<ExceptionWrapper, Observable<?>>() {
                        @Override
                        public Observable<?> call(ExceptionWrapper exceptionWrapper) {
                                if ((exceptionWrapper.throwable instanceof ConnectException ||
                                        exceptionWrapper.throwable instanceof SocketException ||
                                        exceptionWrapper.throwable instanceof TimeoutException) && exceptionWrapper.index < retryCount + 1) {
                                        return Observable.timer(delayTime + (exceptionWrapper.index - 1) * delayTime, java.util.concurrent.TimeUnit.MILLISECONDS);
                                }
                                return Observable.error(exceptionWrapper.throwable);
                        }
                });
        }

        private class ExceptionWrapper {
                private int index;
                private Throwable throwable;

                ExceptionWrapper(int index, Throwable throwable) {
                        this.index = index;
                        this.throwable = throwable;
                }
        }
}
