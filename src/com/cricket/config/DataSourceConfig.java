package com.cricket.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
	    dataSource.setUrl("jdbc:ucanaccess://C:/Sports/Cricket/Database/CricketTeams.mdb");
	    
	    DriverManagerDataSource men = new DriverManagerDataSource();
	    men.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
	    men.setUrl("jdbc:ucanaccess://C:/Sports/CricketMen/Database/CricketTeams.mdb");
        
        DriverManagerDataSource women = new DriverManagerDataSource();
        women.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        women.setUrl("jdbc:ucanaccess://C:/Sports/CricketWomen/Database/CricketTeams.mdb");

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("LOCAL", dataSource);
        targetDataSources.put("MEN", men);
        targetDataSources.put("WOMEN", women);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(dataSource);

        return routingDataSource;
	}

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("com.cricket.model");
        factory.setHibernateProperties(hibernateProperties());
        return factory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto","update");
        properties.put("hibernate.dialect","org.hibernate.dialect.SQLServerDialect");
        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager =new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}