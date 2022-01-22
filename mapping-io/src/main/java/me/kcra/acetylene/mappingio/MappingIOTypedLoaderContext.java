package me.kcra.acetylene.mappingio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.loader.TypedLoaderContext;
import me.kcra.acetylene.core.utils.Pair;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static me.kcra.acetylene.mappingio.MappingIOLoaderContext.getFirstNamespace;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MappingIOTypedLoaderContext extends TypedLoaderContext {
    @Override
    protected void loadFiles0(List<Object> files) {
        final List<MemoryMappingTree> mappingFiles = files.stream()
                .map(file -> {
                    MemoryMappingTree mappingTree = new MemoryMappingTree();
                    try {
                        if (file instanceof File) {
                            MappingReader.read(((File) file).toPath(), mappingTree);
                        } else if (file instanceof Path) {
                            MappingReader.read((Path) file, mappingTree);
                        } else if (file instanceof MemoryMappingTree) {
                            mappingTree = (MemoryMappingTree) file;
                        } else {
                            throw new IllegalArgumentException("Unsupported file");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Could not load file", e);
                    }
                    return mappingTree;
                })
                .toList();
        for (MappingTree.ClassMapping classMapping : mappingFiles.get(0).getClasses()) {
            final List<MappingTree.ClassMapping> otherClasses = mappingFiles.stream()
                    .map(mappingTree -> (MappingTree.ClassMapping) mappingTree.getClass(classMapping.getSrcName()))
                    .filter(Objects::nonNull)
                    .toList();
            addClass(
                    new TypedClassMapping(
                            classMapping.getSrcName(),
                            otherClasses.stream()
                                    .map(classMapping1 -> classMapping1.getDstName(getFirstNamespace(classMapping1)))
                                    .toList(),
                            classMapping.getFields().stream()
                                    .map(fieldMapping -> {
                                        final List<MappingTree.FieldMapping> otherFields = otherClasses.stream()
                                                .map(classMapping1 -> classMapping1.getField(fieldMapping.getSrcName(), fieldMapping.getSrcDesc()))
                                                .filter(Objects::nonNull)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                fieldMapping.getSrcName(),
                                                fieldMapping.getSrcDesc(),
                                                otherFields.stream()
                                                        .map(fieldMapping1 -> {
                                                            final int firstNamespace = getFirstNamespace(fieldMapping1);
                                                            return Pair.of(fieldMapping1.getDstName(firstNamespace), fieldMapping1.getDstDesc(firstNamespace));
                                                        })
                                                        .toList()
                                        );
                                    })
                                    .toList(),
                            classMapping.getMethods().stream()
                                    .map(methodMapping -> {
                                        final List<MappingTree.MethodMapping> otherMethods = otherClasses.stream()
                                                .map(methodMapping1 -> methodMapping1.getMethod(methodMapping.getSrcName(), methodMapping.getSrcDesc()))
                                                .filter(Objects::nonNull)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                methodMapping.getSrcName(),
                                                methodMapping.getSrcDesc(),
                                                otherMethods.stream()
                                                        .map(methodMapping1 -> {
                                                            final int firstNamespace = getFirstNamespace(methodMapping1);
                                                            return Pair.of(methodMapping1.getDstName(firstNamespace), methodMapping1.getDstDesc(firstNamespace));
                                                        })
                                                        .toList()
                                        );
                                    })
                                    .toList()
                    )
            );
        }
    }
}
