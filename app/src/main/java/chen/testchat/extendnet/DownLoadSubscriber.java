package chen.testchat.extendnet;

import java.lang.ref.SoftReference;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      10:43
 * QQ:             1981367757
 */
class DownLoadSubscriber<T> extends Subscriber<T> implements OnProgressUpdateListener {
        private SoftReference<DownLoadCallBack> downLoadCallBack;
        private DownInfo mDownInfo;

        DownLoadSubscriber(DownInfo info) {
                this.mDownInfo = info;
                downLoadCallBack = new SoftReference<>(info.getCallBack());
        }


        @Override
        public void onStart() {
                super.onStart();
                if (downLoadCallBack.get() != null) {
                        downLoadCallBack.get().onStart();
                }
                mDownInfo.setDownLoadStatus(DownState.START.getState());
        }

        @Override
        public void onCompleted() {
                if (downLoadCallBack.get() != null) {
                        downLoadCallBack.get().onCompleted();
                }
                HttpDownLoadManager.getInstance().remove(mDownInfo);
                mDownInfo.setDownLoadStatus(DownState.FINISH.getState());
        }

        @Override
        public void onError(Throwable throwable) {
                if (downLoadCallBack.get() != null) {
                        downLoadCallBack.get().onError(throwable);
                }
                HttpDownLoadManager.getInstance().remove(mDownInfo);
                mDownInfo.setDownLoadStatus(DownState.ERROR.getState());
        }

        @Override
        public void onNext(T t) {
                if (downLoadCallBack.get() != null) {
                        downLoadCallBack.get().onNext(t);
                }

        }

        /**
         * 这里是在子线程中更新，所以需要转到主线程中
         *
         * @param totalLength 下载分总长度
         * @param readLength  目前已下载的长度
         * @param isFinished  是否已完成下载
         */
        @Override
        public void onUpdateProgress(final long totalLength, long readLength, boolean isFinished) {
//                第一次下载,或者是
                if (mDownInfo.getTotalLength() == 0) {
                        mDownInfo.setTotalLength(totalLength);
                }
                mDownInfo.setReadLength(readLength);
                Observable.just(readLength).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long readLength) {
//                                如果已经停止下载，则停止更新
                                if (mDownInfo.getDownLoadStatus() == DownState.PAUSE.getState() || mDownInfo.getDownLoadStatus() == DownState.STOP.getState()) {
                                        return;
                                }
                                mDownInfo.setDownLoadStatus(DownState.DOWN.getState());
                                if (downLoadCallBack.get() != null) {
                                        downLoadCallBack.get().onUpdateProgress(mDownInfo.getTotalLength(), readLength);
                                }
                        }
                });

        }
}
