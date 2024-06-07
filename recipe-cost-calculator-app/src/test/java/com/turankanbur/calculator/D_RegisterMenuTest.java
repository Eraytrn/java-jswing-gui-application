

package com.turankanbur.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.Assert.*;



public class D_RegisterMenuTest {
	
	
    private RegisterMenu registerMenu;
    private Robot robot;

    
    
    @Before
    public void setUp() throws AWTException {
        registerMenu = new RegisterMenu();

        SwingUtilities.invokeLater(() -> {
        	
        	
            registerMenu = new RegisterMenu();
            registerMenu.setVisible(true);
            
            
        });
        
        

        robot = new Robot();

        // Prepare the database for testing
        DBConnection databaseConnectionMenu = DBConnection.getInstance();
        Connection connection = databaseConnectionMenu.getConnection();
        if (connection == null) {
            fail("Failed to open database connection");
        }
        String sql = "CREATE TABLE IF NOT EXISTS Users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "password TEXT NOT NULL)";
        
        
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.execute("DELETE FROM Users");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to set up database: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            registerMenu.dispose();
        });
        
        
        DBConnection databaseConnectionMenu = DBConnection.getInstance();
        Connection connection = databaseConnectionMenu.getConnection();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void pressEnterKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterButtonClicked_passwordMismatch() {
        SwingUtilities.invokeLater(() -> {
            registerMenu.usernameField.setText("newuser");
            registerMenu.jPasswordField1.setText("newpassword");
            registerMenu.textField.setText("newuser@example.com");
            registerMenu.textField_1.setText("differentpassword");
            registerMenu.textField_1.requestFocus();
        });

        // Give some time for the focus request to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing Enter key while the confirm password field is focused
        pressEnterKey();

        // Give some time for the event to be processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if user is not registered due to password mismatch
        try {
        	DBConnection databaseConnectionMenu = DBConnection.getInstance();
        	Connection connection = databaseConnectionMenu.getConnection();     
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
            pstmt.setString(1, "newuser");
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterButtonClicked_passwordAtleast2char() {
        SwingUtilities.invokeLater(() -> {
            registerMenu.usernameField.setText("newuser");
            registerMenu.jPasswordField1.setText("1");
            registerMenu.textField.setText("newuser@example.com");
            registerMenu.textField_1.setText("1");
            registerMenu.textField_1.requestFocus();
        });

        // Give some time for the focus request to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing Enter key while the confirm password field is focused
        pressEnterKey();

        // Give some time for the event to be processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if user is not registered due to short password
        try {
        	DBConnection databaseConnectionMenu = DBConnection.getInstance();
        	Connection connection = databaseConnectionMenu.getConnection();
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
            pstmt.setString(1, "newuser");
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterButtonClicked_invalidEmail() {
        SwingUtilities.invokeLater(() -> {
            registerMenu.usernameField.setText("newuser");
            registerMenu.jPasswordField1.setText("newpassword");
            registerMenu.textField.setText("invalidemail");
            registerMenu.textField_1.setText("newpassword");
            registerMenu.textField_1.requestFocus();
        });

        // Give some time for the focus request to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing Enter key while the confirm password field is focused
        pressEnterKey();

        // Give some time for the event to be processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if user is not registered due to invalid email
        try {
        	DBConnection databaseConnectionMenu = DBConnection.getInstance();
        	Connection connection = databaseConnectionMenu.getConnection();
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
            pstmt.setString(1, "newuser");
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed: " + e.getMessage());
        }
    }

    @Test
    public void testLoginButtonClicked() {
        SwingUtilities.invokeLater(() -> {
            registerMenu.jButton1MouseClicked(null);
        });

        // Give some time for the event to be processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
        
        // Simulate pressing Enter key
        pressEnterKey();

        // Check if the login menu is opened
        try {
            SwingUtilities.invokeAndWait(() -> assertTrue(registerMenu.isDisplayable()));
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSuccessfulRegistration() throws InvocationTargetException, InterruptedException {
      
          
				SwingUtilities.invokeAndWait(() -> {
				    registerMenu.usernameField.setText("newuser");
				    registerMenu.jPasswordField1.setText("newpassword");
				    registerMenu.textField.setText("newuser@example.com");
				    registerMenu.textField_1.setText("newpassword");
				 }); 
				
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
     
			        SwingUtilities.invokeLater(() -> {
			        	
			        	DBConnection databaseConnectionMenu = DBConnection.getInstance();
			        	Connection connection = databaseConnectionMenu.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
            pstmt.setString(1, "newuser");
            ResultSet rs = pstmt.executeQuery();
              
            assertEquals("newuser", rs.getString("username"));
            assertEquals("newuser@example.com", rs.getString("email"));
            assertEquals("newpassword", rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed: " + e.getMessage());
        }
			        });
			        
			        
    }
    @Test
    public void testAddUserToDatabase_PasswordTooShort() {
   	 SwingUtilities.invokeLater(() -> {
    
   		DBFacaderRegister userDatabaseFacadeForRegister = new DBFacaderRegister();
        assertFalse(userDatabaseFacadeForRegister.addUserToDatabase("testuser", "1","1","testuser@")); // Şifre 2 karakterden kısa olduğu için başarısız olmalı
   	 });
   	 
   	 
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       
        pressEnterKey();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
    }

    @Test
    public void testAddUserToDatabase_PasswordsDoNotMatch() {
   	 SwingUtilities.invokeLater(() -> {
   		DBFacaderRegister userDatabaseFacadeForRegister = new DBFacaderRegister();
        assertFalse(userDatabaseFacadeForRegister.addUserToDatabase("testuser", "password","password2","testuser@")); // Şifreler eşleşmediği için başarısız olmalı
   	 });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       
        pressEnterKey();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
    }

    @Test
    public void testAddUserToDatabase_InvalidEmail() {
    	 SwingUtilities.invokeLater(() -> {
    		 DBFacaderRegister userDatabaseFacadeForRegister = new DBFacaderRegister();
        assertFalse(userDatabaseFacadeForRegister.addUserToDatabase("testuser", "password","password","testuseremail"));
    	  });
    	 
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       
        pressEnterKey();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
    } 
    
    @Test
    public void testAddUserToDatabase_SuccessfulRegistration() {
    	 SwingUtilities.invokeLater(() -> {
    		 DBFacaderRegister userDatabaseFacadeForRegister = new DBFacaderRegister();
        assertFalse(userDatabaseFacadeForRegister.addUserToDatabase("testuser", "password","password","testuser@")); 
    	  });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       
        pressEnterKey();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    
    
    public void testadduser() {
    	
    	
    	 SwingUtilities.invokeLater(() -> {
    	DBFacaderRegister user = new DBFacaderRegister(); 
    	user.addUser("testuser","email@", "password");
    	DBFacaderRegister userDatabaseFacadeForRegister = new DBFacaderRegister();
    	  assertFalse(userDatabaseFacadeForRegister.addUserToDatabase("testuser", "password","password","email"
    	  		+ ""
    	  		+ "")); 
    	 });
    	  try {
              Thread.sleep(100);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }

         
          pressEnterKey();
          try {
              Thread.sleep(100);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
    }


}