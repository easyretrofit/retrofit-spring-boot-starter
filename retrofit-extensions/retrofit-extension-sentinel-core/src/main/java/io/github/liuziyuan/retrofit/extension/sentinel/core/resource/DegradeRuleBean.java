package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DegradeRuleBean extends CustomizeDegradeRuleBean {
    private int id;
    private String resourceName;
    private String defaultResourceName;
    private String fallBackMethodName;
    private Class<?> configClazz;
}
