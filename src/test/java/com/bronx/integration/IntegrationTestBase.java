package com.bronx.integration;

import com.bronx.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
@Sql(scripts = "classpath:sqlScripts/dropSchema.sql")
@Sql(scripts = "classpath:sqlScripts/createSchema.sql")
@Sql(scripts = "classpath:sqlScripts/insertData.sql")
public abstract class IntegrationTestBase {


    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.1");

    @BeforeAll
    static  void runContainer(){
        container.start();
    }

    @DynamicPropertySource
    static void posgresProperties (DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }


}
