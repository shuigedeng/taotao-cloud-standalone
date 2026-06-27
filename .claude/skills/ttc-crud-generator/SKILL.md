---
name: crud-generator
description: 自动生成 DDD + CQRS 标准 CRUD 代码
triggers:
  - "生成CRUD"
  - "创建增删改查"
  - "新建模块"
---

# CRUD 代码生成器（DDD + CQRS）

适配项目：taotao-cloud-standalone

## 触发条件
用户输入包含 "生成CRUD" 或 "创建增删改查" 等关键词时自动触发。

## 生成结构

### 1. Domain 层
```
domain/
├── aggregate/{Entity}AggregateRoot.java
├── entity/{Entity}Entity.java
├── val/{Entity}Val.java
├── event/{Entity}CreateEvent.java
├── repository/{Entity}DomainRepository.java
├── service/{Entity}DomainService.java
└── service/impl/{Entity}DomainServiceImpl.java
```

### 2. Application 层
```
application/
├── command/{entity}/
│   ├── dto/
│   │   ├── {Entity}InsertCmd.java
│   │   ├── {Entity}UpdateCmd.java
│   │   ├── {Entity}DeleteCmd.java
│   │   ├── {Entity}ListQry.java
│   │   ├── {Entity}GetQry.java
│   │   └── clientobject/{Entity}CO.java
│   └── executor/
│       ├── {Entity}InsertCmdExe.java
│       ├── {Entity}UpdateCmdExe.java
│       ├── {Entity}DeleteCmdExe.java
│       └── query/
│           ├── {Entity}ListQryExe.java
│           └── {Entity}GetQryExe.java
├── service/{Entity}Service.java
├── service/impl/{Entity}ServiceImpl.java
└── converter/{Entity}Convert.java
```

### 3. Infrastructure 层
```
infrastructure/
├── persistent/
│   ├── persistence/{Entity}PO.java
│   ├── mapper/{Entity}Mapper.java
│   └── repository/{Entity}Repository.java
└── repository/{Entity}DomainRepositoryImpl.java
```

### 4. Interfaces 层
```
interfaces/controller/{role}/{Entity}{Role}Controller.java
```

### 5. API 层（可选）
```
api/
├── rpc/{Entity}RpcService.java
├── rpc/request/{Entity}RpcRequest.java
└── rpc/response/{Entity}RpcResponse.java
```

## 验证

```bash
./gradlew compileJava
```
