package me.kcra.acetylene.core;

import me.kcra.acetylene.core.utils.Identifier;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record TypedClassMapping(String original, List<Pair<Identifier, String>> mappings,
                                List<TypedDescriptableMapping> fields,
                                List<TypedDescriptableMapping> methods) implements Mappable {
    @Override
    public @NotNull String mapped() {
        return String.join(",", mappings.stream().map(Pair::value).toList());
    }

    public @Nullable String mapped(Identifier type) {
        return mappings.stream().filter(e -> e.key().equals(type)).findFirst().map(Pair::value).orElse(null);
    }

    public @Nullable TypedDescriptableMapping field(String original) {
        return fields.stream().filter(e -> e.original().equals(original)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedField(String mapped) {
        return fields.stream().filter(e -> e.mappings().stream().anyMatch(p -> p.value().key().equals(mapped))).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedField(String... mapped) {
        return mappedField(Arrays.asList(mapped));
    }

    public @Nullable TypedDescriptableMapping mappedField(List<String> mapped) {
        return fields.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::key).toList(), mapped)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping method(String original, String descriptor) {
        return methods.stream().filter(e -> e.original().equals(original) && e.descriptor().equals(descriptor)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedMethod(String mapped, String mappedDescriptor) {
        return mappedMethod(Pair.of(mapped, mappedDescriptor));
    }

    public @Nullable TypedDescriptableMapping mappedMethod(Pair<String, String> mapped) {
        return methods.stream().filter(e -> e.mappings().contains(mapped)).findFirst().orElse(null);
    }

    public @Nullable TypedDescriptableMapping mappedMethod(List<Pair<String, String>> mapped) {
        return methods.stream().filter(e -> !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), mapped)).findFirst().orElse(null);
    }
}
