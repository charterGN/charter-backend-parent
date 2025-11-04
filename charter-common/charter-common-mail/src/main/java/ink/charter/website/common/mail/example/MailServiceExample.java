package ink.charter.website.common.mail.example;

import ink.charter.website.common.mail.service.MailService;
import ink.charter.website.common.mail.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件服务使用示例
 *
 * @author charter
 * @create 2025/11/04
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class MailServiceExample {

    private final MailService mailService;

    /**
     * 示例1：发送简单文本邮件
     */
    public void sendSimpleMailExample() {
        String to = "user@example.com";
        String subject = "欢迎注册";
        String content = "感谢您注册我们的网站！";
        
        mailService.sendSimpleMail(to, subject, content);
    }

    /**
     * 示例2：发送HTML邮件
     */
    public void sendHtmlMailExample() {
        String to = "user@example.com";
        String subject = "账号激活";
        String content = "<h1>欢迎注册</h1><p>请点击以下链接激活账号：</p>" +
                        "<a href='https://example.com/activate'>激活链接</a>";
        
        mailService.sendHtmlMail(to, subject, content);
    }

    /**
     * 示例3：发送带附件的邮件
     */
    public void sendAttachmentMailExample() {
        String to = "user@example.com";
        String subject = "月度报告";
        String content = "<h2>月度报告</h2><p>请查看附件中的详细报告。</p>";
        
        File attachment = new File("report.pdf");
        mailService.sendAttachmentMail(to, subject, content, attachment);
    }

    /**
     * 示例4：发送模板邮件
     */
    public void sendTemplateMailExample() {
        String to = "user@example.com";
        String subject = "密码重置";
        String templateName = "reset-password";
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "张三");
        variables.put("resetLink", "https://example.com/reset?token=xxx");
        variables.put("expireTime", "24小时");
        
        mailService.sendTemplateMail(to, subject, templateName, variables);
    }

    /**
     * 示例5：异步发送邮件
     */
    public void sendAsyncMailExample() {
        String to = "user@example.com";
        String subject = "系统通知";
        String content = "您的订单已发货！";
        
        // 异步发送，不阻塞主线程
        mailService.sendSimpleMailAsync(to, subject, content);
        log.info("邮件已提交异步发送队列");
    }

    /**
     * 示例6：使用工具类发送邮件
     */
    public void sendMailWithUtilsExample() {
        // 发送简单邮件
        MailUtils.sendSimple("user@example.com", "测试邮件", "这是一封测试邮件");
        
        // 发送HTML邮件
        MailUtils.sendHtml("user@example.com", "HTML邮件", "<h1>测试</h1>");
        
        // 异步发送
        MailUtils.sendSimpleAsync("user@example.com", "异步邮件", "异步发送测试");
    }

    /**
     * 示例7：发送给多个收件人
     */
    public void sendToMultipleRecipientsExample() {
        String[] recipients = {"user1@example.com", "user2@example.com", "user3@example.com"};
        String subject = "系统维护通知";
        String content = "系统将于今晚22:00进行维护，预计持续2小时。";
        
        mailService.sendSimpleMail(recipients, subject, content);
    }

    /**
     * 示例8：验证邮箱格式
     */
    public void validateEmailExample() {
        String email = "user@example.com";
        
        if (MailUtils.isValidEmail(email)) {
            log.info("邮箱格式正确: {}", email);
            mailService.sendSimpleMail(email, "测试", "测试内容");
        } else {
            log.error("邮箱格式错误: {}", email);
        }
    }
}
