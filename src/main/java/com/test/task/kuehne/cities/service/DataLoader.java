package com.test.task.kuehne.cities.service;

import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.repository.CityPhotoRepository;
import com.test.task.kuehne.cities.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final CityRepository cityRepository;
    private final CityPhotoRepository cityPhotoRepository;

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
            } catch (IOException e) {
                throw new RuntimeException(e);
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