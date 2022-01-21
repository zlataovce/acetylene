package me.kcra.acetylene.core.utils;

public interface PrioritizedIdentifier extends Identifier, Comparable<PrioritizedIdentifier> {
    int priority();

    @Override
    default int compareTo(PrioritizedIdentifier o) {
        return priority() - o.priority();
    }

    static PrioritizedIdentifier of(String name, int priority) {
        return (PrioritizedIdentifier) CACHE.computeIfAbsent(name + priority, key -> new PrioritizedIdentifier() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public int priority() {
                return priority;
            }
        });
    }
}
