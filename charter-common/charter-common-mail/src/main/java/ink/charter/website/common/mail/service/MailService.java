package ink.charter.website.common.mail.service;

import java.io.File;
import java.util.Map;

/**
 * 邮件服务接口
 *
 * @author charter
 * @create 2025/11/04
 */
public interface MailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送简单文本邮件（多个收件人）
     *
     * @param to      收件人数组
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String[] to, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送HTML邮件（多个收件人）
     *
     * @param to      收件人数组
     * @param subject 主题
     * @param content HTML内容
     */
    void sendHtmlMail(String[] to, String subject, String content);

    /**
     * 发送带附件的邮件
     *
     * @param to          收件人
     * @param subject     主题
     * @param content     内容
     * @param attachments 附件文件数组
     */
    void sendAttachmentMail(String to, String subject, String content, File... attachments);

    /**
     * 发送带附件的邮件（多个收件人）
     *
     * @param to          收件人数组
     * @param subject     主题
     * @param content     内容
     * @param attachments 附件文件数组
     */
    void sendAttachmentMail(String[] to, String subject, String content, File... attachments);

    /**
     * 发送模板邮件
     *
     * @param to           收件人
     * @param subject      主题
     * @param templateName 模板名称
     * @param variables    模板变量
     */
    void sendTemplateMail(String to, String subject, String templateName, Map<String, Object> variables);

    /**
     * 发送模板邮件（多个收件人）
     *
     * @param to           收件人数组
     * @param subject      主题
     * @param templateName 模板名称
     * @param variables    模板变量
     */
    void sendTemplateMail(String[] to, String subject, String templateName, Map<String, Object> variables);

    /**
     * 异步发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMailAsync(String to, String subject, String content);

    /**
     * 异步发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     */
    void sendHtmlMailAsync(String to, String subject, String content);

    /**
     * 异步发送模板邮件
     *
     * @param to           收件人
     * @param subject      主题
     * @param templateName 模板名称
     * @param variables    模板变量
     */
    void sendTemplateMailAsync(String to, String subject, String templateName, Map<String, Object> variables);
}
