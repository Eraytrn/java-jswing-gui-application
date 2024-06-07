/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @brief Facade class for managing meal-related operations in the database.
 *        This class provides methods to create meal tables, add meals, fetch
 *        recipes, and calculate meal costs.
 */
public class DBFacadeCreateMeal {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	
	/**
	 * Instance of the CreateMealMenu class.
	 */
	CreateMealMenu createMealMenu;

	/**
	 * @brief Constructor that initializes the database connection and the menu.
	 * @param menu The CreateMealMenu instance to associate with this facade.
	 */
	public DBFacadeCreateMeal(CreateMealMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.createMealMenu = menu;
	}

	/**
	 * @brief Creates the Meals table if it does not already exist. The table
	 *        includes columns for meal ID, name, cost, and recipe name.
	 */
	public void createMealTable() {
		Connection connection = databaseConnection.getConnection();
		String enableForeignKeysQuery = "PRAGMA foreign_keys = ON";
		String createTableQuery = "CREATE TABLE IF NOT EXISTS Meals (" + "meal_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "meal_name TEXT UNIQUE," + "meal_cost TEXT," + "recipe_name TEXT" + ")";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(enableForeignKeysQuery);
			statement.executeUpdate(createTableQuery);
			System.out.println("Meal table created successfully.");
		} catch (SQLException e) {
			System.err.println("Error creating Meal table: " + e.getMessage());
		}
	}

	/**
	 * @brief Creates the MealRecipes table if it does not already exist. The table
	 *        includes columns for meal ID and recipe name, with a foreign key
	 *        constraint on the meal ID.
	 */
	public void createMealRecipesTable() {
		Connection connection = databaseConnection.getConnection();
		String createTableQuery = "CREATE TABLE IF NOT EXISTS MealRecipes (" + "meal_id INTEGER," + "recipe_name TEXT,"
				+ "FOREIGN KEY (meal_id) REFERENCES Meals(meal_id)" + ")";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createTableQuery);
			System.out.println("MealRecipes table created successfully.");
		} catch (SQLException e) {
			System.err.println("Error creating MealRecipes table: " + e.getMessage());
		}
	}

	/**
	 * @brief Adds a new meal to the database. The meal includes an ID, name, cost,
	 *        and associated recipes.
	 */
	public void addMealToDatabase() {
		Connection connection = databaseConnection.getConnection();
		String recipeName = createMealMenu.jTextField2.getText();

		if (recipeName.isEmpty()) {
			JOptionPane.showMessageDialog(createMealMenu, "Lütfen tarif ismini giriniz.", "Eksik Bilgi",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String meal_name = createMealMenu.jTextField2.getText();
		String meal_cost = createMealMenu.jTextField4.getText();
		DefaultTableModel model = (DefaultTableModel) createMealMenu.jTable2.getModel();
		StringBuilder recipes = new StringBuilder();

		for (int i = 0; i < model.getRowCount(); i++) {
			String recipe_name = (String) model.getValueAt(i, 1);
			if (recipe_name != null && !recipe_name.trim().isEmpty()) {
				recipes.append(recipe_name).append(", ");
			}
		}

		if (recipes.length() > 0) {
			recipes.setLength(recipes.length() - 2);
		}

		int meal_id = findUnusedIdMeal();
		String insertMealQuery = "INSERT INTO Meals (meal_id, meal_name, meal_cost, recipe_name) VALUES (?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(insertMealQuery)) {
			statement.setInt(1, meal_id);
			statement.setString(2, meal_name);
			statement.setString(3, meal_cost);
			statement.setString(4, recipes.toString());
			statement.executeUpdate();
			System.out.println("Meal successfully added to the database with ID: " + meal_id);

			for (int i = 0; i < model.getRowCount(); i++) {
				String recipe_name = (String) model.getValueAt(i, 1);
				if (recipe_name != null && !recipe_name.trim().isEmpty()) {
					int recipe_id = Integer.parseInt(model.getValueAt(i, 0).toString());
					String insertMealRecipesQuery = "INSERT INTO MealRecipes (meal_id, recipe_name) VALUES (?, ?)";
					try (PreparedStatement mealRecipesStatement = connection.prepareStatement(insertMealRecipesQuery)) {
						mealRecipesStatement.setInt(1, meal_id);
						mealRecipesStatement.setString(2, recipe_name);
						mealRecipesStatement.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error adding meal to the database: " + e.getMessage());
		}
	}

	/**
	 * @brief Finds an unused ID for a new meal.
	 * @return The unused meal ID.
	 */
	public int findUnusedIdMeal() {
		Connection connection = databaseConnection.getConnection();
		int newId = 1;
		try {
			String query = "SELECT meal_id FROM Meals ORDER BY meal_id";
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					if (rs.getInt("meal_id") != newId) {
						break;
					}
					newId++;
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return newId;
	}

	/**
	 * @brief Fetches recipe data from the database and populates the menu's recipe
	 *        table.
	 */
	public void FetchDataRecipes() {
		Connection connection = databaseConnection.getConnection();
		String query = "SELECT recipe_id, recipe_name, recipe_cost FROM Recipes";
		DefaultTableModel model = (DefaultTableModel) createMealMenu.jTable1.getModel();
		model.setRowCount(0);

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				int id = rs.getInt("recipe_id");
				String name = rs.getString("recipe_name");
				String price = rs.getString("recipe_cost");
				model.addRow(new Object[] { id, name, price });
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @brief Adds the selected recipe from the recipe table to the meal's recipe
	 *        table.
	 */
	public void addRecipeToTable() {
		int selectedRow = createMealMenu.jTable1.getSelectedRow();
		int selectedRecipeId;

		if (selectedRow != -1) {
			selectedRecipeId = Integer.parseInt(createMealMenu.jTable1.getValueAt(selectedRow, 0).toString());
			String recipeName = createMealMenu.jTable1.getValueAt(selectedRow, 1).toString();

			DefaultTableModel model = (DefaultTableModel) createMealMenu.jTable2.getModel();
			model.insertRow(0, new Object[] { selectedRecipeId, recipeName });
		} else {
			JOptionPane.showMessageDialog(createMealMenu, "Please select a recipe.", "No recipe Selected",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @brief Calculates the cost of the selected recipes and updates the cost
	 *        table.
	 */
	public void calculateCost() {
		double totalCost = 0;
		double quantity = Double.parseDouble(createMealMenu.jTextField1.getText());
		int[] selectedRows = createMealMenu.jTable2.getSelectedRows();
		DefaultTableModel model = (DefaultTableModel) createMealMenu.jTable3.getModel();
		model.setRowCount(0); // Clear the table before adding new values

		for (int row : selectedRows) {
			int recipeId = Integer.parseInt(createMealMenu.jTable2.getValueAt(row, 0).toString());
			String price = "";

			for (int i = 0; i < createMealMenu.jTable1.getRowCount(); i++) {
				if (Integer.parseInt(createMealMenu.jTable1.getValueAt(i, 0).toString()) == recipeId) {
					price = createMealMenu.jTable1.getValueAt(i, 2).toString();
					break;
				}
			}

			double cost = Double.parseDouble(price) * quantity;
			totalCost += cost;

			// Add a new row to jTable3 with the calculated cost
			model.addRow(new Object[] { cost });
		}
	}

	/**
	 * @brief Calculates the total cost of the meal based on the individual costs in
	 *        the cost table.
	 */
	public void calculateTotalCost() {
		double totalCost = 0;

		for (int i = 0; i < createMealMenu.jTable3.getRowCount(); i++) {
			Object value = createMealMenu.jTable3.getValueAt(i, 0);
			if (value != null) {
				totalCost += Double.parseDouble(value.toString());
			}
		}

		createMealMenu.jTextField4.setText(Double.toString(totalCost));
	}
}
