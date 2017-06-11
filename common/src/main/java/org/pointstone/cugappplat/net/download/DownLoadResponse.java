package org.pointstone.cugappplat.net.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      12:35
 * QQ:             1981367757
 */
public class DownLoadResponse extends ResponseBody {
        private ResponseBody mResponseBody;
        private OnProgressUpdateListener mListener;

        public DownLoadResponse(ResponseBody responseBody, OnProgressUpdateListener listener) {
                this.mResponseBody = responseBody;
                this.mListener = listener;
        }

        @Override
        public MediaType contentType() {
                return mResponseBody.contentType();
        }

        @Override
        public long contentLength() {
                return mResponseBody.contentLength();
        }


        private BufferedSource mBufferedSource;

        @Override
        public BufferedSource source() {
                if (mBufferedSource == null) {
                        mBufferedSource = Okio.buffer(buffer(mResponseBody.source()));
                }
                return mBufferedSource;
        }

        private Source buffer(BufferedSource source) {
                return new ForwardingSource(source) {

                        long totalReadLength = 0L;

                        @Override
                        public long read(Buffer sink, long byteCount) throws IOException {
                                long readLength = super.read(sink, byteCount);
                                if (readLength != -1) {
                                        totalReadLength += readLength;
                                }
                                if (mListener != null) {
                                        mListener.onUpdateProgress(mResponseBody.contentLength(), totalReadLength, readLength == -1);
                                }
                                return readLength;
                        }
                };
        }
}
