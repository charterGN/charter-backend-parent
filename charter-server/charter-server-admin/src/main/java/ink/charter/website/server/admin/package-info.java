/**
 * Charter 个人网站中控后台管理服务模块
 * 
 * <h2>模块概述</h2>
 * 本模块是 Charter Backend 项目的应用服务层核心组件，负责提供中控后台管理功能的 REST API 服务。
 * 采用领域驱动设计（DDD）和分层架构模式，实现了清晰的职责分离和依赖解耦。
 * 
 * <h2>主要功能域</h2>
 * <ul>
 *   <li><strong>系统管理（sys）</strong>：用户管理、认证授权、会话管理等核心系统功能及首页、博客等后台管理功能</li>
 *   <li><strong>博客管理（blog）</strong>：博客内容功能</li>
 *   <li><strong>首页管理（home）</strong>：首页内容功能</li>
 * </ul>
 * 
 * <h2>包结构说明</h2>
 * <pre>
 * ink.charter.website.server.admin
 * ├── sys/                    # 系统管理模块
 * │   ├── controller/         # REST API 控制器
 * │   ├── service/            # 业务服务层
 * │   ├── scanner/            # 资源扫描器
 * │   └── converter/          # 对象转换器
 * │
 * ├── blog/                   # 博客管理模块
 * │   ├── controller/         # 博客管理 REST API
 * │   ├── service/            # 博客业务服务
 * │   └── converter/          # 博客对象转换器
 * │
 * └── home/                   # 首页管理模块
 *     ├── controller/         # 首页管理 REST API
 *     ├── service/            # 首页业务服务
 *     └── converter/          # 首页对象转换器
 * </pre>
 * 
 * 
 * <h2>依赖关系</h2>
 * <ul>
 *   <li><strong>charter-domain-admin-api</strong>：依赖管理域接口，实现业务逻辑调用</li>
 *   <li><strong>charter-common-*</strong>：依赖基础设施模块，获得技术支持</li>
 *   <li><strong>不直接依赖</strong>：domain-core 实现，通过接口实现解耦</li>
 * </ul>
 * 
 * <h2>设计原则</h2>
 * <ul>
 *   <li><strong>单一职责</strong>：每个包和类都有明确的职责边界</li>
 *   <li><strong>依赖倒置</strong>：依赖抽象接口而非具体实现</li>
 *   <li><strong>开闭原则</strong>：对扩展开放，对修改封闭</li>
 *   <li><strong>分层架构</strong>：严格遵循分层架构，避免跨层调用</li>
 * </ul>
 * 
 * @author charter
 * @create 2025/07/27
 */
package ink.charter.website.server.admin;