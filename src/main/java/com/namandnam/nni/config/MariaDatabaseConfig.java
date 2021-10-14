package com.namandnam.nni.config;

import javax.sql.DataSource;

import com.namandnam.nni.config.properties.MariaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(MariaProperties.class)
public class MariaDatabaseConfig extends DatabaseConfig {
     
    @Autowired
    private MariaProperties mariaProperties;
    
    @Bean(name = "mariaDataSource", destroyMethod = "close")
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource mariaDataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        configureDataSource(mariaDataSource, mariaProperties);
        return mariaDataSource;
    }
     
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("mariaDataSource") DataSource mariaDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(mariaDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
}