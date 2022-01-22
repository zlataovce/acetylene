package me.kcra.acetylene.core.ancestry;

import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public record ClassAncestorTree(List<TypedClassMapping> classes, int offset) {
    public static ClassAncestorTree of(String refClass, TypedMappingFile... files) {
        return of(refClass, Arrays.asList(files));
    }

    public static ClassAncestorTree of(String refClassS, List<TypedMappingFile> files) {
        int i = 0;
        TypedClassMapping refClass = null;
        for (TypedMappingFile refFile : files) {
            refClass = refFile.mappedClass(refClassS);
            if (refClass != null) {
                break;
            }
            i++;
        }
        if (refClass == null) {
            throw new IllegalArgumentException("Reference class not found");
        }
        final List<TypedClassMapping> mappings = new ArrayList<>();
        mappings.add(refClass);
        final AtomicReference<List<String>> currentMappings = new AtomicReference<>(refClass.mappings());
        for (TypedMappingFile file : files.stream().skip(1).toList()) {
            final TypedClassMapping result = file.classes().stream()
                    .filter(e -> !Collections.disjoint(e.mappings(), currentMappings.get()))
                    .findFirst()
                    .orElse(null);
            if (result == null) {
                return new ClassAncestorTree(Collections.unmodifiableList(mappings), i);
            }
            currentMappings.set(result.mappings());
            mappings.add(result);
        }
        return new ClassAncestorTree(Collections.unmodifiableList(mappings), i);
    }

    public @Nullable TypedClassMapping mapping(int index) {
        if ((index - offset) >= size() || (index - offset) < 0) {
            return null;
        }
        return classes.get(index - offset);
    }

    public DescriptableAncestorTree fieldAncestors(String refFieldS) {
        int i = 0;
        TypedDescriptableMapping refField = null;
        for (TypedClassMapping refClass : classes) {
            refField = refClass.mappedField(refFieldS);
            if (refField != null) {
                break;
            }
            i++;
        }
        if (refField == null) {
            throw new IllegalArgumentException("Reference field not found");
        }
        return DescriptableAncestorTree.of(refField, offset + i, true, classes.stream().skip(i + 1).map(tcm -> tcm.fields().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public DescriptableAncestorTree methodAncestors(String refMethodS, String refDescriptor) {
        int i = 0;
        TypedDescriptableMapping refMethod = null;
        for (TypedClassMapping refClass : classes) {
            refMethod = refClass.mappedMethod(refMethodS, refDescriptor);
            if (refMethod != null) {
                break;
            }
            i++;
        }
        if (refMethod == null) {
            throw new IllegalArgumentException("Reference method not found");
        }
        return DescriptableAncestorTree.of(refMethod, offset + i, false, classes.stream().skip(i + 1).map(tcm -> tcm.fields().toArray(TypedDescriptableMapping[]::new)).toList());
    }

    public int size() {
        return classes.size();
    }
}
