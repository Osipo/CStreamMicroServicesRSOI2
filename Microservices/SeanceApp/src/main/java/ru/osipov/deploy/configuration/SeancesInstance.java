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
import ru.osipov.deploy.entities.RoomsCinema;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.Ticket;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "ru.osipov.deploy.repositories",
        entityManagerFactoryRef = "seancesEntityManagerFactory",
        transactionManagerRef = "seancesTransactionManager"
)
public class SeancesInstance {
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.seances")
    public DataSourceProperties seancesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.seances.configuration")
    public DataSource seancesDataSource() {
        return seancesDataSourceProperties().initializeDataSourceBuilder()
                .type(BasicDataSource.class).build();
    }

    @Bean(name = "seancesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean seancesEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(seancesDataSource())
                .packages(RoomsCinema.class,Seance.class,Ticket.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager seancesTransactionManager(
            final @Qualifier("seancesEntityManagerFactory") LocalContainerEntityManagerFactoryBean seancesEntityManagerFactory) {
        return new JpaTransactionManager(seancesEntityManagerFactory.getObject());
    }
}
