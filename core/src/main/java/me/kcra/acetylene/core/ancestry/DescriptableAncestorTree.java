package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record DescriptableAncestorTree(List<TypedDescriptableMapping> descriptables, int offset) {
    public static DescriptableAncestorTree of(TypedDescriptableMapping refDescr, int offset, boolean ignoreDescriptors, TypedDescriptableMapping[]... descs) {
        return of(refDescr, offset, ignoreDescriptors, Arrays.asList(descs));
    }

    public static DescriptableAncestorTree of(TypedDescriptableMapping refDescr, int offset, boolean ignoreDescriptors, List<TypedDescriptableMapping[]> descs) {
        final List<TypedDescriptableMapping> mappings = new ArrayList<>();
        mappings.add(refDescr);
        final AtomicReference<List<Pair<String, String>>> currentMappings = new AtomicReference<>(refDescr.mappings().stream().map(Pair::value).toList());
        for (TypedDescriptableMapping[] mappings1 : descs) {
            final TypedDescriptableMapping result = Arrays.stream(mappings1)
                    .filter(e -> {
                        if (ignoreDescriptors) {
                            return !Collections.disjoint(
                                    e.mappings().stream().map(Pair::value).map(Pair::key).toList(),
                                    currentMappings.get().stream().map(Pair::key).toList()
                            );
                        }
                        return !Collections.disjoint(e.mappings().stream().map(Pair::value).toList(), currentMappings.get());
                    })
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new DescriptableAncestorTree(Collections.unmodifiableList(mappings), offset);
            }
            currentMappings.set(result.mappings().stream().map(Pair::value).toList());
            mappings.add(result);
        }
        return new DescriptableAncestorTree(Collections.unmodifiableList(mappings), offset);
    }

    public @Nullable TypedDescriptableMapping mapping(int index) {
        if ((index - offset) >= size() || (index - offset) < 0) {
            return null;
        }
        return descriptables.get(index - offset);
    }

    public int size() {
        return descriptables.size();
    }
}
