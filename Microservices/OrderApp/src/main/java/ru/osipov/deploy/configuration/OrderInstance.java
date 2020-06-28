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
import ru.osipov.deploy.entities.Order;
import ru.osipov.deploy.entities.OrderItem;

import javax.sql.DataSource;

@Configuration
@Primary
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.osipov.deploy.repositories",
        entityManagerFactoryRef = "ordersEntityManagerFactory",
        transactionManagerRef= "ordersTransactionManager"
)
public class OrderInstance {
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.orders")
    public DataSourceProperties ordersDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.orders.configuration")
    public DataSource ordersDataSource() {
        return ordersDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Primary
    @Bean(name = "ordersEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ordersEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(ordersDataSource())
                .packages(Order.class, OrderItem.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager ordersTransactionManager(
            final @Qualifier("ordersEntityManagerFactory") LocalContainerEntityManagerFactoryBean ordersEntityManagerFactory) {
        return new JpaTransactionManager(ordersEntityManagerFactory.getObject());
    }
}
