package me.kcra.acetylene.srgutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.loader.TypedLoaderContext;
import me.kcra.acetylene.core.utils.Pair;
import net.minecraftforge.srgutils.IMappingFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SrgUtilsTypedLoaderContext extends TypedLoaderContext {
    @Override
    protected void loadFiles0(List<File> files) {
        final List<IMappingFile> mappingFiles = files.stream()
                .map(file -> {
                    try {
                        return IMappingFile.load(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(iMappingFile -> {
                    if (iMappingFile.getClasses().stream().anyMatch(iClass -> iClass.getOriginal().contains("/"))) {
                        return iMappingFile.reverse();
                    }
                    return iMappingFile;
                })
                .toList();
        mappingFiles.get(0).getClasses().forEach(iClass -> {
            final List<IMappingFile.IClass> otherClasses = mappingFiles.stream()
                    .map(iMappingFile -> iMappingFile.getClass(iClass.getOriginal()))
                    .filter(Objects::nonNull)
                    .toList();
            addClass(
                    new TypedClassMapping(
                            iClass.getOriginal(),
                            otherClasses.stream()
                                    .map(IMappingFile.INode::getMapped)
                                    .toList(),
                            iClass.getFields().stream()
                                    .map(iField -> {
                                        final List<IMappingFile.IField> otherFields = otherClasses.stream()
                                                .map(iClass1 -> iClass1.getField(iField.getOriginal()))
                                                .filter(Objects::nonNull)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                iField.getOriginal(),
                                                iField.getDescriptor(),
                                                otherFields.stream()
                                                        .map(iField1 -> Pair.of(iField1.getMapped(), iField1.getMappedDescriptor()))
                                                        .toList()
                                        );
                                    })
                                    .toList(),
                            iClass.getMethods().stream()
                                    .map(iMethod -> {
                                        final List<IMappingFile.IMethod> otherMethods = otherClasses.stream()
                                                .map(iClass1 -> iClass1.getMethod(iMethod.getOriginal(), iMethod.getDescriptor()))
                                                .filter(Objects::nonNull)
                                                .toList();
                                        return new TypedDescriptableMapping(
                                                iMethod.getOriginal(),
                                                iMethod.getDescriptor(),
                                                otherMethods.stream()
                                                        .map(iMethod1 -> Pair.of(iMethod1.getMapped(), iMethod1.getMappedDescriptor()))
                                                        .toList()
                                        );
                                    })
                                    .toList()
                    )
            );
        });
    }
}
