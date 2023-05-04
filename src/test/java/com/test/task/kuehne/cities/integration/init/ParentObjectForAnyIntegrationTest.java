package com.test.task.kuehne.cities.integration.init;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Tag("integrations-tests")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(initializers = {PostgreSQl.Initializer.class})
@DisabledIf(expression = "#{systemProperties['disableIntTests'] != null}",
        reason = "Tests disabled by system property")
public abstract class ParentObjectForAnyIntegrationTest {

    @BeforeAll
    static void init() {
        PostgreSQl.container.start();
    }

}
