---
description: DDD 代码审查 — 检查领域模型、架构合规、CQRS 模式、代码质量
parameters:
  - name: scope
    type: string
    enum: [domain, application, infrastructure, interfaces, all]
    default: all
  - name: strict
    type: boolean
    default: true
---

# DDD 代码审查命令

执行 DDD 代码审查，范围：{{scope}}

## 检查清单

### 领域模型合规
- [ ] 聚合根是否维护了内部不变量（业务规则在聚合内，非 Service）
- [ ] 值对象是否不可变（final 字段、无 setter、构造时自验证）
- [ ] 跨聚合是否通过 ID 引用而非对象引用
- [ ] 领域事件定义是否正确（不可变、构造时赋值）

### 架构合规（依赖方向）
- [ ] `domain` 层是否有外部依赖（禁止依赖 Spring/DB）
- [ ] `application` 层是否直接调用了 `infrastructure`（禁止）
- [ ] `interfaces` Controller 是否直接调用了 Repository/Mapper（禁止）
- [ ] 事务边界是否仅开在 `application/service/` 层

### CQRS + Executor 合规
- [ ] Executor 是否包含了业务规则判断（应委托给领域服务）
- [ ] Application Service 是否只做委托（薄门面）
- [ ] 写操作用 `CmdExe`，读操作用 `QryExe`（CQRS 分离）
- [ ] Converter 是否使用 MapStruct（DTO ↔ Domain Entity）

### 项目特定禁止项
- [ ] 聚合根中注入 Repository 或 Domain Service
- [ ] Controller 中直接调用 Repository 或 Mapper
- [ ] Application Service 中包含业务规则判断
- [ ] 值对象中包含业务行为以外的逻辑
- [ ] Executor 中跨领域编排（应在 Service 层）

### 代码质量
- [ ] 方法长度是否超过 50 行
- [ ] 是否存在重复代码
- [ ] 是否正确处理空值/异常
- [ ] N+1 查询问题
- [ ] 敏感信息硬编码

## 输出格式
```markdown
## DDD 代码审查报告

### 🔴 严重违规（必须修复）
- [文件:行号] 违规描述 + DDD 原则引用 + 修复建议

### 🟡 警告（建议修复）
- [文件:行号] 问题描述 + 优化方案

### 🟢 优化建议
- 建议内容

### ✅ 通过项
- 列举做得好的地方
```
