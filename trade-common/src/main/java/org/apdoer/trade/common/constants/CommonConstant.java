package org.apdoer.trade.common.constants;


/**
 * @author apdoer
 */
public class CommonConstant {

    private CommonConstant() {
    }

    /*持仓表前缀*/
    public static final String POSI_TABLE_NAME = "core_contract_posi_";
    /*历史持仓表前缀*/
    public static final String HIS_POSI_TABLE_NAME = "core_contract_posi_his_";
    /*条件委托表前缀*/
    public static final String ORDER_TABLE_NAME = "core_contract_order_";

    /*持仓redis前缀，规则 p_{contractId}_{userId}*/
    public static final String POSI_REDIS_PREFIX = "p_";
    
    /*条件redis前缀，规则 o_{userId}*/
    public static final String ORDER_REDIS_PREFIX = "o_";
    
	//开仓-数据同步内部通道名称
    public static final String POSI_OPEN_CHANNEL = "posi_open_channel";
	//开仓-强平从数据监听器
    public static final String SLAVE_POSI_OPEN_SYNC_LISTENER = "slave_posi_open_sync_listener";
	//开仓-止盈从数据监听器
    public static final String PROFIT_POSI_OPEN_SYNC_LISTENER = "profit_posi_open_sync_listener";
	//开仓-止损从数据监听器
    public static final String LOSS_POSI_OPEN_SYNC_LISTENER = "loss_posi_open_sync_listener";
    
	//平仓-数据同步内部通道名称
    public static final String POSI_CLOSE_CHANNEL = "posi_close_channel";
	//平仓-强平从数据监听器
    public static final String SLAVE_POSI_CLOSE_SYNC_LISTENER = "slave_posi_close_sync_listener";
	//平仓-止盈从数据监听器
    public static final String PROFIT_POSI_CLOSE_SYNC_LISTENER = "profit_posi_close_sync_listener";
	//平仓-止损从数据监听器
    public static final String LOSS_POSI_CLOSE_SYNC_LISTENER = "loss_posi_close_sync_listener";
}
