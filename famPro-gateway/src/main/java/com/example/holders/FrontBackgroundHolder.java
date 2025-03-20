package com.example.holders;

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
    private  byte[] picture;
    private byte[] favicon;
    @Value("${application.picture.url}")
    private String url;
    @Value("${application.picture.favicon}")
    private String fav;
    @PostConstruct
    void setUP() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream(url);
        assert in != null;
        this.setPicture(IOUtils.toByteArray(in));
    }
    @PostConstruct
    void setFavicon() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream(fav);
        assert in != null;
        this.setFavicon(IOUtils.toByteArray(in));
    }
}
