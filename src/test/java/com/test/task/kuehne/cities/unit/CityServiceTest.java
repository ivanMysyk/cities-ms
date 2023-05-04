package com.test.task.kuehne.cities.unit;

import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.repository.CityPhotoRepository;
import com.test.task.kuehne.cities.repository.CityRepository;
import com.test.task.kuehne.cities.service.impl.CityServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityPhotoRepository cityPhotoRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    public void testGetAllCities() {
        Pageable pageable = PageRequest.of(0, 10);
        List<City> cities = Arrays.asList(new City(1L, "City 1", "", null),
                new City(2L, "City 2", "", null));
        Page<City> page = new PageImpl<>(cities);

        Mockito.when(cityRepository.findAll(pageable)).thenReturn(page);

        Page<City> result = cityService.getAllCities(pageable);

        Mockito.verify(cityRepository).findAll(pageable);
        Assertions.assertEquals(page, result);
    }

    @Test
    public void testSearchCities() {
        Pageable pageable = PageRequest.of(0, 10);
        List<City> cities = Arrays.asList(new City(1L, "City 1", "", null),
                new City(2L, "City 2", "", null));
        Page<City> page = new PageImpl<>(cities);

        Mockito.when(cityRepository.findByNameContainingIgnoreCase("City", pageable)).thenReturn(page);

        Page<City> result = cityService.searchCities("City", pageable);

        Mockito.verify(cityRepository).findByNameContainingIgnoreCase("City", pageable);
        Assertions.assertEquals(page, result);
    }

    @Test
    public void testUpdateCity() {
        City city = new City(1L, "City 1", "", null);

        Mockito.when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        cityService.updateCity(city);

        Mockito.verify(cityRepository).findById(1L);
        Mockito.verify(cityRepository).save(city);
    }

    @Test
    public void testUpdateCityPhoto() {
        CityPhoto photo = new CityPhoto();
        City city = new City(1L, "City 1", "", photo);

        Mockito.when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        cityService.updateCityPhoto(1L, photo);

        Mockito.verify(cityPhotoRepository).save(photo);
        Mockito.verify(cityRepository).findById(1L);
        Mockito.verify(cityRepository).save(city);
    }

    @Test
    public void testUpdateCity_WhenCityNotFound() {
        City cityForUpdate = new City(1L, "City 1", "", null);
        Mockito.when(cityRepository.findById(cityForUpdate.getId())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> cityService.updateCity(cityForUpdate));
        Mockito.verify(cityRepository).findById(cityForUpdate.getId());
    }

    @Test
    public void testUpdateCityPhoto_WhenCityNotFound() {
        Long id = 1L;
        CityPhoto photo = new CityPhoto();
        Mockito.when(cityRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> cityService.updateCityPhoto(id, photo));
        Mockito.verify(cityRepository).findById(id);
    }

    @Test
    public void testUpdateCityPhoto_WhenCityFoundAndNewPhoto() {
        Long id = 1L;
        CityPhoto newPhoto = new CityPhoto();
        City city = new City(id, "City 1", "", newPhoto);

        Mockito.when(cityRepository.findById(id)).thenReturn(Optional.of(city));

        cityService.updateCityPhoto(id, newPhoto);

        Mockito.verify(cityPhotoRepository).save(newPhoto);
        Mockito.verify(cityRepository).findById(id);
        Mockito.verify(cityRepository).save(city);
        Assertions.assertEquals(newPhoto, city.getPhoto());
    }

}
