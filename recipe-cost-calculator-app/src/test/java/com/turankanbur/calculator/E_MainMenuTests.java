package com.turankanbur.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.SwingUtilities;

public class E_MainMenuTests {
	  private DBConnection dbConnection;

	    private DBFacadeAddIng dbFacade;    
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
	    	String createTableQuery1 = "CREATE TABLE IF NOT EXISTS Meals (" + "meal_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "meal_name TEXT UNIQUE," + "meal_cost TEXT," + "recipe_name TEXT" + ")";
	    	String createTableQuery2 = "CREATE TABLE IF NOT EXISTS MealRecipes (" + "meal_id INTEGER," + "recipe_name TEXT,"
					+ "FOREIGN KEY (meal_id) REFERENCES Meals(meal_id)" + ")";
	        String insertQuery1 = "INSERT INTO Ingredients (ingredient_name, ingredient_price) VALUES ('Ingredient1', '10.00')";
	        String insertQuery2 = "INSERT INTO Ingredients (ingredient_name, ingredient_price) VALUES ('Ingredient2', '15.00')";
	        String insertQuery3 = "INSERT INTO Ingredients (ingredient_name, ingredient_price) VALUES ('Ingredient3', '20.00')";
	        String insertQuery4 = "INSERT INTO Meals (meal_name, meal_cost) VALUES ('meal3', '20.00')";
	        try (Statement statement = connection.createStatement()) {
	            statement.executeUpdate(createTableQuery);
	            statement.executeUpdate(createTableQuery1);
	            statement.executeUpdate(createTableQuery2);
	            statement.executeUpdate(insertQuery1);
	            statement.executeUpdate(insertQuery2);
	            statement.executeUpdate(insertQuery3);
	   
		        
	        
             
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
    public void testIngredientButton() throws Exception {
        EventQueue.invokeAndWait(() -> {
            MainMenu mainMenu = new MainMenu("testUser");
            IngredientMenu ingmenu = new IngredientMenu();
            AddIngredientMenu adding = new AddIngredientMenu();
            LERIngMenu remove = new LERIngMenu();
            mainMenu.setVisible(true);

            // Simulate ingredient button click
            MouseEvent clickEvent = new MouseEvent(mainMenu.ingredientbutton, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent1 = new MouseEvent( ingmenu.btnNewButton_2, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent2 = new MouseEvent( remove.backbutton, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent3 = new MouseEvent( ingmenu.btnNewButton_3, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
         
            mainMenu.ingredientbutton.dispatchEvent(clickEvent);   

            ingmenu.btnNewButton_2.dispatchEvent(clickEvent1);
            remove.backbutton.dispatchEvent(clickEvent2);    
            ingmenu.btnNewButton_3.dispatchEvent(clickEvent3);  
            // Assuming IngredientMenu sets visibility and MainMenu disposes
            assertFalse(mainMenu.isVisible());
            // Close the main menu to clean up
            mainMenu.dispose();
        });
    }
  
   
    
    
    @Test
    public void testMealAndExitButton() throws Exception {
    	
        EventQueue.invokeAndWait(() -> {
            MainMenu mainMenu = new MainMenu("testUser");
            RemoveMealMenu removemenu = new RemoveMealMenu();
            PlanMealMenu mealmenu = new PlanMealMenu();
         
            ListMealMenu listmeal = new ListMealMenu();
            MouseEvent clickEvent = new MouseEvent( mealmenu.btnNewButton_2, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent1 = new MouseEvent(  listmeal.jButton3, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent2 = new MouseEvent( mealmenu.btnNewButton_1, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent3 = new MouseEvent( removemenu.jButton2, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent4 = new MouseEvent(   mealmenu.btnNewButton_3, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            mainMenu.setVisible(true);
           
     
            
            mealmenu.btnNewButton_2.dispatchEvent(clickEvent);      
            listmeal.jButton3.dispatchEvent(clickEvent1);      
            mealmenu.btnNewButton_1.dispatchEvent(clickEvent2);      
            removemenu.jButton2.dispatchEvent(clickEvent3);      
            mealmenu.btnNewButton_3.dispatchEvent(clickEvent4
            		
            		);      
            assertTrue(mainMenu.isVisible());
        });
    }

    
    @Test
    public void testRecipeAndExitButton() throws Exception {
    	SwingUtilities.invokeAndWait(() -> {
            MainMenu mainMenu = new MainMenu("testUser");
            RecipeMenu recipe = new RecipeMenu();
            ListRecipeMenu listrecipe = new ListRecipeMenu();
           
            mainMenu.setVisible(true);
CreateRecipeMenu createrecipe = new CreateRecipeMenu();
            // Simulate exit button click
            MouseEvent clickEvent = new MouseEvent(mainMenu.exitbutton, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent1 = new MouseEvent(recipe.btnNewButton_1, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent2 = new MouseEvent(recipe.btnNewButton_2, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent3 = new MouseEvent(recipe.btnNewButton_3, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            MouseEvent clickEvent4 = new MouseEvent(createrecipe.jButton6, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
           
            
            
            mainMenu.recipebutton.dispatchEvent(clickEvent);
          
           
            recipe.btnNewButton_1.dispatchEvent(clickEvent1);      
            createrecipe.jButton6.dispatchEvent(clickEvent4);
            recipe.btnNewButton_2.dispatchEvent(clickEvent2);
          
            listrecipe.jButton3.dispatchEvent(clickEvent);
            recipe.btnNewButton_3.dispatchEvent(clickEvent3);
            mainMenu.recipebutton.dispatchEvent(clickEvent);
    
            assertFalse(mainMenu.isVisible());
        });
    }
    
    @Test
    public void testAboutButton() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MainMenu mainMenu = new MainMenu("testUser");
            mainMenu.setVisible(true);

            // Simulate about button click
            MouseEvent clickEvent = new MouseEvent(mainMenu.aboutbutton, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            mainMenu.aboutbutton.dispatchEvent(clickEvent);

            // Check if about button click did not close the main menu
            assertTrue(mainMenu.isVisible());
            // Close the main menu to clean up
            mainMenu.dispose();
        });
    }
    @Test
    public void testExitButton() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            MainMenu mainMenu = new MainMenu("testUser");
            mainMenu.setVisible(true);

            // Simulate exit button click
            MouseEvent clickEvent = new MouseEvent(mainMenu.exitbutton, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            mainMenu.exitbutton.dispatchEvent(clickEvent);

          
          
            // Check if the main menu is not visible after exit button click
            assertFalse(mainMenu.isVisible());
        });
    }
   
    @Test
    public void testRemoveButtonIngredients() throws Exception {
        SwingUtilities.invokeLater(() -> {
            LERIngMenu remove = new LERIngMenu();
            remove.setVisible(true);
            remove.jButton2.doClick();
            remove.jTable2.changeSelection(2, 2, false, false);
            MouseEvent clickEvent = new MouseEvent(remove.RemoveButton, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            remove.RemoveButton.dispatchEvent(clickEvent);
        });

        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testEditButtonWithoutSelectRowIngredients() throws Exception {
        SwingUtilities.invokeLater(() -> {
            LERIngMenu remove = new LERIngMenu();
            remove.setVisible(true);
            remove.jButton2.doClick();
            remove.EditButton.doClick();
        });

        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testAddButtonEmptyNameIngredients() throws Exception {
        SwingUtilities.invokeLater(() -> {
        	AddIngredientMenu addingredient = new AddIngredientMenu();
        	addingredient.setVisible(true);
        	
        	addingredient.jTextField2.setText("53");
            
            MouseEvent clickEvent = new MouseEvent(addingredient.jButton1, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            addingredient.jButton1.dispatchEvent(clickEvent);
            addingredient.jButton1.doClick();
        });

        
        
        
        
        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testAddButtonEmptyPriceIngredients() throws Exception {
        SwingUtilities.invokeLater(() -> {
        	AddIngredientMenu addingredient = new AddIngredientMenu();
        	addingredient.setVisible(true);
        	addingredient.jTextField1.setText("name");
        	
            
            MouseEvent clickEvent = new MouseEvent(addingredient.jButton1, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false);
            addingredient.jButton1.dispatchEvent(clickEvent);
            addingredient.jButton1.doClick();
        });

        
        
        
        
        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testCreateButtonMeal() throws Exception {
        SwingUtilities.invokeLater(() -> {
        	CreateMealMenu createmeal = new CreateMealMenu();
        	createmeal.setVisible(true);
        	
        	createmeal.jButton1.doClick();
        	
        	createmeal.jTable1.setRowSelectionInterval(0,0);
        	createmeal.jButton2.doClick();
        	createmeal.jTable2.setRowSelectionInterval(0,0);
        	createmeal.jTextField1.setText("1");
        	createmeal.jButton3.doClick();
        	createmeal.jButton7.doClick();
        	createmeal.jTextField2.setText("examplemealtest");
        	createmeal.jButton4.doClick();
           
        });
        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testCreateButtonMealNoName() throws Exception {
        SwingUtilities.invokeLater(() -> {
        	CreateMealMenu createmeal = new CreateMealMenu();
        	createmeal.setVisible(true);
        	createmeal.jButton6.doClick();
        	createmeal.setVisible(true);
        	createmeal.jButton1.doClick();
        	
        	createmeal.jTable1.setRowSelectionInterval(0,0);
        	createmeal.jButton2.doClick();
        	createmeal.jTable2.setRowSelectionInterval(0,0);
        	createmeal.jTextField1.setText("1");
        	createmeal.jButton3.doClick();
        	createmeal.jButton7.doClick();
        
        	createmeal.jButton4.doClick();
           
        });
        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testListButtonMeal() throws Exception {
    	  SwingUtilities.invokeLater(() -> {
    		  ListMealMenu listmeal = new ListMealMenu();
    		  listmeal.setVisible(true);
    		  listmeal.jButton1.doClick();
    		  listmeal.jTable1.setRowSelectionInterval(0,0);
    		  listmeal.jButton3.doClick();
    	  });
    	  try {
              Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testRemoveButtonMeal() throws Exception {
    	  SwingUtilities.invokeLater(() -> {
    		  RemoveMealMenu removemeal = new RemoveMealMenu();
    		  removemeal.setVisible(true);
    		  removemeal.jButton1.doClick();
    		  removemeal.jTable1.setRowSelectionInterval(0,0);
    		  removemeal.jButton3.doClick();
    		  removemeal.jButton2.doClick();
    	  });
    	  try {
              Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testCreateButtonRecipe() throws Exception {
        SwingUtilities.invokeLater(() -> {
        	CreateRecipeMenu createrecipe = new CreateRecipeMenu();
        	createrecipe.setVisible(true);
        	
        	createrecipe.jButton1.doClick();
        	
        	createrecipe.jTable1.setRowSelectionInterval(0,0);
        	createrecipe.jButton2.doClick();
        	createrecipe.jTable2.setRowSelectionInterval(0,0);
        	createrecipe.jTextField1.setText("1");
        	createrecipe.jButton3.doClick();
        	createrecipe.jButton7.doClick();
        	createrecipe.jTextField2.setText("examplerecipetest");
        	createrecipe.jButton4.doClick();
           
        });
        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testCreateButtonRecipeNoName() throws Exception {
        SwingUtilities.invokeLater(() -> {
        	CreateRecipeMenu createrecipe = new CreateRecipeMenu();
        	createrecipe.setVisible(true);
        	
        	createrecipe.jButton1.doClick();
        	
        	createrecipe.jTable1.setRowSelectionInterval(0,0);
        	createrecipe.jButton2.doClick();
        	createrecipe.jTable2.setRowSelectionInterval(0,0);
        	createrecipe.jTextField1.setText("1");
        	createrecipe.jButton3.doClick();
        	createrecipe.jButton7.doClick();
        	
        	createrecipe.jButton4.doClick();
           
        });
        try {
            Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testListButtonRecipe() throws Exception {
    	  SwingUtilities.invokeLater(() -> {
    		  ListRecipeMenu listrecipe = new ListRecipeMenu();
    		  listrecipe.setVisible(true);
    		  listrecipe.jButton1.doClick();
    		  listrecipe.jTable1.setRowSelectionInterval(0,0);
    		  listrecipe.jButton3.doClick();
    	  });
    	  try {
              Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testRemoveButtonRecipe() throws Exception {
    	  SwingUtilities.invokeLater(() -> {
    		  RemoveRecipeMenu removerecipe = new RemoveRecipeMenu();
    		  removerecipe.setVisible(true);
    		  removerecipe.jButton1.doClick();
    		  removerecipe.jTable1.setRowSelectionInterval(0,0);
    		  removerecipe.jButton3.doClick();
    		  removerecipe.jButton2.doClick();
    	  });
    	  try {
              Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testRegisterButton() throws Exception {
    	  SwingUtilities.invokeLater(() -> {
    		  RegisterMenu register = new RegisterMenu();
    		  register.setVisible(true);
    		  
    		  register.usernameField.setText("sasasa");
    		  register.textField.setText("sasa@");
    		  register.jPasswordField1.setText("123");
    		  
    		  
    		
    		  MouseEvent clickEvent = new MouseEvent(register.registerbutton, MouseEvent.MOUSE_CLICKED,
                      System.currentTimeMillis(), 0, 0, 0, 1, false);
    		  register.registerbutton.dispatchEvent(clickEvent);
    	  });
    	  
    	  
    	  try {
              Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
    public void testLoginButton() throws Exception {
    	  SwingUtilities.invokeLater(() -> {
    		  LoginMenu login = new LoginMenu();
    		  login.setVisible(true);
    		  
    		  login.loginusername.setText("sasasa");
    		  login.loginpassword.setText("123");
    		
    		  MouseEvent clickEvent = new MouseEvent(login.btnLogin, MouseEvent.MOUSE_CLICKED,
                      System.currentTimeMillis(), 0, 0, 0, 1, false);
    		  login.btnLogin.dispatchEvent(clickEvent);
    		  
    		
    		
    	  });
    	  
    	  
    	  try {
              Thread.sleep(1000); // İş parçacığının çalışmasını beklemek için
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
}
            