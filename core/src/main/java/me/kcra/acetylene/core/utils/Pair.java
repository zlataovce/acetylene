package me.kcra.acetylene.core.utils;

import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public interface Pair<K, V> {
    @UnknownNullability
    K key();
    @UnknownNullability
    V value();

    static <L, R> Pair<L, R> of(L key, R value) {
        return new Pair<>() {
            @Override
            public L key() {
                return key;
            }

            @Override
            public R value() {
                return value;
            }

            @Override
            public int hashCode() {
                return Objects.hash(key, value);
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof final Pair<?, ?> pair)) {
                    return false;
                }
                return Objects.equals(key, pair.key()) && Objects.equals(value, pair.value());
            }

            @Override
            public String toString() {
                return key + "=" + value;
            }
        };
    }
}
