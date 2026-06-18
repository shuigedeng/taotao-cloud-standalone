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
| MapStruct | 1.6.3 |
| Hutool | 5.8.44 |
| Knife4j / Swagger | 4.5.0 / 3.0.0 |
| Redisson | 4.3.1 |
| RocketMQ | 5.2.4 |
| Netty | 4.2.12.Final |

## 项目结构 — 八模块 DDD 分层

```
taotao-cloud-standalone/
├── api/                  # RPC/gRPC 接口定义 + DTO（纯接口模块，无业务依赖）
├── application/          # 应用层：CQRS Executor 编排、DTO 转换、事务、事件监听
│   ├── command/{biz}/
│   │   ├── dto/                    # Cmd/Qry DTO + clientobject/
│   │   └── executor/               # 写 Executor + query/ 读 Executor
│   ├── service/                    # 应用服务（薄门面，委托给 Executor）
│   ├── converter/                  # MapStruct 转换器（DTO ↔ Domain Entity）
│   ├── adapter/                    # 应用层适配器
│   └── event/                      # 事件监听（RocketMQ 等）
├── assembly/             # Spring Boot 启动器 + application-{env}.yml
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
│   │   ├── persistence/  # JPA @Entity（PO）
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

## 核心架构约定

### 依赖方向（严格禁止违反）
```
api  ←  interfaces  →  application  →  facade
                          ↓
                    domain  ←  infrastructure
```
- `domain`：零外部依赖（不依赖 Spring、不依赖数据库、不依赖框架）
- `application`：依赖 `domain`，可依赖 `facade` 接口
- `infrastructure`：依赖 `domain` 实现仓储，依赖 `application` 实现事件订阅
- `interfaces`：依赖 `application`，不直接依赖 `infrastructure`

### CQRS + Executor 模式
```java
// application/command/dict/executor/DictInsertCmdExe.java
@Component
@RequiredArgsConstructor
public class DictInsertCmdExe {
    private final DictDomainService dictDomainService;

    public Boolean execute(DictInsertCmd cmd) {
        // 参数校验 → 调用领域服务 → 返回结果
        return dictDomainService.insert(dictConvertor.toEntity(cmd));
    }
}

// application/service/impl/DictsServiceImpl.java（薄门面）
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsService {
    private final DictInsertCmdExe dictInsertCmdExe;

    public Boolean insert(DictInsertCmd cmd) {
        return dictInsertCmdExe.execute(cmd);  // 仅委托
    }
}
```

### 领域模型 vs 持久化模型分离
```java
// domain/entity/DictEntity.java — 领域模型，纯业务
public class DictEntity { ... }

// infrastructure/persistent/persistence/DictPO.java — 持久化对象
@Entity
@Table(name = "ttc_dict")
public class DictPO extends BasePO<DictPO> { ... }
```

### Controller 分包规范
```java
@Validated
@RestController
@RequestMapping("/sys/manager/dict")   // manager 端
@RequestMapping("/buyer/dict")          // buyer 端
@RequestMapping("/seller/dict")         // seller 端
public class ManagerDictController extends BusinessController {
    // 只做：参数校验 + 调用 Service + Result 封装
    // 禁止：业务逻辑、直接调用 Repository/Mapper
}
```

## 双持久化方案

| 场景 | 技术 | 位置 |
|------|------|------|
| 标准 CRUD | JPA Repository | `infrastructure/persistent/repository/` |
| 复杂查询 | MyBatis-Plus Mapper | `infrastructure/persistent/mapper/` |
| 领域仓储实现 | 组合两者 | `infrastructure/repository/` |

## 事件驱动架构

```
Domain Event (domain/event/)
  → DomainEventPublisher (infrastructure/event/)
    → GuavaEventPublisher (同步)
    → RedisEventPublisher (异步 Stream)
    → RocketMQ (infrastructure/roketmq/)
      → Application Event Listeners (application/event/)
```

## DDD 核心原则

### 聚合设计
- 跨聚合通过 ID 引用，非对象引用
- 聚合根负责维护内部不变量
- 事务边界仅开在 `application/` 层

### 值对象
- 不可变：所有字段 final，无 setter
- 构造时自验证
- 通过属性值判断相等性（equals/hashCode）

### 领域事件
- 事件定义在 `domain/event/`
- 发布在 `infrastructure/event/`
- 订阅在 `application/event/listener/` 或 `infrastructure/event/`

## 重复代码预防（关键禁止项）
- Controller 中写业务逻辑判断
- Controller 直接调用 Repository 或 Mapper
- 聚合根中注入 Repository 或 Domain Service
- Application Service / Executor 中包含业务规则判断
- 跨聚合直接操作其他聚合的内部状态
- Application Service 直接调用 Mapper/Persistence
- Executor 中写跨领域编排逻辑（应在 Service 层）

## 命名规范

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
| 应用 Service | `{Entity}Service` / `{Entity}ServiceImpl` | `DictsService` / `DictsServiceImpl` |
| 转换器 | `{Entity}Convert` | `DictConvert` |
| PO | `{Entity}PO` | `DictPO` |
| Mapper | `{Entity}Mapper` | `DictMapper` |
| Controller | `{Role}{Entity}Controller` | `ManagerDictController` |

## 构建与测试命令

```bash
# 编译
./gradlew compileJava

# 全量构建
./gradlew build

# 启动 dev 环境
./gradlew :taotao-cloud-standalone-assembly:bootRun --args='--spring.profiles.active=dev'

# 运行测试
./gradlew test
./gradlew :taotao-cloud-standalone-domain:test

# 代码质量门禁
./gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# 安全依赖检查
./gradlew dependencyCheckAnalyze

# 覆盖率报告
./gradlew jacocoTestReport

# 发布到本地 Maven
./gradlew publishToMavenLocal

# 打包可执行 JAR
./gradlew :taotao-cloud-standalone-assembly:bootJar
```

## 环境配置

| 环境 | 配置文件 | 用途 |
|------|----------|------|
| dev | `application-dev.yml` | 本地开发 |
| test | `application-test.yml` | 测试环境 |
| pre | `application-pre.yml` | 预发布 |
| pro | `application-pro.yml` | 生产环境 |

## 特殊注意事项

- JDK 25 预览特性：编译/运行均需要 `--enable-preview`
- 大量 `--add-exports`：JDK 内部模块访问（如 `java.base/java.lang`）
- `taotao-cloud-dependencies:2026.07` 为私有 BOM，外部构建需要私有仓库凭据
- 代码质量门禁：Checkstyle + SpotBugs + PMD + Spotless + OWASP
- Gradle 管理工具：Develocity（`com.gradle.develocity` 插件）
- 基础包路径：`com.taotao.cloud.standalone`
- `build.gradle` 使用 ext 管理版本号，非 `libs.versions.toml` 版本目录
- MapStruct 配置了 `defaultComponentModel=spring`，无需在每个 Mapper 上声明
- Lombok + MapStruct + Record Builder 组合使用
- IDEA 推荐插件：TaoTaoToolKit
