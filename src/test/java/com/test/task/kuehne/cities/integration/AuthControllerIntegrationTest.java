package com.test.task.kuehne.cities.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.test.task.kuehne.cities.integration.init.ParentObjectForAnyIntegrationTest;
import com.test.task.kuehne.cities.model.ERole;
import com.test.task.kuehne.cities.payload.request.LoginRequest;
import com.test.task.kuehne.cities.payload.request.SignupRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIntegrationTest extends ParentObjectForAnyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationTime;

    @Test
    public void testAuthenticateUser() throws Exception {
        LocalDateTime beforeTokenCreate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        SignupRequest signup = SignupRequest.builder()
                .username("username")
                .password("password")
                .email("user@test.com")
                .role(Set.of(ERole.ROLE_USER))
                .build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andDo(print())
                .andExpect(status().isOk());

        LoginRequest login = LoginRequest.builder()
                .username("username")
                .password("password")
                .build();

        ResultActions result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"));

        LocalDateTime afterTokenCreate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        String token = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.token");

        Claims body = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        assertThat(body.get("sub"), is("username"));

        LocalDateTime creationDate = LocalDateTime.ofInstant(body.getIssuedAt().toInstant(), ZoneId.systemDefault());
        LocalDateTime expDate = LocalDateTime.ofInstant(body.getExpiration().toInstant(), ZoneId.systemDefault());

        assertThat(beforeTokenCreate, lessThanOrEqualTo(creationDate));
        assertThat(afterTokenCreate, greaterThanOrEqualTo(creationDate));
        assertThat(creationDate.plusMinutes(jwtExpirationTime / 60000).isEqual(expDate), is(true));
    }

    @Test
    public void testRegisterUser() throws Exception {
        SignupRequest signup = SignupRequest.builder()
                .username("username")
                .password("password")
                .email("user@test.com")
                .role(Set.of(ERole.ROLE_USER))
                .build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
