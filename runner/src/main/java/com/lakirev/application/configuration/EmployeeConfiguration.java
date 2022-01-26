package com.lakirev.application.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"com.lakirev.employee.repository"})
@EntityScan("com.lakirev.employee.model")
@ComponentScan("com.lakirev.employee")
@EnableTransactionManagement
public class EmployeeConfiguration {
}
