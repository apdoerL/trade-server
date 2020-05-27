package org.apdoer.trade.job.controller;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.trade.core.service.TradeCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author apdoer
 */
@RestController
@RequestMapping("/cfd/trade/common")
public class CommonController {

    @Autowired
    private TradeCommonService tradeCommonService;


    @GetMapping("/contractList")
    public ResultVo getContractList() {
        return tradeCommonService.getContractList();
    }
}
