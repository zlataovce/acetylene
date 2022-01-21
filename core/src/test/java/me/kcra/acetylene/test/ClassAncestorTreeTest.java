package me.kcra.acetylene.test;

import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.ancestry.ClassAncestorTree;
import me.kcra.acetylene.srgutils.SrgUtilsMappingLoader;
import me.kcra.acetylene.test.utils.TestUtils;
import me.kcra.acetylene.test.utils.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassAncestorTreeTest {
    private static final List<String> VERSIONS = List.of(
            "1.18.1", "1.18", "1.17.1", "1.17", "1.16.5", "1.16.4", "1.16.3", "1.16.2", "1.16.1",
            "1.16", "1.15.2", "1.15.1", "1.15", "1.14.4", "1.14.3", "1.14.2", "1.14.1", "1.14",
            "1.13.2", "1.13.1", "1.12.2", "1.12.1", "1.12", "1.11.2", "1.11.1", "1.11", "1.10.2",
            /*"1.10.1", */"1.10", "1.9.4"
    );

    @BeforeEach
    public void cleanWorkspace() {
        TestUtils.cleanWorkspace();
    }

    @Test
    public void classNameAncestor() {
        final List<TypedMappingFile> files = new ArrayList<>();
        for (String ver : VERSIONS) {
            File mojangMapping, intermediaryMapping, seargeMapping;
            try (final Timer ignored = Timer.of("Mapping download")) {
                mojangMapping = TestUtils.minecraftResource(ver, "server_mappings");
                intermediaryMapping = TestUtils.intermediaryMapping(ver);
                seargeMapping = TestUtils.seargeMapping(ver);
            }
            try (final Timer ignored = Timer.of("Mapping load")) {
                files.add(
                        SrgUtilsMappingLoader.of(mojangMapping, intermediaryMapping, seargeMapping).loadTyped()
                );
            }
            System.out.println("Added mappings for " + ver + ".");
        }
        try (final Timer ignored = Timer.of("Ancestry compute")) {
            final TypedMappingFile refFile = files.get(0);
            System.out.println(
                    Objects.requireNonNull(
                            ClassAncestorTree.of(
                                    refFile.mappedClass("net/minecraft/network/protocol/game/ClientboundDisconnectPacket"),
                                    files.subList(1, files.size())
                            ).mapping(1)
                    ).mapped()
            );
        }
    }
}
