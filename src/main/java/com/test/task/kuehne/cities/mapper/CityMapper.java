package com.test.task.kuehne.cities.mapper;

import com.test.task.kuehne.cities.dto.CityDto;
import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.model.CustomMultipartFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class CityMapper {

    @Mapping(target = "photo", source = "city.photo", qualifiedByName = "cityPhotoToMultipartFile")
    public abstract CityDto toDto(City city);

    public abstract City toEntity(CityDto cityDto);

    public CityPhoto multipartFileToCityPhoto(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        CityPhoto photo = new CityPhoto();
        photo.setName(multipartFile.getOriginalFilename());
        photo.setContentType(multipartFile.getContentType());
        photo.setSize(multipartFile.getSize());
        photo.setData(multipartFile.getBytes());
        return photo;
    }

    @Named("cityPhotoToMultipartFile")
    public MultipartFile cityPhotoToMultipartFile(CityPhoto photo) {
        if (photo == null) {
            return null;
        }

        return new CustomMultipartFile(
                photo.getData(), photo.getName(), photo.getContentType());
    }
}