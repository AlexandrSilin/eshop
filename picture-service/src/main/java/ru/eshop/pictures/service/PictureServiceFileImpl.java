package ru.eshop.pictures.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eshop.database.persist.PictureRepository;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.model.Picture;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PictureServiceFileImpl implements PictureService {

    private static final Logger logger = LoggerFactory.getLogger(PictureServiceFileImpl.class);
    private final PictureRepository pictureRepository;
    private final ProductRepository productRepository;
    @Value("${picture.storage.path}")
    private String storagePath;

    @Autowired
    public PictureServiceFileImpl(PictureRepository pictureRepository, ProductRepository productRepository) {
        this.pictureRepository = pictureRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Optional<String> getPictureContentTypeById(long id) {
        return pictureRepository.findById(id)
                .map(Picture::getContentType);
    }

    @Override
    public Optional<byte[]> getPictureDataById(long id) {
        return pictureRepository.findById(id)
                .map(pic -> Paths.get(storagePath, pic.getFilename()))
                .filter(Files::exists)
                .map(path -> {
                    try {
                        return Files.readAllBytes(path);
                    } catch (IOException ex) {
                        logger.error("Can't read file for picture with id " + id, ex);
                        throw new RuntimeException(ex);
                    }
                });
    }

    @Override
    public String createPicture(byte[] picture) {
        String fileName = UUID.randomUUID().toString();
        try (OutputStream os = Files.newOutputStream(Paths.get(storagePath, fileName))) {
            os.write(picture);
        } catch (IOException ex) {
            logger.error("Can't write file", ex);
            throw new RuntimeException(ex);
        }
        return fileName;
    }

    @Transactional
    @Override
    public Long deletePicture(Long id) {
        Picture picture = pictureRepository.findById(id).orElseThrow();
        try {
            Files.delete(Paths.get(storagePath, picture.getFilename()));
            productRepository.findAll().forEach(product -> {
                if (product.getPictures().contains(picture)) {
                    List<Picture> picturesList = product.getPictures();
                    picturesList.remove(picture);
                    product.setPicture(picturesList);
                }
            });
            pictureRepository.deleteById(id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return picture.getProduct().getId();
    }

    @Override
    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
    }
}