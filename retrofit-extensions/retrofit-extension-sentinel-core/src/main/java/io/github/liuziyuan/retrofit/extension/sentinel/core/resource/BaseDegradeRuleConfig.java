package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

public abstract class BaseDegradeRuleConfig {

    protected abstract CustomizeDegradeRuleBean generate();

    public CustomizeDegradeRuleBean build() {
        return this.generate();
    }
}
