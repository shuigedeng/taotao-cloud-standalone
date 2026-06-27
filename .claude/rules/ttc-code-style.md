# 代码风格规范 — taotao-cloud-standalone

## 格式化规则

| 规则 | 值 |
|------|-----|
| 缩进 | 4 空格（不使用 Tab） |
| 行宽 | 120 字符（Checkstyle 校验） |
| 大括号 | K&R 风格（左括号不换行） |
| 编码 | UTF-8 |
| JDK | 25（预览特性，`--enable-preview`） |

## 包命名规范

```
com.taotao.cloud.standalone.{layer}.{subdomain}
```

示例：
- `com.taotao.cloud.standalone.domain.aggregate`
- `com.taotao.cloud.standalone.domain.val`
- `com.taotao.cloud.standalone.application.command.dict.executor`
- `com.taotao.cloud.standalone.interfaces.controller.manager`

## 命名约定

| 元素 | 命名风格 | 示例 |
|------|---------|------|
| 类名 | PascalCase | `DictAggregateRoot`, `DictsServiceImpl` |
| 接口 | PascalCase | `DictDomainService`, `DictsService` |
| 方法 | 小驼峰 | `insert()`, `getById()`, `deleteById()` |
| 变量 | 小驼峰 | `dictEntity`, `dictInsertCmd` |
| 常量 | UPPER_SNAKE | `TABLE_NAME`, `DEFAULT_PAGE_SIZE` |
| 枚举 | PascalCase | `OrderStatusEnum.PENDING` |
| 注解 | PascalCase + 无 I 前缀 | `@Service`, `@Component`（非 `@IService`） |

## 导入顺序（Checkstyle 校验）

1. Java 标准库（`java.*`, `javax.*`）
2. 第三方库（`org.*`, `com.*` 非本项目）
3. Spring 框架（`org.springframework.*`）
4. 项目内部包（`com.taotao.*`）
5. 静态导入
6. 每两组之间空一行

## 代码生成工具

### MapStruct
```java
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DictConvert {
    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);
    DictPO toPo(DictEntity entity);
    DictEntity toDomain(DictPO po);
}
```

### Lombok
```java
@Data                              // 简单 PO/DTO
@SuperBuilder                      // 含继承的构建器
@RequiredArgsConstructor           // final 字段构造注入
@Slf4j                            // 日志
@AllArgsConstructor / @NoArgsConstructor  // 全参/无参
```

### Record Builder（JDK 25 预览特性）
```java
@RecordBuilder
public record DictInsertCmd(String name, String code) {}
```

## 质量门禁

项目配置了自动检查的 Gradle 插件：

| 工具 | 检查项 |
|------|--------|
| Checkstyle | 代码风格、导入顺序 |
| Spotless | 格式化自动修复 |
| PMD | 潜在缺陷、死代码 |
| SpotBugs | 字节码级别 Bug 检测 |
| OWASP | 依赖安全漏洞 |

运行命令：
```bash
./gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain
```
