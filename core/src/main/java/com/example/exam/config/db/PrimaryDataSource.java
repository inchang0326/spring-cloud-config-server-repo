package com.example.exam.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Profile("local") // 해당 App. 즉 프로젝트 Config 설정 중 Active Profile이 "local"일 경우에만 적용된다.
@Configuration
@EnableJpaRepositories(basePackages = "com.example.exam.biz",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "publicTransactionManager")
public class PrimaryDataSource {

    @Bean(name = "oracleDataSource")
    @Order(Ordered.LOWEST_PRECEDENCE)
    public DataSource oracleDataSource(@Value("${db.jdbcUrl}") String jdbcUrl,
                                       @Value("${db.userName}") String userName,
                                       @Value("${db.password}") String password,
                                       @Value("${db.hikari.minimumIdle}") Integer minimumIdle,
                                       @Value("${db.hikari.maximumPoolSize}") Integer maximumPoolSize,
                                       @Value("${db.hikari.idleTimeout}") Integer idleTimeout
    ) {

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(userName);
        ds.setPassword(password);
        ds.setMinimumIdle(minimumIdle);
        ds.setMaximumPoolSize(maximumPoolSize);
        ds.setIdleTimeout(idleTimeout);
        ds.setConnectionInitSql("SELECT 'GO' FROM DUAL");

        return ds;
    }

    @Bean(name = "entityManagerFactory")
    @Order(Ordered.LOWEST_PRECEDENCE)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("oracleDataSource") DataSource ds,
                                                                       @Value("${db.dialect}") String dialect) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.ORACLE);
        vendorAdapter.setGenerateDdl(true);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.default_schema", "public");
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.ddl-auto", "none");
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put("hibernate.cache.use_second_level_cache", false);
        properties.put("hibernate.cache.use_query_cache", false);
        properties.put("hibernate.show_sql", false);
        properties.put("javax.persistence.validation.mode", "none");

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPackagesToScan("com.example.exam.entity");
        em.setJpaPropertyMap(properties);

        return em;

    }

    @Bean(name = "publicTransactionManager")
    @Order(Ordered.LOWEST_PRECEDENCE)
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory,
                                                         @Qualifier("oracleDataSource") DataSource dataSource
    ) {
        JpaTransactionManager jtm = new JpaTransactionManager(entityManagerFactory);
        DataSourceTransactionManager dstm = new DataSourceTransactionManager();
        dstm.setDataSource(dataSource);

        ChainedTransactionManager ctm = new ChainedTransactionManager(jtm, dstm);
        return ctm;
    }
}
