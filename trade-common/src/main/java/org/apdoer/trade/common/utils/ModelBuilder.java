package org.apdoer.trade.common.utils;

import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.model.dto.PosiCloseSyncDto;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.apdoer.trade.common.model.vo.CoreContractOrderRespVo;

public class ModelBuilder {
	
	/**
	 * 构建-平仓数据同步-PosiCloseSyncDto
	 * @param closeType
	 * @param posiDataDto
	 * @param removePosi
	 * @return
	 */
    public static PosiCloseSyncDto buildPosiCloseSyncDto(Integer closeType, SlavePosiDataDto posiDataDto, CoreContractPosiPo removePosi) {
    	PosiCloseSyncDto syncDto = new PosiCloseSyncDto();
    	syncDto.setCloseType(closeType);
    	syncDto.setPosiDataDto(posiDataDto);
    	syncDto.setRemovePosi(removePosi);
    	return syncDto;
    }
    
    /**
     * 构建条件单
     * @param orderPo
     * @return
     */
    public static CoreContractOrderRespVo buildCfdContractOrderRespVo(CoreContractOrderPo orderPo) {
		CoreContractOrderRespVo respVo = new CoreContractOrderRespVo();
    	respVo.setOrderId(orderPo.getOrderId().toString());
    	if (null != orderPo.getPosiId()) {
    		respVo.setPosiId(orderPo.getPosiId().toString());
    	}
    	respVo.setUserId(orderPo.getUserId());
    	respVo.setContractId(orderPo.getContractId());
    	respVo.setCurrencyId(orderPo.getCurrencyId());
    	respVo.setPosiSide(orderPo.getPosiSide());
    	respVo.setOpenAmt(orderPo.getOpenAmt());
    	respVo.setLeverage(orderPo.getLeverage());
    	respVo.setTriggerPrice(orderPo.getTriggerPrice());
    	respVo.setRealTriggerPrice(orderPo.getRealTriggerPrice());
    	respVo.setStopProfitPrice(orderPo.getStopProfitPrice());
    	respVo.setStopLossPrice(orderPo.getStopLossPrice());
    	respVo.setStatus(orderPo.getStatus());
    	respVo.setCreateTime(orderPo.getCreateTime());
    	respVo.setUpdateTime(orderPo.getUpdateTime());
    	return respVo;
    }

}
