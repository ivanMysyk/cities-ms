package com.test.task.kuehne.cities.service;

import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.model.CityPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {

    Page<City> getAllCities(Pageable pageable);

    Page<City> searchCities(String name, Pageable pageable);

    void updateCity(City city);

    void updateCityPhoto(Long id, CityPhoto photo);

}
