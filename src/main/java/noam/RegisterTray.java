package noam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by noam on 31/10/14.
 */
public class RegisterTray {
  public static void registerTray() {

    //checking for support
    if(!SystemTray.isSupported()){
      System.out.println("System tray is not supported !!! ");
      return ;
    }
    //get the systemTray of the system
    SystemTray systemTray = SystemTray.getSystemTray();

    //get default toolkit
    //Toolkit toolkit = Toolkit.getDefaultToolkit();
    //get image   Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
    Image image = Toolkit.getDefaultToolkit().getImage("src/images/1.png");

    //popupmenu
    PopupMenu trayPopupMenu = new PopupMenu();

    //1t menuitem for popupmenu
    MenuItem action = new MenuItem("Action");
    action.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Action Clicked");
      }
    });
    trayPopupMenu.add(action);

    //2nd menuitem of popupmenu
    MenuItem close = new MenuItem("Close");
    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    trayPopupMenu.add(close);

    //setting tray icon
    TrayIcon trayIcon = new TrayIcon(image, "SystemTray Demo", trayPopupMenu);
    //adjust to default size as per system recommendation
    trayIcon.setImageAutoSize(true);

    try{
      systemTray.add(trayIcon);
    }catch(AWTException awtException){
      awtException.printStackTrace();
    }
    System.out.println("end of main");
  }
}
