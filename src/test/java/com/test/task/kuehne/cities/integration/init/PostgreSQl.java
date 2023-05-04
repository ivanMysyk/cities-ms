package com.test.task.kuehne.cities.integration.init;

import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class PostgreSQl {

    private final String postgreVersion = "postgres:15";

    private final String encoding = "?characterEncoding=UTF8";

    private final String password = "spring.datasource.password=";

    private final String userName = "spring.datasource.username=";

    private final String url = "spring.datasource.url=";

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(postgreVersion)
            .withFileSystemBind("./src/main/resources/db/csv/cities.csv", "/csv/cities.csv", BindMode.READ_ONLY);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    url + container.getJdbcUrl() + encoding,
                    userName + container.getUsername(),
                    password + container.getPassword()
            ).applyTo(applicationContext);
        }

    }

}
