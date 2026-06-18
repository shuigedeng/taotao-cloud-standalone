# 值对象设计规范 — taotao-cloud-standalone

值对象放在 `domain/val/` 包下。

## 核心特性

### 1. 不可变性
```java
// domain/val/DictVal.java
public final class DictVal {
    private final String code;
    private final String value;

    public DictVal(String code, String value) {
        // 构造时自验证
        if (code == null || code.isBlank()) {
            throw new DomainException("编码不能为空");
        }
        this.code = code;
        this.value = value;
    }

    public String getCode() { return code; }
    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DictVal)) return false;
        DictVal dictVal = (DictVal) o;
        return Objects.equals(code, dictVal.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
```

### 2. 自验证
值对象在构造时必须验证自身有效性，不允许创建无效的值对象。

### 3. 行为内聚
值对象可以包含与其相关的业务行为：

```java
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("货币类型不匹配");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }
}
```

## 本项目值对象模式

### 简单值对象
```java
// domain/val/DictVal.java — 只封装，无额外行为
public class DictVal {
    private final String code;
    private final String value;
}
```

### 值对象规范总结
| 要求 | 说明 |
|------|------|
| 包路径 | `domain/val/` |
| 字段 | 全部 `private final` |
| Setter | 不允许 |
| 构造验证 | 必须校验参数合法性 |
| equals/hashCode | 基于所有值字段覆写 |
| JPA 映射 | 使用 `@Embeddable`（如需） |
