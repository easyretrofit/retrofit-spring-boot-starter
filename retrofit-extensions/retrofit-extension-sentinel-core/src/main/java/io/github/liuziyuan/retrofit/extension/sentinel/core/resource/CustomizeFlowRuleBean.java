package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CustomizeFlowRuleBean {

    private int grade = 1;
    private double count = 0;
    private int strategy = 0;
    private int controlBehavior = 0;
    private int warmUpPeriodSec = 10;
    private int maxQueueingTimeMs = 500;
    private String limitApp = RuleConstant.LIMIT_APP_DEFAULT;


}
