# 个人 DDD 开发配置

## 开发工具
- **IDE**: IntelliJ IDEA Ultimate
- **JDK**: graalvm-jdk-25
- **建模工具**: Miro / PlantUML（事件风暴）
- **API 测试**: Knife4j UI (`/doc.html`)

## 个人偏好
- **测试驱动**: 先写领域层单元测试，再写 Executor 层测试
- **代码生成**: 使用 `.opencode/skills/crud-generator` 生成 CRUD 模板
- **本地调试**: 启动 dev 环境，默认端口查看 `application-dev.yml`

## 本地 Java 参数
```
--enable-preview
--add-exports java.desktop/sun.font=ALL-UNNAMED
--add-exports java.base/java.lang=ALL-UNNAMED
--add-exports java.base/java.util=ALL-UNNAMED
```
