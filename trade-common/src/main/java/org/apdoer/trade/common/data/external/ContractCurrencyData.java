package org.apdoer.trade.common.data.external;

import org.apdoer.common.service.util.BeanUtils;
import org.apdoer.trade.common.db.model.po.CoreContractLeveragePo;
import org.apdoer.trade.common.db.model.po.CoreContractOrderCurrencyPo;
import org.apdoer.trade.common.db.model.po.CoreContractPo;
import org.apdoer.trade.common.model.vo.ContractCurrencyRespVo;
import org.apdoer.trade.common.model.vo.ContractCurrencyWrapRespVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContractCurrencyData {
    private static List<ContractCurrencyRespVo> CONTRACT_CURRENCY_LIST = new ArrayList<>();

    public static List<ContractCurrencyRespVo> get() {
        return CONTRACT_CURRENCY_LIST;
    }

    public static void init() {
        List<ContractCurrencyRespVo> result = new ArrayList<>();

        Map<Integer, CoreContractPo> allContractMap = ContractData.getAll();
        Map<Integer, Map<Integer, CoreContractOrderCurrencyPo>> allCurrencyMap = OrderCurrencyData.getAll();
        Map<Integer, List<CoreContractLeveragePo>> allLeverageMap = ContractLeverageData.getAll();

        for (Map.Entry<Integer, CoreContractPo> contractEntry : allContractMap.entrySet()) {
            ContractCurrencyRespVo contractVo = BeanUtils.convert(contractEntry.getValue(), ContractCurrencyRespVo.class);

            // 初始化order_currency
            List<ContractCurrencyWrapRespVo> orderCurrencyList = new ArrayList<>();
            for (Map.Entry<Integer, Map<Integer, CoreContractOrderCurrencyPo>> currencyEntry : allCurrencyMap.entrySet()) {
                if (currencyEntry.getKey() == contractEntry.getKey().intValue()) {
                    for (Map.Entry<Integer, CoreContractOrderCurrencyPo> currencyValueEntry : currencyEntry.getValue().entrySet()) {
                        ContractCurrencyWrapRespVo currencyVo = BeanUtils.convert(currencyValueEntry.getValue(), ContractCurrencyWrapRespVo.class);
                        orderCurrencyList.add(currencyVo);
                    }
                }
            }
            contractVo.setOrderCurrencyList(orderCurrencyList);

            // 初始化leverage
            List<Integer> leverageList = new ArrayList<>();
            for (Map.Entry<Integer, List<CoreContractLeveragePo>> leverageEntry : allLeverageMap.entrySet()) {
                if (leverageEntry.getKey() == contractEntry.getKey().intValue()) {
                    for (CoreContractLeveragePo leverageValueEntry : leverageEntry.getValue()) {
                        leverageList.add(leverageValueEntry.getLeverage());
                    }
                }
            }
            contractVo.setLeverageList(leverageList);

            result.add(contractVo);
        }

        CONTRACT_CURRENCY_LIST = result;
    }
}
