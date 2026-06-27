---
description: 运行测试并生成 JaCoCo 覆盖率报告
parameters:
  - name: module
    type: string
    description: 测试模块（domain/application/interfaces）
    required: false
  - name: coverage
    type: boolean
    default: true
---

# 测试执行

## 执行步骤

### 1. 清理并编译
```bash
./gradlew clean compileJava
```

### 2. 运行测试
{% if module %}
```bash
./gradlew :taotao-cloud-standalone-{{module}}:test
```
{% else %}
```bash
./gradlew test
```
{% endif %}

### 3. 生成覆盖率报告（如需）
{% if coverage %}
```bash
./gradlew jacocoTestReport
# 报告位置: build/reports/jacoco/test/html/index.html
```
{% endif %}

### 4. 输出测试结果
```
测试统计
总测试数: {{total}}
通过: {{passed}}
失败: {{failed}}
跳过: {{skipped}}
耗时: {{duration}}ms
```

### 5. 如果测试失败
- 读取失败测试的源码
- 分析失败原因
- 报告修复建议
