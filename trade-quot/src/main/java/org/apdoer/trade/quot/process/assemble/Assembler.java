package org.apdoer.trade.quot.process.assemble;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:30
 */
public interface Assembler<S,T> {

    /**
     * 数据组装
     * @param source
     * @return
     */
    T assemble(S source);
}
