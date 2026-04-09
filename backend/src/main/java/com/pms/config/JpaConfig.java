package com.pms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.pms.repository")
public class JpaConfig {
    // SQLite 方言已在 application.yml 中配置：
    // spring.jpa.database-platform: org.hibernate.community.dialect.SQLiteDialect
    // 此配置类确保 JPA repositories 和事务管理正确启用
}
