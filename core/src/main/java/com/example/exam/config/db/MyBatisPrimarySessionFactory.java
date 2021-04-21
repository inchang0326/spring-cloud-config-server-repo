package com.example.exam.config.db;

import ch.qos.logback.classic.Logger;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.example.exam",
        sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class MyBatisPrimarySessionFactory extends MyBatisConfig {

    private final Logger log = (Logger) LoggerFactory.getLogger(MyBatisPrimarySessionFactory.class);

    @Bean // 개발자가 제어하지 못하는 외부 라이브러리 등을 Bean으로 등록한다.
    @Primary // 같은 타입의 다른 Bean이 있어도 해당 Bean이 항상 먼저 등록됨을 보장한다.
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("oracleDataSource") DataSource dataSource) throws Exception { // Datasource와 MyBatis를 연동시켜줌
        log.info("=============== embbededSqlSessionFactory Start ===============");
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(sessionFactoryBean, dataSource);
        log.info("=============== embbededSqlSessionFactory End   ===============");
        return sessionFactoryBean.getObject();
    }
}
