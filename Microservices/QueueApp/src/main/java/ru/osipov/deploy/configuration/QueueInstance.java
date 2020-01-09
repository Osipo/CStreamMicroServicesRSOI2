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
import ru.osipov.deploy.entities.*;

import javax.sql.DataSource;

@Configuration
@Primary
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.osipov.deploy.repositories",
        entityManagerFactoryRef = "queueEntityManagerFactory",
        transactionManagerRef= "queueTransactionManager"
)
public class QueueInstance {
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.queue")
    public DataSourceProperties queueDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.queue.configuration")
    public DataSource queueDataSource() {
        return queueDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Primary
    @Bean(name = "queueEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean queueEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(queueDataSource())
                .packages(QueueItemSeance.class, QueueItemCinema.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager queueTransactionManager(
            final @Qualifier("queueEntityManagerFactory") LocalContainerEntityManagerFactoryBean queueEntityManagerFactory) {
        return new JpaTransactionManager(queueEntityManagerFactory.getObject());
    }
}
