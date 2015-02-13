package database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DaoUtil {

	// private final static String DATABASE_URL =
	// "jdbc:mysql://localhost/student?user=root&password=";
	private StudentDao studentDao;

	/**
	 * CONNECTS to mySQL database.
	 */
	public void configure() throws Exception {
		ConnectionSource connectionSource = null;

		// create data-source for the database
		connectionSource = new JdbcConnectionSource(
				"jdbc:mysql://localhost/java?" + "user=root&password=");

		// setup database and DAOs
		studentDao = new StudentDao(connectionSource);
		System.out.println("\nCONNECTED!!\n");
	}

	public StudentDao getStudentDao() {
		return studentDao;
	}
}
