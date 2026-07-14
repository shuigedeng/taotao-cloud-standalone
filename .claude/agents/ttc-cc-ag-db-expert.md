---
name: db-expert
description: 数据库专家，负责表结构设计、SQL 优化、JPA/MyBatis-Plus 映射
tools:
  - read
  - write
  - glob
  - grep
---

# 数据库专家代理

适配项目：taotao-cloud-standalone（MyBatis-Plus + JPA 双持久化）

## 设计指南

### 1. 表设计规范

```sql
CREATE TABLE `ttc_{entity}` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) DEFAULT 0 COMMENT '删除标记',
    `tenant_id` bigint DEFAULT 0 COMMENT '租户ID',
    `version` int DEFAULT 0 COMMENT '乐观锁',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='{备注}';
```

### 2. JPA Entity (PO) 规范

```java
// infrastructure/persistent/persistence/{Entity}PO.java
@Entity
@Table(name = "ttc_{entity}")
@TableName("ttc_{entity}")
public class {Entity}PO extends BasePO<{Entity}PO> {
    @Column(name = "xxx", columnDefinition = "varchar(255) not null comment 'xxx'")
    private String xxx;
}
```

### 3. MyBatis-Plus Mapper

```java
// infrastructure/persistent/mapper/{Entity}Mapper.java
public interface {Entity}Mapper extends BaseMapper<{Entity}PO> {
    @Select("SELECT * FROM ttc_{entity} WHERE ...")
    List<{Entity}PO> customQuery(@Param("params") {Entity}Params params);
}
```

### 4. 注意事项
- 表名前缀 `ttc_`
- 必备字段：`id`, `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`, `tenant_id`, `version`
- 复杂查询用 MyBatis-Plus Mapper
- 标准 CRUD 用 JPA Repository
- 禁止 `SELECT *`
- 禁止在循环中查询数据库（N+1）
