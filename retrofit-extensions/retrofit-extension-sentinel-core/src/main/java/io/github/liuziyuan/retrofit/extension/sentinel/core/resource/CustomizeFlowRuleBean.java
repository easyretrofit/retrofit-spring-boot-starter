package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static io.github.liuziyuan.retrofit.extension.sentinel.core.AppConstants.DEFAULT;

@Getter
@Setter
@EqualsAndHashCode
public class CustomizeFlowRuleBean {

    private int grade = DEFAULT;
    private double count = DEFAULT;
    private int strategy = DEFAULT;
    private int controlBehavior = DEFAULT;
    private int warmUpPeriodSec = DEFAULT;
    private int maxQueueingTimeMs = DEFAULT;
    private String limitApp = RuleConstant.LIMIT_APP_DEFAULT;


}
