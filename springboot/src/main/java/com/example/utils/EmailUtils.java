package com.example.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮件工具类
 */
@Component
public class EmailUtils {

    @Resource
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
    
    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content HTML内容
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
    
    /**
     * 发送验证码邮件
     *
     * @param to          收件人
     * @param verifyCode  验证码
     */
    public void sendVerificationCodeEmail(String to, String verifyCode) {
        String subject = "【电影票务系统】邮箱验证码";
        String content = "尊敬的用户：<br/><br/>"
                + "您好！您正在进行邮箱验证，验证码为：<br/>"
                + "<h2 style='color:#409EFF;'>" + verifyCode + "</h2>"
                + "此验证码5分钟内有效，请勿泄露给他人。<br/><br/>"
                + "如非本人操作，请忽略此邮件。<br/><br/>"
                + "电影票务系统团队";
        
        try {
            sendHtmlMail(to, subject, content);
        } catch (MessagingException e) {
            // 发送失败时，使用简单文本邮件再次尝试
            String plainContent = "【电影票务系统】验证码：" + verifyCode + "（5分钟内有效，请勿泄露给他人）";
            sendSimpleMail(to, subject, plainContent);
        }
    }
} 