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
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

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

            executeWithRetry(() -> javaMailSender.send(message), to);
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

            executeWithRetry(() -> javaMailSender.send(message), to);
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

            executeWithRetry(() -> javaMailSender.send(message), to);
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
            String resolvedTemplateName = resolveTemplateName(templateName);
            String content = templateEngine.process(resolvedTemplateName, context);
            
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

    /**
     * 根据配置解析模板名称，应用前缀与必要时的后缀
     */
    private String resolveTemplateName(String templateName) {
        String name = templateName;
        String prefix = mailProperties.getTemplatePrefix();
        if (prefix != null && !prefix.isBlank() && !name.startsWith(prefix)) {
            name = prefix + name;
        }

        // 仅在模板解析器未配置后缀时，才追加自定义后缀，避免重复
        String suffix = mailProperties.getTemplateSuffix();
        if (shouldAppendSuffix() && suffix != null && !suffix.isBlank() && !name.endsWith(suffix)) {
            name = name + suffix;
        }
        return name;
    }

    /**
     * 判断当前模板引擎是否已经配置了非空的后缀
     */
    private boolean shouldAppendSuffix() {
        try {
            for (ITemplateResolver resolver : templateEngine.getTemplateResolvers()) {
                if (resolver instanceof AbstractConfigurableTemplateResolver configurable) {
                    String engineSuffix = configurable.getSuffix();
                    if (engineSuffix != null && !engineSuffix.isEmpty()) {
                        return false; // 引擎已配置后缀，避免重复追加
                    }
                }
            }
        } catch (Exception e) {
            // 忽略检查异常，默认追加后缀
        }
        return true;
    }

    /**
     * 执行带重试的发送操作，使用配置中的重试次数与间隔
     */
    private void executeWithRetry(Runnable action, String[] recipients) {
        // retryTimes 表示“失败后的重试次数”，因此总尝试次数 = 初次尝试 + 重试次数
        int maxAttempts = Math.max(1, mailProperties.getRetryTimes() + 1);
        long intervalMs = Math.max(0L, mailProperties.getRetryInterval());

        int attempt = 0; // 尝试次数，从0开始表示初次尝试，后续增加的才是重试次数
        RuntimeException lastException = null;
        while (attempt < maxAttempts) {
            try {
                action.run();
                if (attempt > 0) {
                    log.info("邮件发送重试成功（第{}次），收件人: {}", attempt + 1, String.join(",", recipients));
                }
                return;
            } catch (RuntimeException e) {
                lastException = e;
                attempt++;
                if (attempt >= maxAttempts) {
                    break;
                }
                log.warn("邮件发送失败，准备重试({}/{}), {}ms后重试，收件人: {}", attempt, maxAttempts, intervalMs, String.join(",", recipients), e);
                try {
                    Thread.sleep(intervalMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("邮件发送重试被中断", ie);
                }
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }
}
