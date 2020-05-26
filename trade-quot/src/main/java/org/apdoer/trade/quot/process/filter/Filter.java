package org.apdoer.trade.quot.process.filter;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:29
 */
public interface Filter<S, T> {

    /**
     * 数据过滤
     * @param source
     * @return
     */
    T filter(S source);
}
