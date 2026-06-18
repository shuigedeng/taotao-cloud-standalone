# 聚合设计规范 — taotao-cloud-standalone

## 聚合识别原则

### 1. 事务边界
聚合内修改在一个事务中完成，聚合间使用最终一致性（通过领域事件）。

### 2. 一致性规则
- **强一致性**: 聚合内保证
- **最终一致性**: 聚合间通过领域事件保证

### 3. 跨聚合引用
通过 ID 引用，而非对象引用：

```java
// ✅ 正确：跨聚合用 ID
public class DictAggregateRoot {
    private Long id;
    private String dictName;
    private List<DictItem> items;  // 聚合内实体用对象引用
    // 无跨聚合引用（本聚合不依赖其他聚合）
}

// ❌ 错误：不应直接引用其他聚合的对象
public class OrderAggregateRoot {
    private Customer customer;  // Customer 是另一个聚合根
}

// ✅ 正确：改为 ID 引用
public class OrderAggregateRoot {
    private Long customerId;    // 跨聚合用 ID
}
```

## 聚合根设计模板

```java
// domain/aggregate/DictAggregateRoot.java
public class DictAggregateRoot {
    // 聚合内实体用对象引用
    private List<DictEntity> items;
    // 跨聚合用 ID 引用
    private Long parentId;

    // 业务行为方法（不是 setter）
    public void addItem(DictEntity item) {
        // 校验业务规则
        // 修改内部状态
        // 注册领域事件
    }

    // 无参构造（JPA 要求），protected
    protected DictAggregateRoot() {}

    // 静态工厂方法
    public static DictAggregateRoot create(String name) { ... }
}
```

## 聚合根方法设计

### 命令方法（状态变更 — 有业务语义的名字）
```java
// ✅ 正确：有业务语义
public void submit() { ... }
public void cancel(String reason) { ... }
public void activate() { ... }

// ❌ 错误：贫血模型
public void setStatus(OrderStatus status) { ... }
```

### 查询方法（只读）
```java
public boolean isPending() { return status == OrderStatus.PENDING; }
public boolean canTransitionTo(OrderStatus target) { ... }
```

## 聚合大小
- **小聚合原则**: 一个聚合根通常只包含 1-3 个实体
- **性能考虑**: 避免加载过多不必要的数据

## 不变性维护

聚合根必须保证内部不变量，所有业务规则在聚合内执行：

```java
public class OrderAggregateRoot {
    public void addItem(ProductId productId, Money price, int quantity) {
        if (status != OrderStatus.PENDING) {
            throw new DomainException("只有待支付订单可以添加商品");
        }
        if (quantity <= 0) {
            throw new DomainException("数量必须大于 0");
        }
        // 业务逻辑...
    }
}
```

## 聚合根禁止行为

- ❌ 注入 Repository 或 Domain Service
- ❌ 包含基础设施逻辑（如序列化、网络调用）
- ❌ 暴露内部集合的引用（应返回不可变副本）
