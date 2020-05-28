package org.apdoer.trade.common.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.config.MasterDataInitThreadPool;
import org.apdoer.trade.common.constants.CommonConstant;
import org.apdoer.trade.common.data.base.BaseMasterPosiData;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.db.service.CoreContractDbService;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.eventbus.EventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.trade.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.trade.common.model.dto.SlavePosiDataDto;
import org.apdoer.trade.common.utils.ModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 主持仓数据
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 17:31
 */
@Slf4j
@Component
public class MasterPosiData {

    /**
     * 数据区按用户分片
     */
    private static final int FIXED_COUNT = 10;

    private static BaseMasterPosiData POSI_0 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_1 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_2 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_3 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_4 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_5 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_6 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_7 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_8 = new BaseMasterPosiData();
    private static BaseMasterPosiData POSI_9 = new BaseMasterPosiData();


    /**
     * 预留容量
     */
    @Value("${trade-server.reserve-capacity:10}")
    public int reserveCapacity;

    @Autowired
    private CoreContractDbService coreContractDbService;

    public static ResultVo add(CoreContractPosiPo posiPo) {
        //根据用户路由
        BaseMasterPosiData baseMasterPosiData = route(posiPo.getUserId());
        if (null == baseMasterPosiData) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(), ExceptionCode.USER_ID_ERROR.getName());
        }
        try {
            //新增主持仓区域
            baseMasterPosiData.add(posiPo);
            //从数据区的同步通过eventbus
            GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(CommonConstant.POSI_OPEN_CHANNEL);
            if (eventBus != null) {
                while (eventBus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    //反压
                }
                eventBus.publish(new SourceEvent(posiPo));
            }
            return ResultVoBuildUtils.buildSuccessResultVo();
        } catch (Exception e) {
            log.error("add posi_data error.", e);
            return ResultVoBuildUtils.buildFaildResultVo();
        }
    }

    public static ResultVo remove(SlavePosiDataDto posiDataDto, Integer closeType) {
        // 根据用户id路由
        BaseMasterPosiData baseMasterPosiData = route(posiDataDto.getUserId());
        if (null == baseMasterPosiData) {
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(), ExceptionCode.USER_ID_ERROR.getName());
        }
        try {
            //主持仓区移除
            CoreContractPosiPo removePosi = baseMasterPosiData.remove(posiDataDto);
            if (removePosi == null) {
                //若已经被强平
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.POSI_NOT_EXIST.getCode(), ExceptionCode.POSI_NOT_EXIST.getName());
            } else {
                posiDataDto.setPosiSide(removePosi.getPosiSide());
            }
            GuavaEventBus eventBus = (GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(CommonConstant.POSI_OPEN_CHANNEL);
            if (eventBus != null) {
                while (eventBus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                    //反压
                }
                eventBus.publish(new SourceEvent(ModelBuilder.buildPosiCloseSyncDto(closeType, posiDataDto, removePosi)));
            }
            return ResultVoBuildUtils.buildSuccessResultVo(removePosi);
        } catch (Exception e) {
            log.error("remove posi_data error.", e);
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.CLOSE_POSI_FAILED.getCode(), ExceptionCode.CLOSE_POSI_FAILED.getName());
        }
    }

    public static CoreContractPosiPo get(Integer contractId, Integer userId, Long uuid) {
        // 根据用户id路由
        BaseMasterPosiData baseMasterPosiData = route(userId);
        if (null == baseMasterPosiData) {
            return null;
        }
        return baseMasterPosiData.get(contractId, userId, uuid);
    }


    public void init() throws Exception {
        log.info(">>> init posi start.");
        int initialCapacity = coreContractDbService.getMaxContractId() + reserveCapacity;
        CountDownLatch cdl = new CountDownLatch(FIXED_COUNT);
        //止损数据区初始化
        StopLossSlavePosiData.init(initialCapacity);
        //止盈数据区初始化
        StopProfitSlavePosiData.init(initialCapacity);

        //强平数据区初始化
        SlavePosiData.init(initialCapacity);

        for (int i = 0; i < FIXED_COUNT; i++) {
            MasterDataInitThreadPool.getInstance().excute(new PosiInitRunnable(cdl, initialCapacity, i, coreContractDbService, route(i)
            ));
        }
        cdl.await();
        MasterDataInitThreadPool.getInstance().shutdown();
        //条件委托初始化
        ConditionOrderData.init(FIXED_COUNT, initialCapacity, coreContractDbService);
        log.info("<<< init posi end.");
    }

    @Slf4j
    private static class PosiInitRunnable implements Runnable {

        private CountDownLatch cdl;
        private int initCapacity;
        private int userMod;
        private CoreContractDbService coreContractDbService;
        private BaseMasterPosiData masterPosi;

        public PosiInitRunnable(CountDownLatch cdl,
                                int initCapacity,
                                int userMod,
                                CoreContractDbService coreContractDbService,
                                BaseMasterPosiData masterPosi) {
            this.cdl = cdl;
            this.initCapacity = initCapacity;
            this.userMod = userMod;
            this.coreContractDbService = coreContractDbService;
            this.masterPosi = masterPosi;
        }

        @Override
        public void run() {
            try {
                // 开辟出所有合约的空间，initCapacity=合约数+预留数
                for (int i = 0; i < initCapacity; i++) {
                    masterPosi.init();
                }

                // 查询未平仓数据
                List<CoreContractPosiPo> unClosePosiList = coreContractDbService.getUnClosePosi(userMod);
                // 初始化持仓数据
                if (CollectionUtils.isNotEmpty(unClosePosiList)) {
                    for (CoreContractPosiPo posiPo : unClosePosiList) {
                        MasterPosiData.add(posiPo);
                    }
                }
            } catch (Exception e) {
                log.error("init posi error.", e);
            } finally {
                cdl.countDown();
            }
        }
    }


    private static BaseMasterPosiData route(Integer userId) {
        int mod = userId % 10;
        switch (mod) {
            case 0:
                return POSI_0;
            case 1:
                return POSI_1;
            case 2:
                return POSI_2;
            case 3:
                return POSI_3;
            case 4:
                return POSI_4;
            case 5:
                return POSI_5;
            case 6:
                return POSI_6;
            case 7:
                return POSI_7;
            case 8:
                return POSI_8;
            case 9:
                return POSI_9;
            default:
                log.error("not route master_posi_data. user_id[{}]", userId);
                return null;
        }
    }
}
