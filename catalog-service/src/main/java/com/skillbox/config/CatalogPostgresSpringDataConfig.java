package com.skillbox.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.skillbox.repository.sql",
        entityManagerFactoryRef = "postgresEntityManagerFactory"
)
public class CatalogPostgresSpringDataConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "postgresDataSource")
    @Primary
    public DataSource postgresDataSource() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("postgres");
        dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        
        Properties props = new Properties();
        props.setProperty("URL", url);
        props.setProperty("user", username);
        props.setProperty("password", password);
        
        dataSource.setXaProperties(props);
        dataSource.setPoolSize(5);
        
        return dataSource;
    }

    @Bean(name = "postgresEntityManagerFactory")
    @Primary
    public EntityManagerFactory postgresEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresDataSource());
        em.setPackagesToScan("com.skillbox.security.entity");
        
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty(
                "hibernate.transaction.jta.platform",
                "org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform"
        );
        properties.setProperty("jakarta.persistence.transactionType", "JTA");
        em.setJpaProperties(properties);
        
        em.afterPropertiesSet();
        return em.getObject();
    }
}
