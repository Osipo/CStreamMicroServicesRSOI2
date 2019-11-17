package ru.osipov.deploy.configuration;


import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.osipov.deploy.entities.Film;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "ru.osipov.deploy.repositories",
        entityManagerFactoryRef = "moviesEntityManagerFactory",
        transactionManagerRef = "moviesTransactionManager")
public class MoviesInstance {
    @Bean
    @ConfigurationProperties("app.datasource.movies")
    @Primary
    public DataSourceProperties moviesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.movies.configuration")
    public DataSource moviesDataSource() {
        return moviesDataSourceProperties().initializeDataSourceBuilder()
                .type(BasicDataSource.class).build();
    }

    @Bean(name = "moviesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(moviesDataSource())
                .packages(Film.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager moviesTransactionManager(
            final @Qualifier("moviesEntityManagerFactory") LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory) {
        return new JpaTransactionManager(moviesEntityManagerFactory.getObject());
    }
}
