# 测试规范 — taotao-cloud-standalone

## 测试分层策略

### 1. 领域层测试（单元测试）

目标：验证领域模型、领域服务的业务逻辑。

```java
// 纯 POJO 测试，无 Spring 上下文
class DictDomainServiceTest {
    private DictDomainService dictDomainService;
    @Mock private DictDomainRepository dictDomainRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dictDomainService = new DictDomainServiceImpl(dictDomainRepository);
    }

    @Test
    void shouldInsertDictSuccessfully() {
        DictEntity entity = new DictEntity();
        entity.setDictName("测试字典");
        when(dictDomainRepository.save(any())).thenReturn(true);

        Boolean result = dictDomainService.insert(entity);

        assertThat(result).isTrue();
        verify(dictDomainRepository).save(any());
    }
}
```

### 2. 应用层测试（集成测试）

目标：验证 CQRS Executor 和应用服务的编排逻辑。

```java
@SpringBootTest
@Transactional
class DictsServiceTest {
    @Autowired private DictsService dictsService;

    @Test
    void shouldCreateDict() {
        DictInsertCmd cmd = new DictInsertCmd();
        cmd.setDictName("测试");

        Boolean result = dictsService.insert(cmd);

        assertThat(result).isTrue();
    }
}
```

### 3. Controller 测试

目标：验证 HTTP 参数绑定、权限、响应格式。

```java
@WebMvcTest(ManagerDictController.class)
class ManagerDictControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private DictsService dictsService;

    @Test
    void shouldReturnDictList() {
        when(dictsService.list(any())).thenReturn(new PageResult<>());

        mockMvc.perform(get("/sys/manager/dict/page"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));
    }
}
```

## 覆盖率目标（JaCoCo）

| 层 | 目标覆盖率 |
|----|-----------|
| Domain | ≥ 90% |
| Application | ≥ 80% |
| Interfaces | ≥ 70% |

## 测试命令

```bash
# 运行所有测试
./gradlew test

# 运行指定模块测试
./gradlew :taotao-cloud-standalone-domain:test
./gradlew :taotao-cloud-standalone-application:test

# 运行指定测试类
./gradlew test --tests *DictDomainServiceTest

# 生成覆盖率报告
./gradlew jacocoTestReport
# 报告位置: build/reports/jacoco/test/html/index.html
```

## 禁止项

- ❌ 使用 `@DirtiesContext`（影响测试性能）
- ❌ 测试中使用 `System.out.println`（应使用日志）
- ❌ 依赖测试执行顺序（每个测试应独立）
- ❌ 在领域层测试中加载 Spring 上下文
