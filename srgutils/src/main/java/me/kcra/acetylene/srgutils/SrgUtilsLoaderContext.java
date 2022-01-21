package me.kcra.acetylene.srgutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.kcra.acetylene.core.ClassMapping;
import me.kcra.acetylene.core.DescriptableMapping;
import me.kcra.acetylene.core.loader.LoaderContext;
import net.minecraftforge.srgutils.IMappingFile;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SrgUtilsLoaderContext extends LoaderContext {
    @Override
    protected void loadFile0(File file) {
        IMappingFile mappingFile;
        try {
            mappingFile = IMappingFile.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not load file", e);
        }
        mappingFile.getClasses().forEach(iClass -> addClass(new ClassMapping(
                iClass.getOriginal(),
                iClass.getMapped(),
                iClass.getFields().stream()
                        .map(iField -> new DescriptableMapping(
                                iField.getOriginal(),
                                iField.getMapped(),
                                iField.getDescriptor(),
                                iField.getMappedDescriptor()
                        ))
                        .toList(),
                iClass.getMethods().stream()
                        .map(iMethod -> new DescriptableMapping(
                                iMethod.getOriginal(),
                                iMethod.getMapped(),
                                iMethod.getDescriptor(),
                                iMethod.getMappedDescriptor()
                        ))
                        .toList()
        )));
    }
}
