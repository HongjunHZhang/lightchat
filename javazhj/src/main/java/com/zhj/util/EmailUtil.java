package com.zhj.util;

import com.zhj.LightChatContext;
import com.zhj.entity.EmailVerifyCode;
import com.zhj.entity.dic.RedisSuffix;
import com.zhj.mapper.EmailVerifyCodeMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 789
 */
@Component
public class EmailUtil {

    /**
     * 用于发送邮件的邮箱地址
     */
    private final static String MY_EMAIL_ACCOUNT = "1878020570@qq.com";
    private final static String MY_EMAIL_PASSWORD = "wugojblnkvwhbaji";
    private final static String MY_EMAIL_SMTP_SERVER = "smtp.qq.com";
    private static EmailVerifyCodeMapper emailVerifyCodeMapper;
    private static final Random RANDOM = new Random();
    private static final ThreadLocal<Random> RANDOM_THREAD_LOCAL = new ThreadLocal<>();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<>();
    @Resource
    RedisUtil redisUtil;

    public static void send(String[] receiveMailAccount,String info,String title) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");
        // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", MY_EMAIL_SMTP_SERVER);
        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");
        // 需要请求认证
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        // 3. 创建一封邮件
        for (String s : receiveMailAccount) {
            MimeMessage message = createMessage(session, MY_EMAIL_ACCOUNT, s, info, title);
            Transport transport = session.getTransport();
            transport.connect(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        SIMPLE_DATE_FORMAT_THREAD_LOCAL.remove();
        RANDOM_THREAD_LOCAL.remove();
    }

    public static MimeMessage createMessage(Session session, String sendMail, String receiveMail, String info,String title) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, "邮箱验证码测试", "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "xx用户", "UTF-8"));
        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(title, "UTF-8");
        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(info, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();

        return message;
    }


    public  void sendRegisterEmail(String[] receivers) throws Exception {
        sendByInfo(receivers,RedisSuffix.REGISTER.getMsg(), "您好,这是一封邀请注册邮件，您的验证码为:","，若不是您本人注册请忽略该邮件" +
                "，请不要将您的验证码随意交给其他人,验证码在三分钟内有效","轻聊软件注册邮箱验证","0");
    }

    public  void sendCodeEmail(String[] receivers) throws Exception {
        sendByInfo(receivers,RedisSuffix.RETRIEVE.getMsg(), "您好,这是一封邮箱验证邮件，您的验证码为:","，若不是您本人操作请忽略该邮件" +
                "，请不要将您的验证码随意交给其他人,验证码在三分钟内有效","轻聊软件邮箱验证","1");
    }

    public  void sendChangeEmail(String[] receivers) throws Exception {
        sendByInfo(receivers,RedisSuffix.CHANGE.getMsg(), "您好,这是一封邮箱绑定验证邮件，您的验证码为:","，若不是您本人绑定请忽略该邮件" +
                "，请不要将您的验证码随意交给其他人,验证码在三分钟内有效","轻聊软件邮箱验证","2");
    }

    public void  sendByInfo(String[] receivers,String redisPrefix,String infoPrefix,String infoSuffix,String title,String type) throws Exception {
        if (emailVerifyCodeMapper == null){
            emailVerifyCodeMapper = LightChatContext.zhj.getBean(EmailVerifyCodeMapper.class);
        }
        List<EmailVerifyCode> emailDaoList = createEmailDao(receivers,type);
        for (EmailVerifyCode emailVerifyCode : emailDaoList) {
            redisUtil.set(redisPrefix+emailVerifyCode.getEmail(),emailVerifyCode.getVerifyCode(),3, TimeUnit.MINUTES);
            emailVerifyCodeMapper.insert(emailVerifyCode);
            send(new String[]{emailVerifyCode.getEmail()},infoPrefix+emailVerifyCode.getVerifyCode()+infoSuffix,title);
        }
    }

    public  List<EmailVerifyCode> createEmailDao(String[] receivers,String type){
        List<EmailVerifyCode> list = new ArrayList<>();
        if (RANDOM_THREAD_LOCAL.get() == null){
            RANDOM_THREAD_LOCAL.set(RANDOM);
        }
        if (SIMPLE_DATE_FORMAT_THREAD_LOCAL.get() == null){
            SIMPLE_DATE_FORMAT_THREAD_LOCAL.set(SIMPLE_DATE_FORMAT);
        }
        Date date = new Date();
        for (String receiver : receivers) {
            EmailVerifyCode emailVerifyCode = new EmailVerifyCode();
            emailVerifyCode.setVerifyCode(getSixCode(RANDOM_THREAD_LOCAL.get()));
            emailVerifyCode.setEmail(receiver);
            emailVerifyCode.setGenerateTime(SIMPLE_DATE_FORMAT_THREAD_LOCAL.get().format(date));
            emailVerifyCode.setOverdueTime(SIMPLE_DATE_FORMAT_THREAD_LOCAL.get().format(date.getTime()+3*60*1000));
            emailVerifyCode.setType(type);
            list.add(emailVerifyCode);
        }
        return list;
    }

    public static String getSixCode(Random random){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

    }

}
