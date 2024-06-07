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
 * @brief Provides methods to interact with the database for listing, editing,
 *        and removing ingredients.
 */
public class DBFacadeLERIng {

	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private DBConnection databaseConnection;
	
	/**
	 * Instance of the ListEditAndRemoveIngredientMenu class.
	 */
	LERIngMenu listEditAndRemoveIngredientMenu;

	/**
	 * @brief Constructs a DatabaseFacadeForListEditAndRemoveIngredientMenu object.
	 * @param menu The ListEditAndRemoveIngredientMenu object associated with this
	 *             facade.
	 */
	public DBFacadeLERIng(LERIngMenu menu) {
		databaseConnection = DBConnection.getInstance();
		this.listEditAndRemoveIngredientMenu = menu;
	}

	/**
	 * @brief Fetches data from the Ingredients table and populates the JTable in
	 *        the associated menu.
	 */
	public void FetchData() {
		Connection connection = databaseConnection.getConnection();
		String query = "SELECT ingredient_id, ingredient_name, ingredient_price FROM Ingredients";
		DefaultTableModel model = (DefaultTableModel) listEditAndRemoveIngredientMenu.jTable2.getModel();
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
	 * @brief Updates the database with the new value after editing a cell in the
	 *        JTable.
	 * @param row      The row index of the edited cell.
	 * @param column   The column index of the edited cell.
	 * @param newValue The new value entered by the user.
	 */
	public void saveToDatabase(int row, int column, String newValue) {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Database.db")) {
			String columnName = listEditAndRemoveIngredientMenu.jTable2.getColumnName(column);

			String updateQuery = "UPDATE Ingredients SET ";

			if (columnName.equals("Name")) {
				updateQuery += "ingredient_name = ?";
			} else if (columnName.equals("Price")) {
				updateQuery += "ingredient_price = ?";
			} else if (columnName.equals("Id")) {
				updateQuery += "ingredient_id = ?";
			} else {
				JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu, "Invalid column name.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String targetValue = listEditAndRemoveIngredientMenu.jTable2.getValueAt(row, 0).toString();

			if (columnName.equals("Id")) {
				targetValue = listEditAndRemoveIngredientMenu.jTable2.getValueAt(row, column).toString();
			}

			updateQuery += " WHERE ingredient_id = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
				pstmt.setString(1, newValue);
				pstmt.setString(2, targetValue);
				pstmt.executeUpdate();
			}

			JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu, "Value updated successfully!");
			FetchData();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu,
					"An error occurred while updating the database.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @brief Initiates the process of editing a cell in the JTable.
	 */
	public void EditData() {
		int selectedRow = listEditAndRemoveIngredientMenu.jTable2.getSelectedRow();
		int selectedColumn = listEditAndRemoveIngredientMenu.jTable2.getSelectedColumn();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu, "You must select a row to edit.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Object selectedData = listEditAndRemoveIngredientMenu.jTable2.getValueAt(selectedRow, selectedColumn);

		String newData = JOptionPane.showInputDialog(listEditAndRemoveIngredientMenu, "Enter new value:",
				selectedData.toString());

		if (newData == null) {
			return;
		}

		saveToDatabase(selectedRow, selectedColumn, newData);
	}

	/**
	 * @brief Removes a row and its adjacent rows from the database.
	 * @param row The index of the row to be removed.
	 */
	public void removeAdjacentRowFromDatabase(int row) {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Database.db")) {

			int id = (int) listEditAndRemoveIngredientMenu.jTable2.getValueAt(row, 0);
			String name = (String) listEditAndRemoveIngredientMenu.jTable2.getValueAt(row, 1);
			String price = (String) listEditAndRemoveIngredientMenu.jTable2.getValueAt(row, 2);

			String deleteQuery = "DELETE FROM Ingredients WHERE ingredient_id = ? OR (ingredient_name = ? AND ingredient_price = ?)";

			try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
				pstmt.setInt(1, id);
				pstmt.setString(2, name);
				pstmt.setString(3, price);
				pstmt.executeUpdate();
			}

			JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu, "Ingredient removed successfully!");
			FetchData();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu,
					"An error occurred while removing adjacent rows from the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @brief Initiates the process of removing a row and its adjacent rows.
	 */
	public void removeAdjacentRows() {
		int selectedRow = listEditAndRemoveIngredientMenu.jTable2.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(listEditAndRemoveIngredientMenu,
					"You must select a row to remove adjacent rows.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		removeAdjacentRowFromDatabase(selectedRow);
	}

}