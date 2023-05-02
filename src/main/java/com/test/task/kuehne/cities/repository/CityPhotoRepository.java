package com.test.task.kuehne.cities.repository;

import com.test.task.kuehne.cities.model.CityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityPhotoRepository extends JpaRepository<CityPhoto, String> {

}
