package ru.eshop.dto;

import java.nio.file.Path;

public class PictureDto {
    private String filename;
    private Path path;

    public PictureDto(String filename) {
        this.filename = filename;
        this.path = Path.of("/storage", filename);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
