package org.apdoer.trade.common.data.external;

import org.apdoer.trade.common.db.model.po.CoreContractPo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractData {

    private static Map<Integer, CoreContractPo> CONTRACT_MAP = new HashMap<>();

    public static void add(CoreContractPo contractPo) {
        CONTRACT_MAP.put(contractPo.getContractId(), contractPo);
    }

    public static CoreContractPo get(Integer contractId) {
        return CONTRACT_MAP.get(contractId);
    }

    public static Map<Integer, CoreContractPo> getAll() {
        return CONTRACT_MAP;
    }

    public static void init(List<CoreContractPo> contractList) {
        for (CoreContractPo contractPo : contractList) {
            CONTRACT_MAP.put(contractPo.getContractId(), contractPo);
        }
    }
}
