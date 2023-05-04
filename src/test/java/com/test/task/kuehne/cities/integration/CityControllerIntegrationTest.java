package com.test.task.kuehne.cities.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.kuehne.cities.dto.CityDto;
import com.test.task.kuehne.cities.integration.init.ParentObjectForAnyIntegrationTest;
import com.test.task.kuehne.cities.model.City;
import com.test.task.kuehne.cities.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CityControllerIntegrationTest extends ParentObjectForAnyIntegrationTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser()
    void testGetAllCities() throws Exception {
        mockMvc.perform(get("/cities")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(753))
                .andExpect(jsonPath("$.content[1].id").value(709));
    }

    @Test
    @WithMockUser()
    void testSearchCities() throws Exception {
        mockMvc.perform(get("/cities/search")
                        .param("name", "Jakarta")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void testUpdateCity() throws Exception {

        CityDto cityDto = CityDto.builder()
                .id(1)
                .name("Tokyoo")
                .photoLink("TokyooLink")
                .build();

        mockMvc.perform(put("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cityDto)))
                .andExpect(status().isNoContent());

        Optional<City> updatedCity = cityRepository.findById(1L);
        assertThat(updatedCity.isPresent(), is(true));
        assertThat(updatedCity.get().getName(), equalTo("Tokyoo"));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void testUpdateCityPhoto() throws Exception {
        mockMvc.perform(multipart(HttpMethod.PUT ,"/cities/{id}", 1L)
                        .file(new MockMultipartFile("file", "test.txt",
                                "text/plain", "test data".getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNoContent());

        Optional<City> updatedCity = cityRepository.findById(1L);
        assertThat(updatedCity.isPresent(), is(true));
        assertThat(updatedCity.get().getPhoto().getData(), equalTo("test data".getBytes()));
    }
}
