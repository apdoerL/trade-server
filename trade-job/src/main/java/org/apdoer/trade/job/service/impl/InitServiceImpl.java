package org.apdoer.trade.job.service.impl;

import org.apdoer.trade.job.service.InitService;
import org.springframework.stereotype.Component;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/27 11:17
 */
@Component
public class InitServiceImpl implements InitService {


    @Override
    public void init() {
        //持仓数据通道初始化

        //条件委托id初始化

        //持仓id初始化

        //合约下单币种初始化

        //合约杠杆初始化

        //合约-币种-杠杆映射初始化

        //持仓数据初始化

        //行情模块初始化

        //监听模块初始化

        //交易模块初始化
    }

    @Override
    public void flush() {

    }
}
