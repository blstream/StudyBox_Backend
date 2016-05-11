package com.bls.patronage;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalFileServiceTest {
    private static final Pattern VALID_UUID = Pattern.compile("[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}");

    private static LocalFileService service;
    private static Path location;

    @BeforeClass
    public static void setUp() throws Exception {
        service = LocalFileService.getInstance(Paths.get("./storage"));
        location = Paths.get(new File("").getAbsolutePath());
    }

    @Test
    public void createPathToFileTest() throws Exception {
        Path pathToFile = service.createPathToFile(location);

        assertThat(pathToFile.getParent()).isEqualTo(location);
        assertThat(pathToFile.getFileName().toString()).matches(VALID_UUID);
    }

    @Test
    public void createNonExistingPathAndDeleteIt() throws Exception {
        Path nonExistingDir = location.resolve("testDir");

        assertThat(Files.notExists(nonExistingDir)).isTrue();

        Path pathToFile = service.createPathToFile(nonExistingDir);

        assertThat(pathToFile.getParent()).isEqualTo(nonExistingDir);
        assertThat(pathToFile.getFileName().toString()).matches(VALID_UUID);

        service.deleteFile(nonExistingDir);

        assertThat(Files.notExists(nonExistingDir)).isTrue();
    }
}
