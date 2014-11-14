package noam; /**
 * Created by noam on 27/09/14.
 */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

/**
 * This program demonstrates how to download e-mail messages and save
 * attachments into files on disk.
 *
 * @author www.codejava.net
 *
 */
public class EmailAttachmentReceiver {
  private String saveDirectory;
  public List<Report> reportData;

  public EmailAttachmentReceiver(String saveDirectory) {
    this.saveDirectory = saveDirectory;
    this.reportData = new ArrayList<Report>();
  }

  /**
   * Downloads new messages and saves attachments to disk if any.
   * @param host
   * @param port
   * @param userName
   * @param password
   */
  public void downloadEmailAttachments(String host, String port, String userName, String password) {
    Properties properties = new Properties();

    // server setting
    properties.put("mail.pop3.host", host);
    properties.put("mail.pop3.port", port);

    // SSL setting
    properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.pop3.socketFactory.fallback", "false");
    properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

    Session session = Session.getDefaultInstance(properties);

    try {
      // connects to the message store
      Store store = session.getStore("pop3");
      if (!store.isConnected()) {
        store.connect(userName, password);

        // opens the inbox folder
        Folder folderInbox = store.getFolder("INBOX");
        folderInbox.open(Folder.READ_ONLY);
        // search for all "unseen" messages
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

        Message arrayMessages[] = folderInbox.search(unseenFlagTerm);
        // fetches new messages from server
        System.out.println("Found " + arrayMessages.length + " messages");

        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd' '");
        String nowFormatted = dateFormatter.format(now);
//        System.out.println("creating directory: " + nowFormatted + " " + "נדבי הקטן");
//        System.out.println("creating directory: " + nowFormatted + " " + "noam's birthday");

        for (int i = 0; i < arrayMessages.length; i++) {

          Message message = arrayMessages[i];
          Address[] fromAddress = message.getFrom();
          String from = fromAddress[0].toString();
          String subject = message.getSubject();
          String sentDate = message.getSentDate().toString();

          String contentType = message.getContentType();
          String messageContent = "";

          // store attachment file name, separated by comma
          String attachFiles = "";

          if (contentType.contains("multipart")) {
            String saveHere = this.saveDirectory + "/" + nowFormatted + subject;
            File theDir = new File(saveHere);
            if (!theDir.exists()) {
              System.out.println("creating directory: " + theDir.toString());
              boolean result = false;
              try{
                theDir.mkdir();
                reportData.add(new Report(saveHere, true, ""));
                result = true;
              } catch(SecurityException se){
                reportData.add(new Report(saveHere, false, se.getMessage()));
              }
              if(result) {
                System.out.println("DIR created");
              }
            } else {
              reportData.add(new Report(saveHere, false, "Directory already exists"));
            }
            // content may contain attachments
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
              MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
              if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                // this part is attachment
                String fileName = part.getFileName();
                attachFiles += fileName + ", ";
                part.saveFile(saveHere + File.separator + fileName);
              } else {
                // this part may be the message content
                messageContent = part.getContent().toString();
              }
            }

            if (attachFiles.length() > 1) {
              attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
            }
          } else if (contentType.contains("text/plain")
               || contentType.contains("text/html")) {
            Object content = message.getContent();
            if (content != null) {
              messageContent = content.toString();
            }
          }

          // print out details of each message
          System.out.println("Message #" + (i + 1) + ":");
          System.out.println("\t From: " + from);
          System.out.println("\t Subject: " + subject);
          System.out.println("\t Sent Date: " + sentDate);
          System.out.println("\t Message: " + messageContent);
          System.out.println("\t Attachments: " + attachFiles);

        }

        // disconnect
        folderInbox.close(false);
        store.close();
      }
      else {
        System.out.println("store already connected.");
      }
    } catch (NoSuchProviderException ex) {
      System.out.println("No provider for pop3.");
      ex.printStackTrace();
    } catch (MessagingException ex) {
      System.out.println("Could not connect to the message store");
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
