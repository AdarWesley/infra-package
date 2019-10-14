package org.awesley.infra.query.jpa;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "org.awesley.infra.query.jpa.repositories" })
@EntityScan(basePackages = { "org.awesley.infra.query.jpa.entities" })
@OverrideAutoConfiguration(enabled = false)
@ComponentScan()
@ImportAutoConfiguration(value = {
	DataSourceAutoConfiguration.class
	,JpaRepositoriesAutoConfiguration.class
	,HibernateJpaAutoConfiguration.class
	,JdbcTemplateAutoConfiguration.class
 })
public class InfraQueryJpaTestsConfig {
	
}
