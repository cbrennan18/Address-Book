package addressbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Database {

	private Connection connection;

	private String url;
	private String username;
	private String password;

	public Database() {
		this(Configuration.URL, Configuration.USERNAME, Configuration.PASSWORD);
	}

	public Database(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		}
		catch (ClassNotFoundException e) {
			System.err.println(e);
			
			System.exit(-1);
		}
	}

	public Connection open() throws SQLException {
		System.err.println("opening database connection to " + url);
		connection = DriverManager.getConnection(url, username, password);
		connection.setAutoCommit(false);
		return connection;
	}

	public void commit() throws SQLException {
		System.err.println("committing database connection");
		if (connection != null) {
			connection.commit();
		}
		else {
			throw new SQLException("Cannot commit, no connection created");
		}
	}

	public void rollback() throws SQLException {
		System.err.println("rolling back database connection");
		if (connection != null) {
			connection.rollback();
		}
		else {
			throw new SQLException("Cannot rollback, no connection created");
		}
	}

	public Statement getStatement(boolean updatable) throws SQLException {
		int type = (updatable ? ResultSet.TYPE_SCROLL_INSENSITIVE : ResultSet.TYPE_FORWARD_ONLY);
		int concurrency = (updatable ? ResultSet.CONCUR_UPDATABLE : ResultSet.CONCUR_READ_ONLY);
		if (connection != null) {
			Statement stmt =  connection.createStatement(type, concurrency);
			return stmt;
		}
		else {
			throw new SQLException("Cannot create statement, no connection created");
		}
	}

	public Statement getStatement() throws SQLException {
		return getStatement(false);
	}

	public PreparedStatement getPreparedStatement(String sql, boolean updatable) throws SQLException {
		int type = (updatable ? ResultSet.TYPE_SCROLL_INSENSITIVE : ResultSet.TYPE_FORWARD_ONLY);
		int concurrency = (updatable ? ResultSet.CONCUR_UPDATABLE : ResultSet.CONCUR_READ_ONLY);
		if (connection != null) {
			PreparedStatement stmt = connection.prepareStatement(sql, type, concurrency);
			return stmt;
		}
		else {
			throw new SQLException("Cannot create prepared statement, no connection created");
		}
	}

	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		return getPreparedStatement(sql, false);
	}

	public long getNextSequence(String name) throws SQLException {
		String sql = "select " + name + ".nextval from dual";
		PreparedStatement stmt = getPreparedStatement(sql);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		long seq = rs.getLong(1);
		rs.close();
		stmt.close();

		return seq;
	}

	public long getDatabaseTime() throws SQLException {
		String sql = "select sysdate from dual";
		PreparedStatement stmt = getPreparedStatement(sql);
		ResultSet rs = stmt.executeQuery();
		rs.next();

		Timestamp timestamp = rs.getTimestamp(1);

		rs.close();
		stmt.close();

		if (timestamp == null) throw new SQLException("Error getting date");
		return timestamp.getTime();
	}

	public void close() {
		System.err.println("closing database connection to " + url);
		if (connection != null) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				// do nothing
			}
		}
		connection = null;
	}

}
