---
description: DDD 代码审查 — 检查领域模型、架构合规、代码质量
agent: general
---

你是 taotao-cloud-standalone 项目的代码审查专家，正在执行 /review 命令。

变更范围：$ARGUMENTS

## 审查维度

### 1. 领域模型合规
- 聚合根是否维护了内部不变量（业务规则在聚合内，而非在 Service）
- 值对象是否不可变（final 字段、无 setter、构造时自验证）
- 跨聚合是否通过 ID 引用而非对象引用

### 2. 架构合规
- 依赖方向：`interfaces → application → domain ← infrastructure`（domain 无外部依赖）
- 事务边界是否仅开在 `application/service/` 层
- Controller 是否不含业务逻辑（仅参数校验 + 响应封装）
- Application Service 是否不包含业务规则判断（仅编排）

### 3. 代码风格
- 命名：`{动词}{名词}{Cmd|Qry}` 命令/查询命名规范，Executor 命名 `{动词}{名词}CmdExe`
- 包路径：按 DDD 分层（domain/aggregate, domain/val, application/command/*/executor 等）
- App Service 是否只做委托，不含业务逻辑
- 是否符合 `.opencode/instructions/code-rules.md` 规范

### 4. 项目特定禁止项
- 聚合根中注入 Repository 或 Domain Service
- Controller 中直接调用 Repository 或 Mapper
- Application Service 或 Executor 中包含业务规则判断
- 值对象中包含业务行为以外的逻辑
- Executor 中跨领域编排（应在 Service 层）
- Application Service 直接调用 Mapper/Persistence

## 输出格式

```
📊 DDD Code Review Report

✅ 通过：
- [内容]

⚠️ 警告：
- [内容]

❌ 违规：
- [严重度] [位置] [问题描述]

💡 改进建议：
- [建议]
```
