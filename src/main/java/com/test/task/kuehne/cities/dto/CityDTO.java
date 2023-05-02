package com.test.task.kuehne.cities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private Integer id;

    private String name;

    private String photoLink;
}