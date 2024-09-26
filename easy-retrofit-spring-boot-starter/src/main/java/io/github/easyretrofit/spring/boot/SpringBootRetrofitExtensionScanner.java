package io.github.easyretrofit.spring.boot;

import io.github.easyretrofit.core.resource.ext.ExtensionPropertiesBean;
import io.github.easyretrofit.core.util.PropertiesFileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class SpringBootRetrofitExtensionScanner {

    private static final String RETROFIT_EXTENSION_PROPERTIES = "META-INF/retrofit-extension.properties";
    private static final String RETROFIT_EXTENSION_CLASS_NAME = "retrofit.extension.name";
    private static final String RETROFIT_EXTENSION_PACKAGE_NAME = "retrofit.resource.package";

    /**
     * Scan packageName
     *
     * @return
     * @throws IOException
     */
    @Deprecated
    public Set<String> scan() throws IOException {
        // 获取类路径下的所有META-INF/spring.factories文件
        Set<String> extensionNames = new HashSet<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:" + RETROFIT_EXTENSION_PROPERTIES;
        Resource[] resources = null;
        try {
            resources = resolver.getResources(pattern);
            // 遍历并打印每个文件的内容
            for (Resource resource : resources) {
                if (resource.exists() && resource.isReadable()) {
                    Set<String> propertiesKeys = PropertiesFileUtils.getPropertiesKeys(new InputStreamReader(resource.getInputStream()), RETROFIT_EXTENSION_CLASS_NAME);
                    extensionNames.addAll(propertiesKeys);
                }
            }
        } catch (IOException ignored) {
        }
        return extensionNames;
    }

    public Set<ExtensionPropertiesBean> scanExtensionProperties() throws IOException {
        Set<ExtensionPropertiesBean> extensionProperties = new HashSet<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:" + RETROFIT_EXTENSION_PROPERTIES;
        Resource[] resources = null;
        try {
            resources = resolver.getResources(pattern);
            // 遍历并打印每个文件的内容
            ExtensionPropertiesBean extensionPropertiesBean;
            for (Resource resource : resources) {
                extensionPropertiesBean = new ExtensionPropertiesBean();
                if (resource.exists() && resource.isReadable()) {
                    extensionPropertiesBean.setExtensionClassPaths(PropertiesFileUtils.getPropertiesKeys(new InputStreamReader(resource.getInputStream()), RETROFIT_EXTENSION_CLASS_NAME));
                    extensionPropertiesBean.setResourcePackages(PropertiesFileUtils.getPropertiesKeys(new InputStreamReader(resource.getInputStream()), RETROFIT_EXTENSION_PACKAGE_NAME));
                    extensionProperties.add(extensionPropertiesBean);
                }
            }
        } catch (IOException ignored) {
        }
        return extensionProperties;
    }

}
