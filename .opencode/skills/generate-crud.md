# Generate CRUD — 代码生成技能

适配项目：taotao-cloud-standalone（DDD + CQRS + Gradle + JDK 25）

## 触发条件

通过命令 `/apply` 或用户明确要求生成 CRUD 代码时触发。

## 生成模板

### Domain 层

```
domain/
├── aggregate/{Entity}AggregateRoot.java
├── entity/{Entity}Entity.java
├── val/{Entity}Val.java
├── event/{Entity}CreateEvent.java
├── repository/{Entity}DomainRepository.java
├── service/{Entity}DomainService.java
└── adapter/{Entity}DomainAdapter.java
```

### Application 层

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

### Infrastructure 层

```
infrastructure/
├── persistent/
│   ├── persistence/{Entity}PO.java
│   ├── mapper/{Entity}Mapper.java
│   └── repository/{Entity}Repository.java
└── repository/{Entity}DomainRepositoryImpl.java
```

### Interfaces 层

```
interfaces/controller/{role}/{Entity}{Role}Controller.java
```

## 生成后验证

```bash
gradlew compileJava
```
