package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class RetrofitSentinelDegradeRuleBean {

    private int grade = 0;
    private double count = 0;
    private int timeWindow = 0;
    private int minRequestAmount = 5;
    private double slowRatioThreshold = 1.0;
    private int statIntervalMs = 1000;
    private String limitApp = RuleConstant.LIMIT_APP_DEFAULT;
}
