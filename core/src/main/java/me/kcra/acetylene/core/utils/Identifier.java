package me.kcra.acetylene.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Identifier {
    Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    @NotNull
    String name();

    static Identifier of(String name) {
        return CACHE.computeIfAbsent(name, key -> () -> name);
    }
}
