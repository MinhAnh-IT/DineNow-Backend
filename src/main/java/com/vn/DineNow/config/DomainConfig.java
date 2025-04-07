package com.vn.DineNow.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.vn.DineNow.entities")
@EnableJpaRepositories("com.vn.DineNow.repositories")
@EnableTransactionManagement
public class DomainConfig {
}
