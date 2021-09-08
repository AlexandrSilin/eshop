package ru.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.eshop.dto.PictureDto;
import ru.eshop.pictures.service.PictureService;

@RequestMapping("/pictures")
@RestController
public class PictureControllerApi {
    private final PictureService pictureService;

    @Autowired
    public PictureControllerApi(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/{pictureId}")
    @CrossOrigin
    public PictureDto getPictureById(@PathVariable("pictureId") Long pictureId) {
        return pictureService.findById(pictureId).map(picture -> new PictureDto(picture.getFilename())).orElseThrow();
    }
}
