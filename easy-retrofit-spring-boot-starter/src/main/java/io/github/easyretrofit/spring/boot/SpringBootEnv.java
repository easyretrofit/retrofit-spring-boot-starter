package io.github.easyretrofit.spring.boot;

import io.github.easyretrofit.core.EnvManager;
import org.springframework.core.env.Environment;

/**
 * 实现Core的Env接口，由于Core中依赖SpringBoot Environment类的resolveRequiredPlaceholders方法处理在resources配置文件中的参数，例如 base.url:http:localhost:8080,
 * 需要在core代码中解析${base.url}。
 * 在以后的多种框架中，都可以使用其他框架来这个接口
 */
public class SpringBootEnv implements EnvManager {
    private final Environment environment;

    public SpringBootEnv(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String resolveRequiredPlaceholders(String text) {
        return environment.resolveRequiredPlaceholders(text);
    }
}
