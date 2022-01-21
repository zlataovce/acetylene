package me.kcra.acetylene.mappingio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.DescriptableMapping;
import me.kcra.acetylene.core.loader.LoaderContext;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MappingTreeView;
import net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MappingIOLoaderContext extends LoaderContext {
    @Override
    protected void loadFile0(File file) {
        final MemoryMappingTree mappingTree = new MemoryMappingTree();
        try {
            MappingReader.read(file.toPath(), mappingTree);
        } catch (IOException e) {
            throw new RuntimeException("Could not load file", e);
        }
        for (MappingTree.ClassMapping classMapping : mappingTree.getClasses()) {
            addClass(new ClassMapping(
                    classMapping.getSrcName(),
                    classMapping.getName(getFirstNamespace(classMapping)),
                    classMapping.getFields().stream()
                            .map(fieldMapping -> {
                                final int firstNamespace = getFirstNamespace(fieldMapping);
                                return new DescriptableMapping(
                                        fieldMapping.getSrcName(),
                                        fieldMapping.getDstName(firstNamespace),
                                        fieldMapping.getSrcDesc(),
                                        fieldMapping.getDstDesc(firstNamespace)
                                );
                            })
                            .toList(),
                    classMapping.getMethods().stream()
                            .map(methodMapping -> {
                                final int firstNamespace = getFirstNamespace(methodMapping);
                                return new DescriptableMapping(
                                        methodMapping.getSrcName(),
                                        methodMapping.getDstName(firstNamespace),
                                        methodMapping.getSrcDesc(),
                                        methodMapping.getDstDesc(firstNamespace)
                                );
                            })
                            .toList()
            ));
        }
    }

    protected static int getFirstNamespace(MappingTreeView.ElementMappingView mappingView) {
        return mappingView.getTree().getNamespaceId(mappingView.getTree().getDstNamespaces().get(0));
    }
}
