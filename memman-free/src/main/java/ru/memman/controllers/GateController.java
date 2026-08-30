package ru.memman.controllers;

import ru.memman.holders.FrontBackgroundHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/free/imj")
@AllArgsConstructor

public class GateController {

    private FrontBackgroundHolder frontBackgroundHolder;

    @GetMapping(value = "/start",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getStart() {
        return frontBackgroundHolder.getStart();
    }

    @GetMapping(value = "/favicon",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getFavicon() {
        return frontBackgroundHolder.getFavicon();
    }

    @GetMapping(value = "/ground",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getGround() {
        return frontBackgroundHolder.getGround();

    }
    @GetMapping(value = "/mainPicture",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPicture() {
        return frontBackgroundHolder.getMainPicture();

    }
}


