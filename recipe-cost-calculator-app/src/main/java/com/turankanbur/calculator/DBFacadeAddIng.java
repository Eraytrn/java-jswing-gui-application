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

/**
 * @brief Facade class for managing ingredients in the database. This class
 *        provides methods to create the ingredients table and add ingredients
 *        to the database.
 */
public class DBFacadeAddIng {
	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private static DBConnection databaseConnection;

	/**
	 * The database connection.
	 */
	private Connection connection;


	/**
	 * @brief Constructor that initializes the database connection.
	 */
	public DBFacadeAddIng() {
		databaseConnection = DBConnection.getInstance();
	}

	/**
	 * @brief Creates the ingredients table if it does not already exist. The table
	 *        includes columns for ingredient ID, name, and price.
	 */
	public void createIngredientTable() {
		Connection connection = databaseConnection.getConnection();
		String createTableQuery = "CREATE TABLE IF NOT EXISTS Ingredients ("
				+ "ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT," + "ingredient_name TEXT UNIQUE NOT NULL,"
				+ "ingredient_price TEXT NOT NULL)";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createTableQuery);
			System.out.println("Ingredients tablosu başarıyla oluşturuldu.");
		} catch (SQLException e) {
			System.err.println("Ingredients tablosu oluşturulurken hata oluştu: " + e.getMessage());
		}
	}

	/**
	 * @brief Gets the current database connection.
	 * @return The current database connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @brief Sets the database connection.
	 * @param connection The database connection to set.
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @brief Adds a new ingredient to the database.
	 * @param ingredient_name  The name of the ingredient.
	 * @param ingredient_price The price of the ingredient.
	 */
	public void addIngredientToDatabase(String ingredient_name, String ingredient_price) {
		String formatted_price = ingredient_price;
		Connection connection = databaseConnection.getConnection();
		int newId = findUnusedId();

		String insertQuery = "INSERT INTO Ingredients (ingredient_id, ingredient_name, ingredient_price) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
			pstmt.setInt(1, newId);
			pstmt.setString(2, ingredient_name);
			pstmt.setString(3, formatted_price);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "You have successfully added ingredient.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "An error occurred while adding: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @brief Finds an unused ID for a new ingredient.
	 * @return The unused ingredient ID.
	 */
	public int findUnusedId() {
		int newId = 1;
		try {
			Connection connection = databaseConnection.getConnection();
			String query = "SELECT ingredient_id FROM Ingredients ORDER BY ingredient_id";
			try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				while (rs.next()) {
					if (rs.getInt("ingredient_id") != newId) {
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
}
