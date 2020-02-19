package wcfb.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import wcfb.model.pojo.EmailPojo;

import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String MAIL_SENDER;

    @Autowired
    private JavaMailSender javaMailSender;

    Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    /**
     * 发送邮件
     * @param emailPojo
     */
    public void sendSimpleMail(EmailPojo emailPojo){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(MAIL_SENDER);
            mailMessage.setTo(emailPojo.getRecipient());
            mailMessage.setSubject(emailPojo.getSubject());
            mailMessage.setText(emailPojo.getContent());
            javaMailSender.send(mailMessage);
        } catch (Exception e){
            logger.error("发送失败");
        }
    }

    /**
     * 发送邮件
     * @param emailPojo
     */
    public void sendMail(EmailPojo emailPojo){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(MAIL_SENDER);
            helper.setTo(emailPojo.getRecipient());
            helper.setSubject(emailPojo.getSubject());
            helper.setText(emailPojo.getContent());
            helper.addAttachment(emailPojo.getFile().getName(), emailPojo.getFile());
            javaMailSender.send(mimeMessage);
        } catch (Exception e){
            logger.error("发送失败");
        }
    }
}
