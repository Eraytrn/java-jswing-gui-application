
package com.turankanbur.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Connection;


import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import static org.junit.Assert.*;



public class C_LoginMenuTest {
    private LoginMenu loginMenu;
    private Robot robot;
    private JButton btnLogin;
    private JPasswordField passwordField;
   
   // private PasswordValidationObserverForLogin observer;
    private JTextField usernameField;
    
    
    
    
    
    
    @Before
    public void setUp() throws AWTException, InterruptedException {
        // Create necessary components
        passwordField = new JPasswordField();
        usernameField = new JTextField();
      //  observer = new PasswordValidationObserverForLogin(passwordField, feedbackLabel);

        
        
        // Create the login menu and make it visible
        SwingUtilities.invokeLater(() -> {
            loginMenu = new LoginMenu();
            loginMenu.initComponents(); // Ensure components are initialized
            loginMenu.setVisible(true);
            btnLogin = loginMenu.btnLogin; // Initialize btnLogin after initComponents
        });

        
        // Wait until btnLogin is initialized
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            if (btnLogin != null) {
                latch.countDown();
            }
        });
        latch.await();

        // Initialize the Robot instance for simulating key events
        robot = new Robot();

        // Prepare the database for testing
        try {
        	DBConnection databaseConnectionForLogin = DBConnection.getInstance();
        	Connection connection = databaseConnectionForLogin.getConnection();
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT NOT NULL,"
                    + "password TEXT NOT NULL)";
            stmt.execute(sql);
            stmt.execute("DELETE FROM Users");
            stmt.execute("INSERT INTO Users (username, password) VALUES ('testuser', 'testpassword')");
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown() {
        // Close the login menu
        SwingUtilities.invokeLater(() -> {
            loginMenu.dispose();
        });
        DBConnection databaseConnectionForLogin = DBConnection
        		
        		.getInstance();
        Connection connection = databaseConnectionForLogin.getConnection();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    @Test
    
    public void testBackToRegisterButtonClicked() {
        SwingUtilities.invokeLater(() -> {
            loginMenu.backtoregisterMouseClicked(null);

            // Check if the register menu is opened
            assertTrue(!
            		
            		loginMenu.isDisplayable());
        });
    }

    @Test
    public void testLoginWithEnterKey() {
        SwingUtilities.invokeLater(() -> {
            loginMenu.loginusername.setText("testuser");
            loginMenu.loginpassword.setText("testpassword");
            loginMenu.loginpassword.requestFocus();
        });

        // Give some time for the focus request to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing Enter key while the password field is focused
        SwingUtilities.invokeLater(() -> {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        });

        // Give some time for the event to be processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform assertions to check the login state
        SwingUtilities.invokeLater(() -> {


            assertEquals("testpassword", loginMenu.password);
        });
    }
    @Test
    public void testLoginusernameKeyTyped() {
        SwingUtilities.invokeLater(() -> {
            loginMenu.loginusername.setText("testuser");
            loginMenu.loginusernameKeyTyped(new KeyEvent(loginMenu.loginusername, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'T'));
            assertEquals("testuserT", loginMenu.username);
        });
    }

    @Test
    public void testLoginpasswordKeyTyped() {
        SwingUtilities.invokeLater(() -> {
            loginMenu.loginpassword.setText("password123");
            loginMenu.loginpasswordKeyTyped(new KeyEvent(loginMenu.loginpassword, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'T'));
            assertEquals("password123T", loginMenu.password);
        });
    }

    @Test
    public void testBacktoregisterActionPerformed() {
        SwingUtilities.invokeLater(() -> {
            loginMenu.backtoregisterActionPerformed(null);

            
            // Check if the register menu is opened
            assertTrue(!loginMenu.isDisplayable());
        });
    }

    @Test
    public void testloginuser() {
    	DBFacadeLogin  loginservice = new DBFacadeLogin();
        SwingUtilities.invokeLater(() -> {
            loginMenu.loginusername.setText("testuser");
            loginMenu.loginpassword.setText("testpassword");
            loginservice.loginUser("testuser","testpassword");
        });

        // Give some time for the focus request to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing Enter key while the password field is focused
        SwingUtilities.invokeLater(() -> {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        });
        

        // Give some time for the event to be processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform assertions to check the login state
        SwingUtilities.invokeLater(() -> {
            assertEquals("testuser", loginMenu.username);
            assertEquals("testpassword", loginMenu.password);
        });
    }

    @Test
    public void testBtnLoginMouseClicked_SuccessfulLogin() {
        SwingUtilities.invokeLater(() -> {
            // Set the username and password fields
            usernameField.setText("testuser");
            passwordField.setText("testpassword");
            MouseEvent mouseClick = new MouseEvent(btnLogin, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, btnLogin.getWidth() / 2, btnLogin.getHeight() / 2, 1, false);
            btnLogin.dispatchEvent(mouseClick);
            
        });

        // Introduce a delay to allow Swing to update the UI
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       

       
        SwingUtilities.invokeLater(() -> {
            KeyEvent enterPressed = new KeyEvent(passwordField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
            passwordField.dispatchEvent(enterPressed);
        });

     
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      
        assertNotNull(loginMenu.getMainMenu()); 
    }

    @Test
    public void testBtnLoginMouseClicked_InvalidLogin() {
    	 SwingUtilities.invokeLater(() -> {
        usernameField.setText("invaliduser");
        passwordField.setText("invalidpassword");
        MouseEvent mouseClick = new MouseEvent(btnLogin, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, btnLogin.getWidth() / 2, btnLogin.getHeight() / 2, 1, false);
        btnLogin.dispatchEvent(mouseClick);
    	  });
    	 try {
             Thread.sleep(500);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
    	 SwingUtilities.invokeLater(() -> {
             robot.keyPress(KeyEvent.VK_ENTER);
             robot.keyRelease(KeyEvent.VK_ENTER);
         });
    	 try {
             Thread.sleep(500);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        // Simulate a mouse click event on the btnLogin button
    	 
       

        // Verify if the appropriate action is triggered
        // In this case, it should display an error message
    	 assertNotNull(loginMenu.getMainMenu()); 
    }
        

}