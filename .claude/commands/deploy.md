---
description: 部署应用到指定环境（dev/test/pre/pro）
parameters:
  - name: env
    type: string
    enum: [dev, test, pre, pro]
    required: true
---

# 部署命令

目标环境：{{env}}

## 部署流程

### 1. 运行测试
```bash
./gradlew test
```
测试失败则中止部署。

### 2. 打包
```bash
./gradlew :taotao-cloud-standalone-assembly:bootJar
```

### 3. 启动（指定环境）
```bash
java --enable-preview \
  -jar taotao-cloud-standalone-assembly/build/libs/taotao-cloud-standalone-assembly-*.jar \
  --spring.profiles.active={{env}}
```

### 4. 健康检查
```bash
curl -f http://localhost:{port}/actuator/health
```

## 输出格式
```
部署报告
环境: {{env}}
时间: {{timestamp}}
JAR 大小: {{size}}
健康检查: PASS/FAIL
```
