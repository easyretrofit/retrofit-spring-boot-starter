package io.github.liuziyuan.retrofit.core.resource;

/**
 * @author liuziyuan
 */
public enum UrlStatus {

    /**
     * Non URL
     */
    NULL,

    /**
     * Use retrofitBuilder Default URL
     */
    DEFAULT_URL_ONLY,

    /**
     * User Dynamic URL
     */
    DYNAMIC_URL_ONLY,
    /**
     * Use all of Default and Dynamic Url
     */
    DEFAULT_DYNAMIC_ALL
}
