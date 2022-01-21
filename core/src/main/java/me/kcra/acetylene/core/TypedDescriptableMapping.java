package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public record TypedDescriptableMapping(String original, String descriptor,
                                       List<Pair<String, String>> mappings) implements Descriptable {
    @Override
    public @NotNull String mapped() {
        return mappings.stream().map(Pair::key).collect(Collectors.joining(","));
    }

    public @Nullable String mapped(int index) {
        return mappings.get(index).key();
    }

    @Override
    public @NotNull String mappedDescriptor() {
        return mappings.stream().map(Pair::value).collect(Collectors.joining(","));
    }

    public @Nullable String mappedDescriptor(int index) {
        return mappings.get(index).value();
    }

    public @Nullable Pair<String, String> mapping(int index) {
        return mappings.get(index);
    }
}
