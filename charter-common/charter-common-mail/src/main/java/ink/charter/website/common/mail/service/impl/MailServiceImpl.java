package ink.charter.website.common.mail.service.impl;

import ink.charter.website.common.mail.config.MailProperties;
import ink.charter.website.common.mail.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.util.Map;

/**
 * 邮件服务实现类
 *
 * @author charter
 * @create 2025/11/04
 */
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MailProperties mailProperties;

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        sendSimpleMail(new String[]{to}, subject, content);
    }

    @Override
    public void sendSimpleMail(String[] to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(getFromAddress());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            javaMailSender.send(message);
            log.info("简单文本邮件发送成功，收件人: {}", String.join(",", to));
        } catch (Exception e) {
            log.error("简单文本邮件发送失败，收件人: {}", String.join(",", to), e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        sendHtmlMail(new String[]{to}, subject, content);
    }

    @Override
    public void sendHtmlMail(String[] to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(getFromAddress(), mailProperties.getDefaultFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            javaMailSender.send(message);
            log.info("HTML邮件发送成功，收件人: {}", String.join(",", to));
        } catch (Exception e) {
            log.error("HTML邮件发送失败，收件人: {}", String.join(",", to), e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendAttachmentMail(String to, String subject, String content, File... attachments) {
        sendAttachmentMail(new String[]{to}, subject, content, attachments);
    }

    @Override
    public void sendAttachmentMail(String[] to, String subject, String content, File... attachments) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(getFromAddress(), mailProperties.getDefaultFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            // 添加附件
            if (attachments != null && attachments.length > 0) {
                for (File attachment : attachments) {
                    if (attachment != null && attachment.exists()) {
                        helper.addAttachment(attachment.getName(), attachment);
                    }
                }
            }
            
            javaMailSender.send(message);
            log.info("带附件邮件发送成功，收件人: {}, 附件数: {}", String.join(",", to), 
                    attachments != null ? attachments.length : 0);
        } catch (Exception e) {
            log.error("带附件邮件发送失败，收件人: {}", String.join(",", to), e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendTemplateMail(String to, String subject, String templateName, Map<String, Object> variables) {
        sendTemplateMail(new String[]{to}, subject, templateName, variables);
    }

    @Override
    public void sendTemplateMail(String[] to, String subject, String templateName, Map<String, Object> variables) {
        try {
            // 构建模板上下文
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            
            // 处理模板
            String content = templateEngine.process(templateName, context);
            
            // 发送HTML邮件
            sendHtmlMail(to, subject, content);
            log.info("模板邮件发送成功，收件人: {}, 模板: {}", String.join(",", to), templateName);
        } catch (Exception e) {
            log.error("模板邮件发送失败，收件人: {}, 模板: {}", String.join(",", to), templateName, e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Async("mailTaskExecutor")
    public void sendSimpleMailAsync(String to, String subject, String content) {
        log.info("异步发送简单文本邮件，收件人: {}", to);
        sendSimpleMail(to, subject, content);
    }

    @Override
    @Async("mailTaskExecutor")
    public void sendHtmlMailAsync(String to, String subject, String content) {
        log.info("异步发送HTML邮件，收件人: {}", to);
        sendHtmlMail(to, subject, content);
    }

    @Override
    @Async("mailTaskExecutor")
    public void sendTemplateMailAsync(String to, String subject, String templateName, Map<String, Object> variables) {
        log.info("异步发送模板邮件，收件人: {}, 模板: {}", to, templateName);
        sendTemplateMail(to, subject, templateName, variables);
    }

    /**
     * 获取发件人地址
     */
    private String getFromAddress() {
        String from = mailProperties.getDefaultFrom();
        if (from == null || from.trim().isEmpty()) {
            throw new RuntimeException("未配置默认发件人地址");
        }
        return from;
    }
}
