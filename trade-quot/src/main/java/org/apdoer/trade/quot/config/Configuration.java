package org.apdoer.trade.quot.config;

import java.util.Map;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:20
 */
public interface Configuration<K, V> {

    /**
     * 获取单个配置
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 加入配置
     * @param key
     * @param value
     */
    void set(K key, V value);

    /**
     * 设置一组配置
     * @param map
     */
    void setAll(Map<K,V> map);

    /**
     * 设置一组配置
     * @param map
     */
    void setAll(Configuration<K, V> config);

    /**
     * 设置所有配置
     * @return
     */
    Map<K,V> elements();

}
