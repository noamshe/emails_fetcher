package noam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by noam on 18/10/14.
 */
public class Main {

  public static void main(String[] args) {
    RegisterTray.registerTray();

    Properties prop = new Properties();
    InputStream input = null;

    try {

      String filename = "config.properties";
      input = Main.class.getClassLoader().getResourceAsStream(filename);
      if(input==null){
        System.out.println("Sorry, unable to find " + filename);
        return;
      }
      //load a properties file from class path, inside static method
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally{
      if(input!=null){
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    final String host = prop.getProperty("host");
    final String port = prop.getProperty("port");
    final String saveDirectory = prop.getProperty("saveDirectory");
    final String userName = prop.getProperty("userName");
    final String password = prop.getProperty("password");

//    noam.EmailAttachmentReceiver receiver = new noam.EmailAttachmentReceiver(saveDirectory);
//    receiver.downloadEmailAttachments(host, port, userName, password);
//    noam.Mailer mailer = new noam.Mailer(userName, password, receiver.reportData);

    new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          EmailAttachmentReceiver receiver = new EmailAttachmentReceiver(saveDirectory);
          receiver.downloadEmailAttachments(host, port, userName, password);
          new Mailer(userName, password, receiver.reportData);
          try {
            Thread.sleep(20000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }
}
