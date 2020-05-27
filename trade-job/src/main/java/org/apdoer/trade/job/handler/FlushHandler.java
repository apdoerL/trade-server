package org.apdoer.trade.job.handler;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.trade.common.service.ContractCurrencyInitService;
import org.apdoer.trade.common.service.ContractInitService;
import org.apdoer.trade.common.service.ContractLeverageInitService;
import org.apdoer.trade.common.service.OrderCurrencyInitService;
import org.apdoer.trade.core.service.CoreInitService;
import org.apdoer.trade.monitor.service.MonitorInitService;
import org.apdoer.trade.quot.service.QuotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlushHandler {

    @Autowired
    private ContractInitService contractInitService;

    @Autowired
    private OrderCurrencyInitService orderCurrencyInitService;

    @Autowired
    private ContractLeverageInitService contractLeverageInitService;

    @Autowired
    private ContractCurrencyInitService contractCurrencyInitService;

    @Autowired
    private QuotInitService quotInitService;

    @Autowired
    private MonitorInitService posiMonitorInitService;

    @Autowired
    private CoreInitService traderCoreInitService;

    public ResultVo tradeParamFlushHandle() {
        //合約初始化刷新
        this.contractInitService.flush();
        //下单货币初始化
        this.orderCurrencyInitService.flush();
        //合约杠杠初始化
        this.contractLeverageInitService.flush();
        // 合约-币种-杠杆映射初始化
        this.contractCurrencyInitService.init();

        return ResultVoBuildUtils.buildSuccessResultVo();
    }

    public ResultVo channelFlush() {
        //行情模块初始化
        this.quotInitService.flush();
        //持仓强平监听模块初始化
        this.posiMonitorInitService.flush();
        //交易模块初始化
        this.traderCoreInitService.flush();

        return ResultVoBuildUtils.buildSuccessResultVo();
    }

}