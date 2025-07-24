# Charter Backend 开发日志

## 版本历史

### v1.0.0 (2025-01-20)

#### 🎉 初始版本发布

**项目架构**
- 采用多模块Maven项目结构
- 基于Spring Boot 3.3.6 + Java 17
- 实现DDD（领域驱动设计）架构模式

**核心模块**
- `charter-app`: 主应用入口模块
- `charter-common`: 基础设施模块集合
- `charter-domain`: 领域层模块
- `charter-server`: 业务服务层模块
- `charter-dependencies`: 依赖版本管理

**基础设施功能**
- ✅ Spring Security + JWT 认证授权
- ✅ MyBatis-Plus 数据库操作
- ✅ Redis 缓存支持
- ✅ RabbitMQ 消息队列
- ✅ 操作日志记录
- ✅ 文件上传（阿里云OSS）
- ✅ 全局异常处理
- ✅ API接口文档（Knife4j）
- ✅ 定时任务支持

**业务功能**
- ✅ 用户管理系统
- ✅ 角色权限管理
- ✅ 用户会话管理
- ✅ 系统操作日志

**技术特性**
- 🔧 自动配置机制
- 🔧 多环境配置支持
- 🔧 API前缀自动配置
- 🔧 增强的查询包装器
- 🔧 统一响应格式
- 🔧 参数验证
- 🔧 IP地址解析

**开发工具**
- Maven 多模块管理
- Lombok 代码简化
- MapStruct 对象映射
- Hutool 工具库
- FastJSON2 JSON处理

---

## 开发规范

### 模块依赖规则
1. **基础设施模块** (`charter-common-*`): 不依赖任何业务模块
2. **领域API模块** (`charter-domain-*-api`): 只包含接口定义
3. **领域实现模块** (`charter-domain-*-core`): 依赖对应API模块和基础设施模块
4. **业务服务模块** (`charter-server-*`): 只依赖领域API模块和基础设施模块
5. **主应用模块** (`charter-app`): 导入业务模块和领域实现模块

### 包命名规范
```
ink.charter.website
├── common.*                 # 基础设施
├── domain.*.api.*          # 领域接口
├── domain.*.core.*         # 领域实现
└── server.*.*              # 应用服务
```

### API前缀配置
- 管理后台: `/api-admin/**`
- 首页服务: `/api-home/**`
- 博客服务: `/api-blog/**`

---

## 部署信息

### 环境要求
- Java 17+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+

### 启动命令
```bash
# 开发环境
mvn spring-boot:run -pl charter-app

# 生产环境
java -jar charter-app.jar --spring.profiles.active=prod
```

### 访问地址
- 应用地址: http://localhost:8065
- API文档: http://localhost:8065/doc.html
- Swagger UI: http://localhost:8065/swagger-ui.html

---

## 待开发功能

### 计划
- [ ] 后台中控系统
- [ ] 博客管理系统
- [ ] 标签分类管理
- [ ] 文章搜索功能
- [ ] 网站统计分析

---

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

---

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

---

## 联系方式

- 作者: charter
- 邮箱: 2088694379@qq.com
- 网站: https://www.charter.ink
- GitHub: https://github.com/charterGN