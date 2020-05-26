package org.apdoer.trade.quot.config.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.quot.config.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 11:21
 */
@Slf4j
public class PropertiesConfiguration implements Configuration<String, Object> {
    private final Properties properties;

    public PropertiesConfiguration() {
        this.properties = new Properties();
    }

    public PropertiesConfiguration(Properties properties) {
        this.properties = properties;
    }

    public PropertiesConfiguration(String pathname) {
        this.properties = this.loadconf(pathname);
    }

    private Properties loadconf(String pathname) {
        Properties properties = new Properties();
        File file = new File(pathname);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("File not exists!");
        }

        try {
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            log.error("Properties config load error", e);
        }

        return properties;
    }

    @Override
    public Object get(String key) {
        return properties.get(key);
    }

    @Override
    public void set(String key, Object value) {
        properties.setProperty(key, (String) value);
    }

    @Override
    public void setAll(Map<String, Object> map) {
        properties.putAll(map);
    }

    @Override
    public void setAll(Configuration<String, Object> config) {
        properties.putAll(config.elements());
    }

    @Override
    public Map<String, Object> elements() {
        Map<String, Object> map = new HashMap<>(16);
        Iterator<Object> keys = this.properties.keySet().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, this.properties.get(key));
        }

        return map;
    }
}
