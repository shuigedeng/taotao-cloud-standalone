# Security Scan — 安全扫描技能

适配项目：taotao-cloud-standalone（DDD + Gradle + JDK 25）

## 触发条件

通过命令 `/review` 安全审查阶段或用户明确要求安全扫描时触发。

## 扫描内容

### 1. OWASP 依赖检查
```bash
gradlew dependencyCheckAnalyze
```

### 2. 代码静态分析
```bash
gradlew spotbugsMain spotbugsTest
```

### 3. 敏感信息检查
- 代码中是否硬编码密码/密钥
- application-{env}.yml 中的敏感配置
- 是否包含 .env 文件

### 4. DDD 安全规范
- 领域事件是否包含敏感信息
- 跨聚合 ID 引用是否暴露内部 ID
- API 接口权限控制是否完善（buyer/seller/manager 角色隔离）
