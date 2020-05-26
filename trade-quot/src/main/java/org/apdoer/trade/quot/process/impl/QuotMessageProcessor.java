package org.apdoer.trade.quot.process.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.trade.common.event.payload.IndexPriceMessageProcessPayload;
import org.apdoer.trade.common.service.QuotConfigCenterService;
import org.apdoer.trade.quot.payload.IndexPriceMessageSourcePayload;
import org.apdoer.trade.quot.process.Processor;
import org.apdoer.trade.quot.process.assemble.Assembler;
import org.apdoer.trade.quot.process.assemble.impl.QuotMessageAssembler;
import org.apdoer.trade.quot.process.cleaner.Cleaner;
import org.apdoer.trade.quot.process.cleaner.impl.QuotMessageCleaner;
import org.apdoer.trade.quot.process.filter.Filter;
import org.apdoer.trade.quot.process.filter.impl.QuotRepetitionMessageFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/26 14:17
 */
@Slf4j
public class QuotMessageProcessor implements Processor<IndexPriceMessageSourcePayload, IndexPriceMessageProcessPayload> {
    //数据清洗器
    private List<Cleaner<IndexPriceMessageSourcePayload, IndexPriceMessageSourcePayload>> cleanerList;
    //数据过滤器
    private List<Filter<IndexPriceMessageSourcePayload, IndexPriceMessageSourcePayload>> filterList;

    private Assembler<IndexPriceMessageSourcePayload, IndexPriceMessageProcessPayload> assembler;

    public QuotMessageProcessor(QuotConfigCenterService quotConfigCenterService) {
        //清洗器初始化，后续加需修改：1
        this.cleanerList = new ArrayList<>(1);
        this.cleanerList.add(new QuotMessageCleaner());

        //过滤器添加
        this.filterList = new ArrayList<>(1);
        this.filterList.add(new QuotRepetitionMessageFilter());

        this.assembler = new QuotMessageAssembler(quotConfigCenterService);
    }

    @Override
    public IndexPriceMessageProcessPayload process(IndexPriceMessageSourcePayload source) {
        for (Cleaner<IndexPriceMessageSourcePayload, IndexPriceMessageSourcePayload> cleaner : this.cleanerList) {
            source = cleaner.clean(source);
        }
        for (Filter<IndexPriceMessageSourcePayload, IndexPriceMessageSourcePayload> filter : filterList) {
            source = filter.filter(source);
        }
        return this.assembler.assemble(source);
    }
}
