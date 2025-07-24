package com.example.jobx_batch.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.jobx_batch.dao", sqlSessionFactoryRef = "sqlSessionFactoryBusiness")
public class MapperConfig {

    @Bean(name = "sqlSessionFactoryBusiness")
    public SqlSessionFactory sqlSessionFactoryBusiness(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml")
        );

        return factoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplateBusiness")
    public SqlSessionTemplate sqlSessionTemplateBusiness(@Qualifier("sqlSessionFactoryBusiness") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}