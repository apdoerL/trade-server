package org.apdoer.trade.job.controller;


import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.trade.job.handler.FlushHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 动态刷新参数
 * @author apdoer
 */
@RestController
@RequestMapping("/trade/flush")
public class FlushController {
	
	@Autowired
	private FlushHandler flushHandler;
	
	
	/**
	 * 交易参数-刷新
	 */
	@RequestMapping("/tradeParam")
	public ResultVo tradeParamFlush() {
		return this.flushHandler.tradeParamFlushHandle();
	}
	
	/**
	 * 内部通道初始化
	 * @return
	 */
	@RequestMapping("/systemChannel")
	public ResultVo channelFlush() {
		return this.channelFlush();
	}
}
