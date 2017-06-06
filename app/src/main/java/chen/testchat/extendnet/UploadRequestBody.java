package chen.testchat.extendnet;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/24      22:06
 * QQ:             1981367757
 */

public class UploadRequestBody extends RequestBody {
        private RequestBody mRequestBody;
        private OnUploadProgressListener mOnUploadProgressListener;


        private BufferedSink mBufferedSink;

        public UploadRequestBody(RequestBody requestBody, OnUploadProgressListener onUploadProgressListener) {
                mRequestBody = requestBody;
                mOnUploadProgressListener = onUploadProgressListener;
        }

        @Override
        public MediaType contentType() {
                return mRequestBody.contentType();
        }


        @Override
        public long contentLength() throws IOException {
                return mRequestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
//                写入数据,在写入的过程中进行回调
                if (mBufferedSink == null) {
                        mBufferedSink = Okio.buffer(sink(sink));
                }
        }

        private Sink sink(BufferedSink sink) {
                return new ForwardingSink(sink) {
                        //                        当前一写入的长度
                        private long writtenLength = 0L;
                        //                        文件总长度
                        private long totalLength = 0L;

                        @Override
                        public void write(Buffer source, long byteCount) throws IOException {
                                super.write(source, byteCount);
                                if (totalLength == 0) {
//                                        第一次写入赋值
                                        totalLength = contentLength();
                                }
                                writtenLength += byteCount;
//                                这里进行线程切换，因为写入是在子线程进行，回调要在主线程进行
                                rx.Observable.just(writtenLength).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                                        @Override
                                        public void call(Long aLong) {
                                                if (mOnUploadProgressListener != null) {
                                                        mOnUploadProgressListener.onProgressChanged(aLong, totalLength);
                                                }
                                        }
                                });
                        }
                };
        }
}
