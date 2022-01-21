package me.kcra.acetylene.core.utils;

import org.jetbrains.annotations.NotNull;

public interface PrioritizedIdentifier extends Identifier, Comparable<PrioritizedIdentifier> {
    int priority();

    @Override
    default int compareTo(PrioritizedIdentifier o) {
        return priority() - o.priority();
    }

    static PrioritizedIdentifier of(String name, int priority) {
        Preconditions.checkNotNull(name, "Name cannot be null");
        return (PrioritizedIdentifier) CACHE.computeIfAbsent(name + priority, key -> new PrioritizedIdentifier() {
            @Override
            public @NotNull String name() {
                return name;
            }

            @Override
            public int priority() {
                return priority;
            }
        });
    }
}
