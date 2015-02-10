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
	 * CONNECTS to mySQL database.
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

			// test methods:
			// addStudentToDatabase("hello", "test", 4);
			// addStudentToDatabase("bye", "trial", 2);
			// addStudentToDatabase("lo", "tri", 4);
			// System.out.println(viewStudentsSorted("age"));
			// System.out.println(queryByAge(3));
			System.out.println(deleteStudentFromDatabase(4));

		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}
	}

	/**
	 * SET UP DAO and table if necessary.
	 */
	private void setupDatabase(ConnectionSource connectionSource)
			throws Exception {

		studentDao = DaoManager.createDao(connectionSource, Student.class);

		// Creates a table. Do this only first time.
		// TableUtils.createTableIfNotExists(connectionSource, Student.class);
	}

	/**
	 * ADDS A TUPLE into mySQL database for a student using first name, last
	 * name, and age.
	 */
	public void addStudentToDatabase(String firstName, String lastName, int age) {

		try {
			studentDao.create(new Student(firstName, lastName, age));
		} catch (SQLException e) {
			System.out.println("Error: adding student");
			e.printStackTrace();
		}
	}

	/**
	 * RETURNS STUDENT OBJECT given a particular primary key. Always returns one
	 * student to be used for further methods.
	 */
	private Student getStudentByPKey(int pKey) {

		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();
		try {
			statementBuilder.where().like(Student.PRIMARY_KEY, pKey);
			List<Student> studentList = studentDao.query(statementBuilder
					.prepare());
			return studentList.get(0);// Will always return one student in index
										// 0

		} catch (SQLException e) {
			System.out.println("Error: querying age name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DELETES A STUDENT by using primary key to identify the particular student
	 * needed to be deleted.
	 */
	public String deleteStudentFromDatabase(int pKey) {

		try {
			Student student = getStudentByPKey(pKey);
			studentDao.delete(student);
			return (student.toString() + " successfully deleted");

		} catch (SQLException e) {
			System.out.println("Error: unsuccessful deleting student");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RETURNS LIST sorted by variable 'type.' Type options: 'age', 'lastName'
	 * or default no sorting. List is in string formatted in HTML.
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

	/**
	 * SEARCHES FOR FIRST NAME and returns a string list of all matches that
	 * include user's input. That is: %input%.
	 */
	public String queryByFirstName(String firstName) {
		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();
		try {

			statementBuilder.where().like(Student.FIRST_NAME,
					"%" + firstName + "%");
			List<Student> studentList = studentDao.query(statementBuilder
					.prepare());
			return listToString(studentList);

		} catch (SQLException e) {
			System.out.println("Error: querying first name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR LAST NAME and returns a string list of all matches that
	 * include user's input. That is: %input%.
	 */
	public String queryByLastName(String lastName) {
		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();
		try {

			statementBuilder.where().like(Student.LAST_NAME,
					"%" + lastName + "%");
			List<Student> studentList = studentDao.query(statementBuilder
					.prepare());
			return listToString(studentList);

		} catch (SQLException e) {
			System.out.println("Error: querying last name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR AGE and returns a string list of all exact matches to user's
	 * input.
	 */
	public String queryByAge(int age) {
		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();
		try {

			statementBuilder.where().like(Student.AGE, age);
			List<Student> studentList = studentDao.query(statementBuilder
					.prepare());
			return listToString(studentList);

		} catch (SQLException e) {
			System.out.println("Error: querying age name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RETURNS STUDENTLIST AS STRING in html format. Loops through list. If list
	 * is empty, returns specific message notifying user.
	 */
	private String listToString(List<Student> studentList) {

		if (studentList.isEmpty()) {
			return "No Students Match Your Search";
		}

		StringBuilder sb = new StringBuilder();
		for (Student s : studentList) {
			sb.append(s.toString()).append("\n");
		}

		return sb.toString();
	}
}
