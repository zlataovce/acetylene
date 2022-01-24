package me.kcra.acetylene.core.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

/**
 * A data holder holding two items, a key and a value.
 * <p>
 * {@link Pair} instances are immutable, their keys and values won't change (the state of the keys and values might).
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Pair<K, V> {
    /**
     * Retrieves the key of this {@link Pair}.
     *
     * @return the key
     */
    @JsonGetter
    @UnknownNullability
    K key();
    /**
     * Retrieves the value of this {@link Pair}.
     *
     * @return the value
     */
    @JsonGetter
    @UnknownNullability
    V value();

    /**
     * Creates a new {@link Pair} with a key and a value.
     *
     * @param key the key
     * @param value the value
     * @param <L> the key type
     * @param <R> the value type
     * @return the new pair
     */
    @JsonCreator
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
