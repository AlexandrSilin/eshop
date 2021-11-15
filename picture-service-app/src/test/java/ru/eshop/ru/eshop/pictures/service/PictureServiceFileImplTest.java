package ru.eshop.ru.eshop.pictures.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.eshop.pictures.service.PictureService;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class PictureServiceFileImplTest {

    @Value("${picture.storage.path}")
    private String path;

    @Autowired
    private PictureService pictureService;

    @Test
    public void testCreatePicture() throws Exception {
        assertThrows(NoSuchFileException.class,
                () -> pictureService.createPicture(Files.readAllBytes(Path.of("asdfa.img"))));
        String filename = pictureService.createPicture(Files.readAllBytes(Path.of("/home/netman/Pictures/1801287.svg")));
        assertTrue(Files.exists(Path.of(path + "/" + filename)));
    }
}
