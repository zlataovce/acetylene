package me.kcra.acetylene.test;

import lombok.SneakyThrows;
import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.ancestry.ClassAncestorTree;
import me.kcra.acetylene.srgutils.SrgUtilsMappingLoader;
import me.kcra.acetylene.test.utils.TestUtils;
import me.kcra.acetylene.test.utils.Timer;
import net.minecraftforge.srgutils.IMappingFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassAncestorTreeTest {
    private static final List<String> VERSIONS = List.of(
            "1.18.1", "1.18", "1.17.1", "1.17", "1.16.5", "1.16.4", "1.16.3", "1.16.2", "1.16.1",
            "1.16", "1.15.2", "1.15.1", "1.15", "1.14.4", "1.14.3", "1.14.2", "1.14.1", "1.14",
            "1.13.2", "1.13.1", "1.12.2", "1.12.1", "1.12", "1.11.2", "1.11.1", "1.11", "1.10.2",
            /*"1.10.1", */"1.10", "1.9.4"
    );

    /*@BeforeEach
    public void cleanWorkspace() {
        TestUtils.cleanWorkspace();
    }*/

    @Test
    @SneakyThrows
    public void classNameAncestor() {
        final List<TypedMappingFile> files = new ArrayList<>();
        for (String ver : VERSIONS) {
            File mojangMapping, intermediaryMapping, seargeMapping, spigotMapping;
            try (final Timer ignored = Timer.of("Mapping download")) {
                mojangMapping = TestUtils.minecraftResource(ver, "server_mappings");
                intermediaryMapping = TestUtils.intermediaryMapping(ver);
                seargeMapping = TestUtils.seargeMapping(ver);
                spigotMapping = TestUtils.spigotMapping(ver);
            }
            try (final Timer ignored = Timer.of("Mapping load")) {
                IMappingFile mojangFile = null;
                if (mojangMapping != null) {
                    mojangFile = IMappingFile.load(mojangMapping).reverse();
                }
                files.add(
                        SrgUtilsMappingLoader.of(mojangFile, intermediaryMapping, seargeMapping, spigotMapping).loadTyped()
                );
            }
            System.out.println("Added mappings for " + ver + ".");
        }
        System.out.println("Loaded " + files.size() + " files.");
        try (final Timer ignored = Timer.of("Ancestry compute")) {
            final TypedMappingFile refFile = files.get(0);
            System.out.println("Reference file has " + refFile.size() + " classes mapped.");
            final TypedClassMapping refClass = refFile.mappedClass("net/minecraft/network/protocol/game/ClientboundDisconnectPacket");
            Assertions.assertNotNull(refClass);
            System.out.println("Reference class: " + refClass);
            final ClassAncestorTree ancestorTree = ClassAncestorTree.of(refClass, files.subList(1, files.size()));
            final TypedClassMapping result = ancestorTree.mapping(27); // 1.9.4
            Assertions.assertNotNull(result);
            System.out.println("Result: " + result);
            System.out.println("Traced files: " + ancestorTree.size()); // will always be VERSIONS.size() - 1
        }
    }
}
