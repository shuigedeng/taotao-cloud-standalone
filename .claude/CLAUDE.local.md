# 个人 DDD 开发配置

## 开发工具
- **IDE**: IntelliJ IDEA Ultimate
- **JDK**: graalvm-jdk-25
- **建模工具**: Miro / PlantUML（事件风暴）
- **API 测试**: Knife4j UI（`/doc.html`）

## 个人工作流
- **测试优先**: 先写领域层单元测试，再写 Executor 集成测试
- **CRUD 生成**: 使用 `/crud-generator` 技能快速生成标准模板
- **本地调试**: 启动 dev 配置文件，默认端口见 `application-dev.yml`

## 本地 JVM 参数
```
--enable-preview
--add-exports java.desktop/sun.font=ALL-UNNAMED
--add-exports java.base/java.lang=ALL-UNNAMED
--add-exports java.base/java.util=ALL-UNNAMED
```
