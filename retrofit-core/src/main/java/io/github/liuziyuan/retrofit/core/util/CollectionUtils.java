package io.github.liuziyuan.retrofit.core.util;

import javax.annotation.Nullable;
import java.util.Collection;

public class CollectionUtils {

    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}
