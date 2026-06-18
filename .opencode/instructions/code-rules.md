# 项目编码规范 — taotao-cloud-standalone

> 补充 DDD 架构规范（详见 `.opencode/AGENTS.md` 和 `.opencode/commands/`）中未覆盖的实现细节

---

## 1. 模块依赖规则

```
api  ←  interfaces  ←  application  →  facade
                          ↓
                     domain  ←  infrastructure
```

- `domain`：零外部依赖，不依赖 Spring、不依赖数据库
- `application`：依赖 `domain`，可依赖 `facade` 接口，不依赖 `infrastructure`
- `infrastructure`：依赖 `domain` 实现仓储，依赖 `application` 实现事件订阅
- `interfaces`：依赖 `application`，不直接依赖 `infrastructure`
- `api`：纯 DTO + 接口定义，不依赖任何业务模块

### 禁止违反的依赖
```java
// ❌ 禁止：Controller 直接调用 Repository
@Autowired private OrderRepository orderRepository;

// ❌ 禁止：Application Service 直接调用 Mapper
@Autowired private OrderMapper orderMapper;

// ❌ 禁止：Domain Service 注入 Repository
@Autowired private OrderRepository orderRepository;

// ✅ 正确：Application Service 通过仓储接口操作持久化
private final OrderDomainRepository orderRepository;
```

## 2. 包结构规范

```
com.taotao.cloud.standalone.{module}/
├── aggregate/     # 聚合根（@AggregateRoot）
├── entity/        # 实体（@Entity）
├── val/           # 值对象（final 字段、无 setter、构造自验证）
├── event/         # 领域事件
├── repository/    # 仓储接口
├── service/       # 领域服务（@DomainService）
└── adapter/       # 领域适配器（防腐层接口）
```

## 3. Application 层规范（CQRS + Executor 模式）

本项目 Application 层采用 **CQRS + Executor** 模式，而非传统的 Service 直调仓储模式。

### 目录结构
```
application/
├── command/{biz}/dto/          # Command/Query DTO
│   ├── DictInsertCmd.java
│   ├── DictUpdateCmd.java
│   ├── DictDeleteCmd.java
│   ├── DictListQry.java        # 查询（Query）
│   ├── DictGetQry.java
│   └── clientobject/           # 客户端返回对象（CO）
│       └── DictCO.java
├── command/{biz}/executor/     # Command Executor（写）
│   ├── DictInsertCmdExe.java
│   ├── DictUpdateCmdExe.java
│   └── query/                  # Query Executor（读）
│       └── DictListQryExe.java
├── service/                    # 应用服务（门面）
│   ├── DictsService.java       # 接口
│   └── impl/DictsServiceImpl.java
├── converter/                  # MapStruct 转换器（DTO ↔ Domain）
│   └── DictConvert.java
└── adapter/                    # 应用层适配器
    └── DictAdapter.java
```

### Executor 规范
```java
@Component
@RequiredArgsConstructor
public class DictInsertCmdExe {
    private final DictDomainService dictDomainService;

    public Boolean execute(DictInsertCmd cmd) {
        // 1. 参数校验
        // 2. 调用领域服务
        // 3. 返回结果
        return dictDomainService.insert(dictConvertor.toEntity(cmd));
    }
}
```

### 应用服务（Service）规范
```java
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsService {
    // 委托给 Executor，不做业务逻辑
    private final DictInsertCmdExe dictInsertCmdExe;

    public Boolean insert(DictInsertCmd cmd) {
        return dictInsertCmdExe.execute(cmd);
    }
}
```

## 4. Controller 规范

```java
@Validated
@RestController
@RequestMapping("/sys/manager/dict")
@Tag(name = "管理端-字典管理API", description = "管理端-字典管理API")
public class ManagerDictController extends BusinessController {
    // 只做：参数校验 + 调用 Service + Result 封装
    // 禁止：业务逻辑判断、直接调用 Repository/Mapper
}
```

Controller 按端分包：
```
interfaces/controller/
├── buyer/        # C 端 API
├── seller/       # B 端 API
└── manager/      # 管理端 API
```

接口（RPC/gRPC）在 `interfaces/rpc/` 和 `interfaces/grpc/` 中实现，接口定义在 `api/` 模块。

## 5. 枚举规范

```java
// 枚举在 common 模块定义
public enum OrderStatusEnum {
    PENDING("待付款"),
    PAID("已付款"),
    DELIVERED("已发货"),
    RECEIVED("已收货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;
}
```

## 6. 领域事件规范

```java
// 事件定义在 domain/event/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DictCreateEvent {
    private String name;
}

// 事件发布：infrastructure/event/DomainEventPublisher
// 事件订阅：infrastructure/event/ + application/event/listener/
// 消息队列：RocketMQ（infrastructure/roketmq/RocketmqConfig）
//          Redis Stream（infrastructure/event/RedisEventPublisher）
```

## 7. 数据转换规范

### MapStruct（application/converter/）
```java
// DTO ↔ Domain Entity
@Mapper
public interface DictConvert {
    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);
    DictEntity convert(DictListQry dto);
    DictCO convert(DictEntity entity);
}
```

### Assembler（持久化转换在 infrastructure/）
```java
// Infrastructure 内实现：PO ←→ Domain Entity
@Mapper(componentModel = "spring")
public interface DictAssembler {
    DictPO toPo(DictEntity entity);
    DictEntity toDomain(DictPO po);
}
```

## 8. 构建与测试

```bash
# 全量构建
gradlew build

# 运行所有测试
gradlew test

# 运行指定模块测试
gradlew :taotao-cloud-standalone-domain:test

# 代码质量
gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# 本地启动
gradlew :taotao-cloud-standalone-assembly:bootRun --args='--spring.profiles.active=dev'
```

## 9. 领域服务与适配器规范

> 注：领域服务的初始接口定义和实现规范。领域服务放在 `domain/service/` 下而非 `domain/service/impl/`，实际编码时根据复杂度选择是否拆分接口和实现。

### 领域服务（domain/service/）
```java
public interface DictDomainService {
    Boolean insert(DictEntity dictEntity);
    Boolean update(DictEntity dictEntity);
    DictEntity getById(Long id);
    Boolean deleteById(Long id);
    IPage<DictEntity> list(DictEntity dictEntity, PageQuery pageQuery);
}
```
- 领域服务在 `domain/service/` 定义接口，在 `domain/service/impl/` 实现
- 领域服务可调用仓储接口（`domain/repository/`），但不依赖基础设施

### 领域适配器（domain/adapter/）
```java
// 防腐层接口，定义在 domain/adapter/
// 实现在 facade/ 模块
public interface DictDomainAdapter {
    // 外部系统调用的防腐方法
}
```

## 10. Infrastructure 层规范

### 目录结构
```
infrastructure/
├── persistent/
│   ├── persistence/    # JPA Entity（PO）
│   ├── mapper/         # MyBatis-Plus Mapper
│   ├── repository/     # Spring Data JPA Repository
│   └── params/         # 查询参数对象
├── repository/          # 领域仓储实现
├── event/               # 事件发布/订阅
├── cache/               # Redis 缓存
├── roketmq/             # RocketMQ 配置
├── properties/          # 配置属性
├── factory/             # 工厂类
└── util/                # 工具类
```

### 仓储实现规范
```java
@Service
@AllArgsConstructor
public class DictDomainRepositoryImpl implements DictDomainRepository {
    // 在 infrastructure/repository/ 中实现 domain/repository/ 接口
    // 使用 infrastructure/persistent/ 下的 Mapper 或 Repository
}
```

## 11. 数据库规范

### 表必备字段
```sql
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`create_by` bigint DEFAULT NULL COMMENT '创建人ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT NULL COMMENT '更新人ID',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`is_deleted` tinyint(1) DEFAULT 0 COMMENT '删除标记',
`tenant_id` bigint DEFAULT 0 COMMENT '租户ID',
`version` int DEFAULT 0 COMMENT '乐观锁'
```

### 禁止
- 循环中查询数据库（N+1 问题）
- `SELECT *`
- 在 Java 代码中拼接 SQL
- 跨聚合直接操作其他聚合的数据表


