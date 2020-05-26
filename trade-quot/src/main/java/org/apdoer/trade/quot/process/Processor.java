package org.apdoer.trade.quot.process;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:27
 */
public interface Processor<S, T> {

    /**
     * 数据处理
     *
     * @param source
     * @return
     */
    T process(S source);
}
