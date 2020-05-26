package org.apdoer.trade.quot.source.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.quot.config.Configuration;
import org.apdoer.trade.quot.constants.QuotConstants;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.source.SourceType;
import org.apdoer.trade.quot.source.ZmqSource;
import org.apdoer.trade.quot.source.serialization.Deserializer;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 15:09
 */
@Slf4j
public class ZmqSourceImpl implements ZmqSource<IndexPriceMessageSourcePayload> {

    protected boolean runFlag = true;
    protected int queueSize;
    protected int releaseSize;

    protected static final int DEFAULT_CAPACITY = 100000;
    protected static final int DEFAULT_RELEASE_SIZE = 20000;

    protected ZMQ.Context context;
    protected ZMQ.Socket socket;
    protected Queue<String> queue;
    protected Configuration<String, Object> config;
    protected Deserializer<String, IndexPriceMessageSourcePayload> deserializer;

    public ZmqSourceImpl(Configuration<String, Object> _config, Deserializer<String, IndexPriceMessageSourcePayload> deserializer) {
        this.config = _config;
        this.deserializer = deserializer;
    }

    @Override
    public void run() {
        log.info("Zmq source recv start");
        while (runFlag) {
            try {
                byte[] recv = socket.recv(0);
                if (null != recv) {
                    String json = new String(recv, StandardCharsets.UTF_8);
                    this.queueRelease(this.queue);
                    this.queue.add(json);
                }
            } catch (Exception e) {
                log.error("Zmq recv exception", e);
            }
        }
    }

    protected void queueRelease(Queue<String> queue) {
        //丢弃旧数据
        if (queue.size() == this.queueSize) {
            for (int i = 0; i < releaseSize; i++) {
                queue.poll();
            }
        }
    }

    @Override
    public void init() {
        if (!this.checkParamsIsLegal(this.config)) {
            throw new RuntimeException("Zmq Config error");
        }
        Object queueSizeObj = this.config.get(QuotConstants.SOURCE_QUEUE_SIZE_KEY);
        this.queueSize = queueSizeObj == null ? DEFAULT_CAPACITY : new Integer(queueSizeObj.toString());
        Object releaseSizeObj = this.config.get(QuotConstants.SOURCE_RELEASE_SIZE_KEY);
        this.releaseSize = releaseSizeObj == null ? DEFAULT_RELEASE_SIZE : new Integer(releaseSizeObj.toString());
        this.queue = new ArrayBlockingQueue<>(this.queueSize);
    }

    @Override
    public void open() {
        String url = config.get(QuotConstants.SOURCE_URL_KEY).toString();
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.SUB);
        socket.setRcvHWM(0);
        socket.setReceiveBufferSize(0);
        // todo
        socket.connect(url);
        socket.subscribe("".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public IndexPriceMessageSourcePayload read() {
        return deserializer.deserialize(queue.poll());
    }


    @Override
    public void close() {
        if (null != socket) {
            socket.close();
        }
        this.runFlag = false;
    }

    @Override
    public void cleanup() {
        queue.clear();
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.ZMQ;
    }

    private boolean checkParamsIsLegal(Configuration<String, Object> config) {
        if (null == config) {
            log.error("Config is null");
            return false;
        }
        if (null == config.get(QuotConstants.SOURCE_URL_KEY)) {
            log.error("Config url is null");
            return false;
        }
        return true;
    }
}
