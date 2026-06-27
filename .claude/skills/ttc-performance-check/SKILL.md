---
name: performance-check
description: 性能检查 — 慢查询、N+1、缓存、JPA/MyBatis 优化
triggers:
  - "性能检查"
  - "慢查询"
  - "N+1"
---

# 性能检查技能

适配项目：taotao-cloud-standalone（MyBatis-Plus + JPA 双持久化）

## 检查清单

### 1. N+1 查询检测
- Controller/Service 中循环调用 Repository/Mapper
- `for` 循环中的数据库查询
- JPA `@OneToMany` 默认懒加载

### 2. SQL 优化
- `SELECT *` → 明确指定查询列
- 批量操作使用 `saveBatch` / `updateBatchById`
- 避免 `list()` 全量查询，使用分页

### 3. MyBatis-Plus 优化
- 复杂查询使用自定义 Mapper SQL
- 合理使用 `@Select` 注解写原生 SQL
- 用 `Page` 分页而非手动 `LIMIT`

### 4. JPA 优化
- 只读查询使用 `@Transactional(readOnly = true)`
- 合理设置 `FetchType`
- 使用 `@EntityGraph` 避免 N+1

### 5. Redis 缓存检查
- 是否使用 `@Cacheable` / `@CacheEvict`
- 缓存 key 设计是否合理
- 缓存过期时间设置
