package com.wavesdev.oauth2.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.wavesdev.oauth2")
@EnableTransactionManagement
public class HelloWorldConfiguration {
	@Bean
    public SessionFactory sessionFactory() {
            LocalSessionFactoryBuilder builder =
			new LocalSessionFactoryBuilder(dataSource());
            builder.scanPackages("com.wavesdev.oauth2.model")
                  .addProperties(getHibernateProperties());

            return builder.buildSessionFactory();
    }

	private Properties getHibernateProperties() {
            Properties prop = new Properties();
            prop.put("hibernate.show_sql", "true");
            prop.put("hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect");
            prop.put("hibernate.hbm2ddl.auto",
                    "update");
            
            return prop;
    }

	@Bean(name = "dataSource")
	public DataSource dataSource() {

		BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/springsecurityoauth2");
		ds.setUsername("root");
		ds.setPassword("123");
		return ds;
	}

	//Create a transaction manager
	@Bean
    public HibernateTransactionManager txManager() {
            return new HibernateTransactionManager(sessionFactory());
    }

	@Bean(name="HelloWorld")
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}	

}