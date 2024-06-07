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
 * @brief Provides methods to remove meals from the database.
 */
public class DBFacadeRemoveMeal {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	

	/**
	 * Instance of the RemoveMealMenu class.
	 */
	RemoveMealMenu removeMealMenu;

	/**
	 * @brief Constructs a DatabaseFacadeForRemoveMealMenu object.
	 * @param menu The RemoveMealMenu object associated with this facade.
	 */
	public DBFacadeRemoveMeal(RemoveMealMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.removeMealMenu = menu;
	}

	/**
	 * @brief Removes the selected meal and its associated recipes from the
	 *        database.
	 * @param row The index of the selected row in the JTable.
	 */
	private void removeAdjacentRowFromDatabase(int row) {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Database.db")) {

			int meal_id = (int) removeMealMenu.jTable1.getValueAt(row, 0);

			String deleteQuery = "DELETE FROM Meals WHERE meal_id = ?";
			String deleteQuery2 = "DELETE FROM MealRecipes WHERE meal_id = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
				pstmt.setInt(1, meal_id);
				pstmt.executeUpdate();
			}

			try (PreparedStatement pstmt2 = conn.prepareStatement(deleteQuery2)) {
				pstmt2.setInt(1, meal_id);
				pstmt2.executeUpdate();
			}

			JOptionPane.showMessageDialog(removeMealMenu, "Meal removed successfully!");
			FetchData();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(removeMealMenu,
					"An error occurred while removing adjacent rows from the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @brief Removes the selected row from the database and updates the table.
	 */
	public void removeAdjacentRows() {

		int selectedRow = removeMealMenu.jTable1.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(removeMealMenu, "You must select a row to remove adjacent rows.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		removeAdjacentRowFromDatabase(selectedRow);
	}

	/**
	 * @brief Fetches meal data from the database and populates the JTable in the
	 *        associated menu.
	 */
	public void FetchData() {
		Connection connection = databaseConnection.getConnection();
		String query = "SELECT Meals.meal_id, Meals.meal_name, Meals.meal_cost, GROUP_CONCAT(MealRecipes.recipe_name, ', ') AS recipe_name "
				+ "FROM Meals INNER JOIN MealRecipes " + "ON Meals.meal_id = MealRecipes.meal_id "
				+ "GROUP BY Meals.meal_id";
		DefaultTableModel model = (DefaultTableModel) removeMealMenu.jTable1.getModel();
		model.setRowCount(0);
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

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
