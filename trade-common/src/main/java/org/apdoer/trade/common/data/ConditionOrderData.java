package org.apdoer.trade.common.data;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.data.base.BaseConditionOrderData;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.enums.PosiSideEnum;
import org.apdoer.trade.common.enums.SlaveDataSortTypeEnum;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * 条件单数据区
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 18:35
 */
@Slf4j
public class ConditionOrderData {
    // 从持仓数据存储结构，以{cid}-1作为list下标， List<Map<{_price},Map<{uuid},data>>>
    static BaseConditionOrderData BUY_ORDER  = new BaseConditionOrderData(SlaveDataSortTypeEnum.DESC);
    static BaseConditionOrderData SELL_ORDER = new BaseConditionOrderData(SlaveDataSortTypeEnum.ASC);


    public static void init(int fixedCount, int initCapacity, CoreContractDbService coreContractDbService) throws Exception {
        log.info("==== condition order init start");
        orderMemeorySpeceInit(initCapacity);
        orderDataInit(fixedCount,coreContractDbService);
        log.info("==== condition order init end");
    }

    private static void orderMemeorySpeceInit(int initCapacity) {
        for (int i = 0; i < initCapacity; i++) {
            //申请从持仓空间 看涨,倒序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> emptySlaveBuy = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
            BUY_ORDER.init(emptySlaveBuy);
            // 看跌：正序
            ConcurrentSkipListMap<BigDecimal, ConcurrentHashMap<Long, CoreContractOrderPo>> emptySlaveSell = new ConcurrentSkipListMap<>();
            SELL_ORDER.init(emptySlaveSell);
        }
    }

    private static void orderDataInit(int fixedCount, CoreContractDbService coreContractDbService) throws Exception {
        CountDownLatch cdl = new CountDownLatch(fixedCount);
        ExecutorService executor = Executors.newFixedThreadPool(fixedCount);

        for (int i = 0; i < fixedCount; i ++) {
            executor.execute(new OrderInitRunnable(coreContractDbService, cdl, i));
        }
        cdl.await();
    }

    /**
     * 仅添加时调用
     *
     * @param orderPo
     */
    public static void add(CoreContractOrderPo orderPo) {
        BaseConditionOrderData orderData = orderPo.getPosiSide() == PosiSideEnum.BUY.getCode() ? BUY_ORDER : SELL_ORDER;
        orderData.add(orderPo);
    }

    /**
     * 仅撤单时，调用
     * @param orderPo
     */
    public static void remove(CoreContractOrderPo orderPo) {
        BaseConditionOrderData orderData = orderPo.getPosiSide() == PosiSideEnum.BUY.getCode() ? BUY_ORDER : SELL_ORDER;
        orderData.remove(orderPo);
    }

    @Slf4j
    private static class OrderInitRunnable implements Runnable {

        private CoreContractDbService coreContractDbService;
        private CountDownLatch cdl;
        private int userMod;

        OrderInitRunnable(CoreContractDbService coreContractDbService, CountDownLatch cdl, int userMod) {
            this.coreContractDbService = coreContractDbService;
            this.cdl = cdl;
            this.userMod = userMod;
        }

        @Override
        public void run() {
            log.info("==== condition order userMod={} init start", this.userMod);
            int loadSize = 0;
            try {
                List<CoreContractOrderPo> list = this.coreContractDbService.getUnTriggerOrder(this.userMod);
                if (null != list && list.size() != 0) {
                    loadSize = list.size();
                    for (CoreContractOrderPo orderPo : list) {
                        ConditionOrderData.add(orderPo);
                    }
                }
            } catch (Exception e) {
                log.error("condition order init error userMod={}", this.userMod, e);
            } finally {
                if (null != this.cdl) {
                    cdl.countDown();
                }
                log.info("==== condition order userMod={} init end, loadSize={}", this.userMod, loadSize);
            }
        }

    }
}
