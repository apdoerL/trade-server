package org.apdoer.trade.common.db.model.po;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(
    name = "web_agent_son"
)
@Data
@Builder
public class AgentSonPo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(
            name = "agent_user_id"
    )
    private Integer agentUserId;
    @Id
    @Column(
            name = "son_user_id"
    )
    private Integer sonUserId;
    @Column(
            name = "create_time"
    )
    private Date createTime;
}
