package me.kcra.acetylene.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Identifier {
    Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    @NotNull
    String name();

    static Identifier of(@NotNull String name) {
        Preconditions.checkNotNull(name, "Name must not be null");
        return CACHE.computeIfAbsent(name, key -> new Identifier() {
            @Override
            public @NotNull String name() {
                return name;
            }

            @Override
            public String toString() {
                return name;
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof final Identifier id)) {
                    return false;
                }
                return name.equals(id.name());
            }
        });
    }
}
