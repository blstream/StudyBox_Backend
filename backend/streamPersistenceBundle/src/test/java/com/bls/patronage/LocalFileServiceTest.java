package com.bls.patronage;

import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalFileServiceTest {
    static LocalFileService service;
    static Path location;
    private String UUIDPattern = "[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}";

    @BeforeClass
    public static void setUp() throws Exception {
        service = LocalFileService.getInstance();
        location = Paths.get("/", "root", "test");
    }

    @Test
    public void createPathToFileTest() throws Exception {
        Path pathToFile = service.createPathToFile(location);

        assertThat(pathToFile.getParent()).isEqualTo(location);
        assertThat(pathToFile.getFileName().toString()).matches(UUIDPattern);
    }

    @Test
    public void createNonExistingPathAndDeleteIt() throws Exception {
        Path nonExistingDir = location.resolve("testDir");

        assertThat(Files.notExists(nonExistingDir)).isTrue();

        Path pathToFile = service.createPathToFile(nonExistingDir);

        assertThat(pathToFile.getParent()).isEqualTo(nonExistingDir);
        assertThat(pathToFile.getFileName().toString()).matches(UUIDPattern);

        service.deleteStream(nonExistingDir);

        assertThat(Files.notExists(nonExistingDir)).isTrue();
    }
}
