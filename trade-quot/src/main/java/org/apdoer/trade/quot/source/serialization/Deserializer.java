package org.apdoer.trade.quot.source.serialization;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 10:58
 */
public interface Deserializer<S, T> {

    /**
     * 反序列化
     * @param source
     * @return
     */
    T deserialize(S source);
}
