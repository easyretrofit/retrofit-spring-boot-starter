package io.github.liuziyuan.retrofit;

import org.springframework.core.env.Environment;

public class SpringBootEnv implements Env {
    private final Environment environment;

    public SpringBootEnv(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String resolveRequiredPlaceholders(String text) {
        return environment.resolveRequiredPlaceholders(text);
    }
}
