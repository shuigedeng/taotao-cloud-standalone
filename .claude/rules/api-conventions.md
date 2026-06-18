# API 设计规范 — taotao-cloud-standalone

## RESTful 约定

### URL 格式
```
/{role}/{module}/{resource}[/{id}][/action]
```

角色 (`role`) 区分三端：
| 角色 | 前缀 | 控制器包 | 示例 |
|------|------|---------|------|
| 买家 | `buyer` | `controller/buyer/` | `/buyer/goods` |
| 卖家 | `seller` | `controller/seller/` | `/seller/goods` |
| 管理员 | `manager` 或 `sys` | `controller/manager/` | `/sys/manager/dict` |

### HTTP 方法映射
| 方法 | 用途 | 示例 | 状态码 |
|------|------|------|--------|
| GET | 查询 | `GET /sys/manager/dict/page` | 200 |
| GET | 详情 | `GET /sys/manager/dict/{id}` | 200 |
| POST | 创建 | `POST /sys/manager/dict` | 200 |
| PUT | 更新 | `PUT /sys/manager/dict` | 200 |
| DELETE | 删除 | `DELETE /sys/manager/dict/{id}` | 200 |

### 统一响应格式
```java
// com.taotao.boot.common.model.Result
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;
}

// 分页响应
public class PageResult<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}
```

## Knife4j / Swagger 注解

```java
@Tag(name = "管理端-字典管理API", description = "管理端-字典管理API")
@RestController
@RequestMapping("/sys/manager/dict")
public class ManagerDictController extends BusinessController {

    @Operation(summary = "分页查询字典列表")
    @GetMapping("/page")
    public Result<PageResult<DictCO>> page(DictListQry qry) {
        return Result.success(dictsService.list(qry));
    }

    @Operation(summary = "新增字典")
    @PostMapping
    public Result<Boolean> insert(@Valid @RequestBody DictInsertCmd cmd) {
        return Result.success(dictsService.insert(cmd));
    }
}
```

## 参数校验（Bean Validation）

```java
public class DictInsertCmd {
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称不能超过100个字符")
    private String dictName;

    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    @Min(value = 0, message = "排序值不能小于0")
    private Integer sortNum;
}
```

## API 规范禁止项

- ❌ 接口路径中出现动词（如 `/getDict` → 改为 `GET /dict/{id}`）
- ❌ Controller 方法中使用 `HttpServletRequest`/`HttpServletResponse`（应通过参数绑定）
- ❌ 返回 Entity 对象（应返回 DTO 或 CO）
- ❌ 在 GET 请求中使用 `@RequestBody`
