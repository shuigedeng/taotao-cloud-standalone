---
description: 初始化项目上下文，分析 DDD 工程结构、依赖、分层模式
agent: general
---

你是 taotao-cloud-standalone 项目架构分析师，正在执行 /spec-init 命令。

## 任务目标

分析 DDD 工程结构、依赖关系和分层模式，生成项目上下文总结。

## 执行步骤

### 1. 分析项目结构
- 使用 `read` 读取根目录结构
- 识别 8 个 DDD 模块（api/application/assembly/common/domain/facade/infrastructure/interfaces）
- 确认每个模块的包结构

### 2. 分析技术栈
- JDK 版本（25 预览特性）
- Gradle 版本及关键插件（spotbugs/checkstyle/pmd/spotless/jacoco）
- Spring Boot / Spring Cloud 版本
- 持久化框架（MyBatis-Plus / JPA）
- 消息中间件（RocketMQ / Kafka）
- 注册中心（Nacos）

### 3. 分析 DDD 分层模式
- domain 层：聚合根（aggregate/）、实体（entity/）、值对象（val/）、领域事件（event/）、仓储接口（repository/）、领域服务（service/）、领域适配器（adapter/）
- application 层：CQRS Executor（command/*/executor/）、命令/查询 DTO（command/*/dto/）、应用服务（service/）、MapStruct 转换器（converter/）、适配器（adapter/）
- infrastructure 层：JPA PO（persistent/persistence/）、MyBatis Mapper（persistent/mapper/）、JPA Repository（persistent/repository/）、仓储实现（repository/）、事件发布/订阅（event/）、缓存（cache/）、RocketMQ（roketmq/）、配置（properties/）
- interfaces 层：buyer/seller/manager 三端 Controller + RPC/gRPC 实现
- api 层：RPC 接口定义 + DTO（纯接口模块）
- facade 层：防腐层（ACL）

### 4. 输出项目上下文
生成分析报告，包含：
- 项目全貌（模块 + 职责）
- 技术栈清单
- DDD 各层职责和关键类（含 CQRS Executor 模式说明）
- 双持久化方案（MyBatis-Plus + JPA）
- 事件驱动架构（Guava/Redis/RocketMQ）
- 常见操作指引
