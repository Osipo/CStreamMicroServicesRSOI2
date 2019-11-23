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
import ru.osipov.deploy.entities.Cinema;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "ru.osipov.deploy.repositories",
        entityManagerFactoryRef = "cinemasEntityManagerFactory",
        transactionManagerRef = "cinemasTransactionManager")
public class CinemasInstance {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.cinemas")
    public DataSourceProperties cinemasDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.cinemas.configuration")
    public DataSource cinemasDataSource() {
        return cinemasDataSourceProperties().initializeDataSourceBuilder()
                .type(BasicDataSource.class).build();
    }

    @Bean(name = "cinemasEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean cinemasEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(cinemasDataSource())
                .packages(Cinema.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager cinemasTransactionManager(
            final @Qualifier("cinemasEntityManagerFactory") LocalContainerEntityManagerFactoryBean cinemasEntityManagerFactory) {
        return new JpaTransactionManager(cinemasEntityManagerFactory.getObject());
    }
}
