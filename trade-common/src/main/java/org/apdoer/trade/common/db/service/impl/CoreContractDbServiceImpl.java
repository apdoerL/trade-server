package org.apdoer.trade.common.db.service.impl;

import org.apdoer.trade.common.constants.CommonConstant;
import org.apdoer.trade.common.db.mapper.*;
import org.apdoer.trade.common.db.model.po.*;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.utils.TradeDateUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CoreContractDbServiceImpl implements CoreContractDbService {

    @Resource
    private CoreContractMapper coreContractMapper;

    @Resource
    private ContractPosiMapper contractPosiMapper;

    @Resource
    private CoreContractOrderCurrencyMapper coreContractOrderCurrencyMapper;

    @Resource
    private CoreContractLeverMapper coreContractLeverMapper;
    
    @Resource
    private CoreContractOrderMapper coreContractOrderMapper;

    @Override
    public Integer getMaxContractId() {
        return coreContractMapper.getMaxContractId();
    }

    @Override
    public List<CoreContractPo> getAllContract() {
        return coreContractMapper.selectAll();
    }

    @Override
    public List<CoreContractOrderCurrencyPo> getAllOrderCurrency() {
        return coreContractOrderCurrencyMapper.selectAll();
    }

    @Override
    public List<CoreContractLeveragePo> getAllContractLeverage() {
        return coreContractLeverMapper.selectAll();
    }

    @Override
    public List<CoreContractPosiPo> getUnClosePosi(int userMod) {
        return contractPosiMapper.getUnClosePosi(CommonConstant.POSI_TABLE_NAME + userMod);
    }

    @Override
    public int updatePosi(CoreContractPosiPo posiPo) {
        posiPo.setTableName(CommonConstant.POSI_TABLE_NAME + posiPo.getUserId() % 10);
        return contractPosiMapper.updateByPrimaryKeySelective(posiPo);
    }

    @Override
    public int insertPosi(CoreContractPosiPo posiPo) {
        posiPo.setTableName(CommonConstant.POSI_TABLE_NAME + posiPo.getUserId() % 10);
        return contractPosiMapper.insertSelective(posiPo);
    }

    @Override
    public int insertPosiHistory(CoreContractPosiPo posiPo) {
        posiPo.setTableName(CommonConstant.HIS_POSI_TABLE_NAME + TradeDateUtil.formatMonth(posiPo.getCloseTime()));
        return contractPosiMapper.insertSelective(posiPo);
    }

    @Override
    public List<CoreContractPosiPo> historyOrderList(Integer userId, Long queryDate) {
        Example example = new Example(CoreContractPosiPo.class);
        example.setTableName(CommonConstant.HIS_POSI_TABLE_NAME + TradeDateUtil.formatMonth(queryDate));
        example.createCriteria()
                .andEqualTo("userId", userId);
        example.orderBy("closeTime").desc();
        return contractPosiMapper.selectByExample(example);
    }

	@Override
	public int insertOrder(CoreContractOrderPo orderPo) {
		orderPo.setTableName(CommonConstant.ORDER_TABLE_NAME + orderPo.getUserId() % 10);
        return this.coreContractOrderMapper.insertSelective(orderPo);
	}

	@Override
	public int updateOrder(CoreContractOrderPo orderPo) {
		orderPo.setTableName(CommonConstant.ORDER_TABLE_NAME + orderPo.getUserId() % 10);
		return this.coreContractOrderMapper.updateByPrimaryKeySelective(orderPo);
	}

	@Override
	public CoreContractOrderPo queryOrder(Long orderId, Integer userId, Integer status) {
		String tablename = CommonConstant.ORDER_TABLE_NAME + userId % 10;
		return this.coreContractOrderMapper.queryOrder(userId, orderId, status, tablename);
	}
	
	@Override
	public List<CoreContractOrderPo> queryHistoryOrder(Integer userId, Integer contractId, Integer side, Integer status) {
		String tablename = CommonConstant.ORDER_TABLE_NAME + userId % 10;
		return this.coreContractOrderMapper.queryHistory(tablename, userId, contractId, side, status);
	}

	@Override
	public List<CoreContractOrderPo> getUnTriggerOrder(int userMod) {
		String tablename = CommonConstant.ORDER_TABLE_NAME + userMod;
		return this.coreContractOrderMapper.getUnTriggerOrder(tablename);
	}

	@Override
	public List<CoreContractOrderPo> queryUserUnTriggerOrder(Integer userId, Integer contractId, Integer status) {
		String tablename = CommonConstant.ORDER_TABLE_NAME + userId % 10;
		return this.coreContractOrderMapper.queryUserUnTriggerOrder(userId, contractId, tablename);
	}

}
