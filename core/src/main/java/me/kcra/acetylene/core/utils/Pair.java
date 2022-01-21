package me.kcra.acetylene.core.utils;

import java.util.Objects;

public interface Pair<K, V> {
    K key();
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
                return key.equals(pair.key()) && value.equals(pair.value());
            }

            @Override
            public String toString() {
                return key + "=" + value;
            }
        };
    }
}
