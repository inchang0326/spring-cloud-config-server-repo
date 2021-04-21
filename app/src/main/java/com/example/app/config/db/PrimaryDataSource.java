package com.example.app.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import javax.sql.DataSource;

@Profile("local") // 해당 App. 즉 프로젝트 Config 설정 중 Active Profile이 "local"일 경우에만 적용된다.
@Configuration
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
}