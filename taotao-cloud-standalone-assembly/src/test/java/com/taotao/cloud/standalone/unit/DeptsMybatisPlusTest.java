package com.taotao.cloud.standalone.unit;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.taotao.cloud.standalone.infrastructure.persistent.dept.mapper.DeptMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

/**
 * DeptsMybatisPlusTest
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.url=jdbc:mysql://192.168.218.2:3306/taotao-cloud-goods?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowMultiQueries=true&autoReconnect=true&useCursorFetch=true",
        "spring.datasource.username=root",
        "spring.datasource.password=123456",
})
class DeptsMybatisPlusTest {

    static {
        System.setProperty("spring.profiles.active", "dev");
    }

    @Autowired
    private DeptMapper deptMapper;

    @Test
    void testSelectIdsByRoleId() {
        List<Long> permissions = deptMapper.selectIdsByRoleId(1L);
        Assertions.assertEquals(4, permissions.size(), "");
    }

}
