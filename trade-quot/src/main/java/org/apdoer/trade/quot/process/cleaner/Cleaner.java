package org.apdoer.trade.quot.process.cleaner;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:29
 */
public interface Cleaner<S, T> {

    /**
     * 数据清洗
     *
     * @param source
     * @return
     */
    T clean(S source);
}
