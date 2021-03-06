/*
 * This file is part of acetylene, licensed under the MIT License.
 *
 * Copyright (c) 2022 Matouš Kučera
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.kcra.acetylene.test;

import lombok.SneakyThrows;
import me.kcra.acetylene.core.TypedClassMapping;
import me.kcra.acetylene.core.TypedDescriptableMapping;
import me.kcra.acetylene.core.TypedMappingFile;
import me.kcra.acetylene.core.ancestry.ClassAncestorTree;
import me.kcra.acetylene.core.ancestry.DescriptableAncestorTree;
import me.kcra.acetylene.core.utils.Pair;
import me.kcra.acetylene.srgutils.SrgUtilsMappingLoader;
import me.kcra.acetylene.test.utils.MappingType;
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
                        SrgUtilsMappingLoader.of(
                                Pair.of(MappingType.MOJANG, mojangFile),
                                Pair.of(MappingType.INTERMEDIARY, intermediaryMapping),
                                Pair.of(MappingType.SEARGE, seargeMapping),
                                Pair.of(MappingType.SPIGOT, spigotMapping)
                        ).loadTyped()
                );
            }
            System.out.println("Added mappings for " + ver + ".");
        }
        System.out.println("Loaded " + files.size() + " files.");
        System.out.println("First class: " + files.get(0).classes().get(0));
        try (final Timer ignored = Timer.of("Ancestry compute")) {
            final ClassAncestorTree ancestorTree = ClassAncestorTree.of("net/minecraft/network/protocol/game/ClientboundDisconnectPacket", files);
            System.out.println("Classes mapped: " + ancestorTree.size());
            final TypedClassMapping result = ancestorTree.mapping(28); // 1.9.4
            Assertions.assertNotNull(result);
            System.out.println("Result: " + result);
            Assertions.assertEquals(VERSIONS.size(), ancestorTree.size());

            final DescriptableAncestorTree ancestorTree1 = ancestorTree.fieldAncestors("reason");
            System.out.println("Fields mapped: " + ancestorTree1.size());
            final TypedDescriptableMapping result1 = ancestorTree1.mapping(28); // 1.9.4
            Assertions.assertNotNull(result1);
            System.out.println("Result: " + result1);
            Assertions.assertEquals(VERSIONS.size(), ancestorTree1.size());

            final ClassAncestorTree ancestorTree2 = ClassAncestorTree.of("PacketPlayOutBed", files);
            System.out.println("Classes mapped: " + ancestorTree2.size());
            System.out.println("Offset: " + ancestorTree2.offset());
            final TypedClassMapping result2 = ancestorTree2.mapping(28); // 1.9.4
            Assertions.assertNotNull(result2);
            System.out.println("Result: " + result2);
            Assertions.assertEquals(11, ancestorTree2.size());

            // testing old field lookup
            final ClassAncestorTree ancestorTree3 = ClassAncestorTree.of("net/minecraft/network/protocol/game/ClientboundAddMobPacket", files);
            System.out.println("Classes mapped: " + ancestorTree3.size());
            final DescriptableAncestorTree result3 = ancestorTree3.fieldAncestors("id", "field_11996", "f_131531_"); // data watcher
            System.out.println("Fields mapped: " + result3.size() + ", offset: " + result3.offset());
            System.out.println("Result: " + result3);

            // testing method lookup
            final ClassAncestorTree ancestorTree4 = ClassAncestorTree.of("net/minecraft/network/protocol/Packet", files);
            System.out.println("Classes mapped: " + ancestorTree4.size());
            final DescriptableAncestorTree result4 = ancestorTree4.methodAncestors("write", "(Lnet/minecraft/network/FriendlyByteBuf;)V");
            System.out.println("Methods mapped: " + result4.size() + ", offset: " + result4.offset());
            System.out.println("Result: " + result4);
        }
    }
}
