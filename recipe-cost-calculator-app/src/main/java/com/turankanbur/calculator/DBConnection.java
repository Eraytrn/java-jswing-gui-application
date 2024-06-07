/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @brief Singleton class for managing the database connection. This class
 *        provides a singleton pattern to ensure a single instance of the
 *        database connection throughout the application.
 */
public class DBConnection {
	
	/**
	 * The singleton instance of DatabaseConnectionMenu.
	 */
	private static DBConnection instance;

	/**
	 * The database connection.
	 */
	private Connection connection;


	/**
	 * @brief Private constructor to prevent instantiation. Opens the database
	 *        connection when the singleton instance is created.
	 */
	private DBConnection() {
		openConnection();
	}

	/**
	 * @brief Gets the singleton instance of DatabaseConnectionMenu.
	 * @return The singleton instance of DatabaseConnectionMenu.
	 */
	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}

	/**
	 * @brief Gets the database connection. If the connection is null or closed, it
	 *        attempts to open a new connection.
	 * @return The database connection.
	 */
	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				openConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * @brief Opens a connection to the database. This method initializes the
	 *        connection to the SQLite database.
	 */
	public void openConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:Database.db");
			System.out.println("Veritabanı bağlantısı başarıyla açıldı.");
		} catch (SQLException e) {
			System.err.println("Veritabanı bağlantısı açılırken bir hata oluştu: " + e.getMessage());
		}
	}
}
