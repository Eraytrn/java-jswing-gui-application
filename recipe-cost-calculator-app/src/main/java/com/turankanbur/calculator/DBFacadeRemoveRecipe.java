/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @brief Provides methods to remove recipes from the database.
 */
public class DBFacadeRemoveRecipe {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	
	/**
	 * Instance of the RemoveRecipeMenu class.
	 */
	RemoveRecipeMenu removeRecipeMenu;

	/**
	 * @brief Constructs a DatabaseFacadeForRemoveRecipeMenu object.
	 * @param menu The RemoveRecipeMenu object associated with this facade.
	 */
	public DBFacadeRemoveRecipe(RemoveRecipeMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.removeRecipeMenu = menu;
	}

	/**
	 * @brief Removes the selected recipe and its associated ingredients from the
	 *        database.
	 * @param row The index of the selected row in the JTable.
	 */
	public void removeAdjacentRowFromDatabase(int row) {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Database.db")) {

			int recipe_id = (int) removeRecipeMenu.jTable1.getValueAt(row, 0);

			String deleteQuery = "DELETE FROM Recipes WHERE recipe_id = ?";
			String deleteQuery2 = "DELETE FROM RecipeIngredients WHERE recipe_id = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
				pstmt.setInt(1, recipe_id);
				pstmt.executeUpdate();
			}

			try (PreparedStatement pstmt2 = conn.prepareStatement(deleteQuery2)) {
				pstmt2.setInt(1, recipe_id);
				pstmt2.executeUpdate();
			}

			JOptionPane.showMessageDialog(removeRecipeMenu, "Recipe removed successfully!");
			FetchData();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(removeRecipeMenu,
					"An error occurred while removing adjacent rows from the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @brief Removes the selected row from the database and updates the table.
	 */
	public void removeAdjacentRows() {

		int selectedRow = removeRecipeMenu.jTable1.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(removeRecipeMenu, "You must select a row to remove adjacent rows.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		removeAdjacentRowFromDatabase(selectedRow);
	}

	/**
	 * @brief Fetches recipe data from the database and populates the JTable in the
	 *        associated menu.
	 */
	public void FetchData() {
		Connection connection = databaseConnection.getConnection();
		DefaultTableModel recipeModel = (DefaultTableModel) removeRecipeMenu.jTable1.getModel();
		recipeModel.setRowCount(0);

		try (Statement statement = connection.createStatement()) {

			String recipeQuery = "SELECT Recipes.recipe_id, Recipes.recipe_name, Recipes.recipe_cost, GROUP_CONCAT(RecipeIngredients.ingredient_name, ', ') AS ingredient_names "
					+ "FROM Recipes INNER JOIN RecipeIngredients "
					+ "ON Recipes.recipe_id = RecipeIngredients.recipe_id " + "GROUP BY Recipes.recipe_id";

			ResultSet rs = statement.executeQuery(recipeQuery);

			while (rs.next()) {
				int recipe_id = rs.getInt("recipe_id");
				String recipe_name = rs.getString("recipe_name");
				String recipe_cost = rs.getString("recipe_cost");
				String ingredient_names = rs.getString("ingredient_names");
				recipeModel.addRow(new Object[] { recipe_id, recipe_name, ingredient_names, recipe_cost });
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
