/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @brief Facade class for managing recipe-related operations in the database.
 *        This class provides methods to create recipe tables, add recipes,
 *        fetch ingredients, and calculate recipe costs.
 */
public class DBFacadeCreateRecipe {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	
	/**
	 * Instance of the CreateRecipeMenu class.
	 */
	CreateRecipeMenu createRecipeMenu;

	/**
	 * @brief Constructor that initializes the database connection and the menu.
	 * @param menu The CreateRecipeMenu instance to associate with this facade.
	 */
	public DBFacadeCreateRecipe(CreateRecipeMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.createRecipeMenu = menu;
	}

	/**
	 * @brief Creates the RecipeIngredients table if it does not already exist. The
	 *        table includes columns for recipe ID and ingredient name, with a
	 *        foreign key constraint on the recipe ID.
	 */
	public void createRecipeIngredientsTable() {
		Connection connection = databaseConnection.getConnection();
		String createRecipeIngredientsTableQuery = "CREATE TABLE IF NOT EXISTS RecipeIngredients ("
				+ "recipe_id INTEGER," + "ingredient_name TEXT,"
				+ "FOREIGN KEY (recipe_id) REFERENCES Recipes(recipe_id)" + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createRecipeIngredientsTableQuery);
			System.out.println("RecipeIngredients table successfully created.");
		} catch (SQLException e) {
			System.err.println("Error creating RecipeIngredients table: " + e.getMessage());
		}
	}

	/**
	 * @brief Creates the Recipes table if it does not already exist. The table
	 *        includes columns for recipe ID, name, cost, and ingredient name.
	 */
	public void createRecipeTable() {
		Connection connection = databaseConnection.getConnection();
		String enableForeignKeysQuery = "PRAGMA foreign_keys = ON";
		String createRecipesTableQuery = "CREATE TABLE IF NOT EXISTS Recipes ("
				+ "recipe_id INTEGER PRIMARY KEY AUTOINCREMENT," + "recipe_name TEXT UNIQUE NOT NULL,"
				+ "recipe_cost TEXT NOT NULL," + "ingredient_name TEXT NOT NULL" + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(enableForeignKeysQuery);
			statement.executeUpdate(createRecipesTableQuery);
			System.out.println("Recipes table successfully created.");
		} catch (SQLException e) {
			System.err.println("Error creating Recipes table: " + e.getMessage());
		}
	}

	/**
	 * @brief Finds an unused ID for a new recipe.
	 * @return The unused recipe ID.
	 */
	public int findUnusedIdRecipe() {
		Connection connection = databaseConnection.getConnection();
		int newId = 1;
		try {
			String query = "SELECT recipe_id FROM Recipes ORDER BY recipe_id";
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					if (rs.getInt("recipe_id") != newId) {
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
	 * @brief Adds a new recipe to the database. The recipe includes an ID, name,
	 *        cost, and associated ingredients.
	 */
	public void addRecipeToDatabase() {
		Connection connection = databaseConnection.getConnection();
		String recipeName = createRecipeMenu.jTextField2.getText();

		if (recipeName.isEmpty()) {
			JOptionPane.showMessageDialog(createRecipeMenu, "Lütfen tarif ismini giriniz.", "Eksik Bilgi",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String recipeIngredients = getRecipeIngredients();
		int recipeId = findUnusedIdRecipe();

		try (Statement statement = connection.createStatement()) {
			String insertRecipeQuery = "INSERT INTO Recipes (recipe_id, recipe_name, recipe_cost, ingredient_name) VALUES ("
					+ recipeId + ", '" + recipeName + "', '" + createRecipeMenu.jTextField4.getText() + "', '"
					+ recipeIngredients + "')";
			statement.executeUpdate(insertRecipeQuery);

			DefaultTableModel model = (DefaultTableModel) createRecipeMenu.jTable2.getModel();
			for (int i = 0; i < model.getRowCount(); i++) {
				String ingredientName = (String) model.getValueAt(i, 1);
				if (ingredientName != null && !"0".equals(ingredientName.trim())) {
					String insertIngredientQuery = "INSERT INTO RecipeIngredients (recipe_id, ingredient_name) VALUES ("
							+ recipeId + ", '" + ingredientName + "')";
					statement.executeUpdate(insertIngredientQuery);
				}
			}
			System.out.println("Tarif başarıyla eklendi.");
		} catch (SQLException e) {
			System.err.println("Tarif eklenirken hata oluştu: " + e.getMessage());
		}
	}

	/**
	 * @brief Fetches ingredient data from the database and populates the menu's
	 *        ingredient table.
	 */
	public void FetchDataIngredients() {
		Connection connection = databaseConnection.getConnection();
		String query = "SELECT ingredient_id, ingredient_name, ingredient_price FROM Ingredients";
		DefaultTableModel model = (DefaultTableModel) createRecipeMenu.jTable1.getModel();
		model.setRowCount(0);

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				int id = rs.getInt("ingredient_id");
				String name = rs.getString("ingredient_name");
				String price = rs.getString("ingredient_price");
				model.addRow(new Object[] { id, name, price });
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @brief Lists all ingredients with their prices.
	 */
	public void listIngredients() {
		Connection connection = databaseConnection.getConnection();
		String query = "SELECT ingredient_name, ingredient_price FROM Ingredients";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				String name = rs.getString("ingredient_name");
				String price = rs.getString("ingredient_price");
				System.out.println("Ingredient Name: " + name + ", Price: " + price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @brief Adds the selected ingredient from the ingredient table to the recipe's
	 *        ingredient table.
	 */
	public void addIngredientToTable() {
		int selectedRow = createRecipeMenu.jTable1.getSelectedRow();
		int selectedIngredientId;

		if (selectedRow != -1) {
			selectedIngredientId = Integer.parseInt(createRecipeMenu.jTable1.getValueAt(selectedRow, 0).toString());
			String ingredientName = createRecipeMenu.jTable1.getValueAt(selectedRow, 1).toString();

			DefaultTableModel model = (DefaultTableModel) createRecipeMenu.jTable2.getModel();
			model.insertRow(0, new Object[] { selectedIngredientId, ingredientName });

		} else {
			JOptionPane.showMessageDialog(createRecipeMenu, "Please select an ingredient.", "No Ingredient Selected",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @brief Calculates the cost of the selected ingredients and updates the cost
	 *        table with the calculated costs.
	 */
	public void calculateCost() {
		double totalCost = 0;

		double quantity = Double.parseDouble(createRecipeMenu.jTextField1.getText());

		int[] selectedRows = createRecipeMenu.jTable2.getSelectedRows();

		DefaultTableModel model = (DefaultTableModel) createRecipeMenu.jTable3.getModel();

		for (int row : selectedRows) {

			int ingredientId = Integer.parseInt(createRecipeMenu.jTable2.getValueAt(row, 0).toString());
			String price = "";

			for (int i = 0; i < createRecipeMenu.jTable1.getRowCount(); i++) {
				if (Integer.parseInt(createRecipeMenu.jTable1.getValueAt(i, 0).toString()) == ingredientId) {
					price = createRecipeMenu.jTable1.getValueAt(i, 2).toString();
					break;
				}
			}

			double cost = Double.parseDouble(price) * quantity;
			totalCost += cost;

			model.setValueAt(cost, row, 0);
		}
	}

	/**
	 * @brief Retrieves the ingredients selected for the recipe.
	 * @return A string containing the names of the selected ingredients.
	 */
	public String getRecipeIngredients() {
		DefaultTableModel model = (DefaultTableModel) createRecipeMenu.jTable2.getModel();
		StringBuilder ingredientsBuilder = new StringBuilder();

		for (int i = 0; i < model.getRowCount(); i++) {
			String ingredientName = (String) model.getValueAt(i, 1);
			if (ingredientName != null && !ingredientName.trim().isEmpty()) {
				ingredientsBuilder.append(ingredientName.trim()).append(", ");
			}
		}

		return ingredientsBuilder.toString().replaceAll(", $", "");
	}

	/**
	 * @brief Calculates the total cost of the selected ingredients and updates the
	 *        cost field.
	 */
	public void calculateTotalCost() {
		double totalCost = 0;

		for (int i = 0; i < createRecipeMenu.jTable3.getRowCount(); i++) {
			Object value = createRecipeMenu.jTable3.getValueAt(i, 0);
			if (value != null) {
				totalCost += Double.parseDouble(value.toString());
			}
		}

		createRecipeMenu.jTextField4.setText(Double.toString(totalCost));
	}

}
