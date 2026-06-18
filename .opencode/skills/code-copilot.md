# Code Copilot — 渐进式 Spec 开发技能

适配项目：taotao-cloud-standalone（DDD + CQRS + Gradle + JDK 25）

## 触发条件

通过 `/propose` → `/apply` → `/review` → `/fix` → `/test` → `/archive` 命令触发。

## 核心规则

1. **No Spec No Code** — 没有确认的 Spec 不准写代码
2. **Spec is Truth** — Spec 和代码冲突时，错的一定是代码
3. **DDD 优先** — 所有变更从领域模型出发，而非数据库或 API

## 分层实现原则

| 层 | 职责 | 关键约束 |
|----|------|---------|
| `domain/` | 纯业务逻辑 | 零外部依赖，不依赖 Spring/DB |
| `application/command/*/executor/` | CQRS 执行器 | 调用领域服务，不含跨领域编排 |
| `application/service/` | 应用门面 | 薄层，仅委托给 Executor |
| `application/converter/` | MapStruct 转换 | DTO ↔ Domain Entity |
| `infrastructure/` | 技术实现 | 仓储、MQ、缓存、配置 |
| `interfaces/` | API 入口 | 参数校验 + 响应封装，不含业务 |
| `facade/` | 防腐层 | 外部系统调用的适配 |
| `api/` | 接口定义 | 纯 DTO + RPC 接口 |

## 工作流

### /propose — 创建变更提案
1. 调研涉及的 DDD 模块（domain/aggregate, application/command/*/executor 等）
2. 逐个澄清需求（每次一个问题，用 `question` 工具）
3. 生成 Spec，包含：领域模型变更、业务规则、接口变更、CQRS Executor 变更
4. 用户确认后才进入 /apply

### /apply — 按 Spec 编码
1. 严格遵循 DDD 分层实现（domain → application/command/executor → application/service → interfaces）
2. 每个 Task 执行后 `./gradlew compileJava` 验证
3. 完成后提交 git commit

### /review — DDD 代码审查
1. 检查领域模型合规（聚合边界、值对象不可变）
2. 检查架构合规（依赖方向、事务边界、分层职责）
3. 检查 CQRS Executor 职责（不包含业务规则判断）
4. 检查项目禁止项

### /fix — 修正问题
1. 增量修改，不重构式修复
2. 验证编译通过
3. 更新相关文档

### /test — 运行测试
```bash
gradlew test
gradlew jacocoTestReport
```

### /archive — 归档变更
1. 记录变更总结（涉及模块、领域模型、接口、Executor）
2. 如需更新 AGENTS.md（新模块/新约定/新禁止项）
3. Git commit
