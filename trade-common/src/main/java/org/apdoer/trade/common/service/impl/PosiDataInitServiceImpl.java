package org.apdoer.trade.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.MasterPosiData;
import org.apdoer.trade.common.service.PosiDataInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PosiDataInitServiceImpl implements PosiDataInitService {

    @Autowired
    private MasterPosiData masterPosiData;

    @Override
    public void init() {
        try {
            masterPosiData.init();
        } catch (Exception e) {
            log.error("posi data init error.", e);
            throw new RuntimeException("posi data init error.");
        }
    }

    @Override
    public void flush() {

    }
}