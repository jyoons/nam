package com.namandnam.nni.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public abstract class MybatisConfig {
	public static final String BASE_PACKAGE = "com.namandnam.nni";
    public static final String TYPE_ALIASES_PACKAGE = "com.namandnam.nni.**.vo";
    //public static final String CONFIG_LOCATION_PATH = "classpath:META-INF/mybatis/mybatis-config.xml";
    public static final String MAPPER_LOCATIONS_PATH = "classpath*:/com/namandnam/nni/**/mapper/*.xml";
     
    protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        //sessionFactoryBean.setConfigLocation(pathResolver.getResource(CONFIG_LOCATION_PATH));
        sessionFactoryBean.setMapperLocations(pathResolver.getResources(MAPPER_LOCATIONS_PATH));
        sessionFactoryBean.setConfiguration( getSqlSessionConfig() );
    }
    
    
    private Configuration getSqlSessionConfig() { 
        org.apache.ibatis.session.Configuration sqlSessionConfig = new org.apache.ibatis.session.Configuration();
        sqlSessionConfig.setMapUnderscoreToCamelCase(true);
        sqlSessionConfig.setCacheEnabled(false);
        sqlSessionConfig.setDefaultStatementTimeout(10);
        return sqlSessionConfig;
    }

}
