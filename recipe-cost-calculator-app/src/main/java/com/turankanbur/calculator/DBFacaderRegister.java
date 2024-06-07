/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 * Facade class for interacting with the user database during registration
 * operations.
 */
public class DBFacaderRegister {

	/** The database connection menu instance. */
	private DBConnection databaseConnectionMenu;

	/**
	 * Constructs a new UserDatabaseFacadeForRegister object.
	 */
	public DBFacaderRegister() {
		databaseConnectionMenu = DBConnection.getInstance();
	}

	/**
	 * Creates the Users table in the database if it does not exist.
	 */
	public void createUserTable() {
		Connection connection = databaseConnectionMenu.getConnection();
		String createTableQuery = "CREATE TABLE IF NOT EXISTS Users (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "username TEXT UNIQUE," + "email TEXT UNIQUE," + "password TEXT)";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createTableQuery);
			System.out.println("Users table created successfully.");
		} catch (SQLException e) {
			System.err.println("Error creating Users table: " + e.getMessage());
		}
	}

	/**
	 * Adds a new user to the database.
	 *
	 * @param username the username of the new user
	 * @param email    the email of the new user
	 * @param password the password of the new user
	 * @return true if the user is successfully added, false otherwise
	 */
	public boolean addUser(String username, String email, String password) {
		Connection connection = databaseConnectionMenu.getConnection();
		String insertQuery = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
			pstmt.setString(1, username);
			pstmt.setString(2, email);
			pstmt.setString(3, password);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "An error occurred while registering: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * Validates the user input and adds a new user to the database.
	 *
	 * @param username        the username of the new user
	 * @param password        the password of the new user
	 * @param confirmPassword the confirmed password of the new user
	 * @param email           the email of the new user
	 * @return true if the user is successfully registered, false otherwise
	 */
	public boolean addUserToDatabase(String username, String password, String confirmPassword, String email) {
		if (password.length() < 2) {
			JOptionPane.showMessageDialog(null, "Password must be at least 2 characters.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (!password.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(null, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (!email.contains("@")) {
			JOptionPane.showMessageDialog(null, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			if (addUser(username, email, password)) {
				JOptionPane.showMessageDialog(null, "You have successfully registered.");
				return true;
			} else {
				return false;
			}
		}
	}
}