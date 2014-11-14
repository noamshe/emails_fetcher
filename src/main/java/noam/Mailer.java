package noam;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by noam on 18/10/14.
 */
public class Mailer {
  public Mailer(final String userName, final String password, List<Report> resultsReport) {
    if (!resultsReport.isEmpty()) {
      // SENDING MAIL
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

      Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(userName, password);
        }
      });
      try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("from-email@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,
             InternetAddress.parse("noamshe@gmail.com"));
        message.setSubject("Testing Subject");
        message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");
        for(Report report : resultsReport) {
          if (report.success){
            message.setText("\n " + report.directoryName + " was saved successfully");
          } else {
            message.setText("\n " + report.directoryName + " was NOT saved successfully");
          }
        }

        Transport.send(message);
        System.out.println("Done");
      } catch (MessagingException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
