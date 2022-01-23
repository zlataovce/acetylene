package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.stream.Collectors;

public record TypedDescriptableMapping(String original, String descriptor,
                                       @Unmodifiable List<Pair<Identifier, Pair<String, String>>> mappings) implements Descriptable {
    @Override
    public @NotNull String mapped() {
        return mappings.stream().map(Pair::value).map(Pair::key).collect(Collectors.joining(","));
    }

    public @Nullable String mapped(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).map(Pair::key).orElse(null);
    }

    @Override
    public @NotNull String mappedDescriptor() {
        return mappings.stream().map(Pair::value).map(Pair::value).collect(Collectors.joining(","));
    }

    public @Nullable String mappedDescriptor(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).map(Pair::value).orElse(null);
    }

    public @Nullable Pair<String, String> mapping(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).orElse(null);
    }
}
