package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

public abstract class BaseFlowRuleConfig {

    abstract CustomizeFlowRuleBean generate();

    public CustomizeFlowRuleBean build() {
        return this.generate();
    }
}
