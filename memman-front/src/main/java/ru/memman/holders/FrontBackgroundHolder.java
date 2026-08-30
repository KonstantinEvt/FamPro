package ru.memman.holders;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Getter
@Setter
@Slf4j
public class FrontBackgroundHolder {

    private byte[] favicon;
    private byte[] ground;
    private byte[] mainPicture;


    @Value("${application.picture.favicon}")
    private String fav;
    @Value("${application.picture.ground}")
    private String gro;
    @Value("${application.picture.mainPicture}")
    private String main;

    @PostConstruct
    void setFavicon() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream(fav);
        if (in != null) this.setFavicon(IOUtils.toByteArray(in));
        else log.warn("Favicon not loading");
    }
    @PostConstruct
    void setGround() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream(gro);
        if (in != null) this.setGround(IOUtils.toByteArray(in));
        else log.warn("Ground not loading");
    }
    @PostConstruct
    void setMainPicture() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream(main);
        if (in != null) this.setMainPicture(IOUtils.toByteArray(in));
        else log.warn("Main picture not loading");
    }
}
