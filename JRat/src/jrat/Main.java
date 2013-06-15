/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jrat;
import javax.swing.*;
/**
 *
 * @author root
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
          try {
            // Set System L&F
              
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
    } 
    catch (UnsupportedLookAndFeelException e) {
       // handle exception
    }
    catch (ClassNotFoundException e) {
       // handle exception
    }
    catch (InstantiationException e) {
       // handle exception
    }
    catch (IllegalAccessException e) {
       // handle exception
    }
       JFrame main = new MainWindow();
       
       main.setVisible(true);
    }

}
