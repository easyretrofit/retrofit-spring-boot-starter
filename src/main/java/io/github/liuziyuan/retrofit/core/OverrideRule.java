package io.github.liuziyuan.retrofit.core;

public enum OverrideRule {

    /**
     * if GLOBAL_FIRST, use global resource first, if not found, use local resource
     */
    GLOBAL_FIRST,
    /**
     * if LOCAL_FIRST, use local resource first, if not found, use global resource
     */
    LOCAL_FIRST,
}
