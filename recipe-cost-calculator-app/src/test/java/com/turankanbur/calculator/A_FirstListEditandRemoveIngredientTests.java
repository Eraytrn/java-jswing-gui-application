package com.turankanbur.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.SwingUtilities;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class A_FirstListEditandRemoveIngredientTests {
	
    private DBConnection dbConnection;

    private DBFacadeAddIng dbFacade;  
    private DBFacadeLERIng databaseFacadeForListEditAndRemoveIngredientMenu;
    private LERIngMenu listmenu;
 
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
      
        SwingUtilities.invokeAndWait(() -> listmenu = new LERIngMenu());
  
        
    }

    
    @After
    public void tearDown() {
    	  String dropTableQuery = "DROP TABLE IF EXISTS Ingredients";
          try (Statement statement = connection.createStatement()) {
              statement.executeUpdate(dropTableQuery);
          } catch (SQLException e) {
              e.printStackTrace();
          }
        
    }
    @Test
    public void testSaveToDatabaseForName() {
    	
        // Veritabanına eklenen yeni bir öğe
      
	     String newName = "NewTestIngredient";
	        String newPrice = "20.00";
        // Test sırasında öğenin adını ve fiyatını değiştireceğiz
   

        // Veriyi güncelleme işlemi
        SwingUtilities.invokeLater(() -> {
        	  dbFacade.addIngredientToDatabase("TestIngredient", "10.00");
        	 
            // Test sırasında gerçekleştirilen adım adım işlemler
            // 1. Verileri getirme (Fetch)
        	  databaseFacadeForListEditAndRemoveIngredientMenu.FetchData();

            // 2. Tabloyu güncelleme
            listmenu.jTable2.setRowSelectionInterval(0, 0);
            listmenu.jTable2.setColumnSelectionInterval(1, 1);

            // 3. Veriyi güncelleme
            databaseFacadeForListEditAndRemoveIngredientMenu.saveToDatabase(0, 1, newName); // Name sütununu güncelleme
            databaseFacadeForListEditAndRemoveIngredientMenu.saveToDatabase(0, 2, newPrice); // Price sütununu güncelleme
        });

        // Bekleme süresi ekleyerek GUI iş parçacığının etkileşimi tamamlamasını sağlama
        try {
            // Yeterince uzun bir süre beklemek için
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing the Enter key manually
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Veritabanında güncellenmiş veriyi kontrol etme
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Ingredients WHERE ingredient_name = '" + newName + "' AND ingredient_price = '" + newPrice + "'";
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(!resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to query the database.");
        }
    }

    @Test
    public void testSaveToDatabaseForPrice() {
        // Ingredient details to be added and updated
        String initialName = "TestIngredient";
        String initialPrice = "10.00";
        String newPrice = "20.00";
        
        // Veriyi güncelleme işlemi
        SwingUtilities.invokeLater(() -> {
            dbFacade.addIngredientToDatabase(initialName, initialPrice);
            
            // Test sırasında gerçekleştirilen adım adım işlemler
            // 1. Verileri getirme (Fetch)
            databaseFacadeForListEditAndRemoveIngredientMenu.FetchData();

            // 2. Tabloyu güncelleme (First row, second column for the price)
            listmenu.jTable2.setRowSelectionInterval(1, 1);
            listmenu.jTable2.setColumnSelectionInterval(2, 2);

            // 3. Veriyi güncelleme
            databaseFacadeForListEditAndRemoveIngredientMenu.saveToDatabase(0, 2, newPrice); // Price sütununu güncelleme
        });

        // Bekleme süresi ekleyerek GUI iş parçacığının etkileşimi tamamlamasını sağlama
        try {
            // Yeterince uzun bir süre beklemek için
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate pressing the Enter key manually
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Veritabanında güncellenmiş veriyi kontrol etme
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Ingredients WHERE ingredient_name = '" + initialName + "' AND ingredient_price = '" + newPrice + "'";
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(!resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to query the database.");
        }
    }
  
    @Test
    public void EditButtonActionPerformed() {
        SwingUtilities.invokeLater(() -> {
            
            dbFacade.addIngredientToDatabase("TestIngredient", "10.00");
            // Simulate clicking the "Fetch" button to fetch data
            listmenu.jButton2ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Fetch"));

            // Simulate user selection
            listmenu.jTable2.setRowSelectionInterval(0, 0);
            listmenu.jTable2.setColumnSelectionInterval(1, 1);
            listmenu.EditButton.doClick();
       
        });

        // Bekleyerek GUI iş parçacığının etkileşimi tamamlamasını sağlama
        try {
            Thread.sleep(500); // Örnek bir bekleme süresi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the value is updated in the database
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Ingredients WHERE ingredient_name = 'TestIngredient'";
            assertTrue(statement.executeQuery(query).next());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to query the database.");
        }
    }
    @Test
    public void testBackButtonActionPerformed() {
    
        listmenu.backbutton.doClick();

       
        IngredientMenu ingredientMenu = new IngredientMenu();
       

       
        assertFalse(listmenu.isDisplayable());
    }

    @Test
    public void TestMissSelectEditData() {
        SwingUtilities.invokeLater(() -> {
            
            dbFacade.addIngredientToDatabase("TestIngredient", "10.00");
            // Simulate clicking the "Fetch" button to fetch data
            listmenu.jButton2ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Fetch"));

      
         
            listmenu.EditButtonActionPerformed(null);
       
        });

        // Bekleyerek GUI iş parçacığının etkileşimi tamamlamasını sağlama
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
        // Check if the value is updated in the database
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Ingredients WHERE ingredient_name = 'TestIngredient'";
            assertTrue(statement.executeQuery(query).next());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to query the database.");
        }
    }
    @Test
    public void RemoveButton() {
        SwingUtilities.invokeLater(() -> {
            dbFacade.addIngredientToDatabase("TestIngredientforremove", "10.00");
            listmenu.jButton2ActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Fetch"));
            listmenu.jTable2.setRowSelectionInterval(0, 0);
            listmenu.jTable2.setColumnSelectionInterval(1, 1);
            databaseFacadeForListEditAndRemoveIngredientMenu.removeAdjacentRows();
        });

        // Bekleyerek GUI iş parçacığının etkileşimi tamamlamasını sağlama
        try {
            Thread.sleep(500); // Örnek bir bekleme süresi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the value is updated in the database
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Ingredients WHERE ingredient_name = 'TestIngredient'";
            assertFalse(statement.executeQuery(query).next());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to query the database.");
        }
    }
    
   

}