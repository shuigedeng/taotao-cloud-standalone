---
name: security-review
description: 安全审查 — OWASP 依赖检查 + 代码安全审计
triggers:
  - "安全审查"
  - "安全审计"
  - "漏洞扫描"
---

# 安全审查技能

适配项目：taotao-cloud-standalone

## 执行步骤

### 1. OWASP 依赖检查
```bash
./gradlew dependencyCheckAnalyze
```

### 2. 代码静态分析
```bash
./gradlew spotbugsMain
```

### 3. 安全审查清单
- 硬编码密码/密钥
- SQL 注入（检查 MyBatis `$` 拼接）
- XSS 防护
- 权限控制（buyer/seller/manager 角色隔离）
- 敏感数据脱敏
- API 限流/防重放
- JWT/Token 安全

### 4. 检查位置
| 检查项 | 重点关注文件 |
|--------|------------|
| SQL 注入 | `infrastructure/persistent/mapper/*.xml` |
| 权限控制 | `interfaces/controller/**/*.java` |
| 敏感数据 | `common/`, `domain/event/` |
