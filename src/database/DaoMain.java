package database;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DaoMain {

	private final static String DATABASE_URL = "jdbc:mysql://localhost/student?user=root&password=";

	private Dao<Student, Integer> studentDao;

	public static void main(String[] args) throws Exception {

		new DaoMain().doMain(args);
	}

	/**
	 * Connects to mySQL database.
	 */
	public void doMain(String[] args) throws Exception {
		ConnectionSource connectionSource = null;
		try {
			// create data-source for the database
			connectionSource = new JdbcConnectionSource(
					"jdbc:mysql://localhost/java?" + "user=root&password=");

			// setup database and DAOs
			setupDatabase(connectionSource);
			System.out.println("\nCONNECTED!!\n");

			//test methods:
//			addStudentToDatabase("hello", "test", 4);
//			addStudentToDatabase("bye", "trial", 2);
//			addStudentToDatabase("lo", "tri", 4);
//			System.out.println(viewStudentsSorted("age"));
			System.out.println(queryByAge(4));

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
	private void setupDatabase(ConnectionSource connectionSource)
			throws Exception {

		studentDao = DaoManager.createDao(connectionSource, Student.class);

		// if you need to create the table. do this only first time.
		//TableUtils.createTableIfNotExists(connectionSource, Student.class);
	}

	/**
	 * Add a student tuple to database using First Name, Last Name, and age
	 */
	public void addStudentToDatabase(String firstName, String lastName, int age) {

		try {
			studentDao.create(new Student(firstName, lastName, age));
		} catch (SQLException e) {
			System.out.println("Error: adding student");// know what you were
														// doing when got this
														// error
			e.printStackTrace();
		}
	}

	/**
	 * Returns a string of students sorted by variable 'type.'
	 * 
	 * Type is sorting type. Options: 'age' or 'lastName'. All other strings for type will be
	 * default to no sort (will return in order of database).
	 */
	public String viewStudentsSorted(String type) {
		try {

			// Get List of Students
			List<Student> studentList = studentDao.queryForAll();


			// Sort as necessary
			if (type.equals("age")) {
				Collections.sort(studentList, new AgeComparator());
			} else if (type.equals("lastName")) {
				Collections.sort(studentList, new LastNameComparator());
			} else {
				// Default: no sorting.
			}

			return listToString(studentList);

		} catch (SQLException e) {
			System.out.println("Error: viewing by age");
			e.printStackTrace();
		}
		return null;

	}
	
	public String queryByFirstName(String firstName){
		QueryBuilder<Student, Integer> statementBuilder = studentDao.queryBuilder();
		try {
			
			statementBuilder.where().like(Student.FIRST_NAME , "%" + firstName + "%");
			List<Student> studentList = studentDao.query(statementBuilder.prepare());
			
			return listToString(studentList);
		} catch (SQLException e) {
			System.out.println("Error: querying first name");
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryByLastName(String lastName){
		QueryBuilder<Student, Integer> statementBuilder = studentDao.queryBuilder();
		try {
			
			statementBuilder.where().like(Student.LAST_NAME , "%" + lastName + "%");
			List<Student> studentList = studentDao.query(statementBuilder.prepare());
			
			return listToString(studentList);
		} catch (SQLException e) {
			System.out.println("Error: querying first name");
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryByAge(int age){
		QueryBuilder<Student, Integer> statementBuilder = studentDao.queryBuilder();
		try {
			
			statementBuilder.where().like(Student.AGE , age);
			List<Student> studentList = studentDao.query(statementBuilder.prepare());
			
			return listToString(studentList);
		} catch (SQLException e) {
			System.out.println("Error: querying first name");
			e.printStackTrace();
		}
		return null;
	}
	
	public String listToString(List<Student> studentList){
		
		StringBuilder sb = new StringBuilder();
		
		for (Student s : studentList) {
			sb.append(s.toString()).append("\n");
		}

		return sb.toString();
	}

}
