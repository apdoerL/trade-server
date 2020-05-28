
package org.apdoer.trade.job.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@ComponentScan({
        "org.apdoer.trade.core",
        "org.apdoer.trade.monitor",
        "org.apdoer.trade.common",
        "org.apdoer.trade.quot",
        "org.apdoer.common.service"
})
@MapperScan(basePackages = {
        "org.apdoer.common.service.mapper",
        "org.apdoer.trade.common.db.mapper"
})
public class ServiceScanConfig {
}
