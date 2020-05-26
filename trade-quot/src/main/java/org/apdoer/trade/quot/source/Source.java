package org.apdoer.trade.quot.source;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 10:54
 */
public interface Source<T> {

    /**
     * 初始化
     */
    void init();

    /**
     * 打开连接
     */
    void open();

    /**
     * 读取数据
     * @return
     */
    T read();

    /**
     * 关闭
     */
    void close();

    /**
     * 数据清除
     */
    void cleanup();

    /**
     * 获取数据源类型
     * @return
     */
    SourceType getSourceType();
}
