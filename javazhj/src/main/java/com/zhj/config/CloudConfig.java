//package com.zhj.config;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import javax.sql.DataSource;
//
//@Configuration
//@MapperScan(value = "com.zhj.mapper.cloud",sqlSessionFactoryRef = "db2sqlSessionFactory")
//public class CloudConfig {
//
//
//    @Bean("db2")
//    @ConfigurationProperties(prefix = "spring.datasource.db2")
//    public DataSource getDb1DataSource(){
//        return DataSourceBuilder.create().build();
//    }
//
//
//    @Bean("db2sqlSessionFactory")
//    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db2") DataSource dataSource) throws Exception{
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/cloud/*.xml"));
//        return bean.getObject();
//    }
//
//
//    @Bean("db2sqlSessionTemplate")
//    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("db2sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//
//
//}
