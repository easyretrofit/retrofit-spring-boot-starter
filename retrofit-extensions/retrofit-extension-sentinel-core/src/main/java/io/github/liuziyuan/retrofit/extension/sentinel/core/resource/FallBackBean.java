package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FallBackBean {
    private String resourceName;
    private String fallBackMethodName;
    private Class<?> configClazz;
}
