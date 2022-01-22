package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record DescriptableAncestorTree(List<TypedDescriptableMapping> descriptables) {
    public static DescriptableAncestorTree of(TypedDescriptableMapping refDescr, boolean ignoreDescriptors, TypedDescriptableMapping[]... descs) {
        return of(refDescr, ignoreDescriptors, Arrays.asList(descs));
    }

    public static DescriptableAncestorTree of(TypedDescriptableMapping refDescr, boolean ignoreDescriptors, List<TypedDescriptableMapping[]> descs) {
        final List<TypedDescriptableMapping> mappings = new ArrayList<>();
        mappings.add(refDescr);
        final AtomicReference<List<Pair<String, String>>> currentMappings = new AtomicReference<>(refDescr.mappings());
        for (TypedDescriptableMapping[] mappings1 : descs) {
            final TypedDescriptableMapping result = Arrays.stream(mappings1)
                    .filter(e -> {
                        if (ignoreDescriptors) {
                            return !Collections.disjoint(
                                    e.mappings().stream().map(Pair::key).toList(),
                                    currentMappings.get().stream().map(Pair::key).toList()
                            );
                        }
                        return !Collections.disjoint(e.mappings(), currentMappings.get());
                    })
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new DescriptableAncestorTree(Collections.unmodifiableList(mappings));
            }
            currentMappings.set(result.mappings());
            mappings.add(result);
        }
        return new DescriptableAncestorTree(Collections.unmodifiableList(mappings));
    }

    public @Nullable TypedDescriptableMapping mapping(int index) {
        if (index >= size()) {
            return null;
        }
        return descriptables.get(index);
    }

    public int size() {
        return descriptables.size();
    }
}
