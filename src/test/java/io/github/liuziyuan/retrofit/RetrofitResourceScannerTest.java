package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.demo.api.HelloApi;
import io.github.liuziyuan.retrofit.demo.api.HelloInheritApi;
import io.github.liuziyuan.retrofit.demo.api.TestApi;
import io.github.liuziyuan.retrofit.demo.api.TestInheritApi;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author liuziyuan
 * @date 1/14/2022 5:07 PM
 */
class RetrofitResourceScannerTest {

    private RetrofitResourceScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new RetrofitResourceScanner();
    }

    @Test
    void doScan() {
        final Set<Class<?>> classSet = scanner.doScan("io.github.liuziyuan.retrofit.demo.api");
        List<Class<?>> apiList = new ArrayList<>(Arrays.asList(HelloApi.class, HelloInheritApi.class, TestApi.class, TestInheritApi.class));
        Assert.assertTrue(classSet.size() >= apiList.size());

    }
}