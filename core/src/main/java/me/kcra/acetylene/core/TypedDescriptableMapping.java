package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Collectors;

public record TypedDescriptableMapping(String original, String descriptor, Map<Identifier, Pair<String, String>> mappings) implements Descriptable {
    @Override
    public @NotNull String mapped() {
        return mappings.values().stream().map(Pair::key).collect(Collectors.joining(","));
    }

    public @Nullable String mapped(Identifier type) {
        return mappings.get(type).key();
    }

    @Override
    public @NotNull String mappedDescriptor() {
        return mappings.values().stream().map(Pair::value).collect(Collectors.joining(","));
    }

    public @Nullable String mappedDescriptor(Identifier type) {
        return mappings.get(type).value();
    }

    public @Nullable Pair<String, String> mapping(Identifier type) {
        return mappings.get(type);
    }
}
