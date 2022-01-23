package me.kcra.acetylene.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A generic immutable identifier.
 */
public interface Identifier {
    /**
     * A cache for {@link Identifier} instances based on their names.
     */
    Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    /**
     * Gets the identifying name of this {@link Identifier}.
     *
     * @return the identifying name
     */
    @NotNull
    String name();

    /**
     * Retrieves an {@link Identifier} from the cache, creating it, if missing.
     *
     * @param name the identifying name
     * @return the identifier
     */
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
