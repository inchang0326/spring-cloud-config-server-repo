package com.example.exam.config.db;

import ch.qos.logback.classic.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class MyBatisConfig {

    private final Logger logger = (Logger) LoggerFactory.getLogger(MyBatisConfig.class);

    // @Value : yml, properties 내 값들을 읽어와 사용한다.
    @Value("${spring.mybatis.typeAliasesPackage}")  String typeAliasesPackage;
    @Value("${spring.mybatis.configLocation}")      String configLocation;
    @Value("${spring.mybatis.mapperLocation}")      String mapperLocation;

    void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactoryBean.setConfigLocation(pathResolver.getResource(configLocation));
        sessionFactoryBean.setMapperLocations(pathResolver.getResources(mapperLocation));
        sessionFactoryBean.setVfs(SpringBootVFS.class);
    }
}
