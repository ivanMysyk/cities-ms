package com.test.task.kuehne.cities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Integer id;

    private String name;

    private String photoLink;

    private MultipartFile photo;
}