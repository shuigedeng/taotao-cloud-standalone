---
description: 生成 OpenAPI / Swagger 文档
---

# Swagger 文档生成

## 执行步骤

### 1. 确认 Knife4j 配置
检查 `build.gradle` 中 Knife4j 和 Swagger 依赖是否正确配置。

### 2. 启动服务
```bash
./gradlew :taotao-cloud-standalone-assembly:bootRun --args='--spring.profiles.active=dev'
```

### 3. 访问 API 文档
- Knife4j UI：`http://localhost:{port}/doc.html`
- OpenAPI JSON：`http://localhost:{port}/v3/api-docs`

### 4. 检查 API 完整性
- 所有 Controller 是否有 `@Tag` 注解
- 所有接口方法是否有 `@Operation` 注解
- DTO 字段是否有 `@Schema` 注解
- 请求/响应体是否完整定义
