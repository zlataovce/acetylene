package me.kcra.acetylene.test.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.minecraftforge.srgutils.IMappingFile;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

@UtilityClass
public class TestUtils {
    public final Path BUILD_FOLDER = Path.of(System.getProperty("user.dir"), "build").toAbsolutePath();
    public final Path WORK_FOLDER = Path.of(BUILD_FOLDER.toString(), "test");
    public final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
    private final MessageDigest SHA_1 = safeMessageDigest("SHA-1");

    @SneakyThrows
    public MessageDigest safeMessageDigest(String name) {
        return MessageDigest.getInstance(name);
    }

    @SneakyThrows
    public void cleanWorkspace() {
        if (WORK_FOLDER.toFile().isDirectory()) {
            Files.walkFileTree(WORK_FOLDER, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public File newFile(String fileName) {
        //noinspection ResultOfMethodCallIgnored
        WORK_FOLDER.toFile().mkdirs();
        return Path.of(WORK_FOLDER.toString(), fileName).toFile();
    }

    @SneakyThrows
    public File getFromURL(String url, String fileName, String sha1) {
        final File downloadedFile = newFile(fileName);
        if (downloadedFile.isFile() && sha1 != null) {
            if (getFileChecksum(SHA_1, downloadedFile).equals(sha1)) {
                return downloadedFile;
            } else {
                System.out.println("Checksum mismatch, redownloading file " + fileName + "...");
            }
        } else {
            if (sha1 == null) {
                System.out.println("Missing SHA1 for file " + fileName + ".");
            } else {
                System.out.println("File " + fileName + " doesn't exist, downloading.");
            }
        }
        try (final InputStream inputStream = new URL(url).openStream()) {
            Files.copy(inputStream, downloadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return null;
        }
        return downloadedFile;
    }

    @SneakyThrows
    public String getFromURL(String url) {
        try (final InputStream inputStream = new URL(url).openStream()) {
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        } catch (Exception ignored) {
            // ignored
        }
        return null;
    }

    @SneakyThrows
    public File minecraftResource(String ver, String res) {
        final JsonNode manifest = MAPPER.readTree(new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json"));
        for (JsonNode jsonNode : manifest.path("versions")) {
            if (jsonNode.get("id").asText().equals(ver)) {
                final JsonNode versionManifest = MAPPER.readTree(new URL(jsonNode.get("url").asText()));
                if (versionManifest.path("downloads").has(res)) {
                    return getFromURL(
                            versionManifest.path("downloads").path(res).get("url").asText(),
                            res + "_" + ver + ".res",
                            versionManifest.path("downloads").path(res).get("sha1").asText()
                    );
                }
            }
        }
        return null;
    }

    public File seargeMapping(String ver) {
        return Objects.requireNonNullElseGet(
                seargeMapping0("https://maven.minecraftforge.net/de/oceanlabs/mcp/mcp_config/" + ver + "/mcp_config-" + ver + ".zip", ver),
                () -> seargeMapping0("https://maven.minecraftforge.net/de/oceanlabs/mcp/mcp/" + ver + "/mcp-" + ver + "-srg.zip", ver)
        );
    }

    public InputStream seargeMappingRaw(String ver) {
        return Objects.requireNonNullElseGet(
                seargeMapping1("https://maven.minecraftforge.net/de/oceanlabs/mcp/mcp_config/" + ver + "/mcp_config-" + ver + ".zip", ver),
                () -> seargeMapping1("https://maven.minecraftforge.net/de/oceanlabs/mcp/mcp/" + ver + "/mcp-" + ver + "-srg.zip", ver)
        );
    }

    @SneakyThrows
    private File seargeMapping0(String url, String ver) {
        final File extractedFile = newFile("mcp_" + ver + ".extracted");
        final InputStream output = seargeMapping1(url, ver);
        if (output == null) {
            return null;
        }
        Files.copy(output, extractedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return extractedFile;
    }

    @SneakyThrows
    private InputStream seargeMapping1(String url, String ver) {
        final File file = getFromURL(url, "mcp_" + ver + ".zip", getFromURL(url + ".sha1"));
        if (file == null) {
            return null;
        }
        final ZipFile zipFile = new ZipFile(file);
        return zipFile.getInputStream(
                zipFile.stream()
                        .filter(e -> e.getName().equals("config/joined.tsrg") || e.getName().equals("joined.srg"))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Searge mapping file not found"))
        );
    }

    public File intermediaryMapping(String ver) {
        return getFromURL("https://raw.githubusercontent.com/FabricMC/intermediary/master/mappings/" + ver + ".tiny", ver + ".tiny", null);
    }

    @SneakyThrows
    public File spigotMapping(String ver) {
        JsonNode versionManifest;
        try {
            versionManifest = MAPPER.readTree(new URL("https://hub.spigotmc.org/versions/" + ver + ".json"));
        } catch (FileNotFoundException ignored) {
            return null;
        }
        final String buildDataRev = versionManifest.path("refs").path("BuildData").asText();
        final JsonNode buildDataManifest = MAPPER.readTree(new URL("https://hub.spigotmc.org/stash/projects/SPIGOT/repos/builddata/raw/info.json?at=" + buildDataRev));
        if (buildDataManifest.has("classMappings")) {
            return getFromURL(
                    "https://hub.spigotmc.org/stash/projects/SPIGOT/repos/builddata/raw/mappings/" + buildDataManifest.path("classMappings").asText() + "?at=" + buildDataRev,
                    "spigot_" + ver + ".csrg",
                    null
            );
        }
        return null;
    }

    @SneakyThrows
    public String getFileChecksum(MessageDigest digest, File file) {
        try (final FileInputStream fis = new FileInputStream(file)) {
            final byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        final byte[] bytes = digest.digest();
        final StringBuilder sb = new StringBuilder();
        for (final byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @SneakyThrows
    public void reverseMappingFile(File file, IMappingFile.Format format) {
        final IMappingFile iMappingFile = IMappingFile.load(file);
        if (iMappingFile.getClasses().stream().anyMatch(e -> e.getOriginal().contains("/"))) {
            // needs to be reversed
            iMappingFile.reverse().write(file.toPath(), format, false);
        }
    }
}

