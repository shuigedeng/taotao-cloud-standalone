# PROJECT KNOWLEDGE BASE

**Generated:** 2026-06-18
**Commit:** `56589ba`
**Branch:** (active branch)
**Maintainer:** shuigedeng

## OVERVIEW

DDD 单体服务（商品/分类/字典/部门领域），基于 Spring Boot 4.1.0 / JDK 25 / Gradle 9.5。
严格遵循六边形架构 + 领域驱动设计 + CQRS 模式。

## PROJECT STRUCTURE

```
taotao-cloud-standalone/
├── api/                  # RPC/gRPC 接口定义 + DTO（纯接口模块，无业务依赖）
├── application/          # 应用层：CQRS Executor 编排、DTO 转换、事务、事件监听
│   ├── command/{biz}/
│   │   ├── dto/          # Cmd/Qry DTO + clientobject/
│   │   └── executor/     # 写Executor + query/ 读Executor
│   ├── service/          # 应用服务（薄门面，委托给 Executor）
│   ├── converter/        # MapStruct 转换器（DTO ↔ Domain Entity）
│   ├── adapter/          # 应用层适配器
│   └── event/            # 事件监听器（RocketMQ 等）
├── assembly/             # Spring Boot 启动器 + application.yml 环境配置
├── common/               # 公共工具、枚举、常量
├── domain/               # ★ 领域层（零外部依赖）
│   ├── aggregate/        # 聚合根
│   ├── entity/           # 实体
│   ├── val/              # 值对象（final 字段、无 setter、构造自验证）
│   ├── event/            # 领域事件
│   ├── repository/       # 仓储接口
│   ├── service/          # 领域服务（接口 + impl）
│   └── adapter/          # 领域适配器（防腐层接口）
├── facade/               # 防腐层（ACL）— 独立 Gradle 模块
├── infrastructure/       # 持久化、MQ、事件、缓存、配置
│   ├── persistent/
│   │   ├── persistence/  # JPA Entity（PO）
│   │   ├── mapper/       # MyBatis-Plus Mapper
│   │   ├── repository/   # Spring Data JPA Repository
│   │   └── params/       # 查询参数
│   ├── repository/       # 领域仓储实现
│   ├── event/            # 事件发布（Guava/Redis）
│   ├── cache/            # Redis 缓存
│   ├── roketmq/          # RocketMQ 配置
│   └── properties/       # 配置属性
└── interfaces/           # REST / RPC / gRPC
    ├── controller/
    │   ├── buyer/        # C 端 API
    │   ├── seller/       # B 端 API
    │   └── manager/      # 管理端 API
    ├── rpc/              # RPC 实现
    └── grpc/             # gRPC 实现
```

```
.opencode/
├── commands/             # 工作流命令（9 个）
├── instructions/         # 编码规范
├── skills/               # 技能脚本
├── AGENTS.md             # 项目知识库（本文件）
└── opencode.json         # OpenCode 配置
```

## WHERE TO LOOK

| Task | Location |
|------|----------|
| 新增业务功能 | `application/command/{biz}/executor/` 定义 Executor + `interfaces/controller/` 定义 API |
| 修改领域模型 | `domain/aggregate/` 或 `domain/entity/` |
| 值对象 | `domain/val/` — 所有字段 final，无 setter，构造自验证 |
| 领域事件 | `domain/event/` — 定义事件类 |
| 仓储接口 | `domain/repository/` — 接口在 domain |
| 仓储实现 | `infrastructure/repository/` — 实现 domain 接口 |
| 持久化对象 | `infrastructure/persistent/persistence/` — JPA @Entity PO |
| API 定义 | `api/rpc/` 或 `api/inner/` — 纯接口 + DTO |
| API 实现 | `interfaces/rpc/` 或 `interfaces/grpc/` |
| 外部接口适配 | `facade/` — 防腐层 |
| 应用层事件监听 | `application/event/listener/` — 业务事件处理 |
| 基础设施事件 | `infrastructure/event/` — 事件发布/订阅 |
| 消息监听 | `infrastructure/roketmq/` — RocketMQ 配置/监听 |
| 定时任务 | `infrastructure/job/` |
| MyBatis Mapper | `infrastructure/persistent/mapper/` |
| 代码转换器 | `application/converter/` — MapStruct DTO-Domain 转换 |

## CONVENTIONS

- **分层依赖方向**：`interfaces → application → domain ← infrastructure`
- **跨聚合通过 ID 引用**，非对象引用
- **事务边界仅开在 `application/` 层**（通过 `@Transactional`）
- **Controller 按角色分包**：buyer / seller / manager
- **CQRS 命令/查询命名**：`{动词}{名词}{Cmd|Qry}`，Executor 命名：`{动词}{名词}CmdExe`
- **App Service 薄门面**：只委托给 Executor，不含业务逻辑
- **领域模型与持久化模型分离**：domain entity ≠ PO
- **双持久化**：MyBatis-Plus（Mapper 层）+ JPA（Repository 层），PO 共用
- **MapStruct + Lombok + Record Builder 三件套**：减少样板代码

## APPLICATION LAYER (CQRS)

```
┌─────────────────────────────────────────────────────┐
│  Controller (interfaces/)                           │
├─────────────────────────────────────────────────────┤
│  Service (application/service/) · 薄门面            │
├─────────────────────────────────────────────────────┤
│  Executor (application/command/*/executor/)          │
│  ├── DictInsertCmdExe     · 写操作执行器            │
│  ├── DictUpdateCmdExe                               │
│  ├── DictDeleteCmdExe                               │
│  └── query/                                         │
│      ├── DictListQryExe     · 读操作执行器          │
│      └── DictGetQryExe                              │
├─────────────────────────────────────────────────────┤
│  Converter (application/converter/) · MapStruct     │
│  Adapter  (application/adapter/)   · 适配器        │
└─────────────────────────────────────────────────────┘
```

## ANTI-PATTERNS (THIS PROJECT)

- Controller 中写业务逻辑判断
- Controller 直接调用 Repository 或 Mapper
- 聚合根中注入 Repository 或 Domain Service
- 值对象中包含业务行为以外的逻辑（值对象应只含自验证和同对象方法）
- Application Service 中包含业务规则判断（应委托给领域服务）
- 跨聚合直接操作其他聚合的内部状态
- Application Service 直接调用 Mapper 或 Repository
- Executor 中写跨领域编排逻辑（应委托给 Service）

## UNIQUE STYLES

- **API/Interfaces 分离**：`api/` 模块只放接口定义和 DTO，`interfaces/` 模块放实现
- **CQRS + Executor 模式**：写操作用 `CmdExe`，读操作用 `QryExe`，Service 层只做委托
- **Controller 按端分层**：buyer / seller / manager 三个子包，各端 API 完全隔离
- **防腐层独立为模块**：`facade/` 作为独立 Gradle 模块
- **双持久化方案**：MyBatis-Plus（复杂查询）+ JPA（标准 CRUD），PO 共用
- **MapStruct + Lombok + Record Builder**：减少样板代码的同时保持不可变性
- **领域适配器**：`domain/adapter/` 定义防腐层接口，`facade/` 实现

## EVENT SYSTEM

```
┌──────────┐    ┌──────────────────────┐    ┌───────────────────────┐
│  Domain  │───▶│  Infrastructure      │───▶│  Application          │
│  Event   │    │  DomainEventPublisher │    │  Event Listeners      │
└──────────┘    └──────────────────────┘    └───────────────────────┘
                       │
                       ├── GuavaEventPublisher (同步)
                       ├── RedisEventPublisher (异步 Stream)
                       └── RocketMQ (infrastructure/roketmq/)
```

## COMMANDS

```bash
gradlew build                                                      # 全量构建
gradlew compileJava                                                # 仅编译
gradlew :taotao-cloud-standalone-assembly:bootRun --args='--spring.profiles.active=dev'  # 启动 dev 环境
gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain          # 代码质量门禁
gradlew test                                                        # 运行所有测试
gradlew :taotao-cloud-standalone-domain:test                        # 运行指定模块测试
gradlew jacocoTestReport                                            # 覆盖率报告
gradlew publishToMavenLocal                                         # 发布到本地 Maven
```

## NOTES

- JDK 25 预览特性，编译/运行均需要 `--enable-preview`
- `--add-exports` 大量 JDK 内部模块（`java.base/java.lang` 等）
- `taotao-cloud-dependencies:2026.07` BOM 未开源，外部构建需私有仓库凭据
- 四个环境配置：dev / test / pre / pro
- 代码质量门禁：Checkstyle + SpotBugs + PMD + Spotless + OWASP Dependency Check
- Gradle 9.5，管理工具：Develocity（`com.gradle.develocity` 插件）
- 包基础路径：`com.taotao.cloud.standalone`
