package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import me.kcra.acetylene.core.utils.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record TypedDescriptableMapping(String original, String descriptor,
                                       @Unmodifiable List<Pair<Identifier, Pair<String, String>>> mappings) implements Descriptable {
    private static final List<Integer> ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".chars().boxed().toList();

    @Override
    public @NotNull String mapped() {
        return mappings.stream().map(Pair::value).map(Pair::key).collect(Collectors.joining(","));
    }

    public @Nullable String mapped(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).map(Pair::key).orElse(null);
    }

    public boolean has(Identifier type) {
        return mappings.stream().anyMatch(e -> e.key().equals(type));
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

    public boolean isConstant(Identifier type) {
        Preconditions.checkArgument(has(type), "Invalid type");
        for (char c : Objects.requireNonNull(mapped(type)).toCharArray()) {
            if (ALPHABET.contains((int) c) && Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }
}
