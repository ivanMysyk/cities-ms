package com.test.task.kuehne.cities.controller;

import com.test.task.kuehne.cities.dto.CityDto;
import com.test.task.kuehne.cities.mapper.CityMapper;
import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.model.CityPhoto;
import com.test.task.kuehne.cities.service.CityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cities")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CityController {

    private CityService cityService;

    private CityMapper cityMapper;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<CityDto>> getAllCities(
            @PageableDefault(sort = { "name" }, direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<City> cities = cityService.getAllCities(pageable);
        Page<CityDto> cityDtos = cities.map(cityMapper::toDto);
        return ResponseEntity.ok(cityDtos);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<CityDto>> searchCities(
            @RequestParam String name,
            @PageableDefault(sort = { "name" }, direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<City> cities = cityService.searchCities(name, pageable);
        Page<CityDto> cityDtos = cities.map(cityMapper::toDto);
        return ResponseEntity.ok(cityDtos);
    }

    @PutMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> updateCity(@RequestBody CityDto cityDto) {
        City city = cityMapper.toEntity(cityDto);
        cityService.updateCity(city);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> updateCity(
            @RequestParam("file") MultipartFile multipartFile,
            @PathVariable Long id) throws IOException {
        CityPhoto photo = cityMapper.multipartFileToCityPhoto(multipartFile);
        cityService.updateCityPhoto(id, photo);
        return ResponseEntity.noContent().build();
    }

}
