package com.taotao.cloud.standalone.unit;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.standalone.infrastructure.persistent.persistence.DeptPO;
import com.taotao.cloud.standalone.infrastructure.persistent.repository.DeptRepository;
import jakarta.persistence.Query;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

/**
 * DeptsDataJpaTest
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Setter
@Getter
//@Import({com.taotao.boot.security.spring.configuration.PropertiesAutoConfiguration.class,
//	org.springframework.cache.support.NoOpCacheManager.class,
//	org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.class,})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.url=jdbc:mysql://192.168.218.2:3306/taotao-cloud-goods?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowMultiQueries=true&autoReconnect=true&useCursorFetch=true",
        "spring.datasource.username=root",
        "spring.datasource.password=123456",
        "spring.jpa.show-sql=true",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
        "spring.jpa.properties.hibernate.hbm2ddl.auto=none",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true",
        "logging.level.web=TRACE",
        "logging.level.root=INFO",
})
//@TestPropertySource(locations = "classpath:application-dev.yml")
public class DeptsDataJpaTest {

    private static final Logger logger = LoggerFactory.getLogger(DeptsDataJpaTest.class);

    static {
        logger.info("This is a debug message");
        System.setProperty("spring.profiles.active", "dev");
    }

    @Autowired
    private DeptRepository deptRepository;

    @Test
    void testSave() {
        logger.info("This is a debug message");
        logger.info("3333333333333333333333");

        LogUtils.info("1111111111111111111111111111");

//		repo.save(u1);
//		assertThat(q.getResultList()).hasSize(1);
//		repo.save(u2);
//		assertThat(q.getResultList()).hasSize(2);
//		User u = repo.save(u3);
//		assertThat(q.getResultList()).hasSize(3);
//		u3.setRole(User.ADMIN);
//		repo.save(u3);

//		Assertions.assertEquals(
//			entityManager.find(User.class, u.getId()).getRole()).isEqualTo(User.ADMIN);
    }

    @Test
    void testFindAll() {
//		entityManager.persist(u1);
//		entityManager.persist(u2);
//		entityManager.persist(u3);

        List<DeptPO> entities = deptRepository.findAll();
        Assertions.assertEquals(3, entities.size(), "");
        Assertions.assertNotNull(entities.get(0).getId());
        Assertions.assertNotNull(entities.get(1).getId());
        Assertions.assertNotNull(entities.get(2).getId());
    }

    @Test
    void testFindByName() {
        // Arrange - 设置测试数据
//		User user1 = new User(null, "John Doe", "john.doe@example.com");
//		User user2 = new User(null, "Jane Doe", "jane.doe@example.com");
//		userRepository.save(user1);
//		userRepository.save(user2);

        // Act - 执行查询操作
        DeptPO deptPO = deptRepository.find(new DeptPO());

        List<DeptPO> deptPOS = deptRepository.findAll();

        // Assert - 验证查询结果
        Assertions.assertEquals(1, deptPOS.size(), "Should find one John Doe");
        Assertions.assertEquals("John Doe", deptPOS.get(0).getName());

        Assertions.assertEquals(1, deptPOS.size(), "Should find one Jane Doe");
        Assertions.assertEquals("Jane Doe", deptPOS.get(0).getName());
    }
}
