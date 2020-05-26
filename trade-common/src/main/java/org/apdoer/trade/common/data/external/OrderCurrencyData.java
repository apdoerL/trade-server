package org.apdoer.trade.common.data.external;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.db.model.po.CoreContractOrderCurrencyPo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OrderCurrencyData {

    private static Map<Integer, Map<Integer, CoreContractOrderCurrencyPo>> ORDER_CURRENCY_DATA = new HashMap<>();

    public static void add(CoreContractOrderCurrencyPo orderCurrencyPo) {
        // todo 动态加载
    }

    public static CoreContractOrderCurrencyPo get(Integer contractId, Integer currencyId) {
        Map<Integer, CoreContractOrderCurrencyPo> specialContractMap = ORDER_CURRENCY_DATA.get(contractId);
        if (null != specialContractMap) {
            CoreContractOrderCurrencyPo specialCurrency = specialContractMap.get(currencyId);
            if (null != specialCurrency) {
                return specialCurrency;
            }
        }
        log.error("not found cfd_contract_order_currency. contractId[{}] currencyId[{}]", contractId, currencyId);
        return null;
    }

    public static Map<Integer, Map<Integer, CoreContractOrderCurrencyPo>> getAll() {
        return ORDER_CURRENCY_DATA;
    }

    public static void init(List<CoreContractOrderCurrencyPo> orderCurrencyPoList) {
        // 先按{contract_id}分组
        Map<Integer, List<CoreContractOrderCurrencyPo>> collect = orderCurrencyPoList.stream()
                .collect(Collectors.groupingBy(CoreContractOrderCurrencyPo::getContractId));

        for (Map.Entry<Integer, List<CoreContractOrderCurrencyPo>> entry : collect.entrySet()) {
            // 再按{currency_id}分组
            Map<Integer, CoreContractOrderCurrencyPo> currencyMap = new HashMap<>(entry.getValue().size());
            for (CoreContractOrderCurrencyPo orderCurrencyPo : entry.getValue()) {
                currencyMap.put(orderCurrencyPo.getCurrencyId(), orderCurrencyPo);
            }
            ORDER_CURRENCY_DATA.put(entry.getKey(), currencyMap);
        }
    }
}
