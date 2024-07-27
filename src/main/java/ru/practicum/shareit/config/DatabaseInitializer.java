package ru.practicum.shareit.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Component
public class DatabaseInitializer {

    private final DataSource dataSource;

    @Value("classpath:schema.sql")
    private Resource schemaScript;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void initializeDatabase() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaScript);
        populator.execute(dataSource);
    }
}
