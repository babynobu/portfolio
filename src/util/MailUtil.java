package util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    // ★環境変数から取得（GitHubに漏れない）
    private static final String SMTP_USER = System.getenv("PORTFOLIO_SMTP_USER");
    private static final String SMTP_PASS = System.getenv("PORTFOLIO_SMTP_PASS");

    // FromはGmail制約的にSMTP_USERと一致が安全
    private static final String FROM_ADDRESS = SMTP_USER;

    // 表示名で「自動送信・返信不可」を表現
    private static final String FROM_NAME = "Portfolio お問い合わせ（自動送信）";

    private MailUtil() {}

    public static void sendContactReceipt(String to, String subject, String text) throws MessagingException {

        if (SMTP_USER == null || SMTP_USER.isEmpty() || SMTP_PASS == null || SMTP_PASS.isEmpty()) {
            throw new MessagingException("SMTP設定（環境変数 PORTFOLIO_SMTP_USER / PORTFOLIO_SMTP_PASS）が未設定です。");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        MimeMessage msg = new MimeMessage(session);

        // ★ここが修正箇所：FROM_NAME の文字コード指定が例外になる可能性があるためtry-catch
        try {
            msg.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // 万一UTF-8指定が失敗した場合は、アドレスのみで設定
            msg.setFrom(new InternetAddress(FROM_ADDRESS));
        }

        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject, "UTF-8");
        msg.setText(text + "\n\n※このメールは自動送信です。返信はできません。", "UTF-8");

        Transport.send(msg);
    }
}
