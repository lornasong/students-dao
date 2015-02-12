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
			// System.out.println(updateStudentInformation(3, "lo", "yay", 6));
			// System.out.println(listToString(queryByAge("2")));
			// System.out.println(listToString(queryByMultipleFields("bye",
			// "hi",
			// "2")));

			System.out.println(listToString(getStudentListSorted("")));

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
			System.out.println("Error: querying for student using primary key");
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
	 * MODIFIES STUDENT information: first name, last name, age. Identify
	 * student to modify by pKey. User submits new information for student. If
	 * only one field has changed, include original information of fields that
	 * have not changed.
	 */
	public String updateStudentInformation(int pKey, String newFirstName,
			String newLastName, int newAge) {

		Student student = getStudentByPKey(pKey);
		student.setFirstName(newFirstName);
		student.setLastName(newLastName);
		student.setAge(newAge);

		try {
			studentDao.update(student);
			return student.toString() + " has been updated.";
		} catch (SQLException e) {
			System.out.println("Error: student info failed to update");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RETURNS LIST sorted by variable 'type.' Type options: 'age', 'lastName',
	 * 'firstName' or default no sorting (sorted by primary key).
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(getStudentListSorted)
	 */
	public QueryBuilder<Student, Integer> getStudentListSorted(String type) {

		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();

		// Sort as necessary
		if (type.equals("age")) {
			statementBuilder.orderBy(Student.AGE, true);

		} else if (type.equals("lastName")) {
			statementBuilder.orderBy(Student.LAST_NAME, true);
		} else if (type.equals("firstName")) {
			statementBuilder.orderBy(Student.FIRST_NAME, true);
		} else {
			// Default: no sorting.
		}

		return statementBuilder;

	}

	/**
	 * SEARCHES FOR FIRST NAME and returns QueryBuilder of all matches that
	 * include user's input. That is: %input%. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByFirstName)
	 */
	public QueryBuilder<Student, Integer> queryByFirstName(String firstName) {
		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();
		try {

			statementBuilder.where().like(Student.FIRST_NAME,
					"%" + firstName + "%");
			return statementBuilder;

		} catch (SQLException e) {
			System.out.println("Error: querying first name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR LAST NAME and returns QueryBuilder of all matches that
	 * include user's input. That is: %input%. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByLastName)
	 */
	public QueryBuilder<Student, Integer> queryByLastName(String lastName) {
		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();
		try {

			statementBuilder.where().like(Student.LAST_NAME,
					"%" + lastName + "%");
			return statementBuilder;

		} catch (SQLException e) {
			System.out.println("Error: querying last name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR AGE and returns a QueryBuilder of all exact matches to
	 * user's input. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByAge)
	 */
	public QueryBuilder<Student, Integer> queryByAge(String ageString) {

		QueryBuilder<Student, Integer> statementBuilder = studentDao
				.queryBuilder();

		try {

			// No user input, return all
			if (ageString == null || ageString.trim().isEmpty()) {
				statementBuilder.where().isNotNull(Student.AGE);

			} else {
				// User inputed something, return matches if input like number

				int age = Integer.parseInt(ageString);
				statementBuilder.where().like(Student.AGE, age);
			}
			return statementBuilder;

		} catch (SQLException e) {
			System.out.println("Error: querying age");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error: age string cannot be parsed to int");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR AGE, FIRSTNAME, LASTNAME and returns QueryBuilder of matches
	 * to user input. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByMultipleFields)
	 * 
	 * SELECT * FROM student WHERE (firstName IS NULL OR FIRST_NAME = firstName)
	 * AND (lastName IS NULL OR LAST_NAME = lastName) AND (age IS NULL OR AGE =
	 * age)
	 */
	public QueryBuilder<Student, Integer> queryByMultipleFields(
			String firstName, String lastName, String ageString) {

		QueryBuilder<Student, Integer> allBuilder = studentDao.queryBuilder();

		try {

			// No user input, return all
			if (ageString == null || ageString.trim().isEmpty()) {
				allBuilder.where()
						.like(Student.LAST_NAME, "%" + lastName + "%").and()
						.like(Student.FIRST_NAME, "%" + firstName + "%");

				// user inputed something, return matches if input like number
			} else {

				int age = Integer.parseInt(ageString);

				allBuilder.where()
						.like(Student.LAST_NAME, "%" + lastName + "%").and()
						.like(Student.FIRST_NAME, "%" + firstName + "%").and()
						.like(Student.AGE, age);
			}
			return allBuilder;

		} catch (SQLException e) {
			System.out.println("Error: querying all fields");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error: age string cannot be parsed to int");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RETURNS STUDENTLIST AS STRING in html format. Loops through list. If list
	 * is empty, returns specific message notifying user.
	 */
	public String listToString(QueryBuilder<Student, Integer> statementBuilder) {

		List<Student> studentList;

		try {
			studentList = studentDao.query(statementBuilder.prepare());

			if (studentList.isEmpty()) {
				return "No Students Match Your Search";
			}

			StringBuilder sb = new StringBuilder();
			for (Student s : studentList) {
				sb.append(s.toString()).append("<br/>");
			}

			return sb.toString();

		} catch (SQLException e) {
			System.out.println("Issue printing list");
			e.printStackTrace();
		}

		return null;
	}
}
