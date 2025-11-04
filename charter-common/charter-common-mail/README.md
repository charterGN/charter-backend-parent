# Charter Common Mail - 邮件服务模块

## 模块说明

本模块提供了完整的邮件发送功能，支持简单文本邮件、HTML邮件、带附件邮件和模板邮件的发送。

## 功能特性

- ✅ 简单文本邮件发送
- ✅ HTML格式邮件发送
- ✅ 带附件邮件发送
- ✅ Thymeleaf模板邮件发送
- ✅ 异步邮件发送
- ✅ 多收件人支持
- ✅ 自动配置和装配
- ✅ 邮件发送失败重试机制

## 快速开始

### 1. 添加依赖

在需要使用邮件服务的模块的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ink.charter</groupId>
    <artifactId>charter-common-mail</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置邮件参数

在 `application.yml` 或 `application-local.yml` 中配置邮件相关参数：

```yaml
# Spring Mail 基础配置
spring:
  mail:
    host: smtp.163.com              # SMTP服务器地址
    username: your-email@163.com    # 发件人邮箱
    password: your-password         # 邮箱密码或授权码
    default-encoding: UTF-8
    protocol: smtp
    port: 465                       # SMTP端口
    properties:
      mail:
        smtp:
          auth: true
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory
            port: 465
          ssl:
            enable: true
          starttls:
            enable: true
            required: true

# Charter Mail 扩展配置
charter:
  mail:
    enabled: true                   # 是否启用邮件服务
    default-from: your-email@163.com # 默认发件人地址
    default-from-name: Charter Website # 默认发件人名称
    template-prefix: mail/          # 模板路径前缀
    template-suffix: .html          # 模板文件后缀
    retry-times: 3                  # 发送失败重试次数
    retry-interval: 1000            # 重试间隔时间（毫秒）
```

### 3. 使用邮件服务

#### 方式一：注入 MailService

```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final MailService mailService;
    
    public void sendWelcomeEmail(String email, String username) {
        String subject = "欢迎注册";
        String content = "欢迎 " + username + " 注册我们的网站！";
        mailService.sendSimpleMail(email, subject, content);
    }
}
```

#### 方式二：使用 MailUtils 工具类

```java
@Service
public class NotificationService {
    
    public void notifyUser(String email) {
        MailUtils.sendSimple(email, "系统通知", "您有新的消息");
    }
}
```

## 使用示例

### 1. 发送简单文本邮件

```java
mailService.sendSimpleMail(
    "user@example.com", 
    "欢迎注册", 
    "感谢您注册我们的网站！"
);
```

### 2. 发送HTML邮件

```java
String htmlContent = "<h1>欢迎注册</h1><p>请点击链接激活账号</p>";
mailService.sendHtmlMail(
    "user@example.com", 
    "账号激活", 
    htmlContent
);
```

### 3. 发送带附件的邮件

```java
File attachment = new File("report.pdf");
mailService.sendAttachmentMail(
    "user@example.com",
    "月度报告",
    "<h2>请查看附件</h2>",
    attachment
);
```

### 4. 发送模板邮件

```java
Map<String, Object> variables = new HashMap<>();
variables.put("username", "张三");
variables.put("resetLink", "https://example.com/reset?token=xxx");
variables.put("expireTime", "24小时");

mailService.sendTemplateMail(
    "user@example.com",
    "密码重置",
    "reset-password",  // 模板名称（不含路径和后缀）
    variables
);
```

### 5. 异步发送邮件

```java
// 异步发送，不阻塞主线程
mailService.sendSimpleMailAsync(
    "user@example.com",
    "系统通知",
    "您的订单已发货"
);
```

### 6. 发送给多个收件人

```java
String[] recipients = {
    "user1@example.com",
    "user2@example.com",
    "user3@example.com"
};

mailService.sendSimpleMail(
    recipients,
    "系统维护通知",
    "系统将于今晚进行维护"
);
```

## 邮件模板

### 模板位置

邮件模板文件放在 `src/main/resources/templates/mail/` 目录下。

### 内置模板

模块提供了两个示例模板：

1. **welcome.html** - 欢迎注册模板
2. **reset-password.html** - 密码重置模板

### 自定义模板

创建新的模板文件，例如 `notification.html`：

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>通知</title>
</head>
<body>
    <h1>系统通知</h1>
    <p>尊敬的 <span th:text="${username}">用户</span>，</p>
    <p th:text="${message}">通知内容</p>
</body>
</html>
```

使用模板：

```java
Map<String, Object> variables = new HashMap<>();
variables.put("username", "张三");
variables.put("message", "您的订单已发货");

mailService.sendTemplateMail(
    "user@example.com",
    "订单通知",
    "notification",
    variables
);
```

## 常见邮箱配置

### 163邮箱

```yaml
spring:
  mail:
    host: smtp.163.com
    port: 465
    username: your-email@163.com
    password: your-auth-code  # 使用授权码，不是登录密码
```

### QQ邮箱

```yaml
spring:
  mail:
    host: smtp.qq.com
    port: 587
    username: your-email@qq.com
    password: your-auth-code  # 使用授权码
```

### Gmail

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

### 企业邮箱（阿里云）

```yaml
spring:
  mail:
    host: smtp.mxhichina.com
    port: 465
    username: your-email@yourdomain.com
    password: your-password
```

## 注意事项

1. **授权码**：大多数邮箱服务商需要使用授权码而不是登录密码
2. **SSL/TLS**：建议使用SSL加密连接（端口465）或TLS（端口587）
3. **发送频率**：注意邮箱服务商的发送频率限制
4. **异步发送**：大量邮件发送建议使用异步方式
5. **模板路径**：模板文件必须放在配置的路径下（默认：templates/mail/）

## API文档

详细的API文档请参考：
- [MailService.java](src/main/java/ink/charter/website/common/mail/service/MailService.java)
- [MailUtils.java](src/main/java/ink/charter/website/common/mail/utils/MailUtils.java)
- [MailServiceExample.java](src/main/java/ink/charter/website/common/mail/example/MailServiceExample.java)

## 技术栈

- Spring Boot 3.3.6
- Spring Mail
- Thymeleaf 模板引擎
- Jakarta Mail API

## 作者

Charter - 2025/11/04
