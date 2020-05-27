package org.apdoer.trade.core.handler;

import org.apdoer.trade.common.constants.CommonConstant;
import org.apdoer.trade.common.db.model.po.CoreContractOrderPo;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.model.vo.CoreContractOrderRespVo;
import org.apdoer.trade.common.model.vo.PosiListRespVo;
import org.apdoer.trade.common.utils.ModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author apdoer
 */
@Service
public class RedisCacheHandler {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplatePosi;


    /**
     * 新增持仓redis
     * @param userId
     * @param uuid
     * @param posi
     */
    public void hPutPosi(Integer userId, Long uuid, CoreContractPosiPo posi) {
        String key = CommonConstant.POSI_REDIS_PREFIX + userId;
        redisTemplatePosi.opsForHash().put(key, uuid.toString(), posi);
    }

    /**
     * 获取当前持仓列表
     * @param userId
     * @return
     */
    public List<PosiListRespVo> hEntriesPosi(Integer userId) {
        String key = CommonConstant.POSI_REDIS_PREFIX + userId;
        Map<Object, Object> resultMap = redisTemplatePosi.opsForHash().entries(key);

        List<PosiListRespVo> result = new ArrayList<>(resultMap.size());
        for (Map.Entry<Object, Object> entry : resultMap.entrySet()) {
            CoreContractPosiPo posiPo = (CoreContractPosiPo) entry.getValue();
            result.add(PosiListRespVo.convertFrom(posiPo));
        }
        result.sort(Comparator.comparing(PosiListRespVo::getCreateTime).reversed());
        return result;
    }

    /**
     * 移除指定持仓
     * @param userId
     * @param uuid
     */
    public void hDelete(Integer userId, Long uuid) {
        String key = CommonConstant.POSI_REDIS_PREFIX + userId;
        redisTemplatePosi.opsForHash().delete(key, uuid.toString());
    }

    /**
     * 条件单入-redis
     * @param userId
     * @param orderId
     * @param order
     */
    public void hPutConditionOrder(Integer userId, Long orderId, CoreContractOrderPo order) {
        String key = CommonConstant.ORDER_REDIS_PREFIX + userId;
        redisTemplatePosi.opsForHash().put(key, orderId.toString(), order);
    }
    
    /**
     * 条件单-删除redis
     * @param userId
     * @param uuid
     */
    public void hDeleteConditionOrder(Integer userId, Long uuid) {
        String key = CommonConstant.ORDER_REDIS_PREFIX + userId;
        redisTemplatePosi.opsForHash().delete(key, uuid.toString());
    }
    
    /**
     * 获取条件单列表
     * @param userId
     * @param contractId
     * @return
     */
    public List<CoreContractOrderRespVo> hEntriesConditionOrder(Integer userId, Integer contractId) {
        String key = CommonConstant.ORDER_REDIS_PREFIX + userId;
        Map<Object, Object> resultMap = redisTemplatePosi.opsForHash().entries(key);

        List<CoreContractOrderRespVo> result = new ArrayList<>(resultMap.size());
        for (Map.Entry<Object, Object> entry : resultMap.entrySet()) {
            CoreContractOrderPo order = (CoreContractOrderPo) entry.getValue();
        	if (null == contractId || contractId.intValue() == order.getContractId().intValue()) {
        		result.add(ModelBuilder.buildCfdContractOrderRespVo(order));
        	}
        }
        result.sort(Comparator.comparing(CoreContractOrderRespVo::getCreateTime).reversed());
        return result;
    }
}