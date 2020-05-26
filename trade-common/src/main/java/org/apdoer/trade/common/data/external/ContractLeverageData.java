package org.apdoer.trade.common.data.external;

import org.apdoer.trade.common.db.model.po.CoreContractLeveragePo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContractLeverageData {
    private static Map<Integer, List<CoreContractLeveragePo>> CONTRACT_LEVERAGE_DATA = new HashMap<>();

    public static Map<Integer, List<CoreContractLeveragePo>> getAll() {
        return CONTRACT_LEVERAGE_DATA;
    }

    public static List<CoreContractLeveragePo> get(Integer contractId) {
        return CONTRACT_LEVERAGE_DATA.get(contractId);
    }

    public static void init(List<CoreContractLeveragePo> cfdContractLeveragePos) {
        CONTRACT_LEVERAGE_DATA = cfdContractLeveragePos.stream()
                .collect(Collectors.groupingBy(CoreContractLeveragePo::getContractId));
    }
}
