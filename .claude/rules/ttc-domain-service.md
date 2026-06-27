# 领域服务设计规范 — taotao-cloud-standalone

领域服务在 `domain/service/` 包下定义接口，在 `domain/service/impl/` 下实现。

## 何时使用领域服务

### ✅ 适用场景
1. **跨聚合的业务逻辑** — 协调多个聚合
2. **无状态的计算服务** — 纯粹的业务计算
3. **外部领域概念** — 不属于任何一个聚合根的业务操作

### ❌ 不适用场景
1. **应该属于聚合根的行为** — 如果方法只操作一个聚合内部状态
2. **纯粹的技术性操作** — 属于 infrastructure 层
3. **应用层的用例编排** — 属于 application 层的 Service 或 Executor

## 领域服务模板

```java
// domain/service/DictDomainService.java — 接口
public interface DictDomainService {
    Boolean insert(DictEntity dictEntity);
    Boolean update(DictEntity dictEntity);
    DictEntity getById(Long id);
    Boolean deleteById(Long id);
    IPage<DictEntity> list(DictEntity dictEntity, PageQuery pageQuery);
}

// domain/service/impl/DictDomainServiceImpl.java — 实现
public class DictDomainServiceImpl implements DictDomainService {
    private final DictDomainRepository dictDomainRepository;

    @Override
    public Boolean insert(DictEntity dictEntity) {
        // 业务规则校验
        if (dictEntity.getDictName() == null) {
            throw new DomainException("字典名称不能为空");
        }
        // 调用仓储保存
        return dictDomainRepository.save(dictEntity);
    }
}
```

## 规范

### 1. 无状态设计
领域服务不持有状态，只提供行为。所有状态通过方法参数传入。

### 2. 业务语义明确
方法名必须表达业务意图，而非技术操作。

```java
// ✅ 好的命名
public boolean isDictCodeDuplicated(String code) { ... }
public void validateDictBeforeDelete(Long id) { ... }

// ❌ 不好的命名
public void check(String code) { ... }
public void doAction(Long id) { ... }
```

### 3. 领域服务 vs 应用服务

| 对比维度 | 领域服务 (Domain Service) | 应用服务 (Application Service) |
|---------|------------------------|-----------------------------|
| 位置 | `domain/service/` | `application/service/` |
| 职责 | 业务规则、领域逻辑 | 用例编排、事务、权限 |
| 依赖 | 仓储接口 (`domain/repository/`) | Executor、领域服务 |
| 技术注解 | 无 Spring 注解 | `@Service`, `@Transactional` |
| 事务 | 不管理事务 | 管理事务边界 |

### 4. 异常处理
- 领域服务抛 `DomainException`（或子类）
- 不捕获技术异常（留给基础设施层处理）
