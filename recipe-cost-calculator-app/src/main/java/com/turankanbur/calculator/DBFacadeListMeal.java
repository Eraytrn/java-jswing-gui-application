/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 * @brief Provides methods to fetch data for listing meals.
 */
public class DBFacadeListMeal {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	
	/**
	 * Instance of the ListMealMenu class.
	 */
	ListMealMenu listMealMenu;

	/**
	 * @brief Constructs a DatabaseFacadeForListMealMenu object.
	 * @param menu The ListMealMenu object associated with this facade.
	 */
	public DBFacadeListMeal(ListMealMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.listMealMenu = menu;
	}

	/**
	 * @brief Fetches data from the database and populates the JTable in the
	 *        associated menu.
	 */
	public void FetchData() {
		Connection connection = databaseConnection.getConnection();
		String query = "SELECT Meals.meal_id, Meals.meal_name, Meals.meal_cost, GROUP_CONCAT(MealRecipes.recipe_name, ', ') AS recipe_name "
				+ "FROM Meals INNER JOIN MealRecipes " + "ON Meals.meal_id = MealRecipes.meal_id "
				+ "GROUP BY Meals.meal_id";
		DefaultTableModel model = (DefaultTableModel) listMealMenu.jTable1.getModel();
		model.setRowCount(0); // Clear the table

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

			// Add results to the table
			while (rs.next()) {
				int meal_id = rs.getInt("meal_id");
				String meal_name = rs.getString("meal_name");
				String meal_cost = rs.getString("meal_cost");
				String recipe_name = rs.getString("recipe_name");

				model.addRow(new Object[] { meal_id, meal_name, recipe_name, meal_cost });
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
