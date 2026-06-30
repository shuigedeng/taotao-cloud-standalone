# taotao-cloud-standalone — DDD + CQRS 单体服务

## 技术栈

| 技术 | 版本 |
|------|------|
| JDK | 25（预览特性，需 `--enable-preview`） |
| Gradle | 9.5 |
| Spring Boot | 4.1.0 |
| Spring Cloud | 2025.1.1 |
| Spring Cloud Alibaba | 2025.1.0.0 |
| Spring Security | 7.1.0 |
| MyBatis-Plus | 3.5.16 |
| Querydsl | 5.1.0 |
| MapStruct | 1.6.3（`defaultComponentModel=spring`，无需重复声明） |
| Hutool | 5.8.44 |
| Knife4j / Swagger | 4.5.0 / 3.0.0 |
| Redisson | 4.3.1 |
| RocketMQ | 5.2.4 |
| Netty | 4.2.12.Final |

`build.gradle` 使用 **ext** 管理版本号（非 `libs.versions.toml`）。`taotao-cloud-dependencies:2026.08` 为私有 BOM，外部构建需私有仓库凭据。

## 项目结构

```
taotao-cloud-standalone/
├── api/                        # RPC/gRPC 接口定义 + DTO（纯接口，无业务依赖）
├── application/                # 应用层
│   └── command/{biz}/
│       ├── dto/                # Cmd/Qry DTO + clientobject/
│       └── executor/           # 写 Executor + query/ 读 Executor
├── assembly/                   # Spring Boot 启动器 + application-{env}.yml
├── common/                     # 公共工具、枚举、常量
├── domain/                     # ★ 领域层（零 Spring/DB/框架依赖）
│   ├── aggregate/              # 聚合根
│   ├── entity/                 # 实体
│   ├── val/                    # 值对象（final、无 setter、构造自验证）
│   ├── event/                  # 领域事件
│   ├── repository/             # 仓储接口
│   └── service/                # 领域服务（接口+实现）
├── facade/                     # 防腐层（ACL）独立模块
├── infrastructure/             # 持久化、MQ、缓存、配置
│   └── persistent/
│       ├── persistence/        # JPA @Entity（PO）
│       ├── mapper/             # MyBatis-Plus Mapper
│       └── repository/         # Spring Data JPA Repository
└── interfaces/                 # REST / RPC / gRPC
    └── controller/
        ├── buyer/              # C 端
        ├── seller/             # B 端
        └── manager/            # 管理端
```

基础包路径：`com.taotao.cloud.standalone`

## 硬性规则

### 依赖方向（严格禁止违反）
```
api  ←  interfaces  →  application  →  facade
                          ↓
                    domain  ←  infrastructure
```
- `domain`：零外部依赖（不依赖 Spring、DB、框架）
- `application`：依赖 `domain`，可依赖 `facade` 接口
- `infrastructure`：依赖 `domain` 实现仓储，依赖 `application` 实现事件订阅
- `interfaces`：依赖 `application`，**不直接依赖** `infrastructure`

### 禁止项
- Controller 中写业务逻辑或直接调用 Repository/Mapper
- 聚合根中注入 Repository 或 Domain Service
- Application Service / Executor 中包含业务规则判断
- 跨聚合直接操作其他聚合的内部状态（通过 ID 引用）
- Application Service 直接调用 Mapper/Persistence

## 架构约定

### CQRS + Executor 模式
- `application/command/{biz}/executor/DictInsertCmdExe.java`：写 Executor 负责参数校验 → 调用领域服务 → 返回结果
- `application/command/{biz}/executor/query/DictListQryExe.java`：读 Executor 负责查询逻辑
- `application/service/`：薄门面 Service，仅委托给 Executor（不包含业务逻辑）

### 双持久化
| 场景 | 技术 | 位置 |
|------|------|------|
| 标准 CRUD | JPA Repository | `infrastructure/persistent/repository/` |
| 复杂查询 | MyBatis-Plus Mapper | `infrastructure/persistent/mapper/` |
| 领域仓储实现 | 组合两者 | `infrastructure/repository/` |

### 领域模型 vs 持久化模型
- `domain/entity/DictEntity.java` — 纯业务领域模型
- `infrastructure/persistent/persistence/DictPO.java` — JPA `@Entity` 持久化对象，继承 `BasePO`

### Controller 分包
- 三端隔离：`buyer/` / `seller/` / `manager/`，各自独立 `@RequestMapping`
- Controller 只做：参数校验 + 调用 Service + 封装 `Result`

### 事件架构
```
domain/event/                    # 事件定义
  → infrastructure/event/        # 发布（Guava 同步 / Redis Stream 异步）
    → application/event/         # 监听（RocketMQ 等）
```

### 命名规范

| 元素 | 模式 | 示例 |
|------|------|------|
| 聚合根 | `{Entity}AggregateRoot` | `DictAggregateRoot` |
| 实体 | `{Entity}Entity` | `DictEntity` |
| 值对象 | `{Entity}Val` | `DictVal` |
| 领域事件 | `{Entity}{Action}Event` | `DictCreateEvent` |
| 仓储接口 | `{Entity}DomainRepository` | `DictDomainRepository` |
| 领域服务 | `{Entity}DomainService` | `DictDomainService` |
| Cmd DTO | `{Entity}{Action}Cmd` | `DictInsertCmd` |
| Qry DTO | `{Entity}{Action}Qry` | `DictListQry` |
| 写 Executor | `{Entity}{Action}CmdExe` | `DictInsertCmdExe` |
| 读 Executor | `{Entity}{Action}QryExe` | `DictListQryExe` |
| 应用 Service | `{Entity}sService` / `{Entity}sServiceImpl` | `DictsService` / `DictsServiceImpl` |
| PO | `{Entity}PO` | `DictPO` |
| Mapper | `{Entity}Mapper` | `DictMapper` |
| Controller | `{Role}{Entity}Controller` | `ManagerDictController` |

## 常用命令

```bash
# 编译
gradlew compileJava

# 全量构建
gradlew build

# 启动 dev 环境
gradlew :taotao-cloud-standalone-assembly:bootRun --args='--spring.profiles.active=dev'

# 运行所有测试
gradlew test

# 运行指定模块测试
gradlew :taotao-cloud-standalone-domain:test

# 代码质量门禁
gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# 安全依赖检查
gradlew dependencyCheckAnalyze

# 覆盖率报告
gradlew jacocoTestReport

# 打包可执行 JAR
gradlew :taotao-cloud-standalone-assembly:bootJar

# 发布到本地 Maven
gradlew publishToMavenLocal
```

## 环境配置

| 环境 | 配置文件 | 用途 |
|------|----------|------|
| dev | `application-dev.yml` | 本地开发 |
| test | `application-test.yml` | 测试环境 |
| pre | `application-pre.yml` | 预发布 |
| pro | `application-pro.yml` | 生产环境 |

## 详细规则参考

DDD 聚合设计、值对象、领域服务、代码风格、API 规范、测试规范等详细规则分散在 `.claude/rules/` 目录中，按需查阅：

- `ttc-architecture.md` — 架构合规、分层约束
- `ttc-aggregate-design.md` — 聚合根设计原则
- `value-object.md` — 值对象不可变规范
- `ttc-domain-service.md` — 领域服务模式
- `ttc-code-style.md` — 代码风格、Lombok + MapStruct 用法
- `ttc-api-conventions.md` — API 命名、版本、响应格式
- `ttc-testing.md` — 测试分层策略

## 已知陷阱

- **JDK 25 预览特性**：编译和运行均需 `--enable-preview`，Gradle JVM 参数在 `gradle.properties` 中已有配置
- **JVM `--add-exports`**：大量 JDK 内部模块访问（`java.base/java.lang`、`jdk.compiler/com.sun.tools.javac.*`），已配置在 `gradle.properties` 中
- **私有 BOM**：`taotao-cloud-dependencies:2026.08` 需私有仓库凭据，外部构建无法直接运行
- **代码质量门禁**：Checkstyle + SpotBugs + PMD + Spotless + OWASP 均为强制步骤，`build` 任务中包含
- **MapStruct**：`defaultComponentModel=spring` 已全局配置，Mapper 接口上无需重复声明 `@Mapper(componentModel = "spring")`
- **IDEA 插件**：推荐安装 TaoTaoToolKit（项目定制插件）
