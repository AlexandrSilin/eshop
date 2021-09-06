package ru.eshop.database.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eshop.database.persist.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
