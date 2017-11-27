package com.architecture.extend.baselib.storage.remote;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class ProgressRequestBody extends RequestBody {
    private final ApiCallBack mListener;
    protected RequestBody delegate;  //实际的待包装请求体
    protected CountingSink countingSink; //包装完成的BufferedSink


    public ProgressRequestBody(RequestBody body, ApiCallBack listener) {
        delegate = body;
        mListener = listener;
    }

    /**
     * 重写调用实际的响应体的contentType
     */
    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     */
    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 重写进行写入
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();  //必须调用flush，否则最后一部分数据可能不会被写入
    }

    /**
     * 包装
     */
    protected final class CountingSink extends ForwardingSink {
        private long bytesWritten = 0;   //当前写入字节数
        private long contentLength = 0;  //总字节长度，避免多次调用contentLength()方法
        private long lastNotifyTime;  //最后一次刷新的时间
        private long lastWriteBytes;     //最后一次写入字节数据

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            if (contentLength <= 0) {
                contentLength = contentLength(); //获得contentLength的值，后续不再调用
            }
            bytesWritten += byteCount;
            long curTime = System.currentTimeMillis();
            //每200毫秒刷新一次数据
            if (mListener != null) {
                if (curTime - lastNotifyTime >= 50 || bytesWritten == contentLength) {
                    //计算下载速度
                    long diffTime = (curTime - lastNotifyTime) / 1000;
                    if (diffTime == 0) {
                        diffTime += 1;
                    }
                    long diffBytes = bytesWritten - lastWriteBytes;
                    long networkSpeed = diffBytes / diffTime;

                    mListener.onUploadProgressUpdate(bytesWritten == contentLength, bytesWritten,
                            contentLength, networkSpeed);


                    lastNotifyTime = System.currentTimeMillis();
                    lastWriteBytes = bytesWritten;
                }
            }
        }
    }
}
