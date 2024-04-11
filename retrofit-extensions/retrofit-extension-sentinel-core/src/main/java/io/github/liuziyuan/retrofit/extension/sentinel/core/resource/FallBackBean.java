package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import io.github.liuziyuan.retrofit.extension.sentinel.core.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FallBackBean {
    private long id;
    private String type;
    private String defaultResourceName;
    private String resourceName;
    private String fallBackMethodName;
    private DegradeRuleBean degradeRuleBean;
    private FlowRuleBean flowRuleBean;

    public FallBackBean(long id, String resourceName, String fallBackMethodName, DegradeRuleBean degradeRuleBean) {
        this.id = id;
        this.resourceName = resourceName;
        this.fallBackMethodName = fallBackMethodName;
        this.degradeRuleBean = degradeRuleBean;
        this.type = AppConstants.DEGRADE_TYPE;
        this.defaultResourceName = degradeRuleBean.getDefaultResourceName();
    }

    public FallBackBean(long id, String resourceName, String fallBackMethodName, FlowRuleBean flowRuleBean) {
        this.id = id;
        this.resourceName = resourceName;
        this.fallBackMethodName = fallBackMethodName;
        this.flowRuleBean = flowRuleBean;
        this.type = AppConstants.FLOW_TYPE;
        this.defaultResourceName = flowRuleBean.getDefaultResourceName();
    }
}
