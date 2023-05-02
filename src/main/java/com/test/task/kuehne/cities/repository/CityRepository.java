package com.test.task.kuehne.cities.repository;

import com.test.task.kuehne.cities.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<City> findAllByPhotoIsNull();

}
