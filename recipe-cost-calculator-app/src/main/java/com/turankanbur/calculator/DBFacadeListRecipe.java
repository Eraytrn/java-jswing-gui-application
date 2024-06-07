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
 * @brief Provides methods to fetch data for listing recipes.
 */
public class DBFacadeListRecipe {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	
	/**
	 * Instance of the ListRecipeMenu class.
	 */
	ListRecipeMenu listRecipeMenu;

	/**
	 * @brief Constructs a DatabaseFacadeForListRecipeMenu object.
	 * @param menu The ListRecipeMenu object associated with this facade.
	 */
	public DBFacadeListRecipe(ListRecipeMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.listRecipeMenu = menu;
	}

	/**
	 * @brief Fetches recipe data from the database and populates the JTable in the
	 *        associated menu.
	 */
	public void FetchDataRecipes() {
		Connection connection = databaseConnection.getConnection();
		DefaultTableModel recipeModel = (DefaultTableModel) listRecipeMenu.jTable1.getModel();
		recipeModel.setRowCount(0);

		try (Statement statement = connection.createStatement()) {

			String recipeQuery = "SELECT Recipes.recipe_id, Recipes.recipe_name, Recipes.recipe_cost, GROUP_CONCAT(RecipeIngredients.ingredient_name, ', ') AS ingredient_names "
					+ "FROM Recipes INNER JOIN RecipeIngredients "
					+ "ON Recipes.recipe_id = RecipeIngredients.recipe_id " + "GROUP BY Recipes.recipe_id";

			ResultSet rs = statement.executeQuery(recipeQuery);

			if (!rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(listRecipeMenu, "No recipes added.");
			} else {
				while (rs.next()) {
					int recipe_id = rs.getInt("recipe_id");
					String recipe_name = rs.getString("recipe_name");
					String recipe_cost = rs.getString("recipe_cost");
					String ingredient_names = rs.getString("ingredient_names");
					recipeModel.addRow(new Object[] { recipe_id, recipe_name, ingredient_names, recipe_cost });
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
