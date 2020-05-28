package org.apdoer.trade.common.event.listener.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apdoer.trade.common.db.model.po.CoreContractPosiPo;
import org.apdoer.trade.common.event.SourceEvent;
import org.apdoer.trade.common.event.listener.SourceEventListener;
import org.apdoer.trade.common.handler.PosiOpenSyncHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 开仓数据监听器
 * @author apdoer
 */
@Service("posiOpenSourceListener")
@Scope("prototype")
public class PosiOpenSourceListener implements SourceEventListener {
	
	private PosiOpenSyncHandler handler;
	
	
	// 已监听的内部数据通道名称
	private Set<String> subscribeChannels = new HashSet<>();

	@Override
	@Subscribe
	@AllowConcurrentEvents
	public void listen(SourceEvent event) {
		if (null != event && null != event.getData()) {
			Object data = event.getData();
			if (data instanceof CoreContractPosiPo) {
				this.handler.handle((CoreContractPosiPo)data);
			}
		}
	}
	
	@Override
	public Set<String> getSubscribeSystemChannels() {
		return this.subscribeChannels;
	}

	@Override
	public boolean isSubscribeSystemChannel(String channel) {
		return this.subscribeChannels.contains(channel);
	}

	@Override
	public void subScribeSystemChannel(String channel) {
		if (!this.isSubscribeSystemChannel(channel)) {
			this.subscribeChannels.add(channel);
		}
	}

	public void setHandler(PosiOpenSyncHandler handler) {
		this.handler = handler;
	}
}
