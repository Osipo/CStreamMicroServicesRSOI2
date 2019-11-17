package ru.osipov.deploy.configuration;

import com.zaxxer.hikari.HikariDataSource;
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
import ru.osipov.deploy.entities.Genre;

import javax.sql.DataSource;

@Configuration
@Primary
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.osipov.deploy.repositories",
        entityManagerFactoryRef = "genresEntityManagerFactory",
        transactionManagerRef= "genresTransactionManager"
)
public class GenreInstance {
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.genres")
    public DataSourceProperties genresDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.genres.configuration")
    public DataSource genresDataSource() {
        return genresDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Primary
    @Bean(name = "genresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean genresEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(genresDataSource())
                .packages(Genre.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager genresTransactionManager(
            final @Qualifier("genresEntityManagerFactory") LocalContainerEntityManagerFactoryBean genresEntityManagerFactory) {
        return new JpaTransactionManager(genresEntityManagerFactory.getObject());
    }
}
