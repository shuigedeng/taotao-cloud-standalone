---
name: event-storming
description: 事件风暴工作流，识别领域事件、命令和聚合
triggers:
  - "事件风暴"
  - "领域建模"
  - "识别聚合"
---

# 事件风暴工作流

适配项目：taotao-cloud-standalone

## 步骤1：识别领域事件

通过 `question` 工具询问用户：
1. 业务流程中有哪些关键事件？
2. 哪些事件会改变系统状态？
3. 哪些事件会触发其他行为？

输出格式：
```markdown
## 领域事件列表

1. **{Entity}Created** — 事件描述
   - 触发条件: ...
   - 结果: ...
   - 订阅者: ...
```

## 步骤2：识别命令

| 命令 | 触发事件 | 执行者 | 验证规则 |
|------|---------|--------|---------|
| Create{Entity} | {Entity}Created | 管理员 | ... |

## 步骤3：识别聚合

```markdown
### {Entity}聚合
- **为什么是聚合根**: ...
- **包含实体**: ...
- **包含值对象**: ...
- **命令**: ...
- **事件**: ...
- **不变性**: ...
```

## 步骤4：映射到项目结构

| DDD 元素 | 项目位置 |
|---------|---------|
| 聚合根 | `domain/aggregate/{Entity}AggregateRoot.java` |
| 实体 | `domain/entity/{Entity}Entity.java` |
| 值对象 | `domain/val/{Entity}Val.java` |
| 领域事件 | `domain/event/{Entity}{Action}Event.java` |
| 仓储接口 | `domain/repository/{Entity}DomainRepository.java` |
| 领域服务 | `domain/service/{Entity}DomainService.java` |
| Command | `application/command/{entity}/dto/{Entity}InsertCmd.java` |
| Executor | `application/command/{entity}/executor/{Entity}InsertCmdExe.java` |
