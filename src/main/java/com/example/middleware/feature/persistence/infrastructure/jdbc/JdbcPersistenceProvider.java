package com.example.middleware.feature.persistence.infrastructure.jdbc;

import org.springframework.stereotype.Component;

import com.example.middleware.feature.persistence.domain.PersistenceProvider;

@Component
public class JdbcPersistenceProvider
        implements PersistenceProvider {

    @Override
    public String getProviderName() {
        return "jdbc";
    }

}
