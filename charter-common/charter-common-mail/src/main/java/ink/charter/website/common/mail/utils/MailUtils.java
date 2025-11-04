package ink.charter.website.common.mail.utils;

import ink.charter.website.common.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

/**
 * 邮件工具类
 * 提供静态方法便捷调用邮件服务
 *
 * @author charter
 * @create 2025/11/04
 */
@Slf4j
public class MailUtils {

    private static MailService mailService;

    /**
     * 设置邮件服务实例（由配置类调用）
     */
    public static void setMailService(MailService mailService) {
        MailUtils.mailService = mailService;
    }

    /**
     * 发送简单文本邮件
     */
    public static void sendSimple(String to, String subject, String content) {
        mailService.sendSimpleMail(to, subject, content);
    }

    /**
     * 发送HTML邮件
     */
    public static void sendHtml(String to, String subject, String content) {
        mailService.sendHtmlMail(to, subject, content);
    }

    /**
     * 发送带附件的邮件
     */
    public static void sendWithAttachment(String to, String subject, String content, File... attachments) {
        mailService.sendAttachmentMail(to, subject, content, attachments);
    }

    /**
     * 发送模板邮件
     */
    public static void sendTemplate(String to, String subject, String templateName, Map<String, Object> variables) {
        mailService.sendTemplateMail(to, subject, templateName, variables);
    }

    /**
     * 异步发送简单文本邮件
     */
    public static void sendSimpleAsync(String to, String subject, String content) {
        mailService.sendSimpleMailAsync(to, subject, content);
    }

    /**
     * 异步发送HTML邮件
     */
    public static void sendHtmlAsync(String to, String subject, String content) {
        mailService.sendHtmlMailAsync(to, subject, content);
    }

    /**
     * 异步发送模板邮件
     */
    public static void sendTemplateAsync(String to, String subject, String templateName, Map<String, Object> variables) {
        mailService.sendTemplateMailAsync(to, subject, templateName, variables);
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
}
