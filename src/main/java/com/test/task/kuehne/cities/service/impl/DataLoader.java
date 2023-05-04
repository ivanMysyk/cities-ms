package com.test.task.kuehne.cities.service.impl;

import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.repository.CityPhotoRepository;
import com.test.task.kuehne.cities.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
@RequiredArgsConstructor
@Profile("container")
public class DataLoader implements ApplicationRunner {
    private final CityRepository cityRepository;
    private final CityPhotoRepository cityPhotoRepository;

    private final Logger log = LoggerFactory.getLogger(DataLoader.class.getName());

    @Override
    public void run(ApplicationArguments args) {
        cityRepository.findAllByPhotoIsNull().forEach(city -> {
            try {
                byte[] photoData = downloadPhoto(city.getPhotoLink());
                if (photoData != null && photoData.length > 0 ) {
                    String[] nameFromUrl = city.getPhotoLink().split("/");
                    String[] typeFromUrl = city.getPhotoLink().split("\\.");
                    CityPhoto photo = CityPhoto.builder()
                            .data(photoData)
                            .name(nameFromUrl[nameFromUrl.length - 1])
                            .contentType(typeFromUrl[typeFromUrl.length - 1])
                            .size((long) photoData.length)
                            .build();
                    cityPhotoRepository.save(photo);
                    city.setPhoto(photo);
                    cityRepository.save(city);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    private byte[] downloadPhoto(String url) throws IOException {
        URL photoUrl = new URL(url);
        try(InputStream inputStream = photoUrl.openStream()) {
            return inputStream.readAllBytes();
        }
        catch (Exception ignored) {
            return null;
        }
    }
}