/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Facade class for interacting with the user database during login operations.
 */
public class DBFacadeLogin {

	/** The database connection menu instance. */
	DBConnection databaseConnectionMenu;

	/**
	 * Constructs a new UserDatabaseFacadeForLogin object.
	 */
	public DBFacadeLogin() {
		databaseConnectionMenu = DBConnection.getInstance();
	}

	/**
	 * Validates user credentials by querying the database.
	 *
	 * @param username the username to validate
	 * @param password the password to validate
	 * @return true if the user with the given credentials exists in the database,
	 *         false otherwise
	 */
	public boolean loginUser(String username, String password) {
		Connection connection = databaseConnectionMenu.getConnection();
		String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet resultSet = pstmt.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			System.err.println("An error occurred during login: " + e.getMessage());
			return false;
		}
	}
}