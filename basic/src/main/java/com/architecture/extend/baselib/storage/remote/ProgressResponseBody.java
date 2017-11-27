package com.architecture.extend.baselib.storage.remote;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody mResponseBody;
    private final ApiCallBack mListener;
    private BufferedSource mBufferedSource;

    /**
     * 构造函数，赋值
     *
     * @param responseBody 待包装的响应体
     * @param
     */
    public ProgressResponseBody(ResponseBody responseBody, ApiCallBack listener) {
        this.mResponseBody = responseBody;
        mListener = listener;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     * @throws IOException 异常
     */
    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            //包装
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            private long contentLength;
            //当前读取字节数
            long totalBytesRead = 0L;
            private long lastNotifyTime = 0L;  //最后一次刷新的时间
            private long lastWriteBytes = 0L;     //最后一次写入字节数据

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                long curTime = System.currentTimeMillis();
                //每200毫秒刷新一次数据
                if (curTime - lastNotifyTime >= 200 || totalBytesRead == contentLength) {
                    //计算下载速度
                    long diffTime = (curTime - lastNotifyTime) / 1000;
                    if (diffTime == 0) {
                        diffTime += 1;
                    }
                    long diffBytes = totalBytesRead - lastWriteBytes;
                    long networkSpeed = diffBytes / diffTime;

                    if (mListener != null) {
                        mListener.onDownLoadProgressUpdate(bytesRead == -1, totalBytesRead,
                                contentLength, networkSpeed);
                    }

                    lastNotifyTime = System.currentTimeMillis();
                    lastWriteBytes = totalBytesRead;
                }
                return bytesRead;
            }
        };
    }
}
