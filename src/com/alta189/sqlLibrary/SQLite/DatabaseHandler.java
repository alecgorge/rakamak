package com.alta189.sqlLibrary.SQLite;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
	private sqlCore core;
	private Connection connection;
	private File SQLFile;

	public DatabaseHandler(sqlCore core, File SQLFile) {
		this.core = core;
		this.SQLFile = SQLFile;
	}

	public Connection getConnection() {
		if (this.connection == null) {
			initialize();
		}
		return this.connection;
	}

	public void closeConnection() {
		if (this.connection != null)
			try {
				this.connection.close();
			} catch (SQLException ex) {
				this.core.writeError("Error on Connection close: " + ex, Boolean.valueOf(true));
			}
	}

	public Boolean initialize() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.SQLFile.getAbsolutePath());
			return Boolean.valueOf(true);
		} catch (SQLException ex) {
			this.core.writeError("SQLite exception on initialize " + ex, Boolean.valueOf(true));
		} catch (ClassNotFoundException ex) {
			this.core.writeError("You need the SQLite library " + ex, Boolean.valueOf(true));
		}
		return Boolean.valueOf(false);
	}

	public Boolean createTable(String query) {
		try {
			if (query == null) {
				this.core.writeError("SQL Create Table query empty.", Boolean.valueOf(true));
				return Boolean.valueOf(false);
			}
			Statement statement = this.connection.createStatement();
			statement.execute(query);
			return Boolean.valueOf(true);
		} catch (SQLException ex) {
			this.core.writeError(ex.getMessage(), Boolean.valueOf(true));
		}
		return Boolean.valueOf(false);
	}

	public ResultSet sqlQuery(String query) {
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(query);

			return result;
		} catch (SQLException ex) {
			if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked"))) {
				return retryResult(query);
			}
			this.core.writeError("Error at SQL Query: " + ex.getMessage(), Boolean.valueOf(false));
		}

		return null;
	}

	public void insertQuery(String query) {
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();

			statement.executeQuery(query);
		} catch (SQLException ex) {
			if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked"))) {
				retry(query);
			} else if (!ex.toString().contains("not return ResultSet"))
				this.core.writeError("Error at SQL INSERT Query: " + ex, Boolean.valueOf(false));
		}
	}

	public void updateQuery(String query) {
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();

			statement.executeQuery(query);
		} catch (SQLException ex) {
			if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked"))) {
				retry(query);
			} else if (!ex.toString().contains("not return ResultSet"))
				this.core.writeError("Error at SQL UPDATE Query: " + ex, Boolean.valueOf(false));
		}
	}

	public void deleteQuery(String query) {
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();

			statement.executeQuery(query);
		} catch (SQLException ex) {
			if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked"))) {
				retry(query);
			} else if (!ex.toString().contains("not return ResultSet"))
				this.core.writeError("Error at SQL DELETE Query: " + ex, Boolean.valueOf(false));
		}
	}

	public Boolean wipeTable(String table) {
		try {
			if (!this.core.checkTable(table).booleanValue()) {
				this.core.writeError("Error at Wipe Table: table, " + table + ", does not exist", Boolean.valueOf(true));
				return Boolean.valueOf(false);
			}
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			String query = "DELETE FROM " + table + ";";
			statement.executeQuery(query);

			return Boolean.valueOf(true);
		} catch (SQLException ex) {
			if ((!ex.getMessage().toLowerCase().contains("locking")) && (!ex.getMessage().toLowerCase().contains("locked"))) {
				if (!ex.toString().contains("not return ResultSet"))
					this.core.writeError("Error at SQL WIPE TABLE Query: " + ex, Boolean.valueOf(false));
			}
		}
		return Boolean.valueOf(false);
	}

	public Boolean checkTable(String table) {
		try {
			DatabaseMetaData dbm = getConnection().getMetaData();
			ResultSet tables = dbm.getTables(null, null, table, null);
			if (tables.next()) {
				return Boolean.valueOf(true);
			}

			return Boolean.valueOf(false);
		} catch (SQLException e) {
			this.core.writeError("Failed to check if table \"" + table + "\" exists: " + e.getMessage(), Boolean.valueOf(true));
		}
		return Boolean.valueOf(false);
	}

	private ResultSet retryResult(String query) {
		Boolean passed = Boolean.valueOf(false);

		while (!passed.booleanValue()) {
			try {
				Connection connection = getConnection();
				Statement statement = connection.createStatement();

				ResultSet result = statement.executeQuery(query);

				passed = Boolean.valueOf(true);

				return result;
			} catch (SQLException ex) {
				if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked")))
					passed = Boolean.valueOf(false);
				else {
					this.core.writeError("Error at SQL Query: " + ex.getMessage(), Boolean.valueOf(false));
				}
			}
		}

		return null;
	}

	private void retry(String query) {
		Boolean passed = Boolean.valueOf(false);

		while (!passed.booleanValue())
			try {
				Connection connection = getConnection();
				Statement statement = connection.createStatement();

				statement.executeQuery(query);

				passed = Boolean.valueOf(true);

				return;
			} catch (SQLException ex) {
				if ((ex.getMessage().toLowerCase().contains("locking")) || (ex.getMessage().toLowerCase().contains("locked")))
					passed = Boolean.valueOf(false);
				else
					this.core.writeError("Error at SQL Query: " + ex.getMessage(), Boolean.valueOf(false));
			}
	}
}

/*
 * Location: /Users/alec/Downloads/Rakamak.jar Qualified Name:
 * com.alta189.sqlLibrary.SQLite.DatabaseHandler JD-Core Version: 0.6.0
 */