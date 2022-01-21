package me.kcra.acetylene.mappingio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.loader.LoaderContext;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.format.MappingFormat;
import net.fabricmc.mappingio.tree.MappingTree;
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
        mappingTree.getClasses().forEach(classEntry -> {
            final MappingTree.ClassMapping classMapping = (MappingTree.ClassMapping) classEntry;
        });
    }
}
