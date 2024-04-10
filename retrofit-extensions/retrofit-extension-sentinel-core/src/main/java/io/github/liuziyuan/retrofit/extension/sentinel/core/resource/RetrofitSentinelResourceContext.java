package io.github.liuziyuan.retrofit.extension.sentinel.core.resource;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter(AccessLevel.MODULE)
public final class RetrofitSentinelResourceContext {

    private HashMap<String, List<FallBackBean>> fallBackBeanMap = new HashMap<>();

    public List<FallBackBean> getFallBackBeans(String resourceName) {
        List<FallBackBean> fallBackBeans = fallBackBeanMap.get(resourceName);
        if (fallBackBeans == null) {
            fallBackBeans = new ArrayList<>();
        }
        return fallBackBeans;
    }

    public void addFallBackBean(String resourceName, FallBackBean fallBackBean) {
        List<FallBackBean> fallBackBeans = getFallBackBeans(resourceName);
        fallBackBeans.add(fallBackBean);
        fallBackBeanMap.put(resourceName, fallBackBeans);
    }

    public String getFallBackMethodName(String resourceName) {
        List<FallBackBean> fallBackBeans = getFallBackBeans(resourceName);
        return fallBackBeans.stream().map(FallBackBean::getFallBackMethodName).filter(StringUtils::isNotEmpty)
                .findFirst().orElse(null);
    }

    public void merge(RetrofitSentinelResourceContext otherContext) {

    }

    public void check() {
        fallBackBeanMap.forEach((resourceName, fallBackBeans) -> {
            long fallBackMethodCount = fallBackBeans.stream().filter(fallBackBean -> StringUtils.isNotEmpty(fallBackBean.getFallBackMethodName())).count();
            if (fallBackMethodCount > 1) {
                throw new RuntimeException("resourceName:" + resourceName + " has more than one fallBackMethodName");
            }
        });
    }

}
