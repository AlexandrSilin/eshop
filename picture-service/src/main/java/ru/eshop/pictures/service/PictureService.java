package ru.eshop.pictures.service;

import ru.eshop.database.persist.model.Picture;

import java.util.Optional;

public interface PictureService {

    Optional<String> getPictureContentTypeById(long id);

    Optional<byte[]> getPictureDataById(long id);

    String createPicture(byte[] picture);

    Long deletePicture(Long id);

    Optional<Picture> findById(Long id);
}
