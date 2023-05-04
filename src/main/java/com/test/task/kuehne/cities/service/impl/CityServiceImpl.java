package com.test.task.kuehne.cities.service.impl;

import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.repository.CityPhotoRepository;
import com.test.task.kuehne.cities.repository.CityRepository;
import com.test.task.kuehne.cities.service.CityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;

    private CityPhotoRepository cityPhotoRepository;

    @Override
    public Page<City> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public Page<City> searchCities(String name, Pageable pageable) {
        return cityRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional
    public void updateCity(City cityForUpdate) {
        cityRepository.findById(cityForUpdate.getId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        cityRepository.save(cityForUpdate);
    }

    @Override
    @Transactional
    public void updateCityPhoto(Long id, CityPhoto photo) {
        cityPhotoRepository.save(photo);
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        city.setPhoto(photo);
        cityRepository.save(city);
    }

}