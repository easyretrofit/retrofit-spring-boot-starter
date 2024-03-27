package io.github.liuziyuan.retrofit.core;

public enum OverrideRule {


    /**
     * if GLOBAL_ONLY, use global resource only
     */
    GLOBAL_ONLY,
    /**
     * if GLOBAL_FIRST, use global resource first, if not found, use local resource
     */
    GLOBAL_FIRST,

    /**
     * if MERGE, merge global and local resource
     */
    MERGE,
    /**
     * if LOCAL_FIRST, use local resource first, if not found, use global resource
     */
    LOCAL_FIRST,

    /**
     * if LOCAL_ONLY, use local resource only
     */
    LOCAL_ONLY;
}
