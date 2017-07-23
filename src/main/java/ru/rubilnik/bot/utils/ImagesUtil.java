package ru.rubilnik.bot.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
public class ImagesUtil {
    public InputStream getImageInputStream(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());

        return bis;
    }
}
