# Optimize Query — 查询优化技能

适配项目：taotao-cloud-standalone（DDD + MyBatis-Plus + JPA + JDK 25）

## 触发条件

当检测到慢查询、N+1 问题或用户要求优化查询性能时触发。

## 检查清单

### 1. N+1 查询检测
- Controller/Service 中循环调用 Repository/Mapper
- `for` 循环中的数据库查询
- JPA `@OneToMany` 默认懒加载导致的 N+1

### 2. 索引建议
- WHERE 条件字段是否有索引
- ORDER BY 字段是否有索引
- JOIN 关联字段是否有索引

### 3. MyBatis-Plus 优化
- `select *` → 明确指定查询列
- 批量操作使用 `saveBatch`/`updateBatchById`
- 避免 `list()` 全量查询，使用分页

### 4. JPA 优化
- 使用 `@EntityGraph` 避免 N+1
- 只读查询使用 `@Transactional(readOnly = true)`
- 合理使用 `FetchType.LAZY`

### 5. DDD 查询规范
- 读操作走 `QryExe`，不走领域模型
- 复杂查询走 MyBatis-Plus Mapper 自定义 SQL
- 标准 CRUD 走 JPA Repository
