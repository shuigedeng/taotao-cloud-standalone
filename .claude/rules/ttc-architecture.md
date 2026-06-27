# 架构规范 — taotao-cloud-standalone

## 八模块 DDD 分层

```
┌─────────────────────────────────────────────────────────────────┐
│  api/         · RPC/gRPC 接口定义 + DTO（纯接口，无业务依赖）    │
├─────────────────────────────────────────────────────────────────┤
│  interfaces/  · REST Controller + RPC/gRPC 实现                 │
│                 buyer / seller / manager 三端分包               │
├─────────────────────────────────────────────────────────────────┤
│  application/ · CQRS Executor + 应用服务（薄门面）              │
│                 command/{biz}/executor/ + service/             │
├─────────────────────────────────────────────────────────────────┤
│  domain/      · ★ 领域层（零外部依赖，不含 Spring/DB 注解）      │
│                 aggregate/ entity/ val/ event/ service/         │
├─────────────────────────────────────────────────────────────────┤
│  infrastructure/ · 持久化、MQ、缓存、仓储实现                    │
│                    persistent/ event/ cache/ roketmq/           │
├─────────────────────────────────────────────────────────────────┤
│  facade/      · 防腐层（ACL），独立 Gradle 模块                   │
├─────────────────────────────────────────────────────────────────┤
│  common/      · 公共枚举、工具类、常量                           │
├─────────────────────────────────────────────────────────────────┤
│  assembly/    · Spring Boot 启动器 + 环境配置                    │
└─────────────────────────────────────────────────────────────────┘
```

## 依赖方向（严格禁止违反）

```
api  ←  interfaces  →  application  →  facade
                          ↓
                    domain  ←  infrastructure
```

### 各层依赖规则

| 层 | 可以依赖 | 禁止依赖 |
|----|---------|---------|
| domain | 无（零外部依赖） | Spring、DB 框架、infrastructure |
| application | domain, facade 接口 | infrastructure |
| infrastructure | domain, application（事件订阅） | interfaces（反向依赖） |
| interfaces | application | infrastructure |
| api | 无 | 任何业务模块 |

### 常见违规示例

```java
// ❌ 禁止：Controller 直接调用 Repository
@Autowired private DictMapper dictMapper;

// ❌ 禁止：Application Service 直接调用 Mapper
@Autowired private DictMapper dictMapper;

// ❌ 禁止：Domain Service 注入 Repository
@Autowired private DictDomainRepository repository;

// ✅ 正确：Application Service 通过领域服务操作
private final DictDomainService dictDomainService;
```

## CQRS 流程

```
Controller
  → DictsService.insert(Cmd)        // 应用服务（薄门面）
    → DictInsertCmdExe.execute(Cmd) // 执行器（参数校验 + 调用领域服务）
      → DictDomainService.insert()  // 领域服务（业务逻辑）
        → DictDomainRepository      // 仓储接口（domain 层定义）
          → DictDomainRepositoryImpl // 仓储实现（infrastructure 层）
            → DictMapper / JpaRepository // 持久化
```

## Controller 规范

- 按角色分包：`controller/buyer/`、`controller/seller/`、`controller/manager/`
- 只做：HTTP 参数解析、参数校验（`@Validated`）、Result 封装
- 禁止：业务逻辑判断、直接调用 Repository/Mapper
- 命名：`{Role}{Entity}Controller`（如 `ManagerDictController`）
- 统一继承 `BusinessController`

## 事务边界

- `@Transactional` 仅开在 `application/service/` 层
- 读操作使用 `@Transactional(readOnly = true)`
- 领域层不允许出现事务注解
