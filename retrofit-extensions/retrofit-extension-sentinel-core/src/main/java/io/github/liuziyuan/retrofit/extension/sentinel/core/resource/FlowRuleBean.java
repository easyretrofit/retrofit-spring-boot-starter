package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FlowRuleBean extends CustomizeFlowRuleBean {
    private String resourceName;
}
