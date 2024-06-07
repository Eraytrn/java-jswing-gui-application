package com.turankanbur.calculator;


import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.SwingUtilities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class B_SecondAddIngredientTests {
	
    private DBConnection dbConnection;
    private AddIngredientMenu addIngredientMenu;
    private DBFacadeAddIng dbFacade;    
    

    private Connection connection;

    @Before
    public void setUp() throws InvocationTargetException, InterruptedException {
        dbConnection = DBConnection.getInstance();
        dbConnection.openConnection();
        connection = dbConnection.getConnection();
       
        dbFacade = new DBFacadeAddIng();
        // Create the Ingredients table for testing
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Ingredients (" +
                "ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ingredient_name TEXT UNIQUE," +
                "ingredient_price TEXT)";
        String insertQuery1 = "INSERT INTO Ingredients (ingredient_name, ingredient_price) VALUES ('Ingredient1', '10.00')";
        String insertQuery2 = "INSERT INTO Ingredients (ingredient_name, ingredient_price) VALUES ('Ingredient2', '15.00')";
        String insertQuery3 = "INSERT INTO Ingredients (ingredient_name, ingredient_price) VALUES ('Ingredient3', '20.00')";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeAndWait(() -> addIngredientMenu = new AddIngredientMenu());
     
        
    }

    
//    @After
//    public void tearDown() {
//    	  String dropTableQuery = "DROP TABLE IF EXISTS Ingredients";
//          try (Statement statement = connection.createStatement()) {
//              statement.executeUpdate(dropTableQuery);
//          } catch (SQLException e) {
//              e.printStackTrace();
//          }
//        dbConnection.closeConnection();
//    }

    @Test
    public void testOpenConnection() {
        assertNotNull(connection);
    }

    @Test
    public void testAddIngredientMenuComponents() {
        assertTrue(addIngredientMenu.jLabel1 != null);
        assertTrue(addIngredientMenu.jLabel2 != null);
        assertTrue(addIngredientMenu.jLabel3 != null);
        assertTrue(addIngredientMenu.jLabel4 != null);
        assertTrue(addIngredientMenu.jTextField1 != null);
        assertTrue(addIngredientMenu.jTextField2 != null);
        assertTrue(addIngredientMenu.jButton1 != null);
        assertTrue(addIngredientMenu.jButton2 != null);
    }
    
//    @Test
//    public void testCloseConnection() {
//        dbConnection.closeConnection();
//        try {
//            assertFalse(!connection.isClosed());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    

   

    @Test
    public void testFindUnusedId() {
    	  SwingUtilities.invokeLater(() -> {
        int newId = dbFacade.findUnusedId();
        dbFacade.addIngredientToDatabase("TestIngredient", "10.00");
        newId = dbFacade.findUnusedId();
        assertEquals(2, newId);
    	  });
      
        try {
            Thread.sleep(500); // Örnek bir bekleme süresi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testTextField1ActionPerformed() {
        SwingUtilities.invokeLater(() -> {
            addIngredientMenu.jTextField1.setText("TestIngredient");
            addIngredientMenu.jTextField1.postActionEvent();
            assertEquals("TestIngredient", addIngredientMenu.ingredient_name);
        });
    }

    @Test
    public void testTextField2ActionPerformed() {
        SwingUtilities.invokeLater(() -> {
            addIngredientMenu.jTextField2.setText("10.00");
            addIngredientMenu.jTextField2.postActionEvent();
            assertEquals("10.00", addIngredientMenu.ingredient_price);
        });
    }

    @Test
    public void testButton1ActionPerformed() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            addIngredientMenu.jTextField1.setText("TestIngredient");
            addIngredientMenu.jTextField2.setText("10.00");

            // Kullanıcı etkileşimini otomatikleştirmek için Robot sınıfını kullanıyoruz
            try {
                Robot robot = new Robot();
                // Test sırasında JOptionPane mesaj diyalogunu bekleyip "Enter" tuşuna basmak için zaman ayarı yapıyoruz
                Thread interactionThread = new Thread(() -> {
                    try {
                        Thread.sleep(500); // Biraz bekliyoruz ki JOptionPane açılsın
                        robot.keyPress(KeyEvent.VK_ENTER);
                        robot.keyRelease(KeyEvent.VK_ENTER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                interactionThread.start();

                addIngredientMenu.jButton1.doClick();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });

        String query = "SELECT * FROM Ingredients WHERE ingredient_name = 'TestIngredient'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            assertTrue(rs.next());
            assertEquals("TestIngredient", rs.getString("ingredient_name"));
            assertEquals("10.00", rs.getString("ingredient_price"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testButton2ActionPerformed() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            addIngredientMenu.jButton2.doClick();
        });

        // Verify that the IngredientMenu is visible and AddIngredientMenu is disposed
        boolean ingredientMenuVisible = false;
        for (Frame frame : Frame.getFrames()) {
            if (frame instanceof IngredientMenu && frame.isVisible()) {
                ingredientMenuVisible = true;
                break;
            }
        }
        assertTrue(ingredientMenuVisible);
        assertTrue(!addIngredientMenu.isVisible());
    }
    

}