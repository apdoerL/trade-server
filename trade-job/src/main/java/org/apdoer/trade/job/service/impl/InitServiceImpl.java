package org.apdoer.trade.job.service.impl;

import org.apdoer.trade.common.service.*;
import org.apdoer.trade.common.utils.OrderNumberUtil;
import org.apdoer.trade.common.utils.PosiNumberUtil;
import org.apdoer.trade.core.service.CoreInitService;
import org.apdoer.trade.job.job.manager.SourceJobManager;
import org.apdoer.trade.job.service.InitService;
import org.apdoer.trade.monitor.service.MonitorInitService;
import org.apdoer.trade.quot.service.QuotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 11:17
 */
@Component
public class InitServiceImpl implements InitService {

    @Autowired
    private OrderNumberUtil orderNumberUtil;

    @Autowired
    private PosiNumberUtil posiNumberUtil;

    @Autowired
    private ContractInitService contractInitService;

    @Autowired
    private OrderCurrencyInitService orderCurrencyInitService;

    @Autowired
    private ContractLeverageInitService contractLeverageInitService;

    @Autowired
    private ContractCurrencyInitService contractCurrencyInitService;

    @Autowired
    private PosiDataInitService posiDataInitService;

    @Autowired
    private QuotInitService quotInitService;

    @Autowired
    private CoreInitService coreInitService;

    @Autowired
    private MonitorInitService monitorInitService;

    @Autowired
    private PosiEventInitService posiEventInitService;

    @Autowired
    private SourceJobManager sourceJobManager;

    @Override
    @PostConstruct
    public void init() {
        //持仓数据通道初始化
        posiEventInitService.init();
        //条件委托id初始化
        orderNumberUtil.init();
        //持仓id初始化
        posiNumberUtil.init();
        //合约初始化
        contractInitService.init();
        //合约下单币种初始化
        orderCurrencyInitService.init();
        //合约杠杆初始化
        contractLeverageInitService.init();
        //合约-币种-杠杆映射初始化
        contractCurrencyInitService.init();
        //持仓数据初始化
        posiDataInitService.init();
        //行情模块初始化
        quotInitService.init();
        //监听模块初始化
        monitorInitService.init();
        //交易模块初始化
        coreInitService.init();
        //任务开启
        sourceJobManager.init();
    }


    @Override
    public void flush() {

    }
}
