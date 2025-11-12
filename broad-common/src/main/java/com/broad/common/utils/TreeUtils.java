package com.broad.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class TreeUtils {
    private TreeUtils() {}

    public static <T, ID> List<T> build(List<T> nodes,
                                        Function<T, ID> idGetter,
                                        Function<T, ID> parentIdGetter,
                                        BiConsumer<T, List<T>> childrenSetter,
                                        ID rootParentId) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }
        Map<ID, List<T>> bucket = new HashMap<>();
        List<T> roots = new ArrayList<>();
        for (T n : nodes) {
            ID pid = parentIdGetter.apply(n);
            bucket.computeIfAbsent(pid, k -> new ArrayList<>()).add(n);
        }
        for (T n : nodes) {
            ID id = idGetter.apply(n);
            List<T> children = bucket.get(id);
            if (children == null) {
                children = new ArrayList<>();
            }
            childrenSetter.accept(n, children);
        }
        List<T> rootBucket = bucket.get(rootParentId);
        if (rootBucket != null) {
            roots.addAll(rootBucket);
        }
        return roots;
    }
}
