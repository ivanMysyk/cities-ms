package com.test.task.kuehne.cities.unit;

import com.test.task.kuehne.cities.controller.CityController;
import com.test.task.kuehne.cities.dto.CityDto;
import com.test.task.kuehne.cities.mapper.CityMapper;
import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.model.CustomMultipartFile;
import com.test.task.kuehne.cities.service.CityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {

    @Mock
    private CityService cityService;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityController cityController;

    @Test
    public void testGetAllCities() {
        Pageable pageable = PageRequest.of(0, 10);
        List<City> cities = Collections.singletonList(new City());
        Page<City> page = new PageImpl<>(cities, pageable, cities.size());
        Mockito.when(cityService.getAllCities(pageable)).thenReturn(page);
        List<CityDto> cityDtos = Collections.singletonList(new CityDto());
        Page<CityDto> expected = new PageImpl<>(cityDtos, pageable, cityDtos.size());
        Mockito.when(cityMapper.toDto(Mockito.any())).thenReturn(cityDtos.get(0));

        ResponseEntity<Page<CityDto>> response = cityController.getAllCities(pageable);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    public void testSearchCities() {
        Pageable pageable = PageRequest.of(0, 10);
        List<City> cities = Collections.singletonList(new City());
        Page<City> page = new PageImpl<>(cities, pageable, cities.size());
        Mockito.when(cityService.searchCities(Mockito.anyString(), Mockito.any())).thenReturn(page);
        List<CityDto> cityDtos = Collections.singletonList(new CityDto());
        Page<CityDto> expected = new PageImpl<>(cityDtos, pageable, cityDtos.size());
        Mockito.when(cityMapper.toDto(Mockito.any())).thenReturn(cityDtos.get(0));

        ResponseEntity<Page<CityDto>> response = cityController.searchCities("City", pageable);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
    }

    @Test
    public void testUpdateCity() {
        CityDto cityDto = new CityDto();
        City city = new City();
        Mockito.when(cityMapper.toEntity(cityDto)).thenReturn(city);

        ResponseEntity<Void> response = cityController.updateCity(cityDto);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(cityService).updateCity(city);
    }

    @Test
    public void testUpdateCityPhoto() throws IOException {
        MultipartFile multipartFile = new CustomMultipartFile(
                "Test".getBytes(),"file", "text/plain");
        CityPhoto photo = new CityPhoto();
        Mockito.when(cityMapper.multipartFileToCityPhoto(multipartFile)).thenReturn(photo);

        ResponseEntity<Void> response = cityController.updateCity(multipartFile, 1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(cityService).updateCityPhoto(1L, photo);
    }

    @Test
    public void testGetAllCities_WhenNoCitiesFound() {
        // Stage 1: Mock
        Pageable pageable = PageRequest.of(0, 10);
        Page<City> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        Mockito.when(cityService.getAllCities(pageable)).thenReturn(page);

        // Stage 2: Act
        ResponseEntity<Page<CityDto>> response = cityController.getAllCities(pageable);

        // Stage 3: Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(new PageImpl<>(Collections.emptyList(), pageable, 0), response.getBody());
    }
}