package database;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DaoMain {

	private final static String DATABASE_URL = "jdbc:mysql://localhost/student?user=root&password=";

	private Dao<Student, Integer> studentDao;

	public static void main(String[] args) throws Exception {

        new DaoMain().doMain(args);
	}

	public void doMain(String[] args) throws Exception {
		ConnectionSource connectionSource = null;
		try {
			// create our data-source for the database
			connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost/java?" + "user=root&password=");

			// instantiate dao
			// studentDao = DaoManager.createDao(connectionSource,
			// Student.class);

			// setup our database and DAOs
			setupDatabase(connectionSource);
			// // read and write some data
			// readWriteData();
			// // do a bunch of bulk operations
			// readWriteBunch();
			// // show how to use the SelectArg object
			// useSelectArgFeature();
			// // show how to use the SelectArg object
			// useTransactions(connectionSource);
			System.out.println("\n\nIt seems to have worked\n\n");

		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}
	}
	
	/**
	 * Setup our database and DAOs
	 */
	private void setupDatabase(ConnectionSource connectionSource) throws Exception {

		studentDao = DaoManager.createDao(connectionSource, Student.class);

		// if you need to create the table
		//TableUtils.createTableIfNotExists(connectionSource, Student.class);
	}
	
	//make add methods here
	public void addStudentToDatabase(Student student) {
		System.out.println("Adding Student!");
		try {
			studentDao.create(student);
		} catch (SQLException e) {
			System.out.println("Error: adding student");//know what you were doing when got this error
			e.printStackTrace();
		}
	}

}
